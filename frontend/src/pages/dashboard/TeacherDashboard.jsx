import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import StatsCard from '../../components/ui/StatsCard';
import Card from '../../components/ui/Card';
import PageHeader from '../../components/ui/PageHeader';
import api from '../../config/api';
import {
  HiOutlineBookOpen,
  HiOutlineUserGroup,
  HiOutlineClipboardList,
  HiOutlineCurrencyDollar,
  HiOutlineCalendar,
  HiOutlinePlus,
} from 'react-icons/hi';

const TeacherDashboard = () => {
  const { user } = useAuth();
  const [courses, setCourses] = useState([]);
  const [upcomingClasses, setUpcomingClasses] = useState([]);
  const [totalStudents, setTotalStudents] = useState(0);
  const [assignments, setAssignments] = useState([]);
  const [earnings, setEarnings] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        // Fetch teacher's courses
        const coursesRes = await api.get('/courses/my-courses').catch(() => ({ data: [] }));
        setCourses(coursesRes.data || []);

        // Fetch upcoming classes
        const classesRes = await api.get('/classes/upcoming').catch(() => ({ data: [] }));
        setUpcomingClasses(classesRes.data || []);

        // Fetch total students
        const studentsRes = await api.get('/teachers/stats/students').catch(() => ({ data: { total: 0 } }));
        setTotalStudents(studentsRes.data?.total || 0);

        // Fetch assignments
        const assignmentsRes = await api.get('/assignments/my-assignments').catch(() => ({ data: [] }));
        setAssignments(assignmentsRes.data || []);

        // Fetch earnings
        const earningsRes = await api.get('/earnings/total').catch(() => ({ data: { total: 0 } }));
        setEarnings(earningsRes.data?.total || 0);
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchDashboardData();
  }, []);

  const stats = [
    { title: 'My Courses', value: courses.length.toString(), icon: HiOutlineBookOpen, color: 'primary' },
    { title: 'Total Students', value: totalStudents.toString(), icon: HiOutlineUserGroup, color: 'success' },
    { title: 'Assignments', value: assignments.length.toString(), icon: HiOutlineClipboardList, color: 'warning' },
    { title: 'Earnings', value: `₹${earnings.toLocaleString()}`, icon: HiOutlineCurrencyDollar, color: 'info' },
  ];

  if (loading) {
    return (
      <div className="space-y-6">
        <PageHeader
          title={`Welcome, ${user?.name || 'Teacher'}`}
          subtitle="Loading your dashboard..."
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
        title={`Welcome, ${user?.name || 'Teacher'}`}
        subtitle="Here's what's happening with your classes today."
      />

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
        {stats.map((stat, i) => (
          <StatsCard key={i} {...stat} />
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Upcoming Classes */}
        <div className="lg:col-span-2">
          <Card>
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-semibold text-navy-800">Upcoming Classes</h2>
              <a
                href="/classes"
                className="text-sm text-brand-primary hover:text-brand-dark font-medium"
              >
                View All
              </a>
            </div>
            {upcomingClasses.length > 0 ? (
              <div className="space-y-3">
                {upcomingClasses.slice(0, 3).map((cls) => (
                  <div
                    key={cls.id}
                    className="flex items-center justify-between p-4 rounded-xl bg-surface-bg border border-surface-border hover:shadow-sm transition-shadow"
                  >
                    <div className="flex items-center gap-4">
                      <div className="w-12 h-12 rounded-xl bg-brand-light flex items-center justify-center">
                        <HiOutlineCalendar className="w-6 h-6 text-brand-primary" />
                      </div>
                      <div>
                        <p className="font-semibold text-navy-800 text-sm">{cls.title || cls.courseName}</p>
                        <p className="text-xs text-text-muted">
                          {new Date(cls.date).toLocaleDateString()} at {cls.startTime}
                        </p>
                      </div>
                    </div>
                    <div className="text-right">
                      <p className="text-sm font-medium text-navy-700">{cls.enrolledStudents || 0}</p>
                      <p className="text-xs text-text-muted">students</p>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="text-center py-12">
                <HiOutlineCalendar className="mx-auto h-12 w-12 text-text-muted mb-3" />
                <p className="text-text-muted">No upcoming classes scheduled</p>
                <p className="text-sm text-text-muted mt-1">Create a class to get started</p>
              </div>
            )}
          </Card>
        </div>

        {/* Quick Actions */}
        <Card>
          <h2 className="text-lg font-semibold text-navy-800 mb-4">Quick Actions</h2>
          <div className="space-y-3">
            <a
              href="/courses/new"
              className="flex items-center gap-3 p-3 rounded-xl bg-brand-light border border-brand-primary/20 hover:bg-brand-primary/10 transition-colors"
            >
              <div className="w-10 h-10 rounded-lg bg-brand-primary flex items-center justify-center">
                <HiOutlinePlus className="w-5 h-5 text-white" />
              </div>
              <div>
                <p className="font-medium text-navy-800 text-sm">Create Course</p>
                <p className="text-xs text-text-muted">Start a new course</p>
              </div>
            </a>
            <a
              href="/assignments/new"
              className="flex items-center gap-3 p-3 rounded-xl bg-surface-bg border border-surface-border hover:bg-surface-bg/80 transition-colors"
            >
              <div className="w-10 h-10 rounded-lg bg-warning/10 flex items-center justify-center">
                <HiOutlineClipboardList className="w-5 h-5 text-warning" />
              </div>
              <div>
                <p className="font-medium text-navy-800 text-sm">New Assignment</p>
                <p className="text-xs text-text-muted">Create assignment</p>
              </div>
            </a>
            <a
              href="/classes/new"
              className="flex items-center gap-3 p-3 rounded-xl bg-surface-bg border border-surface-border hover:bg-surface-bg/80 transition-colors"
            >
              <div className="w-10 h-10 rounded-lg bg-success/10 flex items-center justify-center">
                <HiOutlineCalendar className="w-5 h-5 text-success" />
              </div>
              <div>
                <p className="font-medium text-navy-800 text-sm">Schedule Class</p>
                <p className="text-xs text-text-muted">Add to calendar</p>
              </div>
            </a>
          </div>
        </Card>
      </div>

      {/* Getting Started */}
      {courses.length === 0 && (
        <Card>
          <h2 className="text-lg font-semibold text-navy-800 mb-4">Get Started</h2>
          <div className="text-center py-8">
            <HiOutlineBookOpen className="mx-auto h-16 w-16 text-brand-primary mb-4" />
            <h3 className="text-lg font-semibold text-navy-800 mb-2">Welcome to Skill Nest!</h3>
            <p className="text-text-muted mb-6 max-w-md mx-auto">
              Start your teaching journey by creating your first course. Share your knowledge with students worldwide.
            </p>
            <a
              href="/courses/new"
              className="inline-flex items-center px-6 py-3 bg-brand-primary text-white rounded-xl font-medium hover:bg-brand-dark transition-colors"
            >
              <HiOutlinePlus className="w-5 h-5 mr-2" />
              Create Your First Course
            </a>
          </div>
        </Card>
      )}
    </div>
  );
};

export default TeacherDashboard;
