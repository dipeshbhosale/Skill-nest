import { useState, useEffect } from 'react';
import StatsCard from '../../components/ui/StatsCard';
import CourseCard from '../../components/ui/CourseCard';
import api from '../../config/api';
import {
  HiOutlineBookOpen,
  HiOutlineAcademicCap,
  HiOutlineUsers,
  HiOutlineCurrencyDollar,
  HiOutlineCalendar,
  HiOutlineClock,
} from 'react-icons/hi';

const TeacherDashboard = () => {
  const [courses, setCourses] = useState([]);
  const [classes, setClasses] = useState([]);
  const [submissions, setSubmissions] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [coursesRes, classesRes, submissionsRes] = await Promise.all([
          api.get('/courses').catch(() => ({ data: [] })),
          api.get('/classes').catch(() => ({ data: [] })),
          api.get('/submissions').catch(() => ({ data: [] })),
        ]);
        setCourses(coursesRes.data || []);
        setClasses(classesRes.data || []);
        setSubmissions(submissionsRes.data || []);
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const totalStudents = courses.reduce((sum, c) => sum + (c.enrolledStudents || 0), 0);

  const stats = [
    { title: 'Total Courses', value: courses.length.toString(), change: 0, icon: HiOutlineBookOpen, color: 'primary' },
    { title: 'Total Students', value: totalStudents.toString(), change: 0, icon: HiOutlineUsers, color: 'success' },
    { title: 'Classes This Week', value: classes.length.toString(), change: 0, icon: HiOutlineAcademicCap, color: 'warning' },
    { title: 'Total Earnings', value: '₹0', change: 0, icon: HiOutlineCurrencyDollar, color: 'info' },
  ];

  const upcomingClasses = classes.slice(0, 4);
  const recentSubmissions = submissions.slice(0, 3);
  const recentCourses = courses.slice(0, 3);

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-white">Teacher Dashboard</h1>
          <p className="text-text-secondary mt-1">Welcome back! Here's your overview.</p>
        </div>
        <button className="inline-flex items-center gap-2 gradient-primary text-white px-5 py-2.5 rounded-xl text-sm font-medium shadow-lg shadow-brand-primary/25 hover:shadow-brand-primary/40 transition-all">
          <HiOutlineBookOpen className="w-4 h-4" />
          Create Course
        </button>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {stats.map((stat, i) => (
          <StatsCard key={i} {...stat} />
        ))}
      </div>

      {/* Main Content Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Upcoming Classes */}
        <div className="lg:col-span-2 bg-surface-card rounded-2xl p-6 border border-navy-600/20">
          <div className="flex items-center justify-between mb-5">
            <h2 className="text-lg font-semibold text-white">Upcoming Classes</h2>
            <button className="text-sm text-brand-primary hover:text-brand-accent">View All</button>
          </div>
          {upcomingClasses.length === 0 ? (
            <p className="text-text-muted text-sm">No upcoming classes scheduled.</p>
          ) : (
            <div className="space-y-3">
              {upcomingClasses.map((cls) => (
                <div
                  key={cls.id}
                  className="flex items-center gap-4 p-4 rounded-xl bg-navy-700/50 hover:bg-navy-700 transition-colors"
                >
                  <div className="w-12 h-12 rounded-xl gradient-primary flex items-center justify-center flex-shrink-0">
                    <HiOutlineClock className="w-5 h-5 text-white" />
                  </div>
                  <div className="flex-1 min-w-0">
                    <h3 className="text-sm font-medium text-white truncate">{cls.title}</h3>
                    <p className="text-xs text-text-muted">{cls.courseId}</p>
                  </div>
                  <div className="text-right flex-shrink-0">
                    <p className="text-sm text-white">{cls.time}</p>
                    <p className="text-xs text-text-muted">{cls.date}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Recent Submissions */}
        <div className="bg-surface-card rounded-2xl p-6 border border-navy-600/20">
          <div className="flex items-center justify-between mb-5">
            <h2 className="text-lg font-semibold text-white">Recent Submissions</h2>
            <button className="text-sm text-brand-primary hover:text-brand-accent">View All</button>
          </div>
          {recentSubmissions.length === 0 ? (
            <p className="text-text-muted text-sm">No submissions yet.</p>
          ) : (
            <div className="space-y-3">
              {recentSubmissions.map((sub) => (
                <div key={sub.id} className="p-3 rounded-xl bg-navy-700/50 hover:bg-navy-700 transition-colors">
                  <div className="flex items-center justify-between mb-1">
                    <p className="text-sm font-medium text-white">{sub.studentName || 'Student'}</p>
                    <span className={`px-2 py-0.5 rounded-full text-xs font-medium
                      ${sub.status === 'graded' ? 'bg-success/20 text-success' : 'bg-warning/20 text-warning'}`}>
                      {sub.status || 'pending'}
                    </span>
                  </div>
                  <p className="text-xs text-text-muted">{sub.assignmentId}</p>
                  <p className="text-xs text-text-muted mt-1">{sub.submittedAt || ''}</p>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* My Courses */}
      <div>
        <div className="flex items-center justify-between mb-5">
          <h2 className="text-lg font-semibold text-white">My Courses</h2>
          <button className="text-sm text-brand-primary hover:text-brand-accent">View All</button>
        </div>
        {recentCourses.length === 0 ? (
          <p className="text-text-muted text-sm">No courses created yet. Create your first course!</p>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {recentCourses.map((course) => (
              <CourseCard key={course.id} course={course} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default TeacherDashboard;
