import { useState, useEffect } from 'react';
import StatsCard from '../../components/ui/StatsCard';
import DataTable from '../../components/ui/DataTable';
import Card from '../../components/ui/Card';
import PageHeader from '../../components/ui/PageHeader';
import api from '../../config/api';
import {
  HiOutlineUsers,
  HiOutlineBookOpen,
  HiOutlineAcademicCap,
  HiOutlineCurrencyDollar,
} from 'react-icons/hi';

const AdminDashboard = () => {
  const [users, setUsers] = useState([]);
  const [courses, setCourses] = useState([]);
  const [classes, setClasses] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [usersRes, coursesRes, classesRes] = await Promise.all([
          api.get('/users').catch(() => ({ data: [] })),
          api.get('/courses').catch(() => ({ data: [] })),
          api.get('/classes').catch(() => ({ data: [] })),
        ]);
        setUsers(usersRes.data || []);
        setCourses(coursesRes.data || []);
        setClasses(classesRes.data || []);
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const stats = [
    { title: 'Total Users', value: users.length.toString(), icon: HiOutlineUsers, color: 'primary' },
    { title: 'Total Courses', value: courses.length.toString(), icon: HiOutlineBookOpen, color: 'success' },
    { title: 'Active Classes', value: classes.length.toString(), icon: HiOutlineAcademicCap, color: 'warning' },
    { title: 'Total Teachers', value: users.filter(u => u.role === 'teacher').length.toString(), icon: HiOutlineCurrencyDollar, color: 'info' },
  ];

  const recentUsers = users.slice(0, 5).map((user, index) => ({
    id: user.id || index,
    name: user.name || 'Unknown',
    email: user.email || '',
    role: user.role || 'student',
    status: user.status || 'active',
    joined: user.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'N/A',
  }));

  const userColumns = [
    { header: 'Name', render: (row) => <span className="text-navy-800 font-medium">{row.name}</span> },
    { header: 'Email', accessor: 'email' },
    {
      header: 'Role',
      render: (row) => (
        <span
          className={`px-2.5 py-0.5 rounded-full text-xs font-medium capitalize ${
            row.role === 'teacher'
              ? 'bg-brand-light text-brand-primary'
              : row.role === 'admin'
              ? 'bg-red-50 text-error'
              : 'bg-green-50 text-success'
          }`}
        >
          {row.role}
        </span>
      ),
    },
    {
      header: 'Status',
      render: (row) => (
        <span
          className={`px-2.5 py-0.5 rounded-full text-xs font-medium ${
            row.status === 'active' ? 'bg-green-50 text-success' : 'bg-gray-100 text-text-muted'
          }`}
        >
          {row.status}
        </span>
      ),
    },
    { header: 'Joined', accessor: 'joined' },
  ];

  return (
    <div className="space-y-6">
      <PageHeader title="Admin Dashboard" subtitle="Platform overview and management." />

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
        {stats.map((stat, i) => (
          <StatsCard key={i} {...stat} />
        ))}
      </div>

      {/* Recent Users */}
      <div>
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-lg font-semibold text-navy-800">Recent Users</h2>
          <button className="text-sm text-brand-primary hover:text-brand-secondary font-medium">
            View All
          </button>
        </div>
        <DataTable columns={userColumns} data={recentUsers} />
      </div>

      {/* Bottom cards */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card>
          <h2 className="text-lg font-semibold text-navy-800 mb-4">User Distribution</h2>
          {users.length === 0 ? (
            <p className="text-text-muted text-sm">No users registered yet.</p>
          ) : (
            <div className="space-y-4">
              {(() => {
                const studentCount = users.filter((u) => u.role === 'student').length;
                const teacherCount = users.filter((u) => u.role === 'teacher').length;
                const adminCount = users.filter((u) => u.role === 'admin').length;
                const total = users.length || 1;
                return [
                  { label: 'Students', count: studentCount, total, color: 'bg-success' },
                  { label: 'Teachers', count: teacherCount, total, color: 'bg-brand-primary' },
                  { label: 'Admins', count: adminCount, total, color: 'bg-error' },
                ].map((item, i) => (
                  <div key={i}>
                    <div className="flex justify-between text-sm mb-1">
                      <span className="text-text-secondary">{item.label}</span>
                      <span className="text-navy-700 font-medium">{item.count}</span>
                    </div>
                    <div className="w-full h-2 bg-surface-bg rounded-full overflow-hidden">
                      <div
                        className={`h-full ${item.color} rounded-full transition-all`}
                        style={{ width: `${(item.count / item.total) * 100}%` }}
                      />
                    </div>
                  </div>
                ));
              })()}
            </div>
          )}
        </Card>

        <Card>
          <h2 className="text-lg font-semibold text-navy-800 mb-4">Quick Actions</h2>
          <div className="grid grid-cols-2 gap-3">
            {[
              { label: 'Add User', icon: HiOutlineUsers, color: 'from-brand-primary to-brand-secondary' },
              { label: 'Add Course', icon: HiOutlineBookOpen, color: 'from-success to-emerald-400' },
              { label: 'View Reports', icon: HiOutlineAcademicCap, color: 'from-warning to-orange-400' },
              { label: 'Settings', icon: HiOutlineCurrencyDollar, color: 'from-info to-blue-400' },
            ].map((action, i) => (
              <button
                key={i}
                className="flex flex-col items-center gap-2 p-4 rounded-xl bg-surface-bg hover:bg-surface-hover transition-colors border border-surface-border"
              >
                <div
                  className={`w-10 h-10 rounded-xl bg-gradient-to-br ${action.color} flex items-center justify-center shadow-md`}
                >
                  <action.icon className="w-5 h-5 text-white" />
                </div>
                <span className="text-xs text-navy-600 font-medium">{action.label}</span>
              </button>
            ))}
          </div>
        </Card>
      </div>
    </div>
  );
};

export default AdminDashboard;
