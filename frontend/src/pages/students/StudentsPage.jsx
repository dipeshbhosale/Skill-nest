import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import PageHeader from '../../components/ui/PageHeader';
import Card from '../../components/ui/Card';
import Badge from '../../components/ui/Badge';
import DataTable from '../../components/ui/DataTable';
import api from '../../config/api';
import { HiOutlineSearch, HiOutlineMail } from 'react-icons/hi';

const StudentsPage = () => {
  const { user } = useAuth();
  const [students, setStudents] = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStudents = async () => {
      try {
        const res = await api.get('/users?role=student').catch(() => ({ data: [] }));
        setStudents(res.data || []);
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    };
    fetchStudents();
  }, []);

  const filtered = students.filter(
    (s) =>
      s.name?.toLowerCase().includes(search.toLowerCase()) ||
      s.rollNo?.toLowerCase().includes(search.toLowerCase()) ||
      s.email?.toLowerCase().includes(search.toLowerCase())
  );

  const columns = [
    {
      header: 'Student',
      render: (row) => (
        <div className="flex items-center gap-3">
          <div className="w-9 h-9 rounded-full bg-brand-light flex items-center justify-center flex-shrink-0">
            <span className="text-xs font-bold text-brand-primary">{row.name?.[0]}</span>
          </div>
          <div>
            <p className="font-medium text-navy-800 text-sm">{row.name}</p>
            <p className="text-xs text-text-muted">{row.rollNo}</p>
          </div>
        </div>
      ),
    },
    { header: 'Email', render: (row) => <span className="text-sm text-navy-600">{row.email}</span> },
    { header: 'Department', render: (row) => <span className="text-sm text-navy-600">{row.department}</span> },
    { header: 'Semester', render: (row) => <span className="text-sm text-navy-600">{row.semester}</span> },
    {
      header: 'Attendance',
      render: (row) => (
        <div className="flex items-center gap-2">
          <div className="w-16 h-1.5 bg-surface-bg rounded-full overflow-hidden">
            <div
              className={`h-full rounded-full ${row.attendance >= 85 ? 'bg-success' : row.attendance >= 75 ? 'bg-warning' : 'bg-error'}`}
              style={{ width: `${row.attendance}%` }}
            />
          </div>
          <span className="text-xs font-medium text-navy-700">{row.attendance}%</span>
        </div>
      ),
    },
    {
      header: 'Grade',
      render: (row) => (
        <Badge variant={row.grade?.startsWith('A') ? 'success' : row.grade?.startsWith('B') ? 'warning' : 'error'}>
          {row.grade}
        </Badge>
      ),
    },
  ];

  return (
    <div className="space-y-6">
      <PageHeader title="Students" subtitle="View and manage student records" />

      {/* Search */}
      <div className="flex items-center gap-2 bg-white rounded-xl px-4 py-3 border border-surface-border card-shadow max-w-md">
        <HiOutlineSearch className="w-5 h-5 text-text-muted" />
        <input
          type="text"
          placeholder="Search students by name, roll no, or email..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="bg-transparent outline-none text-sm text-navy-700 placeholder:text-text-muted w-full"
        />
      </div>

      <DataTable columns={columns} data={filtered} emptyMessage="No students found." />
    </div>
  );
};

export default StudentsPage;
