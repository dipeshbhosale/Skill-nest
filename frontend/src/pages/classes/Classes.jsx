import { useState, useEffect } from 'react';
import Button from '../../components/ui/Button';
import Modal from '../../components/ui/Modal';
import Input from '../../components/ui/Input';
import DataTable from '../../components/ui/DataTable';
import { useAuth } from '../../context/AuthContext';
import api from '../../config/api';
import { HiOutlinePlus, HiOutlineCalendar, HiOutlineClock, HiOutlineUsers, HiOutlineVideoCamera } from 'react-icons/hi';
import toast from 'react-hot-toast';

const Classes = () => {
  const { isTeacher } = useAuth();
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [viewMode, setViewMode] = useState('list');
  const [classes, setClasses] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchClasses = async () => {
      try {
        const response = await api.get('/classes');
        setClasses(response.data || []);
      } catch (error) {
        console.error('Error fetching classes:', error);
        setClasses([]);
      } finally {
        setLoading(false);
      }
    };
    fetchClasses();
  }, []);

  const liveClasses = classes.filter(c => c.status === 'live').length;
  const scheduledClasses = classes.filter(c => c.status === 'scheduled').length;
  const completedClasses = classes.filter(c => c.status === 'completed').length;

  const columns = [
    {
      header: 'Class',
      render: (row) => (
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl gradient-primary flex items-center justify-center flex-shrink-0">
            <HiOutlineVideoCamera className="w-5 h-5 text-white" />
          </div>
          <div>
            <p className="text-sm font-medium text-white">{row.title}</p>
            <p className="text-xs text-text-muted">{row.course}</p>
          </div>
        </div>
      ),
    },
    {
      header: 'Date & Time',
      render: (row) => (
        <div>
          <p className="text-sm text-white">{row.date}</p>
          <p className="text-xs text-text-muted">{row.time} · {row.duration}</p>
        </div>
      ),
    },
    {
      header: 'Students',
      render: (row) => (
        <div className="flex items-center gap-1">
          <HiOutlineUsers className="w-4 h-4 text-text-muted" />
          <span className="text-sm">{row.students}</span>
        </div>
      ),
    },
    {
      header: 'Status',
      render: (row) => (
        <span className={`px-3 py-1 rounded-full text-xs font-medium
          ${row.status === 'live' ? 'bg-error/20 text-error animate-pulse' :
            row.status === 'scheduled' ? 'bg-warning/20 text-warning' :
            'bg-success/20 text-success'}`}>
          {row.status === 'live' ? '● Live' : row.status.charAt(0).toUpperCase() + row.status.slice(1)}
        </span>
      ),
    },
    {
      header: 'Action',
      render: (row) => (
        <div className="flex gap-2">
          {row.status === 'live' && (
            <Button variant="primary" size="sm">Join Now</Button>
          )}
          {row.status === 'scheduled' && isTeacher && (
            <Button variant="secondary" size="sm">Manage</Button>
          )}
          {row.status === 'completed' && (
            <Button variant="ghost" size="sm">View Details</Button>
          )}
        </div>
      ),
    },
  ];

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-white">Classes</h1>
          <p className="text-text-secondary mt-1">Manage and attend your class sessions</p>
        </div>
        {isTeacher && (
          <Button variant="primary" icon={HiOutlinePlus} onClick={() => setShowCreateModal(true)}>
            Schedule Class
          </Button>
        )}
      </div>

      {/* Quick Stats */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div className="bg-surface-card rounded-2xl p-5 border border-navy-600/20">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-error/20 flex items-center justify-center">
              <HiOutlineVideoCamera className="w-5 h-5 text-error" />
            </div>
            <div>
              <p className="text-xs text-text-muted">Live Now</p>
              <p className="text-lg font-bold text-white">{liveClasses}</p>
            </div>
          </div>
        </div>
        <div className="bg-surface-card rounded-2xl p-5 border border-navy-600/20">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-warning/20 flex items-center justify-center">
              <HiOutlineCalendar className="w-5 h-5 text-warning" />
            </div>
            <div>
              <p className="text-xs text-text-muted">Upcoming</p>
              <p className="text-lg font-bold text-white">{scheduledClasses}</p>
            </div>
          </div>
        </div>
        <div className="bg-surface-card rounded-2xl p-5 border border-navy-600/20">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-success/20 flex items-center justify-center">
              <HiOutlineClock className="w-5 h-5 text-success" />
            </div>
            <div>
              <p className="text-xs text-text-muted">Completed</p>
              <p className="text-lg font-bold text-white">{completedClasses}</p>
            </div>
          </div>
        </div>
      </div>

      {/* Classes Table */}
      {classes.length === 0 ? (
        <div className="text-center py-12 bg-surface-card rounded-2xl border border-navy-600/20">
          <p className="text-text-muted text-lg">No classes scheduled yet</p>
        </div>
      ) : (
        <DataTable columns={columns} data={classes} />
      )}

      {/* Create Class Modal */}
      <Modal isOpen={showCreateModal} onClose={() => setShowCreateModal(false)} title="Schedule New Class" size="lg">
        <form onSubmit={(e) => { e.preventDefault(); toast.success('Class scheduled!'); setShowCreateModal(false); }} className="space-y-4">
          <Input label="Class Title" placeholder="e.g., Introduction to Spring Boot" required />
          <div className="space-y-1.5">
            <label className="block text-sm font-medium text-text-secondary">Course</label>
            <select className="w-full bg-navy-700 border border-navy-600/30 rounded-xl px-4 py-3 text-sm text-white focus:outline-none focus:ring-2 focus:ring-brand-primary/50">
              <option value="">Select a course</option>
              <option value="1">Advanced Java Programming</option>
              <option value="2">Data Structures & Algorithms</option>
              <option value="3">React.js Masterclass</option>
            </select>
          </div>
          <div className="grid grid-cols-2 gap-4">
            <Input label="Date" type="date" required />
            <Input label="Time" type="time" required />
          </div>
          <Input label="Duration (minutes)" type="number" placeholder="60" required />
          <Input label="Meeting Link" placeholder="https://meet.google.com/..." />
          <div className="flex justify-end gap-3 pt-4">
            <Button variant="secondary" onClick={() => setShowCreateModal(false)}>Cancel</Button>
            <Button variant="primary" type="submit">Schedule Class</Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Classes;
