import { useState, useEffect } from 'react';
import Button from '../../components/ui/Button';
import Modal from '../../components/ui/Modal';
import Input from '../../components/ui/Input';
import { useAuth } from '../../context/AuthContext';
import api from '../../config/api';
import { HiOutlineUpload, HiOutlineDocumentText, HiOutlineDownload, HiOutlineTrash, HiOutlineFolder } from 'react-icons/hi';
import toast from 'react-hot-toast';

const Materials = () => {
  const { isTeacher } = useAuth();
  const [showUploadModal, setShowUploadModal] = useState(false);
  const [materials, setMaterials] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMaterials = async () => {
      try {
        const response = await api.get('/materials');
        setMaterials(response.data || []);
      } catch (error) {
        console.error('Error fetching materials:', error);
        setMaterials([]);
      } finally {
        setLoading(false);
      }
    };
    fetchMaterials();
  }, []);

  const getFileIcon = (type) => {
    const colors = { PDF: 'text-error', DOCX: 'text-info', PPTX: 'text-warning', ZIP: 'text-success' };
    return colors[type] || 'text-text-muted';
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-white">Materials</h1>
          <p className="text-text-secondary mt-1">Learning resources and notes</p>
        </div>
        {isTeacher && (
          <Button variant="primary" icon={HiOutlineUpload} onClick={() => setShowUploadModal(true)}>
            Upload Material
          </Button>
        )}
      </div>

      {/* Materials Grid */}
      {loading ? (
        <div className="text-center py-12 text-text-secondary">Loading materials...</div>
      ) : materials.length === 0 ? (
        <div className="text-center py-12 bg-navy-800/50 rounded-xl border border-navy-700/50">
          <HiOutlineFolder className="w-12 h-12 text-text-muted mx-auto mb-3" />
          <h3 className="text-lg font-medium text-white mb-2">No Materials Yet</h3>
          <p className="text-text-secondary text-sm">
            {isTeacher ? 'Upload your first learning material to get started.' : 'No learning materials have been uploaded yet.'}
          </p>
        </div>
      ) : (
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {materials.map((mat) => (
          <div key={mat.id} className="bg-surface-card rounded-2xl p-5 border border-navy-600/20 hover:border-brand-primary/30 transition-all group">
            <div className="flex items-start gap-3">
              <div className="w-12 h-12 rounded-xl bg-navy-700 flex items-center justify-center flex-shrink-0">
                <HiOutlineDocumentText className={`w-6 h-6 ${getFileIcon(mat.type)}`} />
              </div>
              <div className="flex-1 min-w-0">
                <h3 className="text-sm font-medium text-white truncate group-hover:text-brand-accent transition-colors">
                  {mat.title}
                </h3>
                <p className="text-xs text-text-muted mt-0.5">{mat.course}</p>
                <div className="flex items-center gap-3 mt-2">
                  <span className="text-xs text-text-muted">{mat.type} · {mat.size}</span>
                </div>
              </div>
            </div>
            <div className="flex items-center justify-between mt-4 pt-3 border-t border-navy-600/20">
              <span className="text-xs text-text-muted">{mat.uploadedAt}</span>
              <div className="flex gap-2">
                <button className="p-1.5 rounded-lg text-text-muted hover:text-info hover:bg-info/10 transition-colors">
                  <HiOutlineDownload className="w-4 h-4" />
                </button>
                {isTeacher && (
                  <button className="p-1.5 rounded-lg text-text-muted hover:text-error hover:bg-error/10 transition-colors">
                    <HiOutlineTrash className="w-4 h-4" />
                  </button>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
      )}

      {/* Upload Modal */}
      <Modal isOpen={showUploadModal} onClose={() => setShowUploadModal(false)} title="Upload Material">
        <form onSubmit={(e) => { e.preventDefault(); toast.success('Material uploaded!'); setShowUploadModal(false); }} className="space-y-4">
          <Input label="Title" placeholder="Material title" required />
          <div className="space-y-1.5">
            <label className="block text-sm font-medium text-text-secondary">Course</label>
            <select className="w-full bg-navy-700 border border-navy-600/30 rounded-xl px-4 py-3 text-sm text-white focus:outline-none focus:ring-2 focus:ring-brand-primary/50">
              <option value="">Select course</option>
              <option>Advanced Java Programming</option>
              <option>Data Structures & Algorithms</option>
              <option>React.js Masterclass</option>
            </select>
          </div>
          <div className="border-2 border-dashed border-navy-600/30 rounded-xl p-8 text-center cursor-pointer hover:border-brand-primary/30 transition-colors">
            <HiOutlineUpload className="w-10 h-10 text-text-muted mx-auto mb-2" />
            <p className="text-sm text-text-secondary">Drag & drop files here</p>
            <p className="text-xs text-text-muted mt-1">PDF, DOCX, PPTX up to 10MB</p>
          </div>
          <div className="flex justify-end gap-3 pt-4">
            <Button variant="secondary" onClick={() => setShowUploadModal(false)}>Cancel</Button>
            <Button variant="primary" type="submit">Upload</Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Materials;
