import { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import {
  HiOutlineMenu,
  HiOutlineBell,
  HiOutlineSearch,
  HiOutlineMoon,
  HiOutlineSun,
} from 'react-icons/hi';

const Navbar = ({ onMenuToggle }) => {
  const { user } = useAuth();
  const [searchQuery, setSearchQuery] = useState('');
  const [showNotifications, setShowNotifications] = useState(false);

  return (
    <header className="sticky top-0 z-30 bg-navy-800/80 backdrop-blur-lg border-b border-navy-600/30">
      <div className="flex items-center justify-between px-4 lg:px-8 py-4">
        {/* Left section */}
        <div className="flex items-center gap-4">
          <button
            onClick={onMenuToggle}
            className="lg:hidden p-2 rounded-xl text-text-secondary hover:text-white hover:bg-navy-700/50 transition-colors"
          >
            <HiOutlineMenu className="w-6 h-6" />
          </button>

          {/* Breadcrumb */}
          <div className="hidden sm:block">
            <p className="text-xs text-text-muted">Pages / Dashboard</p>
            <h2 className="text-lg font-bold text-white">Dashboard</h2>
          </div>
        </div>

        {/* Right section */}
        <div className="flex items-center gap-3">
          {/* Search */}
          <div className="hidden md:flex items-center bg-navy-700 rounded-full px-4 py-2 gap-2">
            <HiOutlineSearch className="w-4 h-4 text-text-muted" />
            <input
              type="text"
              placeholder="Search..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="bg-transparent text-sm text-white placeholder-text-muted outline-none w-40"
            />
          </div>

          {/* Notifications */}
          <button
            onClick={() => setShowNotifications(!showNotifications)}
            className="relative p-2 rounded-xl text-text-secondary hover:text-white hover:bg-navy-700/50 transition-colors"
          >
            <HiOutlineBell className="w-5 h-5" />
            <span className="absolute top-1 right-1 w-2 h-2 bg-brand-primary rounded-full" />
          </button>

          {/* User Avatar */}
          <div className="flex items-center gap-3 pl-3 border-l border-navy-600/30">
            <div className="w-9 h-9 rounded-full gradient-primary flex items-center justify-center text-white text-sm font-bold">
              {user?.name?.charAt(0)?.toUpperCase() || 'U'}
            </div>
            <div className="hidden sm:block">
              <p className="text-sm font-medium text-white">{user?.name || 'User'}</p>
              <p className="text-xs text-text-muted capitalize">{user?.role || 'Guest'}</p>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Navbar;
