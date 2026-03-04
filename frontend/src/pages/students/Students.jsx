import { useState, useEffect } from 'react';
import DataTable from '../../components/ui/DataTable';
import Button from '../../components/ui/Button';
import api from '../../config/api';
import { HiOutlineSearch, HiOutlineUserGroup, HiOutlineMail } from 'react-icons/hi';

const Students = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStudents = async () => {
      try {
        const response = await api.get('/users');
        const allUsers = response.data || [];
        // Filter to only show students
        const studentUsers = allUsers.filter(user => user.role === 'STUDENT');
        setStudents(studentUsers);
      } catch (error) {
        console.error('Error fetching students:', error);
        setStudents([]);
      } finally {
        setLoading(false);
      }
    };
    fetchStudents();
  }, []);

  const activeStudents = students.filter(s => s.status === 'active').length;
  const avgAttendance = students.length > 0 
    ? Math.round(students.reduce((sum, s) => sum + (parseInt(s.attendance) || 0), 0) / students.length)
    : 0;

  const columns = [
    {
      header: 'Student',
      render: (row) => (
        <div className="flex items-center gap-3">
          <div className="w-9 h-9 rounded-full gradient-primary flex items-center justify-center text-sm font-bold text-white">
            {row.avatar}
          </div>
          <div>
            <p className="text-sm font-medium text-white">{row.name}</p>
            <p className="text-xs text-text-muted">{row.email}</p>
          </div>
        </div>
      ),
    },
    { header: 'Courses', render: (row) => <span className="text-sm text-white">{row.courses}</span> },
    {
      header: 'Attendance',
      render: (row) => {
        const pct = parseInt(row.attendance);
        return (
          <div className="flex items-center gap-2">
            <div className="w-16 h-1.5 bg-navy-700 rounded-full overflow-hidden">
              <div className={`h-full rounded-full ${pct >= 90 ? 'bg-success' : pct >= 75 ? 'bg-warning' : 'bg-error'}`} style={{ width: row.attendance }} />
            </div>
            <span className="text-sm">{row.attendance}</span>
          </div>
        );
      },
    },
    {
      header: 'Status',
      render: (row) => (
        <span className={`px-3 py-1 rounded-full text-xs font-medium ${row.status === 'active' ? 'bg-success/20 text-success' : 'bg-text-muted/20 text-text-muted'}`}>
          {row.status}
        </span>
      ),
    },
    {
      header: 'Actions',
      render: (row) => (
        <div className="flex gap-2">
          <Button variant="ghost" size="sm" icon={HiOutlineMail}>Message</Button>
          <Button variant="ghost" size="sm">View</Button>
        </div>
      ),
    },
  ];

  const filtered = students.filter((s) =>
    s.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    s.email.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-white">Students</h1>
          <p className="text-text-secondary mt-1">Manage your students and enrollments</p>
        </div>
        <div className="flex items-center bg-surface-card rounded-xl px-4 py-3 border border-navy-600/20 w-72">
          <HiOutlineSearch className="w-5 h-5 text-text-muted mr-3" />
          <input
            type="text"
            placeholder="Search students..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="bg-transparent text-sm text-white placeholder-text-muted outline-none flex-1"
          />
        </div>
      </div>

      {/* Quick Stats */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div className="bg-surface-card rounded-2xl p-5 border border-navy-600/20 flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-brand-primary/20 flex items-center justify-center">
            <HiOutlineUserGroup className="w-5 h-5 text-brand-accent" />
          </div>
          <div>
            <p className="text-xs text-text-muted">Total Students</p>
            <p className="text-lg font-bold text-white">{students.length}</p>
          </div>
        </div>
        <div className="bg-surface-card rounded-2xl p-5 border border-navy-600/20 flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-success/20 flex items-center justify-center">
            <HiOutlineUserGroup className="w-5 h-5 text-success" />
          </div>
          <div>
            <p className="text-xs text-text-muted">Active</p>
            <p className="text-lg font-bold text-white">{activeStudents}</p>
          </div>
        </div>
        <div className="bg-surface-card rounded-2xl p-5 border border-navy-600/20 flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-warning/20 flex items-center justify-center">
            <HiOutlineUserGroup className="w-5 h-5 text-warning" />
          </div>
          <div>
            <p className="text-xs text-text-muted">Avg. Attendance</p>
            <p className="text-lg font-bold text-white">{avgAttendance}%</p>
          </div>
        </div>
      </div>

      {loading ? (
        <div className="text-center py-12 text-text-secondary">Loading students...</div>
      ) : students.length === 0 ? (
        <div className="text-center py-12 bg-navy-800/50 rounded-xl border border-navy-700/50">
          <HiOutlineUserGroup className="w-12 h-12 text-text-muted mx-auto mb-3" />
          <h3 className="text-lg font-medium text-white mb-2">No Students Yet</h3>
          <p className="text-text-secondary text-sm">Students will appear here once they enroll.</p>
        </div>
      ) : (
        <DataTable columns={columns} data={filtered} />
      )}
    </div>
  );
};

export default Students;
