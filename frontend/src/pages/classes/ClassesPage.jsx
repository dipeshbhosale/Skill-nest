import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import PageHeader from '../../components/ui/PageHeader';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Badge from '../../components/ui/Badge';
import api from '../../config/api';
import { HiOutlinePlus, HiOutlineAcademicCap, HiOutlineUserGroup, HiOutlineClock } from 'react-icons/hi';

const ClassesPage = () => {
  const { user } = useAuth();
  const [classes, setClasses] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchClasses = async () => {
      try {
        const res = await api.get('/classes').catch(() => ({ data: [] }));
        setClasses(res.data || []);
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    };
    fetchClasses();
  }, []);

  return (
    <div className="space-y-6">
      <PageHeader
        title="Classes"
        subtitle="View and manage class sections"
        actions={
          user?.role === 'admin' && <Button icon={HiOutlinePlus}>Create Class</Button>
        }
      />

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
        {classes.map((cls) => (
          <Card key={cls.id} className="hover:shadow-lg transition-shadow cursor-pointer">
            <div className="flex items-start justify-between mb-4">
              <div className="w-12 h-12 rounded-xl bg-brand-light flex items-center justify-center">
                <HiOutlineAcademicCap className="w-6 h-6 text-brand-primary" />
              </div>
              <Badge
                variant={cls.status === 'active' ? 'success' : cls.status === 'upcoming' ? 'warning' : 'default'}
              >
                {cls.status}
              </Badge>
            </div>

            <h3 className="font-bold text-navy-800 text-lg">{cls.name}</h3>
            <p className="text-sm text-text-muted mt-1">{cls.course}</p>

            <div className="mt-4 space-y-2">
              <div className="flex items-center gap-2 text-sm text-navy-600">
                <HiOutlineUserGroup className="w-4 h-4 text-text-muted" />
                <span>{cls.teacher}</span>
              </div>
              <div className="flex items-center gap-2 text-sm text-navy-600">
                <HiOutlineClock className="w-4 h-4 text-text-muted" />
                <span>{cls.schedule}</span>
              </div>
            </div>

            <div className="mt-4 pt-4 border-t border-surface-border flex items-center justify-between text-sm">
              <span className="text-text-muted">{cls.students} students</span>
              <span className="text-text-muted">{cls.room}</span>
            </div>
          </Card>
        ))}
      </div>
    </div>
  );
};

export default ClassesPage;
