import { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import Input from '../../components/ui/Input';
import Button from '../../components/ui/Button';
import { HiOutlineUser, HiOutlineMail, HiOutlineLockClosed, HiOutlineBell, HiOutlineShieldCheck, HiOutlineCamera } from 'react-icons/hi';
import toast from 'react-hot-toast';

const Settings = () => {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('profile');

  const tabs = [
    { id: 'profile', label: 'Profile', icon: HiOutlineUser },
    { id: 'security', label: 'Security', icon: HiOutlineShieldCheck },
    { id: 'notifications', label: 'Notifications', icon: HiOutlineBell },
  ];

  const [profile, setProfile] = useState({
    name: user?.name || 'Prof. Smith',
    email: user?.email || 'smith@skillnest.com',
    phone: '+91 98765 43210',
    bio: 'Experienced educator with 10+ years in computer science and programming.',
    qualifications: 'M.Tech Computer Science, B.Tech IT',
    perClassRate: '500',
  });

  const [notifications, setNotifications] = useState({
    emailNotifs: true,
    classReminders: true,
    assignmentUpdates: true,
    newEnrollments: true,
    platformUpdates: false,
  });

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-white">Settings</h1>
        <p className="text-text-secondary mt-1">Manage your account preferences</p>
      </div>

      {/* Tabs */}
      <div className="flex gap-1 bg-surface-card rounded-xl p-1 border border-navy-600/20 w-fit">
        {tabs.map((tab) => (
          <button
            key={tab.id}
            onClick={() => setActiveTab(tab.id)}
            className={`flex items-center gap-2 px-4 py-2.5 rounded-lg text-sm font-medium transition-all
              ${activeTab === tab.id ? 'gradient-primary text-white' : 'text-text-secondary hover:text-white'}`}
          >
            <tab.icon className="w-4 h-4" />
            {tab.label}
          </button>
        ))}
      </div>

      {/* Profile Tab */}
      {activeTab === 'profile' && (
        <div className="bg-surface-card rounded-2xl p-6 border border-navy-600/20">
          {/* Avatar Section */}
          <div className="flex items-center gap-5 mb-8 pb-6 border-b border-navy-600/20">
            <div className="relative">
              <div className="w-20 h-20 rounded-full gradient-primary flex items-center justify-center text-2xl font-bold text-white">
                {profile.name.charAt(0)}
              </div>
              <button className="absolute -bottom-1 -right-1 w-7 h-7 rounded-full bg-brand-primary flex items-center justify-center">
                <HiOutlineCamera className="w-3.5 h-3.5 text-white" />
              </button>
            </div>
            <div>
              <h3 className="text-lg font-semibold text-white">{profile.name}</h3>
              <p className="text-sm text-text-muted capitalize">{user?.role || 'Teacher'}</p>
            </div>
          </div>

          {/* Form */}
          <form onSubmit={(e) => { e.preventDefault(); toast.success('Profile updated!'); }} className="space-y-5">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
              <Input
                label="Full Name"
                value={profile.name}
                onChange={(e) => setProfile({ ...profile, name: e.target.value })}
                icon={HiOutlineUser}
              />
              <Input
                label="Email"
                type="email"
                value={profile.email}
                onChange={(e) => setProfile({ ...profile, email: e.target.value })}
                icon={HiOutlineMail}
              />
              <Input
                label="Phone"
                value={profile.phone}
                onChange={(e) => setProfile({ ...profile, phone: e.target.value })}
              />
              <Input
                label="Per Class Rate (₹)"
                value={profile.perClassRate}
                onChange={(e) => setProfile({ ...profile, perClassRate: e.target.value })}
              />
            </div>
            <Input
              label="Qualifications"
              value={profile.qualifications}
              onChange={(e) => setProfile({ ...profile, qualifications: e.target.value })}
            />
            <div className="space-y-1.5">
              <label className="block text-sm font-medium text-text-secondary">Bio</label>
              <textarea
                value={profile.bio}
                onChange={(e) => setProfile({ ...profile, bio: e.target.value })}
                rows={4}
                className="w-full bg-navy-700 border border-navy-600/30 rounded-xl px-4 py-3 text-sm text-white placeholder-text-muted focus:outline-none focus:ring-2 focus:ring-brand-primary/50 resize-none"
              />
            </div>
            <div className="flex justify-end">
              <Button variant="primary" type="submit">Save Changes</Button>
            </div>
          </form>
        </div>
      )}

      {/* Security Tab */}
      {activeTab === 'security' && (
        <div className="bg-surface-card rounded-2xl p-6 border border-navy-600/20">
          <h3 className="text-lg font-semibold text-white mb-6">Change Password</h3>
          <form onSubmit={(e) => { e.preventDefault(); toast.success('Password updated!'); }} className="space-y-5 max-w-md">
            <Input
              label="Current Password"
              type="password"
              placeholder="••••••••"
              icon={HiOutlineLockClosed}
              required
            />
            <Input
              label="New Password"
              type="password"
              placeholder="••••••••"
              icon={HiOutlineLockClosed}
              required
            />
            <Input
              label="Confirm New Password"
              type="password"
              placeholder="••••••••"
              icon={HiOutlineLockClosed}
              required
            />
            <Button variant="primary" type="submit">Update Password</Button>
          </form>
        </div>
      )}

      {/* Notifications Tab */}
      {activeTab === 'notifications' && (
        <div className="bg-surface-card rounded-2xl p-6 border border-navy-600/20">
          <h3 className="text-lg font-semibold text-white mb-6">Notification Preferences</h3>
          <div className="space-y-4 max-w-md">
            {[
              { key: 'emailNotifs', label: 'Email Notifications', desc: 'Receive email updates' },
              { key: 'classReminders', label: 'Class Reminders', desc: 'Get reminders before classes' },
              { key: 'assignmentUpdates', label: 'Assignment Updates', desc: 'New assignments and deadlines' },
              { key: 'newEnrollments', label: 'New Enrollments', desc: 'When students enroll in your courses' },
              { key: 'platformUpdates', label: 'Platform Updates', desc: 'News and feature updates' },
            ].map((item) => (
              <div key={item.key} className="flex items-center justify-between p-4 rounded-xl bg-navy-700/50">
                <div>
                  <p className="text-sm font-medium text-white">{item.label}</p>
                  <p className="text-xs text-text-muted">{item.desc}</p>
                </div>
                <button
                  onClick={() => setNotifications({ ...notifications, [item.key]: !notifications[item.key] })}
                  className={`relative w-11 h-6 rounded-full transition-colors duration-200
                    ${notifications[item.key] ? 'bg-brand-primary' : 'bg-navy-600'}`}
                >
                  <span className={`absolute top-0.5 left-0.5 w-5 h-5 bg-white rounded-full transition-transform duration-200
                    ${notifications[item.key] ? 'translate-x-5' : 'translate-x-0'}`} />
                </button>
              </div>
            ))}
            <Button variant="primary" onClick={() => toast.success('Preferences saved!')}>
              Save Preferences
            </Button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Settings;
