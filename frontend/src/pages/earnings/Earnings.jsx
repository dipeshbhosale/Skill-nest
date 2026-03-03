import StatsCard from '../../components/ui/StatsCard';
import DataTable from '../../components/ui/DataTable';
import { HiOutlineCurrencyDollar, HiOutlineCalendar, HiOutlineTrendingUp, HiOutlineDownload } from 'react-icons/hi';
import Button from '../../components/ui/Button';

const Earnings = () => {
  const stats = [
    { title: 'Total Earnings', value: '₹45,200', change: 15, icon: HiOutlineCurrencyDollar, color: 'primary' },
    { title: 'This Month', value: '₹8,500', change: 8, icon: HiOutlineCalendar, color: 'success' },
    { title: 'Pending Payout', value: '₹3,200', icon: HiOutlineTrendingUp, color: 'warning' },
    { title: 'Classes Completed', value: '86', change: 12, icon: HiOutlineCalendar, color: 'info' },
  ];

  const earnings = [
    { id: 1, course: 'Advanced Java', class: 'Spring Boot Basics', date: 'Mar 3, 2026', students: 45, rate: '₹500', total: '₹500', status: 'paid' },
    { id: 2, course: 'DSA', class: 'Trees & Graphs', date: 'Mar 3, 2026', students: 62, rate: '₹500', total: '₹500', status: 'pending' },
    { id: 3, course: 'React.js', class: 'React Hooks', date: 'Mar 2, 2026', students: 35, rate: '₹500', total: '₹500', status: 'paid' },
    { id: 4, course: 'Advanced Java', class: 'Microservices', date: 'Mar 1, 2026', students: 42, rate: '₹500', total: '₹500', status: 'paid' },
    { id: 5, course: 'DSA', class: 'Dynamic Programming', date: 'Feb 28, 2026', students: 58, rate: '₹500', total: '₹500', status: 'paid' },
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

      <DataTable columns={columns} data={earnings} />
    </div>
  );
};

export default Earnings;
