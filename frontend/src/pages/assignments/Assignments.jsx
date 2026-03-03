import { useState } from 'react';
import Button from '../../components/ui/Button';
import Modal from '../../components/ui/Modal';
import Input from '../../components/ui/Input';
import DataTable from '../../components/ui/DataTable';
import { useAuth } from '../../context/AuthContext';
import { HiOutlinePlus, HiOutlineClipboardList, HiOutlineUpload, HiOutlineEye } from 'react-icons/hi';
import toast from 'react-hot-toast';

const Assignments = () => {
  const { isTeacher, isStudent } = useAuth();
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showSubmitModal, setShowSubmitModal] = useState(false);
  const [selectedAssignment, setSelectedAssignment] = useState(null);

  const assignments = [
    { id: 1, title: 'Build REST API with Spring Boot', course: 'Advanced Java', dueDate: 'Mar 5, 2026', submissions: 32, total: 45, status: 'active' },
    { id: 2, title: 'Implement Binary Search Tree', course: 'DSA', dueDate: 'Mar 7, 2026', submissions: 48, total: 62, status: 'active' },
    { id: 3, title: 'React Todo App with Hooks', course: 'React.js', dueDate: 'Mar 10, 2026', submissions: 12, total: 38, status: 'active' },
    { id: 4, title: 'Design Database Schema', course: 'DBMS', dueDate: 'Feb 28, 2026', submissions: 38, total: 40, status: 'closed' },
    { id: 5, title: 'AWS EC2 Deployment', course: 'Cloud Computing', dueDate: 'Mar 2, 2026', submissions: 25, total: 30, status: 'closed' },
  ];

  const studentAssignments = [
    { id: 1, title: 'Build REST API with Spring Boot', course: 'Advanced Java', dueDate: 'Mar 5, 2026', status: 'pending', grade: null },
    { id: 2, title: 'Implement Binary Search Tree', course: 'DSA', dueDate: 'Mar 7, 2026', status: 'pending', grade: null },
    { id: 3, title: 'React Todo App with Hooks', course: 'React.js', dueDate: 'Mar 10, 2026', status: 'submitted', grade: null },
    { id: 4, title: 'Design Database Schema', course: 'DBMS', dueDate: 'Feb 28, 2026', status: 'graded', grade: 'A' },
  ];

  const teacherColumns = [
    {
      header: 'Assignment',
      render: (row) => (
        <div>
          <p className="text-sm font-medium text-white">{row.title}</p>
          <p className="text-xs text-text-muted">{row.course}</p>
        </div>
      ),
    },
    { header: 'Due Date', accessor: 'dueDate' },
    {
      header: 'Submissions',
      render: (row) => (
        <div>
          <p className="text-sm text-white">{row.submissions}/{row.total}</p>
          <div className="w-20 h-1.5 bg-navy-700 rounded-full overflow-hidden mt-1">
            <div className="h-full gradient-primary rounded-full" style={{ width: `${(row.submissions / row.total) * 100}%` }} />
          </div>
        </div>
      ),
    },
    {
      header: 'Status',
      render: (row) => (
        <span className={`px-3 py-1 rounded-full text-xs font-medium ${row.status === 'active' ? 'bg-success/20 text-success' : 'bg-text-muted/20 text-text-muted'}`}>
          {row.status.charAt(0).toUpperCase() + row.status.slice(1)}
        </span>
      ),
    },
    {
      header: 'Actions',
      render: (row) => (
        <div className="flex gap-2">
          <Button variant="ghost" size="sm" icon={HiOutlineEye}>View</Button>
        </div>
      ),
    },
  ];

  const studentColumns = [
    {
      header: 'Assignment',
      render: (row) => (
        <div>
          <p className="text-sm font-medium text-white">{row.title}</p>
          <p className="text-xs text-text-muted">{row.course}</p>
        </div>
      ),
    },
    { header: 'Due Date', accessor: 'dueDate' },
    {
      header: 'Status',
      render: (row) => (
        <span className={`px-3 py-1 rounded-full text-xs font-medium
          ${row.status === 'graded' ? 'bg-success/20 text-success' :
            row.status === 'submitted' ? 'bg-info/20 text-info' :
            'bg-warning/20 text-warning'}`}>
          {row.status.charAt(0).toUpperCase() + row.status.slice(1)}
        </span>
      ),
    },
    {
      header: 'Grade',
      render: (row) => (
        <span className="text-sm font-medium text-white">{row.grade || '-'}</span>
      ),
    },
    {
      header: 'Action',
      render: (row) => (
        row.status === 'pending' ? (
          <Button variant="primary" size="sm" icon={HiOutlineUpload} onClick={() => { setSelectedAssignment(row); setShowSubmitModal(true); }}>
            Submit
          </Button>
        ) : (
          <Button variant="ghost" size="sm" icon={HiOutlineEye}>View</Button>
        )
      ),
    },
  ];

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-white">Assignments</h1>
          <p className="text-text-secondary mt-1">
            {isTeacher ? 'Create and manage assignments' : 'View and submit your assignments'}
          </p>
        </div>
        {isTeacher && (
          <Button variant="primary" icon={HiOutlinePlus} onClick={() => setShowCreateModal(true)}>
            Create Assignment
          </Button>
        )}
      </div>

      <DataTable
        columns={isTeacher ? teacherColumns : studentColumns}
        data={isTeacher ? assignments : studentAssignments}
      />

      {/* Create Assignment Modal */}
      <Modal isOpen={showCreateModal} onClose={() => setShowCreateModal(false)} title="Create Assignment" size="lg">
        <form onSubmit={(e) => { e.preventDefault(); toast.success('Assignment created!'); setShowCreateModal(false); }} className="space-y-4">
          <Input label="Title" placeholder="Assignment title" required />
          <div className="space-y-1.5">
            <label className="block text-sm font-medium text-text-secondary">Course</label>
            <select className="w-full bg-navy-700 border border-navy-600/30 rounded-xl px-4 py-3 text-sm text-white focus:outline-none focus:ring-2 focus:ring-brand-primary/50">
              <option value="">Select course</option>
              <option>Advanced Java Programming</option>
              <option>Data Structures & Algorithms</option>
              <option>React.js Masterclass</option>
            </select>
          </div>
          <div className="space-y-1.5">
            <label className="block text-sm font-medium text-text-secondary">Description</label>
            <textarea
              placeholder="Assignment instructions..."
              rows={4}
              className="w-full bg-navy-700 border border-navy-600/30 rounded-xl px-4 py-3 text-sm text-white placeholder-text-muted focus:outline-none focus:ring-2 focus:ring-brand-primary/50 resize-none"
            />
          </div>
          <Input label="Due Date" type="datetime-local" required />
          <div className="flex justify-end gap-3 pt-4">
            <Button variant="secondary" onClick={() => setShowCreateModal(false)}>Cancel</Button>
            <Button variant="primary" type="submit">Create</Button>
          </div>
        </form>
      </Modal>

      {/* Submit Assignment Modal */}
      <Modal isOpen={showSubmitModal} onClose={() => setShowSubmitModal(false)} title="Submit Assignment">
        <form onSubmit={(e) => { e.preventDefault(); toast.success('Assignment submitted!'); setShowSubmitModal(false); }} className="space-y-4">
          <div className="bg-navy-700/50 rounded-xl p-4">
            <h4 className="text-sm font-medium text-white">{selectedAssignment?.title}</h4>
            <p className="text-xs text-text-muted mt-1">{selectedAssignment?.course} · Due: {selectedAssignment?.dueDate}</p>
          </div>
          <div className="space-y-1.5">
            <label className="block text-sm font-medium text-text-secondary">Your Submission</label>
            <textarea
              placeholder="Write your answer or paste a link..."
              rows={4}
              className="w-full bg-navy-700 border border-navy-600/30 rounded-xl px-4 py-3 text-sm text-white placeholder-text-muted focus:outline-none focus:ring-2 focus:ring-brand-primary/50 resize-none"
            />
          </div>
          <div className="border-2 border-dashed border-navy-600/30 rounded-xl p-6 text-center">
            <HiOutlineUpload className="w-8 h-8 text-text-muted mx-auto mb-2" />
            <p className="text-sm text-text-muted">Drag & drop files or click to upload</p>
            <input type="file" className="hidden" />
          </div>
          <div className="flex justify-end gap-3 pt-4">
            <Button variant="secondary" onClick={() => setShowSubmitModal(false)}>Cancel</Button>
            <Button variant="primary" type="submit">Submit</Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Assignments;
