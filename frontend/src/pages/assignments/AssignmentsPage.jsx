import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import PageHeader from '../../components/ui/PageHeader';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Badge from '../../components/ui/Badge';
import DataTable from '../../components/ui/DataTable';
import api from '../../config/api';
import { HiOutlinePlus, HiOutlineClipboardList } from 'react-icons/hi';

const AssignmentsPage = () => {
  const { user } = useAuth();
  const [assignments, setAssignments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('all');

  useEffect(() => {
    const fetchAssignments = async () => {
      try {
        const endpoint = user?.role === 'student' 
          ? '/assignments/my-assignments' 
          : '/assignments';
        const res = await api.get(endpoint).catch(() => ({ data: [] }));
        setAssignments(res.data || []);
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    };
    fetchAssignments();
  }, [user]);

  const filters = ['all', 'pending', 'in-progress', 'completed'];
  const filtered = filter === 'all' ? assignments : assignments.filter((a) => a.status === filter);

  const columns = [
    {
      header: 'Assignment',
      render: (row) => (
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-brand-light flex items-center justify-center flex-shrink-0">
            <HiOutlineClipboardList className="w-5 h-5 text-brand-primary" />
          </div>
          <div>
            <p className="font-medium text-navy-800 text-sm">{row.title}</p>
            <p className="text-xs text-text-muted">{row.course}</p>
          </div>
        </div>
      ),
    },
    {
      header: 'Due Date',
      render: (row) => (
        <span className="text-sm text-navy-600">
          {new Date(row.dueDate).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' })}
        </span>
      ),
    },
    {
      header: 'Submissions',
      render: (row) => (
        <div>
          <span className="text-sm font-medium text-navy-700">{row.submissions}/{row.total}</span>
          <div className="w-20 h-1.5 bg-surface-bg rounded-full overflow-hidden mt-1">
            <div
              className="h-full bg-brand-primary rounded-full"
              style={{ width: `${(row.submissions / row.total) * 100}%` }}
            />
          </div>
        </div>
      ),
    },
    {
      header: 'Status',
      render: (row) => (
        <Badge
          variant={
            row.status === 'completed' ? 'success' : row.status === 'in-progress' ? 'warning' : 'error'
          }
        >
          {row.status === 'in-progress' ? 'In Progress' : row.status.charAt(0).toUpperCase() + row.status.slice(1)}
        </Badge>
      ),
    },
  ];

  return (
    <div className="space-y-6">
      <PageHeader
        title="Assignments"
        subtitle="Manage and track all assignments"
        actions={
          (user?.role === 'admin' || user?.role === 'teacher') && (
            <Button icon={HiOutlinePlus}>Create Assignment</Button>
          )
        }
      />

      {/* Filter tabs */}
      <div className="flex gap-2">
        {filters.map((f) => (
          <button
            key={f}
            onClick={() => setFilter(f)}
            className={`px-4 py-2 rounded-xl text-sm font-medium capitalize transition-all ${
              filter === f
                ? 'bg-brand-primary text-white shadow-md shadow-brand-primary/25'
                : 'bg-white text-navy-600 border border-surface-border hover:bg-surface-hover'
            }`}
          >
            {f === 'in-progress' ? 'In Progress' : f}
          </button>
        ))}
      </div>

      <DataTable columns={columns} data={filtered} emptyMessage="No assignments found." />
    </div>
  );
};

export default AssignmentsPage;
