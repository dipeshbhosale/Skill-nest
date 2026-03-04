import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import {
  HiOutlineViewGrid,
  HiOutlineBookOpen,
  HiOutlineClipboardList,
  HiOutlineCalendar,
  HiOutlineUserGroup,
  HiOutlineAcademicCap,
  HiOutlineFolder,
  HiOutlineCurrencyDollar,
  HiOutlineCog,
  HiOutlineLogout,
  HiX,
} from 'react-icons/hi';

const Sidebar = ({ open, onClose }) => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const getDashboardPath = () => {
    if (user?.role === 'admin') return '/admin/dashboard';
    if (user?.role === 'teacher') return '/teacher/dashboard';
    return '/student/dashboard';
  };

  const navItems = [
    { label: 'Dashboard', icon: HiOutlineViewGrid, path: getDashboardPath() },
    { label: 'Courses', icon: HiOutlineBookOpen, path: '/courses' },
    { label: 'Assignments', icon: HiOutlineClipboardList, path: '/assignments' },
    { label: 'Attendance', icon: HiOutlineCalendar, path: '/attendance' },
    { label: 'Calendar', icon: HiOutlineCalendar, path: '/calendar' },
    { label: 'Classes', icon: HiOutlineAcademicCap, path: '/classes' },
    ...(user?.role === 'admin' || user?.role === 'teacher'
      ? [{ label: 'Students', icon: HiOutlineUserGroup, path: '/students' }]
      : []),
    { label: 'Materials', icon: HiOutlineFolder, path: '/materials' },
    ...(user?.role === 'admin' || user?.role === 'teacher'
      ? [{ label: 'Earnings', icon: HiOutlineCurrencyDollar, path: '/earnings' }]
      : []),
    { label: 'Settings', icon: HiOutlineCog, path: '/settings' },
  ];

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  const linkClasses = ({ isActive }) =>
    `flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-medium transition-all duration-200 ${
      isActive
        ? 'bg-brand-primary text-white shadow-md shadow-brand-primary/25'
        : 'text-navy-400 hover:bg-surface-hover hover:text-navy-700'
    }`;

  return (
    <>
      {/* Mobile overlay */}
      {open && (
        <div
          className="fixed inset-0 bg-black/30 z-40 lg:hidden"
          onClick={onClose}
        />
      )}

      {/* Sidebar */}
      <aside
        className={`
          fixed lg:static inset-y-0 left-0 z-50
          w-[280px] bg-white border-r border-surface-border
          flex flex-col
          transform transition-transform duration-300 ease-in-out
          ${open ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}
        `}
      >
        {/* Logo */}
        <div className="flex items-center justify-between px-6 h-20 border-b border-surface-border">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl gradient-primary flex items-center justify-center">
              <span className="text-white text-lg font-bold">S</span>
            </div>
            <div>
              <h1 className="text-lg font-bold text-navy-800">Skill Nest</h1>
              <p className="text-xs text-text-muted">Education Platform</p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="lg:hidden p-1 rounded-lg hover:bg-surface-hover"
          >
            <HiX className="w-5 h-5 text-navy-400" />
          </button>
        </div>

        {/* Navigation */}
        <nav className="flex-1 px-4 py-6 space-y-1 overflow-y-auto">
          <p className="px-4 mb-3 text-xs font-semibold text-text-muted uppercase tracking-wider">
            Menu
          </p>
          {navItems.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              className={linkClasses}
              onClick={onClose}
            >
              <item.icon className="w-5 h-5 flex-shrink-0" />
              {item.label}
            </NavLink>
          ))}
        </nav>

        {/* User section */}
        <div className="p-4 border-t border-surface-border">
          <div className="flex items-center gap-3 px-4 py-3 rounded-xl bg-surface-bg">
            <div className="w-10 h-10 rounded-full bg-brand-light flex items-center justify-center">
              <span className="text-sm font-bold text-brand-primary">
                {user?.name?.[0]?.toUpperCase() || 'U'}
              </span>
            </div>
            <div className="flex-1 min-w-0">
              <p className="text-sm font-semibold text-navy-700 truncate">
                {user?.name || 'User'}
              </p>
              <p className="text-xs text-text-muted capitalize">{user?.role || 'student'}</p>
            </div>
            <button
              onClick={handleLogout}
              className="p-2 rounded-lg hover:bg-white text-navy-400 hover:text-error transition-colors"
              title="Logout"
            >
              <HiOutlineLogout className="w-5 h-5" />
            </button>
          </div>
        </div>
      </aside>
    </>
  );
};

export default Sidebar;
