import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import StatsCard from '../../components/ui/StatsCard';
import Card from '../../components/ui/Card';
import PageHeader from '../../components/ui/PageHeader';
import api from '../../config/api';
import {
  HiOutlineBookOpen,
  HiOutlineClipboardList,
  HiOutlineCalendar,
  HiOutlineAcademicCap,
} from 'react-icons/hi';

const StudentDashboard = () => {
  const { user } = useAuth();
  const [enrolledCourses, setEnrolledCourses] = useState([]);
  const [todayClasses, setTodayClasses] = useState([]);
  const [assignments, setAssignments] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        // Fetch enrolled courses
        const enrollmentsRes = await api.get('/enrollments/my-enrollments').catch(() => ({ data: [] }));
        setEnrolledCourses(enrollmentsRes.data || []);

        // Fetch today's classes
        const classesRes = await api.get('/classes/today').catch(() => ({ data: [] }));
        setTodayClasses(classesRes.data || []);

        // Fetch pending assignments
        const assignmentsRes = await api.get('/assignments/my-assignments').catch(() => ({ data: [] }));
        setAssignments(assignmentsRes.data || []);
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchDashboardData();
  }, []);

  const pendingAssignments = assignments.filter(a => !a.submitted);

  const stats = [
    { title: 'Enrolled Courses', value: enrolledCourses.length.toString(), icon: HiOutlineBookOpen, color: 'primary' },
    { title: 'Pending Assignments', value: pendingAssignments.length.toString(), icon: HiOutlineClipboardList, color: 'warning' },
    { title: 'Classes Today', value: todayClasses.length.toString(), icon: HiOutlineCalendar, color: 'success' },
    { title: 'Overall Grade', value: 'N/A', icon: HiOutlineAcademicCap, color: 'info' },
  ];

  if (loading) {
    return (
      <div className="space-y-6">
        <PageHeader
          title={`Welcome, ${user?.name || 'Student'}`}
          subtitle="Loading your academic overview..."
        />
        <div className="flex items-center justify-center py-12">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-brand-primary"></div>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <PageHeader
        title={`Welcome, ${user?.name || 'Student'}`}
        subtitle="Here's your academic overview for today."
      />

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
        {stats.map((stat, i) => (
          <StatsCard key={i} {...stat} />
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Today's Schedule */}
        <div className="lg:col-span-2">
          <Card>
            <h2 className="text-lg font-semibold text-navy-800 mb-4">Today's Schedule</h2>
            {todayClasses.length > 0 ? (
              <div className="space-y-3">
                {todayClasses.map((item) => (
                  <div
                    key={item.id}
                    className="flex items-center justify-between p-4 rounded-xl bg-surface-bg border border-surface-border"
                  >
                    <div className="flex items-center gap-4">
                      <div className="w-12 h-12 rounded-xl bg-brand-light flex items-center justify-center">
                        <HiOutlineBookOpen className="w-6 h-6 text-brand-primary" />
                      </div>
                      <div>
                        <p className="font-semibold text-navy-800 text-sm">{item.courseName || item.title}</p>
                        <p className="text-xs text-text-muted">
                          {item.teacherName || 'Instructor'} {item.room && `• ${item.room}`}
                        </p>
                      </div>
                    </div>
                    <span className="text-sm font-medium text-navy-600 bg-surface-bg px-3 py-1 rounded-lg">
                      {item.startTime || 'TBD'}
                    </span>
                  </div>
                ))}
              </div>
            ) : (
              <div className="text-center py-12">
                <HiOutlineCalendar className="mx-auto h-12 w-12 text-text-muted mb-3" />
                <p className="text-text-muted">No classes scheduled for today</p>
                <p className="text-sm text-text-muted mt-1">Check your calendar for upcoming sessions</p>
              </div>
            )}
          </Card>
        </div>

        {/* Pending Assignments */}
        <Card>
          <h2 className="text-lg font-semibold text-navy-800 mb-4">Pending Assignments</h2>
          {pendingAssignments.length > 0 ? (
            <div className="space-y-3">
              {pendingAssignments.slice(0, 3).map((a) => (
                <div key={a.id} className="p-3 rounded-xl bg-surface-bg border border-surface-border">
                  <p className="font-medium text-navy-800 text-sm">{a.title}</p>
                  <p className="text-xs text-text-muted mt-1">{a.courseName}</p>
                  <div className="flex items-center justify-between mt-2">
                    <span className="text-xs text-text-muted">Due: {new Date(a.dueDate).toLocaleDateString()}</span>
                    <span className="text-xs px-2 py-0.5 rounded-full font-medium bg-red-50 text-error">
                      Pending
                    </span>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-12">
              <HiOutlineClipboardList className="mx-auto h-12 w-12 text-text-muted mb-3" />
              <p className="text-text-muted">No pending assignments</p>
              <p className="text-sm text-text-muted mt-1">You're all caught up!</p>
            </div>
          )}
        </Card>
      </div>

      {/* Getting Started */}
      {enrolledCourses.length === 0 && (
        <Card>
          <h2 className="text-lg font-semibold text-navy-800 mb-4">Get Started</h2>
          <div className="text-center py-8">
            <HiOutlineAcademicCap className="mx-auto h-16 w-16 text-brand-primary mb-4" />
            <h3 className="text-lg font-semibold text-navy-800 mb-2">Welcome to Skill Nest!</h3>
            <p className="text-text-muted mb-6 max-w-md mx-auto">
              Start your learning journey by enrolling in courses. Browse available courses to get started.
            </p>
            <a
              href="/courses"
              className="inline-flex items-center px-6 py-3 bg-brand-primary text-white rounded-xl font-medium hover:bg-brand-dark transition-colors"
            >
              Browse Courses
            </a>
          </div>
        </Card>
      )}
    </div>
  );
};

export default StudentDashboard;
