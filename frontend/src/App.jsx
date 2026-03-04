import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import ProtectedRoute from './components/auth/ProtectedRoute';
import DashboardLayout from './components/layout/DashboardLayout';
import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';
import AdminDashboard from './pages/dashboard/AdminDashboard';
import TeacherDashboard from './pages/dashboard/TeacherDashboard';
import StudentDashboard from './pages/dashboard/StudentDashboard';
import CoursesPage from './pages/courses/CoursesPage';
import AssignmentsPage from './pages/assignments/AssignmentsPage';
import AttendancePage from './pages/attendance/AttendancePage';
import CalendarPage from './pages/calendar/CalendarPage';
import ClassesPage from './pages/classes/ClassesPage';
import StudentsPage from './pages/students/StudentsPage';
import MaterialsPage from './pages/materials/MaterialsPage';
import EarningsPage from './pages/earnings/EarningsPage';
import SettingsPage from './pages/settings/SettingsPage';

const DashboardRedirect = () => {
  const { user } = useAuth();
  if (user?.role === 'admin') return <Navigate to="/admin/dashboard" replace />;
  if (user?.role === 'teacher') return <Navigate to="/teacher/dashboard" replace />;
  return <Navigate to="/student/dashboard" replace />;
};

function App() {
  return (
    <Routes>
      {/* Public */}
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />

      {/* Protected – wrapped by layout */}
      <Route
        path="/"
        element={
          <ProtectedRoute>
            <DashboardLayout />
          </ProtectedRoute>
        }
      >
        <Route index element={<DashboardRedirect />} />
        <Route path="dashboard" element={<DashboardRedirect />} />

        {/* Admin */}
        <Route
          path="admin/dashboard"
          element={
            <ProtectedRoute allowedRoles={['admin']}>
              <AdminDashboard />
            </ProtectedRoute>
          }
        />

        {/* Teacher */}
        <Route
          path="teacher/dashboard"
          element={
            <ProtectedRoute allowedRoles={['teacher']}>
              <TeacherDashboard />
            </ProtectedRoute>
          }
        />

        {/* Student */}
        <Route
          path="student/dashboard"
          element={
            <ProtectedRoute allowedRoles={['student']}>
              <StudentDashboard />
            </ProtectedRoute>
          }
        />

        {/* Shared pages */}
        <Route path="courses" element={<CoursesPage />} />
        <Route path="assignments" element={<AssignmentsPage />} />
        <Route path="attendance" element={<AttendancePage />} />
        <Route path="calendar" element={<CalendarPage />} />
        <Route path="classes" element={<ClassesPage />} />
        <Route path="students" element={<StudentsPage />} />
        <Route path="materials" element={<MaterialsPage />} />
        <Route path="earnings" element={<EarningsPage />} />
        <Route path="settings" element={<SettingsPage />} />
      </Route>

      {/* Fallback */}
      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
}

export default App;
