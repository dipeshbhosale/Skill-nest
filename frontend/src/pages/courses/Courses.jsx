import { useState, useEffect } from 'react';
import CourseCard from '../../components/ui/CourseCard';
import Button from '../../components/ui/Button';
import Modal from '../../components/ui/Modal';
import Input from '../../components/ui/Input';
import { useAuth } from '../../context/AuthContext';
import api from '../../config/api';
import { HiOutlinePlus, HiOutlineSearch, HiOutlineFilter } from 'react-icons/hi';
import toast from 'react-hot-toast';

const Courses = () => {
  const { isTeacher, isAdmin } = useAuth();
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [newCourse, setNewCourse] = useState({ title: '', description: '', category: '' });
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchCourses = async () => {
      try {
        const response = await api.get('/courses');
        setCourses(response.data || []);
      } catch (error) {
        console.error('Error fetching courses:', error);
        setCourses([]);
      } finally {
        setLoading(false);
      }
    };
    fetchCourses();
  }, []);

  const categories = ['all', ...new Set(courses.map(c => c.category).filter(Boolean))];

  const filteredCourses = courses.filter((c) => {
    const matchesSearch = c.title.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesCategory = selectedCategory === 'all' || c.category === selectedCategory;
    return matchesSearch && matchesCategory;
  });

  const handleCreateCourse = (e) => {
    e.preventDefault();
    toast.success('Course created successfully!');
    setShowCreateModal(false);
    setNewCourse({ title: '', description: '', category: '' });
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-white">Courses</h1>
          <p className="text-text-secondary mt-1">{isTeacher ? 'Manage your courses' : 'Browse and enroll in courses'}</p>
        </div>
        {(isTeacher || isAdmin) && (
          <Button variant="primary" icon={HiOutlinePlus} onClick={() => setShowCreateModal(true)}>
            Create Course
          </Button>
        )}
      </div>

      {/* Filters */}
      <div className="flex flex-col sm:flex-row gap-4">
        <div className="flex-1 flex items-center bg-surface-card rounded-xl px-4 py-3 border border-navy-600/20">
          <HiOutlineSearch className="w-5 h-5 text-text-muted mr-3" />
          <input
            type="text"
            placeholder="Search courses..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="bg-transparent text-sm text-white placeholder-text-muted outline-none flex-1"
          />
        </div>
        <div className="flex gap-2 overflow-x-auto pb-1">
          {categories.map((cat) => (
            <button
              key={cat}
              onClick={() => setSelectedCategory(cat)}
              className={`px-4 py-2 rounded-xl text-sm font-medium whitespace-nowrap transition-all
                ${selectedCategory === cat
                  ? 'gradient-primary text-white'
                  : 'bg-surface-card text-text-secondary border border-navy-600/20 hover:border-brand-primary/30'}`}
            >
              {cat === 'all' ? 'All' : cat}
            </button>
          ))}
        </div>
      </div>

      {/* Courses Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {filteredCourses.map((course) => (
          <CourseCard key={course.id} course={course} />
        ))}
      </div>

      {filteredCourses.length === 0 && (
        <div className="text-center py-12">
          <p className="text-text-muted text-lg">No courses found</p>
        </div>
      )}

      {/* Create Course Modal */}
      <Modal isOpen={showCreateModal} onClose={() => setShowCreateModal(false)} title="Create New Course" size="lg">
        <form onSubmit={handleCreateCourse} className="space-y-4">
          <Input
            label="Course Title"
            placeholder="e.g., Advanced Java Programming"
            value={newCourse.title}
            onChange={(e) => setNewCourse({ ...newCourse, title: e.target.value })}
            required
          />
          <div className="space-y-1.5">
            <label className="block text-sm font-medium text-text-secondary">Description</label>
            <textarea
              placeholder="Describe your course..."
              value={newCourse.description}
              onChange={(e) => setNewCourse({ ...newCourse, description: e.target.value })}
              rows={4}
              className="w-full bg-navy-700 border border-navy-600/30 rounded-xl px-4 py-3 text-sm text-white placeholder-text-muted focus:outline-none focus:ring-2 focus:ring-brand-primary/50 resize-none"
            />
          </div>
          <Input
            label="Category"
            placeholder="e.g., Programming, Web Development"
            value={newCourse.category}
            onChange={(e) => setNewCourse({ ...newCourse, category: e.target.value })}
            required
          />
          <div className="flex justify-end gap-3 pt-4">
            <Button variant="secondary" onClick={() => setShowCreateModal(false)}>Cancel</Button>
            <Button variant="primary" type="submit">Create Course</Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Courses;
