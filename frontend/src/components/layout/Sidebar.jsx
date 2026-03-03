import { NavLink, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import {
  HiOutlineHome,
  HiOutlineBookOpen,
  HiOutlineAcademicCap,
  HiOutlineCalendar,
  HiOutlineClipboardList,
  HiOutlineUsers,
  HiOutlineCog,
  HiOutlineCurrencyDollar,
  HiOutlineDocumentText,
  HiOutlineChartBar,
  HiOutlineLogout,
  HiOutlineUserGroup,
} from 'react-icons/hi';

const Sidebar = ({ isOpen, onClose }) => {
  const { user, logout, isAdmin, isTeacher, isStudent } = useAuth();
  const location = useLocation();

  const teacherLinks = [
    { to: '/dashboard', icon: HiOutlineHome, label: 'Dashboard' },
    { to: '/courses', icon: HiOutlineBookOpen, label: 'My Courses' },
    { to: '/classes', icon: HiOutlineAcademicCap, label: 'Classes' },
    { to: '/assignments', icon: HiOutlineClipboardList, label: 'Assignments' },
    { to: '/students', icon: HiOutlineUsers, label: 'Students' },
    { to: '/attendance', icon: HiOutlineDocumentText, label: 'Attendance' },
    { to: '/materials', icon: HiOutlineDocumentText, label: 'Materials' },
    { to: '/calendar', icon: HiOutlineCalendar, label: 'Calendar' },
    { to: '/earnings', icon: HiOutlineCurrencyDollar, label: 'Earnings' },
    { to: '/settings', icon: HiOutlineCog, label: 'Settings' },
  ];

  const studentLinks = [
    { to: '/dashboard', icon: HiOutlineHome, label: 'Dashboard' },
    { to: '/courses', icon: HiOutlineBookOpen, label: 'My Courses' },
    { to: '/classes', icon: HiOutlineAcademicCap, label: 'Classes' },
    { to: '/assignments', icon: HiOutlineClipboardList, label: 'Assignments' },
    { to: '/materials', icon: HiOutlineDocumentText, label: 'Materials' },
    { to: '/calendar', icon: HiOutlineCalendar, label: 'Calendar' },
    { to: '/settings', icon: HiOutlineCog, label: 'Settings' },
  ];

  const adminLinks = [
    { to: '/dashboard', icon: HiOutlineHome, label: 'Dashboard' },
    { to: '/users', icon: HiOutlineUserGroup, label: 'Users' },
    { to: '/courses', icon: HiOutlineBookOpen, label: 'Courses' },
    { to: '/analytics', icon: HiOutlineChartBar, label: 'Analytics' },
    { to: '/settings', icon: HiOutlineCog, label: 'Settings' },
  ];

  const links = isAdmin ? adminLinks : isTeacher ? teacherLinks : studentLinks;

  return (
    <>
      {/* Mobile overlay */}
      {isOpen && (
        <div
          className="fixed inset-0 bg-black/50 z-40 lg:hidden"
          onClick={onClose}
        />
      )}

      {/* Sidebar */}
      <aside
        className={`fixed top-0 left-0 h-full w-[280px] bg-navy-800 border-r border-navy-600/30 z-50 transform transition-transform duration-300 ease-in-out
          ${isOpen ? 'translate-x-0' : '-translate-x-full'} lg:translate-x-0`}
      >
        {/* Logo */}
        <div className="flex items-center gap-3 px-6 py-6 border-b border-navy-600/30">
          <div className="w-10 h-10 rounded-xl gradient-primary flex items-center justify-center">
            <HiOutlineBookOpen className="w-6 h-6 text-white" />
          </div>
          <div>
            <h1 className="text-lg font-bold text-white">Skill Nest</h1>
            <p className="text-xs text-text-muted capitalize">{user?.role || 'Platform'}</p>
          </div>
        </div>

        {/* Navigation Links */}
        <nav className="flex-1 px-4 py-4 space-y-1 overflow-y-auto max-h-[calc(100vh-180px)]">
          {links.map((link) => {
            const isActive = location.pathname === link.to;
            return (
              <NavLink
                key={link.to}
                to={link.to}
                onClick={onClose}
                className={`flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-medium transition-all duration-200
                  ${isActive
                    ? 'gradient-primary text-white shadow-lg shadow-brand-primary/25'
                    : 'text-text-secondary hover:text-white hover:bg-navy-700/50'
                  }`}
              >
                <link.icon className="w-5 h-5" />
                {link.label}
              </NavLink>
            );
          })}
        </nav>

        {/* Logout */}
        <div className="absolute bottom-0 left-0 right-0 p-4 border-t border-navy-600/30">
          <button
            onClick={logout}
            className="flex items-center gap-3 w-full px-4 py-3 rounded-xl text-sm font-medium text-text-secondary hover:text-error hover:bg-error/10 transition-all duration-200"
          >
            <HiOutlineLogout className="w-5 h-5" />
            Logout
          </button>
        </div>
      </aside>
    </>
  );
};

export default Sidebar;
