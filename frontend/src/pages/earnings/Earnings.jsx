import { useState, useEffect } from 'react';
import StatsCard from '../../components/ui/StatsCard';
import DataTable from '../../components/ui/DataTable';
import { HiOutlineCurrencyDollar, HiOutlineCalendar, HiOutlineTrendingUp, HiOutlineDownload, HiOutlineCash } from 'react-icons/hi';
import Button from '../../components/ui/Button';
import api from '../../config/api';

const Earnings = () => {
  const [earnings, setEarnings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchEarnings = async () => {
      try {
        const response = await api.get('/earnings');
        setEarnings(response.data || []);
      } catch (error) {
        console.error('Error fetching earnings:', error);
        setEarnings([]);
      } finally {
        setLoading(false);
      }
    };
    fetchEarnings();
  }, []);

  const totalEarnings = earnings.reduce((sum, e) => sum + (parseFloat(e.amount) || 0), 0);
  const pendingAmount = earnings.filter(e => e.status === 'pending').reduce((sum, e) => sum + (parseFloat(e.amount) || 0), 0);
  const paidAmount = earnings.filter(e => e.status === 'paid').reduce((sum, e) => sum + (parseFloat(e.amount) || 0), 0);
  const completedClasses = earnings.length;

  const stats = [
    { title: 'Total Earnings', value: `₹${totalEarnings.toLocaleString()}`, icon: HiOutlineCurrencyDollar, color: 'primary' },
    { title: 'Paid', value: `₹${paidAmount.toLocaleString()}`, icon: HiOutlineCalendar, color: 'success' },
    { title: 'Pending Payout', value: `₹${pendingAmount.toLocaleString()}`, icon: HiOutlineTrendingUp, color: 'warning' },
    { title: 'Classes Completed', value: String(completedClasses), icon: HiOutlineCalendar, color: 'info' },
  ];

  const columns = [
    {
      header: 'Class',
      render: (row) => (
        <div>
          <p className="text-sm font-medium text-white">{row.class}</p>
          <p className="text-xs text-text-muted">{row.course}</p>
        </div>
      ),
    },
    { header: 'Date', accessor: 'date' },
    { header: 'Students', render: (row) => <span>{row.students}</span> },
    { header: 'Rate', accessor: 'rate' },
    { header: 'Total', render: (row) => <span className="text-white font-medium">{row.total}</span> },
    {
      header: 'Status',
      render: (row) => (
        <span className={`px-3 py-1 rounded-full text-xs font-medium
          ${row.status === 'paid' ? 'bg-success/20 text-success' : 'bg-warning/20 text-warning'}`}>
          {row.status.charAt(0).toUpperCase() + row.status.slice(1)}
        </span>
      ),
    },
  ];

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-white">Earnings</h1>
          <p className="text-text-secondary mt-1">Track your teaching earnings</p>
        </div>
        <Button variant="secondary" icon={HiOutlineDownload}>Export Report</Button>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {stats.map((stat, i) => (
          <StatsCard key={i} {...stat} />
        ))}
      </div>

      {/* Monthly Chart Placeholder */}
      <div className="bg-surface-card rounded-2xl p-6 border border-navy-600/20">
        <h3 className="text-lg font-semibold text-white mb-4">Monthly Earnings</h3>
        <div className="flex items-end gap-2 h-40">
          {[30, 45, 60, 40, 75, 55, 80, 65, 70, 85, 60, 90].map((h, i) => (
            <div key={i} className="flex-1 flex flex-col items-center gap-1">
              <div
                className="w-full gradient-primary rounded-t-md transition-all duration-300 hover:opacity-80"
                style={{ height: `${h}%` }}
              />
              <span className="text-[10px] text-text-muted">
                {['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'][i]}
              </span>
            </div>
          ))}
        </div>
      </div>

      {loading ? (
        <div className="text-center py-12 text-text-secondary">Loading earnings...</div>
      ) : earnings.length === 0 ? (
        <div className="text-center py-12 bg-navy-800/50 rounded-xl border border-navy-700/50">
          <HiOutlineCash className="w-12 h-12 text-text-muted mx-auto mb-3" />
          <h3 className="text-lg font-medium text-white mb-2">No Earnings Yet</h3>
          <p className="text-text-secondary text-sm">Your earnings will appear here after completing classes.</p>
        </div>
      ) : (
        <DataTable columns={columns} data={earnings} />
      )}
    </div>
  );
};

export default Earnings;
