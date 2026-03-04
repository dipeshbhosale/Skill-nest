import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import PageHeader from '../../components/ui/PageHeader';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Badge from '../../components/ui/Badge';
import api from '../../config/api';
import { HiOutlinePlus, HiOutlineFolder, HiOutlineDownload, HiOutlineDocumentText, HiOutlinePhotograph, HiOutlineFilm } from 'react-icons/hi';

const MaterialsPage = () => {
  const { user } = useAuth();
  const [materials, setMaterials] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedCourse, setSelectedCourse] = useState('all');

  useEffect(() => {
    const fetchMaterials = async () => {
      try {
        const res = await api.get('/materials').catch(() => ({ data: [] }));
        setMaterials(res.data || []);
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    };
    fetchMaterials();
  }, []);

  const typeIcons = {
    pdf: HiOutlineDocumentText,
    pptx: HiOutlineDocumentText,
    image: HiOutlinePhotograph,
    video: HiOutlineFilm,
  };

  const typeColors = {
    pdf: 'bg-red-50 text-error',
    pptx: 'bg-amber-50 text-warning',
    image: 'bg-green-50 text-success',
    video: 'bg-blue-50 text-info',
  };

  const courses = ['all', ...new Set(materials.map((m) => m.course))];
  const filtered = selectedCourse === 'all' ? materials : materials.filter((m) => m.course === selectedCourse);

  return (
    <div className="space-y-6">
      <PageHeader
        title="Materials"
        subtitle="Course materials and resources"
        actions={
          (user?.role === 'admin' || user?.role === 'teacher') && (
            <Button icon={HiOutlinePlus}>Upload Material</Button>
          )
        }
      />

      {/* Course filter */}
      <div className="flex gap-2 flex-wrap">
        {courses.map((c) => (
          <button
            key={c}
            onClick={() => setSelectedCourse(c)}
            className={`px-4 py-2 rounded-xl text-sm font-medium capitalize transition-all ${
              selectedCourse === c
                ? 'bg-brand-primary text-white shadow-md shadow-brand-primary/25'
                : 'bg-white text-navy-600 border border-surface-border hover:bg-surface-hover'
            }`}
          >
            {c === 'all' ? 'All Courses' : c}
          </button>
        ))}
      </div>

      {/* Materials grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
        {filtered.map((material) => {
          const IconComponent = typeIcons[material.type] || HiOutlineFolder;
          const colorClass = typeColors[material.type] || 'bg-gray-50 text-navy-400';
          return (
            <Card key={material.id} className="hover:shadow-lg transition-shadow cursor-pointer">
              <div className="flex items-start justify-between mb-4">
                <div className={`w-12 h-12 rounded-xl ${colorClass} flex items-center justify-center`}>
                  <IconComponent className="w-6 h-6" />
                </div>
                <Badge variant="default">{material.type.toUpperCase()}</Badge>
              </div>

              <h3 className="font-semibold text-navy-800 text-sm mb-1">{material.name}</h3>
              <p className="text-xs text-text-muted">{material.course}</p>

              <div className="mt-4 pt-4 border-t border-surface-border">
                <div className="flex items-center justify-between text-xs text-text-muted">
                  <span>{material.uploadedBy}</span>
                  <span>{material.size}</span>
                </div>
                <div className="flex items-center justify-between mt-2">
                  <span className="text-xs text-text-muted">
                    {new Date(material.date).toLocaleDateString('en-IN', { day: 'numeric', month: 'short' })}
                  </span>
                  <button className="flex items-center gap-1 text-xs text-brand-primary hover:text-brand-secondary font-medium">
                    <HiOutlineDownload className="w-4 h-4" />
                    Download
                  </button>
                </div>
              </div>
            </Card>
          );
        })}
      </div>
    </div>
  );
};

export default MaterialsPage;
