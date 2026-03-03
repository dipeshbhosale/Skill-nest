import { useAuth } from '../../context/AuthContext';
import TeacherDashboard from './TeacherDashboard';
import StudentDashboard from './StudentDashboard';
import AdminDashboard from './AdminDashboard';

const Dashboard = () => {
  const { isAdmin, isTeacher, isStudent } = useAuth();

  if (isAdmin) return <AdminDashboard />;
  if (isTeacher) return <TeacherDashboard />;
  return <StudentDashboard />;
};

export default Dashboard;
