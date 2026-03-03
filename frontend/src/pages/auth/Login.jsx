import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import Input from '../../components/ui/Input';
import Button from '../../components/ui/Button';
import { HiOutlineMail, HiOutlineLockClosed, HiOutlineBookOpen } from 'react-icons/hi';
import toast from 'react-hot-toast';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!email || !password) {
      toast.error('Please fill in all fields');
      return;
    }
    setLoading(true);
    try {
      const user = await login(email, password);
      toast.success('Welcome back!');
      navigate('/dashboard');
    } catch (error) {
      toast.error(error || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  // Demo login handler
  const handleDemoLogin = (role) => {
    const demoUsers = {
      admin: { email: 'admin@skillnest.com', name: 'Admin User', role: 'admin' },
      teacher: { email: 'teacher@skillnest.com', name: 'Prof. Smith', role: 'teacher' },
      student: { email: 'student@skillnest.com', name: 'John Doe', role: 'student' },
    };
    const demoUser = demoUsers[role];
    localStorage.setItem('token', 'demo-token-' + role);
    localStorage.setItem('user', JSON.stringify(demoUser));
    window.location.href = '/dashboard';
  };

  return (
    <div className="min-h-screen bg-navy-900 flex">
      {/* Left - Branding Panel */}
      <div className="hidden lg:flex lg:w-1/2 gradient-primary items-center justify-center relative overflow-hidden">
        <div className="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGNpcmNsZSBjeD0iMjAiIGN5PSIyMCIgcj0iMSIgZmlsbD0icmdiYSgyNTUsMjU1LDI1NSwwLjEpIi8+PC9zdmc+')] opacity-50" />
        <div className="relative z-10 text-center px-12">
          <div className="w-20 h-20 rounded-2xl bg-white/20 backdrop-blur-sm flex items-center justify-center mx-auto mb-6">
            <HiOutlineBookOpen className="w-10 h-10 text-white" />
          </div>
          <h1 className="text-4xl font-bold text-white mb-4">Skill Nest</h1>
          <p className="text-lg text-white/80 max-w-md">
            Empowering education through technology. Learn, teach, and grow together.
          </p>
        </div>
      </div>

      {/* Right - Login Form */}
      <div className="flex-1 flex items-center justify-center p-8">
        <div className="w-full max-w-md">
          {/* Mobile Logo */}
          <div className="lg:hidden flex items-center gap-3 mb-8">
            <div className="w-10 h-10 rounded-xl gradient-primary flex items-center justify-center">
              <HiOutlineBookOpen className="w-6 h-6 text-white" />
            </div>
            <h1 className="text-xl font-bold text-white">Skill Nest</h1>
          </div>

          <h2 className="text-2xl font-bold text-white mb-2">Welcome Back</h2>
          <p className="text-text-secondary mb-8">Enter your credentials to access your account</p>

          <form onSubmit={handleSubmit} className="space-y-5">
            <Input
              label="Email"
              type="email"
              placeholder="your@email.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              icon={HiOutlineMail}
              required
            />
            <Input
              label="Password"
              type="password"
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              icon={HiOutlineLockClosed}
              required
            />

            <div className="flex items-center justify-between">
              <label className="flex items-center gap-2 text-sm text-text-secondary">
                <input type="checkbox" className="rounded border-navy-600" />
                Remember me
              </label>
              <Link to="/forgot-password" className="text-sm text-brand-primary hover:text-brand-accent">
                Forgot password?
              </Link>
            </div>

            <Button type="submit" variant="primary" size="lg" className="w-full" loading={loading}>
              Sign In
            </Button>
          </form>

          {/* Demo Login */}
          <div className="mt-6">
            <p className="text-xs text-text-muted text-center mb-3">Quick Demo Access</p>
            <div className="flex gap-2">
              <Button variant="secondary" size="sm" className="flex-1" onClick={() => handleDemoLogin('admin')}>
                Admin
              </Button>
              <Button variant="secondary" size="sm" className="flex-1" onClick={() => handleDemoLogin('teacher')}>
                Teacher
              </Button>
              <Button variant="secondary" size="sm" className="flex-1" onClick={() => handleDemoLogin('student')}>
                Student
              </Button>
            </div>
          </div>

          <p className="text-sm text-text-secondary text-center mt-6">
            Don't have an account?{' '}
            <Link to="/register" className="text-brand-primary hover:text-brand-accent font-medium">
              Sign Up
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Login;
