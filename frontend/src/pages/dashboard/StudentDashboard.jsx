import StatsCard from '../../components/ui/StatsCard';
import CourseCard from '../../components/ui/CourseCard';
import {
  HiOutlineBookOpen,
  HiOutlineAcademicCap,
  HiOutlineClipboardList,
  HiOutlineCalendar,
  HiOutlineClock,
} from 'react-icons/hi';

const StudentDashboard = () => {
  const stats = [
    { title: 'Enrolled Courses', value: '6', change: 2, icon: HiOutlineBookOpen, color: 'primary' },
    { title: 'Completed Classes', value: '42', change: 10, icon: HiOutlineAcademicCap, color: 'success' },
    { title: 'Pending Assignments', value: '3', change: -1, icon: HiOutlineClipboardList, color: 'warning' },
    { title: 'Attendance Rate', value: '92%', change: 5, icon: HiOutlineCalendar, color: 'info' },
  ];

  const enrolledCourses = [
    { id: 1, title: 'Advanced Java Programming', description: 'Deep dive into Java Spring Boot', category: 'Programming', teacherName: 'Prof. Smith', totalClasses: 24, enrolledStudents: 45, progress: 75 },
    { id: 2, title: 'Data Structures & Algorithms', description: 'Master DSA for interviews', category: 'CS', teacherName: 'Prof. Johnson', totalClasses: 30, enrolledStudents: 62, progress: 45 },
    { id: 3, title: 'React.js Masterclass', description: 'Build modern web apps', category: 'Web Dev', teacherName: 'Prof. Davis', totalClasses: 20, enrolledStudents: 38, progress: 30 },
  ];

  const upcomingClasses = [
    { id: 1, title: 'Spring Boot REST API', course: 'Advanced Java', time: '10:00 AM', date: 'Today' },
    { id: 2, title: 'Graph Algorithms', course: 'DSA', time: '2:00 PM', date: 'Today' },
    { id: 3, title: 'State Management', course: 'React.js', time: '11:00 AM', date: 'Tomorrow' },
  ];

  const pendingAssignments = [
    { id: 1, title: 'Build REST API', course: 'Advanced Java', dueDate: 'Mar 5, 2026', status: 'pending' },
    { id: 2, title: 'Implement BST', course: 'DSA', dueDate: 'Mar 7, 2026', status: 'pending' },
    { id: 3, title: 'Todo App with Hooks', course: 'React.js', dueDate: 'Mar 10, 2026', status: 'submitted' },
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
          <div className="space-y-3">
            {upcomingClasses.map((cls) => (
              <div key={cls.id} className="flex items-center gap-4 p-4 rounded-xl bg-navy-700/50 hover:bg-navy-700 transition-colors">
                <div className="w-12 h-12 rounded-xl gradient-primary flex items-center justify-center flex-shrink-0">
                  <HiOutlineClock className="w-5 h-5 text-white" />
                </div>
                <div className="flex-1">
                  <h3 className="text-sm font-medium text-white">{cls.title}</h3>
                  <p className="text-xs text-text-muted">{cls.course}</p>
                </div>
                <div className="text-right">
                  <p className="text-sm text-white">{cls.time}</p>
                  <p className="text-xs text-text-muted">{cls.date}</p>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Pending Assignments */}
        <div className="bg-surface-card rounded-2xl p-6 border border-navy-600/20">
          <h2 className="text-lg font-semibold text-white mb-5">Assignments</h2>
          <div className="space-y-3">
            {pendingAssignments.map((a) => (
              <div key={a.id} className="p-3 rounded-xl bg-navy-700/50">
                <div className="flex items-center justify-between mb-1">
                  <p className="text-sm font-medium text-white">{a.title}</p>
                  <span className={`px-2 py-0.5 rounded-full text-xs font-medium
                    ${a.status === 'submitted' ? 'bg-success/20 text-success' : 'bg-warning/20 text-warning'}`}>
                    {a.status}
                  </span>
                </div>
                <p className="text-xs text-text-muted">{a.course} · Due: {a.dueDate}</p>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* My Courses */}
      <div>
        <h2 className="text-lg font-semibold text-white mb-5">My Courses</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {enrolledCourses.map((course) => (
            <CourseCard key={course.id} course={course} />
          ))}
        </div>
      </div>
    </div>
  );
};

export default StudentDashboard;
