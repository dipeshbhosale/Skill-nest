import { useState, useEffect } from 'react';
import StatsCard from '../../components/ui/StatsCard';
import CourseCard from '../../components/ui/CourseCard';
import api from '../../config/api';
import {
  HiOutlineBookOpen,
  HiOutlineAcademicCap,
  HiOutlineClipboardList,
  HiOutlineCalendar,
  HiOutlineClock,
} from 'react-icons/hi';

const StudentDashboard = () => {
  const [enrolledCourses, setEnrolledCourses] = useState([]);
  const [upcomingClasses, setUpcomingClasses] = useState([]);
  const [assignments, setAssignments] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [coursesRes, classesRes, assignmentsRes] = await Promise.all([
          api.get('/courses').catch(() => ({ data: [] })),
          api.get('/classes').catch(() => ({ data: [] })),
          api.get('/assignments').catch(() => ({ data: [] })),
        ]);
        setEnrolledCourses(coursesRes.data || []);
        setUpcomingClasses(classesRes.data || []);
        setAssignments(assignmentsRes.data || []);
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const pendingAssignments = assignments.filter(a => a.status !== 'submitted');

  const stats = [
    { title: 'Enrolled Courses', value: enrolledCourses.length.toString(), change: 0, icon: HiOutlineBookOpen, color: 'primary' },
    { title: 'Completed Classes', value: '0', change: 0, icon: HiOutlineAcademicCap, color: 'success' },
    { title: 'Pending Assignments', value: pendingAssignments.length.toString(), change: 0, icon: HiOutlineClipboardList, color: 'warning' },
    { title: 'Attendance Rate', value: 'N/A', change: 0, icon: HiOutlineCalendar, color: 'info' },
  ];

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-white">Student Dashboard</h1>
        <p className="text-text-secondary mt-1">Track your learning progress.</p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {stats.map((stat, i) => (
          <StatsCard key={i} {...stat} />
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Upcoming Classes */}
        <div className="lg:col-span-2 bg-surface-card rounded-2xl p-6 border border-navy-600/20">
          <h2 className="text-lg font-semibold text-white mb-5">Upcoming Classes</h2>
          {upcomingClasses.length === 0 ? (
            <p className="text-text-muted text-sm">No upcoming classes scheduled.</p>
          ) : (
            <div className="space-y-3">
              {upcomingClasses.map((cls) => (
                <div key={cls.id} className="flex items-center gap-4 p-4 rounded-xl bg-navy-700/50 hover:bg-navy-700 transition-colors">
                  <div className="w-12 h-12 rounded-xl gradient-primary flex items-center justify-center flex-shrink-0">
                    <HiOutlineClock className="w-5 h-5 text-white" />
                  </div>
                  <div className="flex-1">
                    <h3 className="text-sm font-medium text-white">{cls.title}</h3>
                    <p className="text-xs text-text-muted">{cls.courseId}</p>
                  </div>
                  <div className="text-right">
                    <p className="text-sm text-white">{cls.time}</p>
                    <p className="text-xs text-text-muted">{cls.date}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Pending Assignments */}
        <div className="bg-surface-card rounded-2xl p-6 border border-navy-600/20">
          <h2 className="text-lg font-semibold text-white mb-5">Assignments</h2>
          {assignments.length === 0 ? (
            <p className="text-text-muted text-sm">No assignments yet.</p>
          ) : (
            <div className="space-y-3">
              {assignments.map((a) => (
                <div key={a.id} className="p-3 rounded-xl bg-navy-700/50">
                  <div className="flex items-center justify-between mb-1">
                    <p className="text-sm font-medium text-white">{a.title}</p>
                    <span className={`px-2 py-0.5 rounded-full text-xs font-medium
                      ${a.status === 'submitted' ? 'bg-success/20 text-success' : 'bg-warning/20 text-warning'}`}>
                      {a.status || 'pending'}
                    </span>
                  </div>
                  <p className="text-xs text-text-muted">Due: {a.dueDate || 'N/A'}</p>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* My Courses */}
      <div>
        <h2 className="text-lg font-semibold text-white mb-5">My Courses</h2>
        {enrolledCourses.length === 0 ? (
          <p className="text-text-muted text-sm">No courses enrolled yet. Start exploring courses!</p>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {enrolledCourses.map((course) => (
              <CourseCard key={course.id} course={course} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default StudentDashboard;
