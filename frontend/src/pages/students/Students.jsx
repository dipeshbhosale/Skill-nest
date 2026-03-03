import { useState } from 'react';
import DataTable from '../../components/ui/DataTable';
import Button from '../../components/ui/Button';
import { HiOutlineSearch, HiOutlineUserGroup, HiOutlineMail } from 'react-icons/hi';

const Students = () => {
  const [searchQuery, setSearchQuery] = useState('');

  const students = [
    { id: 1, name: 'Alice Johnson', email: 'alice@email.com', courses: 3, attendance: '95%', status: 'active', avatar: 'A' },
    { id: 2, name: 'Bob Wilson', email: 'bob@email.com', courses: 2, attendance: '88%', status: 'active', avatar: 'B' },
    { id: 3, name: 'Carol Davis', email: 'carol@email.com', courses: 4, attendance: '92%', status: 'active', avatar: 'C' },
    { id: 4, name: 'David Brown', email: 'david@email.com', courses: 1, attendance: '75%', status: 'inactive', avatar: 'D' },
    { id: 5, name: 'Eva Martinez', email: 'eva@email.com', courses: 3, attendance: '97%', status: 'active', avatar: 'E' },
    { id: 6, name: 'Frank Thomas', email: 'frank@email.com', courses: 2, attendance: '82%', status: 'active', avatar: 'F' },
  ];

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
            <p className="text-lg font-bold text-white">248</p>
          </div>
        </div>
        <div className="bg-surface-card rounded-2xl p-5 border border-navy-600/20 flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-success/20 flex items-center justify-center">
            <HiOutlineUserGroup className="w-5 h-5 text-success" />
          </div>
          <div>
            <p className="text-xs text-text-muted">Active</p>
            <p className="text-lg font-bold text-white">235</p>
          </div>
        </div>
        <div className="bg-surface-card rounded-2xl p-5 border border-navy-600/20 flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-warning/20 flex items-center justify-center">
            <HiOutlineUserGroup className="w-5 h-5 text-warning" />
          </div>
          <div>
            <p className="text-xs text-text-muted">Avg. Attendance</p>
            <p className="text-lg font-bold text-white">89%</p>
          </div>
        </div>
      </div>

      <DataTable columns={columns} data={filtered} />
    </div>
  );
};

export default Students;
