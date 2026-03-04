import { useState, useEffect } from 'react';
import StatsCard from '../../components/ui/StatsCard';
import DataTable from '../../components/ui/DataTable';
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
    { title: 'Total Users', value: users.length.toString(), change: 0, icon: HiOutlineUsers, color: 'primary' },
    { title: 'Total Courses', value: courses.length.toString(), change: 0, icon: HiOutlineBookOpen, color: 'success' },
    { title: 'Active Classes', value: classes.length.toString(), change: 0, icon: HiOutlineAcademicCap, color: 'warning' },
    { title: 'Revenue', value: '₹0', change: 0, icon: HiOutlineCurrencyDollar, color: 'info' },
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
    { header: 'Name', render: (row) => <span className="text-white font-medium">{row.name}</span> },
    { header: 'Email', accessor: 'email' },
    { header: 'Role', render: (row) => (
      <span className={`px-2 py-0.5 rounded-full text-xs font-medium capitalize
        ${row.role === 'teacher' ? 'bg-brand-primary/20 text-brand-accent' : row.role === 'admin' ? 'bg-error/20 text-error' : 'bg-success/20 text-success'}`}>
        {row.role}
      </span>
    )},
    { header: 'Status', render: (row) => (
      <span className={`px-2 py-0.5 rounded-full text-xs font-medium
        ${row.status === 'active' ? 'bg-success/20 text-success' : 'bg-text-muted/20 text-text-muted'}`}>
        {row.status}
      </span>
    )},
    { header: 'Joined', accessor: 'joined' },
  ];

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-white">Admin Dashboard</h1>
        <p className="text-text-secondary mt-1">Platform overview and management.</p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {stats.map((stat, i) => (
          <StatsCard key={i} {...stat} />
        ))}
      </div>

      {/* Recent Users */}
      <div>
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-lg font-semibold text-white">Recent Users</h2>
          <button className="text-sm text-brand-primary hover:text-brand-accent">View All</button>
        </div>
        <DataTable columns={userColumns} data={recentUsers} />
      </div>

      {/* Platform Stats */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-surface-card rounded-2xl p-6 border border-navy-600/20">
          <h2 className="text-lg font-semibold text-white mb-4">User Distribution</h2>
          {users.length === 0 ? (
            <p className="text-text-muted text-sm">No users registered yet.</p>
          ) : (
            <div className="space-y-4">
              {(() => {
                const studentCount = users.filter(u => u.role === 'student').length;
                const teacherCount = users.filter(u => u.role === 'teacher').length;
                const adminCount = users.filter(u => u.role === 'admin').length;
                const total = users.length || 1;
                return [
                  { label: 'Students', count: studentCount, total, color: 'bg-success' },
                  { label: 'Teachers', count: teacherCount, total, color: 'bg-brand-primary' },
                  { label: 'Admins', count: adminCount, total, color: 'bg-error' },
                ].map((item, i) => (
                  <div key={i}>
                    <div className="flex justify-between text-sm mb-1">
                      <span className="text-text-secondary">{item.label}</span>
                      <span className="text-white">{item.count}</span>
                    </div>
                    <div className="w-full h-2 bg-navy-700 rounded-full overflow-hidden">
                      <div
                        className={`h-full ${item.color} rounded-full`}
                        style={{ width: `${(item.count / item.total) * 100}%` }}
                      />
                    </div>
                  </div>
                ));
              })()}
            </div>
          )}
        </div>

        <div className="bg-surface-card rounded-2xl p-6 border border-navy-600/20">
          <h2 className="text-lg font-semibold text-white mb-4">Quick Actions</h2>
          <div className="grid grid-cols-2 gap-3">
            {[
              { label: 'Add User', icon: HiOutlineUsers, color: 'from-brand-primary to-brand-secondary' },
              { label: 'Add Course', icon: HiOutlineBookOpen, color: 'from-success to-emerald-400' },
              { label: 'View Reports', icon: HiOutlineAcademicCap, color: 'from-warning to-orange-400' },
              { label: 'Settings', icon: HiOutlineCurrencyDollar, color: 'from-info to-blue-400' },
            ].map((action, i) => (
              <button
                key={i}
                className="flex flex-col items-center gap-2 p-4 rounded-xl bg-navy-700/50 hover:bg-navy-700 transition-colors"
              >
                <div className={`w-10 h-10 rounded-xl bg-gradient-to-br ${action.color} flex items-center justify-center`}>
                  <action.icon className="w-5 h-5 text-white" />
                </div>
                <span className="text-xs text-text-secondary">{action.label}</span>
              </button>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
