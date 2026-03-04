import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import PageHeader from '../../components/ui/PageHeader';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Badge from '../../components/ui/Badge';
import api from '../../config/api';
import { HiOutlinePlus, HiOutlineSearch, HiOutlineBookOpen } from 'react-icons/hi';

const CoursesPage = () => {
  const { user } = useAuth();
  const [courses, setCourses] = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchCourses = async () => {
      try {
        const res = await api.get('/courses').catch(() => ({ data: [] }));
        setCourses(res.data || []);
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    };
    fetchCourses();
  }, []);

  const filtered = courses.filter(
    (c) =>
      c.name?.toLowerCase().includes(search.toLowerCase()) ||
      c.code?.toLowerCase().includes(search.toLowerCase())
  );

  const colors = [
    'from-brand-primary to-brand-secondary',
    'from-success to-emerald-400',
    'from-info to-blue-400',
    'from-warning to-orange-400',
    'from-purple-500 to-pink-500',
    'from-teal-500 to-cyan-400',
  ];

  if (loading) {
    return (
      <div className="space-y-6">
        <PageHeader title="Courses" subtitle="Browse and manage all courses" />
        <div className="flex items-center justify-center py-12">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-brand-primary"></div>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <PageHeader
        title="Courses"
        subtitle="Browse and manage all courses"
        actions={
          (user?.role === 'admin' || user?.role === 'teacher') && (
            <Button icon={HiOutlinePlus}>Add Course</Button>
          )
        }
      />

      {/* Search bar */}
      <div className="flex items-center gap-2 bg-white rounded-xl px-4 py-3 border border-surface-border card-shadow max-w-md">
        <HiOutlineSearch className="w-5 h-5 text-text-muted" />
        <input
          type="text"
          placeholder="Search courses..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="bg-transparent outline-none text-sm text-navy-700 placeholder:text-text-muted w-full"
        />
      </div>

      {/* Course grid */}
      {filtered.length > 0 ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {filtered.map((course, i) => (
          <Card key={course.id} noPadding className="overflow-hidden hover:shadow-lg transition-shadow cursor-pointer">
            {/* Gradient header */}
            <div className={`h-32 bg-gradient-to-br ${colors[i % colors.length]} p-5 flex flex-col justify-between`}>
              <div className="flex items-center justify-between">
                <span className="text-xs font-medium text-white/80 bg-white/20 px-2.5 py-1 rounded-lg backdrop-blur">
                  {course.code}
                </span>
                <Badge variant={course.status === 'active' ? 'success' : course.status === 'upcoming' ? 'warning' : 'default'}>
                  {course.status}
                </Badge>
              </div>
              <h3 className="text-white font-bold text-lg leading-tight">{course.name}</h3>
            </div>

            {/* Details */}
            <div className="p-5">
              <div className="flex items-center gap-2 mb-3">
                <div className="w-8 h-8 rounded-full bg-brand-light flex items-center justify-center">
                  <span className="text-xs font-bold text-brand-primary">
                    {course.teacher?.[0] || 'T'}
                  </span>
                </div>
                <span className="text-sm text-navy-600">{course.teacher}</span>
              </div>

              <div className="flex items-center justify-between text-sm text-text-muted">
                <span>{course.students} students</span>
                <span>{course.credits} credits</span>
              </div>

              {/* Progress bar */}
              <div className="mt-3">
                <div className="flex justify-between text-xs mb-1">
                  <span className="text-text-muted">Progress</span>
                  <span className="text-navy-700 font-medium">{course.progress || 0}%</span>
                </div>
                <div className="w-full h-1.5 bg-surface-bg rounded-full overflow-hidden">
                  <div
                    className={`h-full rounded-full bg-gradient-to-r ${colors[i % colors.length]}`}
                    style={{ width: `${course.progress || 0}%` }}
                  />
                </div>
              </div>
            </div>
          </Card>
        ))}
      </div>
      ) : (
        <Card>
          <div className="text-center py-12">
            <HiOutlineBookOpen className="mx-auto h-16 w-16 text-text-muted mb-4" />
            <h3 className="text-lg font-semibold text-navy-800 mb-2">
              {search ? 'No courses found' : 'No courses available'}
            </h3>
            <p className="text-text-muted mb-6 max-w-md mx-auto">
              {search
                ? 'Try adjusting your search terms'
                : (user?.role === 'teacher' || user?.role === 'admin')
                ? 'Get started by creating your first course'
                : 'Check back later for available courses'}
            </p>
            {!search && (user?.role === 'teacher' || user?.role === 'admin') && (
              <Button icon={HiOutlinePlus}>Create Course</Button>
            )}
          </div>
        </Card>
      )}
    </div>
  );
};

export default CoursesPage;
