import { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import PageHeader from '../../components/ui/PageHeader';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import toast from 'react-hot-toast';
import {
  HiOutlineUser,
  HiOutlineMail,
  HiOutlineLockClosed,
  HiOutlineBell,
  HiOutlineGlobe,
  HiOutlineShieldCheck,
} from 'react-icons/hi';

const SettingsPage = () => {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('profile');

  const tabs = [
    { id: 'profile', label: 'Profile', icon: HiOutlineUser },
    { id: 'security', label: 'Security', icon: HiOutlineShieldCheck },
    { id: 'notifications', label: 'Notifications', icon: HiOutlineBell },
    { id: 'preferences', label: 'Preferences', icon: HiOutlineGlobe },
  ];

  const handleSave = () => {
    toast.success('Settings saved successfully!');
  };

  return (
    <div className="space-y-6">
      <PageHeader title="Settings" subtitle="Manage your account preferences" />

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
        {/* Tab navigation */}
        <Card className="lg:col-span-1 h-fit">
          <nav className="space-y-1">
            {tabs.map((tab) => (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id)}
                className={`flex items-center gap-3 w-full px-4 py-3 rounded-xl text-sm font-medium transition-all ${
                  activeTab === tab.id
                    ? 'bg-brand-primary text-white shadow-md shadow-brand-primary/25'
                    : 'text-navy-600 hover:bg-surface-hover'
                }`}
              >
                <tab.icon className="w-5 h-5" />
                {tab.label}
              </button>
            ))}
          </nav>
        </Card>

        {/* Tab content */}
        <div className="lg:col-span-3">
          {activeTab === 'profile' && (
            <Card>
              <h2 className="text-lg font-semibold text-navy-800 mb-6">Profile Information</h2>

              {/* Avatar */}
              <div className="flex items-center gap-4 mb-6 p-4 rounded-xl bg-surface-bg border border-surface-border">
                <div className="w-16 h-16 rounded-full bg-brand-light flex items-center justify-center">
                  <span className="text-2xl font-bold text-brand-primary">{user?.name?.[0]?.toUpperCase() || 'U'}</span>
                </div>
                <div>
                  <p className="font-semibold text-navy-800">{user?.name || 'User Name'}</p>
                  <p className="text-sm text-text-muted capitalize">{user?.role || 'student'}</p>
                </div>
                <Button variant="secondary" size="sm" className="ml-auto">
                  Change Photo
                </Button>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-navy-600 mb-2">Full Name</label>
                  <input
                    type="text"
                    defaultValue={user?.name || ''}
                    className="w-full px-4 py-3 rounded-xl border border-surface-border bg-white text-navy-700 focus:outline-none focus:ring-2 focus:ring-brand-primary/30 focus:border-brand-primary"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-navy-600 mb-2">Email</label>
                  <input
                    type="email"
                    defaultValue={user?.email || ''}
                    className="w-full px-4 py-3 rounded-xl border border-surface-border bg-white text-navy-700 focus:outline-none focus:ring-2 focus:ring-brand-primary/30 focus:border-brand-primary"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-navy-600 mb-2">Phone</label>
                  <input
                    type="tel"
                    placeholder="+91 XXXXX XXXXX"
                    className="w-full px-4 py-3 rounded-xl border border-surface-border bg-white text-navy-700 placeholder:text-text-muted focus:outline-none focus:ring-2 focus:ring-brand-primary/30 focus:border-brand-primary"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-navy-600 mb-2">Department</label>
                  <input
                    type="text"
                    placeholder="Computer Science"
                    className="w-full px-4 py-3 rounded-xl border border-surface-border bg-white text-navy-700 placeholder:text-text-muted focus:outline-none focus:ring-2 focus:ring-brand-primary/30 focus:border-brand-primary"
                  />
                </div>
              </div>

              <div className="mt-4">
                <label className="block text-sm font-medium text-navy-600 mb-2">Bio</label>
                <textarea
                  rows={3}
                  placeholder="Tell us about yourself..."
                  className="w-full px-4 py-3 rounded-xl border border-surface-border bg-white text-navy-700 placeholder:text-text-muted focus:outline-none focus:ring-2 focus:ring-brand-primary/30 focus:border-brand-primary resize-none"
                />
              </div>

              <div className="flex justify-end mt-6">
                <Button onClick={handleSave}>Save Changes</Button>
              </div>
            </Card>
          )}

          {activeTab === 'security' && (
            <Card>
              <h2 className="text-lg font-semibold text-navy-800 mb-6">Security Settings</h2>

              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-navy-600 mb-2">Current Password</label>
                  <input
                    type="password"
                    className="w-full px-4 py-3 rounded-xl border border-surface-border bg-white text-navy-700 focus:outline-none focus:ring-2 focus:ring-brand-primary/30 focus:border-brand-primary"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-navy-600 mb-2">New Password</label>
                  <input
                    type="password"
                    className="w-full px-4 py-3 rounded-xl border border-surface-border bg-white text-navy-700 focus:outline-none focus:ring-2 focus:ring-brand-primary/30 focus:border-brand-primary"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-navy-600 mb-2">Confirm New Password</label>
                  <input
                    type="password"
                    className="w-full px-4 py-3 rounded-xl border border-surface-border bg-white text-navy-700 focus:outline-none focus:ring-2 focus:ring-brand-primary/30 focus:border-brand-primary"
                  />
                </div>
              </div>

              <div className="flex justify-end mt-6">
                <Button onClick={handleSave}>Update Password</Button>
              </div>
            </Card>
          )}

          {activeTab === 'notifications' && (
            <Card>
              <h2 className="text-lg font-semibold text-navy-800 mb-6">Notification Preferences</h2>

              <div className="space-y-4">
                {[
                  { label: 'Email Notifications', desc: 'Receive email notifications for important updates' },
                  { label: 'Assignment Reminders', desc: 'Get reminders before assignment deadlines' },
                  { label: 'Class Reminders', desc: 'Notify before scheduled classes' },
                  { label: 'Grade Updates', desc: 'When new grades are published' },
                  { label: 'Announcements', desc: 'College-wide announcements' },
                ].map((item, i) => (
                  <div key={i} className="flex items-center justify-between p-4 rounded-xl bg-surface-bg border border-surface-border">
                    <div>
                      <p className="font-medium text-navy-800 text-sm">{item.label}</p>
                      <p className="text-xs text-text-muted">{item.desc}</p>
                    </div>
                    <label className="relative inline-flex items-center cursor-pointer">
                      <input type="checkbox" defaultChecked={i < 3} className="sr-only peer" />
                      <div className="w-11 h-6 bg-gray-200 peer-focus:ring-2 peer-focus:ring-brand-primary/30 rounded-full peer peer-checked:bg-brand-primary transition-colors after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:after:translate-x-5" />
                    </label>
                  </div>
                ))}
              </div>

              <div className="flex justify-end mt-6">
                <Button onClick={handleSave}>Save Preferences</Button>
              </div>
            </Card>
          )}

          {activeTab === 'preferences' && (
            <Card>
              <h2 className="text-lg font-semibold text-navy-800 mb-6">General Preferences</h2>

              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-navy-600 mb-2">Language</label>
                  <select className="w-full px-4 py-3 rounded-xl border border-surface-border bg-white text-navy-700 focus:outline-none focus:ring-2 focus:ring-brand-primary/30 focus:border-brand-primary">
                    <option>English</option>
                    <option>Hindi</option>
                    <option>Marathi</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-navy-600 mb-2">Timezone</label>
                  <select className="w-full px-4 py-3 rounded-xl border border-surface-border bg-white text-navy-700 focus:outline-none focus:ring-2 focus:ring-brand-primary/30 focus:border-brand-primary">
                    <option>Asia/Kolkata (IST)</option>
                    <option>UTC</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-navy-600 mb-2">Date Format</label>
                  <select className="w-full px-4 py-3 rounded-xl border border-surface-border bg-white text-navy-700 focus:outline-none focus:ring-2 focus:ring-brand-primary/30 focus:border-brand-primary">
                    <option>DD/MM/YYYY</option>
                    <option>MM/DD/YYYY</option>
                    <option>YYYY-MM-DD</option>
                  </select>
                </div>
              </div>

              <div className="flex justify-end mt-6">
                <Button onClick={handleSave}>Save Preferences</Button>
              </div>
            </Card>
          )}
        </div>
      </div>
    </div>
  );
};

export default SettingsPage;
