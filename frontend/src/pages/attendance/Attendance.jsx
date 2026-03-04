import { useState, useEffect } from 'react';
import DataTable from '../../components/ui/DataTable';
import Button from '../../components/ui/Button';
import { useAuth } from '../../context/AuthContext';
import api from '../../config/api';
import { HiOutlineDocumentText, HiOutlineCheck, HiOutlineX, HiOutlineClipboardList } from 'react-icons/hi';

const Attendance = () => {
  const { isTeacher } = useAuth();
  const [selectedClass, setSelectedClass] = useState('all');
  const [attendanceData, setAttendanceData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAttendance = async () => {
      try {
        const response = await api.get('/attendance');
        setAttendanceData(response.data || []);
      } catch (error) {
        console.error('Error fetching attendance:', error);
        setAttendanceData([]);
      } finally {
        setLoading(false);
      }
    };
    fetchAttendance();
  }, []);

  const presentCount = attendanceData.filter(a => a.status === 'present').length;
  const absentCount = attendanceData.filter(a => a.status === 'absent').length;
  const attendanceRate = attendanceData.length > 0 
    ? ((presentCount / attendanceData.length) * 100).toFixed(1)
    : 0;

  const columns = [
    {
      header: 'Student',
      render: (row) => (
        <div className="flex items-center gap-3">
          <div className="w-8 h-8 rounded-full gradient-primary flex items-center justify-center text-xs font-bold text-white">
            {row.student.charAt(0)}
          </div>
          <span className="text-sm text-white">{row.student}</span>
        </div>
      ),
    },
    { header: 'Course', accessor: 'course' },
    { header: 'Class', accessor: 'class' },
    { header: 'Date', accessor: 'date' },
    {
      header: 'Status',
      render: (row) => (
        <span className={`inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-medium
          ${row.status === 'present' ? 'bg-success/20 text-success' : 'bg-error/20 text-error'}`}>
          {row.status === 'present' ? <HiOutlineCheck className="w-3 h-3" /> : <HiOutlineX className="w-3 h-3" />}
          {row.status.charAt(0).toUpperCase() + row.status.slice(1)}
        </span>
      ),
    },
    ...(isTeacher ? [{
      header: 'Action',
      render: (row) => (
        <Button variant="ghost" size="sm">
          {row.status === 'present' ? 'Mark Absent' : 'Mark Present'}
        </Button>
      ),
    }] : []),
  ];

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-white">Attendance</h1>
          <p className="text-text-secondary mt-1">Track class attendance records</p>
        </div>
        {isTeacher && (
          <Button variant="primary" icon={HiOutlineDocumentText}>Export Report</Button>
        )}
      </div>

      {/* Stats */}
      <div className="grid grid-cols-1 sm:grid-cols-4 gap-4">
        {[
          { label: 'Total Records', value: String(attendanceData.length), color: 'bg-brand-primary/20 text-brand-accent' },
          { label: 'Present', value: String(presentCount), color: 'bg-success/20 text-success' },
          { label: 'Absent', value: String(absentCount), color: 'bg-error/20 text-error' },
          { label: 'Rate', value: `${attendanceRate}%`, color: 'bg-warning/20 text-warning' },
        ].map((stat, i) => (
          <div key={i} className="bg-surface-card rounded-2xl p-5 border border-navy-600/20 text-center">
            <p className="text-xs text-text-muted">{stat.label}</p>
            <p className={`text-2xl font-bold mt-1 ${stat.color.split(' ')[1]}`}>{stat.value}</p>
          </div>
        ))}
      </div>

      {loading ? (
        <div className="text-center py-12 text-text-secondary">Loading attendance records...</div>
      ) : attendanceData.length === 0 ? (
        <div className="text-center py-12 bg-navy-800/50 rounded-xl border border-navy-700/50">
          <HiOutlineClipboardList className="w-12 h-12 text-text-muted mx-auto mb-3" />
          <h3 className="text-lg font-medium text-white mb-2">No Attendance Records</h3>
          <p className="text-text-secondary text-sm">Attendance records will appear here once classes are held.</p>
        </div>
      ) : (
        <DataTable columns={columns} data={attendanceData} />
      )}
    </div>
  );
};

export default Attendance;
