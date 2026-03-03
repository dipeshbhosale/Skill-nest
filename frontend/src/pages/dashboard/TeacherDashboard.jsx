import StatsCard from '../../components/ui/StatsCard';
import CourseCard from '../../components/ui/CourseCard';
import {
  HiOutlineBookOpen,
  HiOutlineAcademicCap,
  HiOutlineUsers,
  HiOutlineCurrencyDollar,
  HiOutlineCalendar,
  HiOutlineClock,
} from 'react-icons/hi';

const TeacherDashboard = () => {
  // Mock data
  const stats = [
    { title: 'Total Courses', value: '12', change: 8, icon: HiOutlineBookOpen, color: 'primary' },
    { title: 'Total Students', value: '248', change: 12, icon: HiOutlineUsers, color: 'success' },
    { title: 'Classes This Week', value: '18', change: -3, icon: HiOutlineAcademicCap, color: 'warning' },
    { title: 'Total Earnings', value: '₹45,200', change: 15, icon: HiOutlineCurrencyDollar, color: 'info' },
  ];

  const recentCourses = [
    { id: 1, title: 'Advanced Java Programming', description: 'Deep dive into Java Spring Boot microservices', category: 'Programming', teacherName: 'Prof. Smith', totalClasses: 24, enrolledStudents: 45 },
    { id: 2, title: 'Data Structures & Algorithms', description: 'Master DSA for competitive programming', category: 'CS Fundamentals', teacherName: 'Prof. Smith', totalClasses: 30, enrolledStudents: 62 },
    { id: 3, title: 'React.js Masterclass', description: 'Build modern web apps with React and Tailwind CSS', category: 'Web Dev', teacherName: 'Prof. Smith', totalClasses: 20, enrolledStudents: 38 },
  ];

  const upcomingClasses = [
    { id: 1, title: 'Java - Spring Boot Basics', course: 'Advanced Java', time: '10:00 AM', date: 'Today', students: 45 },
    { id: 2, title: 'Trees & Graphs', course: 'DSA', time: '2:00 PM', date: 'Today', students: 62 },
    { id: 3, title: 'React Hooks Deep Dive', course: 'React.js', time: '11:00 AM', date: 'Tomorrow', students: 38 },
    { id: 4, title: 'Microservices Architecture', course: 'Advanced Java', time: '3:00 PM', date: 'Tomorrow', students: 45 },
  ];

  const recentSubmissions = [
    { id: 1, student: 'Alice Johnson', assignment: 'Spring Boot REST API', course: 'Java', status: 'pending', time: '2 hours ago' },
    { id: 2, student: 'Bob Wilson', assignment: 'Binary Search Tree', course: 'DSA', status: 'graded', time: '5 hours ago' },
    { id: 3, student: 'Carol Davis', assignment: 'React Todo App', course: 'React.js', status: 'pending', time: '1 day ago' },
  ];

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
                  <p className="text-xs text-text-muted">{cls.course}</p>
                </div>
                <div className="text-right flex-shrink-0">
                  <p className="text-sm text-white">{cls.time}</p>
                  <p className="text-xs text-text-muted">{cls.date}</p>
                </div>
                <div className="flex items-center gap-1 flex-shrink-0">
                  <HiOutlineUsers className="w-4 h-4 text-text-muted" />
                  <span className="text-xs text-text-muted">{cls.students}</span>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Recent Submissions */}
        <div className="bg-surface-card rounded-2xl p-6 border border-navy-600/20">
          <div className="flex items-center justify-between mb-5">
            <h2 className="text-lg font-semibold text-white">Recent Submissions</h2>
            <button className="text-sm text-brand-primary hover:text-brand-accent">View All</button>
          </div>
          <div className="space-y-3">
            {recentSubmissions.map((sub) => (
              <div key={sub.id} className="p-3 rounded-xl bg-navy-700/50 hover:bg-navy-700 transition-colors">
                <div className="flex items-center justify-between mb-1">
                  <p className="text-sm font-medium text-white">{sub.student}</p>
                  <span className={`px-2 py-0.5 rounded-full text-xs font-medium
                    ${sub.status === 'graded' ? 'bg-success/20 text-success' : 'bg-warning/20 text-warning'}`}>
                    {sub.status}
                  </span>
                </div>
                <p className="text-xs text-text-muted">{sub.assignment} · {sub.course}</p>
                <p className="text-xs text-text-muted mt-1">{sub.time}</p>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* My Courses */}
      <div>
        <div className="flex items-center justify-between mb-5">
          <h2 className="text-lg font-semibold text-white">My Courses</h2>
          <button className="text-sm text-brand-primary hover:text-brand-accent">View All</button>
        </div>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {recentCourses.map((course) => (
            <CourseCard key={course.id} course={course} />
          ))}
        </div>
      </div>
    </div>
  );
};

export default TeacherDashboard;
