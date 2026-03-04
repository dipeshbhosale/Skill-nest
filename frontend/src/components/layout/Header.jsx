import { useAuth } from '../../context/AuthContext';
import { HiOutlineMenu, HiOutlineBell, HiOutlineSearch } from 'react-icons/hi';

const Header = ({ onMenuClick }) => {
  const { user } = useAuth();

  return (
    <header className="h-20 bg-white border-b border-surface-border flex items-center justify-between px-4 md:px-8">
      {/* Left: hamburger + search */}
      <div className="flex items-center gap-4">
        <button
          onClick={onMenuClick}
          className="lg:hidden p-2 rounded-xl hover:bg-surface-hover text-navy-400"
        >
          <HiOutlineMenu className="w-6 h-6" />
        </button>

        <div className="hidden md:flex items-center gap-2 bg-surface-bg rounded-xl px-4 py-2.5 w-72">
          <HiOutlineSearch className="w-5 h-5 text-text-muted" />
          <input
            type="text"
            placeholder="Search..."
            className="bg-transparent outline-none text-sm text-navy-700 placeholder:text-text-muted w-full"
          />
        </div>
      </div>

      {/* Right: notifications + profile */}
      <div className="flex items-center gap-3">
        <button className="relative p-2.5 rounded-xl hover:bg-surface-hover text-navy-400 transition-colors">
          <HiOutlineBell className="w-5 h-5" />
          <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-error rounded-full" />
        </button>

        <div className="h-8 w-px bg-surface-border" />

        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-full bg-brand-light flex items-center justify-center">
            <span className="text-sm font-bold text-brand-primary">
              {user?.name?.[0]?.toUpperCase() || 'U'}
            </span>
          </div>
          <div className="hidden sm:block">
            <p className="text-sm font-semibold text-navy-700">{user?.name || 'User'}</p>
            <p className="text-xs text-text-muted capitalize">{user?.role || 'student'}</p>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
