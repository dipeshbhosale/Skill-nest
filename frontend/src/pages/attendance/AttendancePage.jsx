import { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import PageHeader from '../../components/ui/PageHeader';
import Card from '../../components/ui/Card';
import Badge from '../../components/ui/Badge';
import Button from '../../components/ui/Button';
import { HiOutlineCalendar, HiOutlineCheck, HiOutlineX } from 'react-icons/hi';

const AttendancePage = () => {
  const { user } = useAuth();
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
  const [selectedCourse, setSelectedCourse] = useState('all');

  const courses = [
    { id: 'all', name: 'All Courses' },
  ];

  const attendanceData = [];

  const filteredData =
    selectedCourse === 'all'
      ? attendanceData
      : attendanceData.filter((a) => a.course === courses.find((c) => c.id === selectedCourse)?.name);

  const totalPresent = filteredData.filter((a) => a.status === 'present').length;
  const totalAbsent = filteredData.filter((a) => a.status === 'absent').length;
  const totalLate = filteredData.filter((a) => a.status === 'late').length;

  return (
    <div className="space-y-6">
      <PageHeader
        title="Attendance"
        subtitle="Track and manage attendance records"
        actions={
          (user?.role === 'admin' || user?.role === 'teacher') && (
            <Button icon={HiOutlineCalendar}>Mark Attendance</Button>
          )
        }
      />

      {/* Stats */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <Card className="flex items-center gap-4">
          <div className="w-12 h-12 rounded-xl bg-green-50 flex items-center justify-center">
            <HiOutlineCheck className="w-6 h-6 text-success" />
          </div>
          <div>
            <p className="text-2xl font-bold text-navy-800">{totalPresent}</p>
            <p className="text-sm text-text-muted">Present</p>
          </div>
        </Card>
        <Card className="flex items-center gap-4">
          <div className="w-12 h-12 rounded-xl bg-red-50 flex items-center justify-center">
            <HiOutlineX className="w-6 h-6 text-error" />
          </div>
          <div>
            <p className="text-2xl font-bold text-navy-800">{totalAbsent}</p>
            <p className="text-sm text-text-muted">Absent</p>
          </div>
        </Card>
        <Card className="flex items-center gap-4">
          <div className="w-12 h-12 rounded-xl bg-amber-50 flex items-center justify-center">
            <HiOutlineCalendar className="w-6 h-6 text-warning" />
          </div>
          <div>
            <p className="text-2xl font-bold text-navy-800">{totalLate}</p>
            <p className="text-sm text-text-muted">Late</p>
          </div>
        </Card>
      </div>

      {/* Filters */}
      <div className="flex flex-wrap gap-4">
        <input
          type="date"
          value={selectedDate}
          onChange={(e) => setSelectedDate(e.target.value)}
          className="px-4 py-2.5 rounded-xl border border-surface-border bg-white text-navy-700 text-sm focus:outline-none focus:ring-2 focus:ring-brand-primary/30"
        />
        <select
          value={selectedCourse}
          onChange={(e) => setSelectedCourse(e.target.value)}
          className="px-4 py-2.5 rounded-xl border border-surface-border bg-white text-navy-700 text-sm focus:outline-none focus:ring-2 focus:ring-brand-primary/30"
        >
          {courses.map((c) => (
            <option key={c.id} value={c.id}>{c.name}</option>
          ))}
        </select>
      </div>

      {/* Attendance list */}
      <Card noPadding>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-surface-border">
                <th className="px-6 py-4 text-left text-xs font-semibold text-text-muted uppercase tracking-wider">Student</th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-text-muted uppercase tracking-wider">Roll No</th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-text-muted uppercase tracking-wider">Course</th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-text-muted uppercase tracking-wider">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-surface-border">
              {filteredData.map((student) => (
                <tr key={student.id} className="hover:bg-surface-bg transition-colors">
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-3">
                      <div className="w-9 h-9 rounded-full bg-brand-light flex items-center justify-center">
                        <span className="text-xs font-bold text-brand-primary">{student.name[0]}</span>
                      </div>
                      <span className="text-sm font-medium text-navy-800">{student.name}</span>
                    </div>
                  </td>
                  <td className="px-6 py-4 text-sm text-navy-600">{student.rollNo}</td>
                  <td className="px-6 py-4 text-sm text-navy-600">{student.course}</td>
                  <td className="px-6 py-4">
                    <Badge
                      variant={
                        student.status === 'present' ? 'success' : student.status === 'absent' ? 'error' : 'warning'
                      }
                    >
                      {student.status.charAt(0).toUpperCase() + student.status.slice(1)}
                    </Badge>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </Card>
    </div>
  );
};

export default AttendancePage;
