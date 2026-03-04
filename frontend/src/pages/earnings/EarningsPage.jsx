import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import PageHeader from '../../components/ui/PageHeader';
import StatsCard from '../../components/ui/StatsCard';
import Card from '../../components/ui/Card';
import DataTable from '../../components/ui/DataTable';
import Badge from '../../components/ui/Badge';
import api from '../../config/api';
import {
  HiOutlineCurrencyDollar,
  HiOutlineTrendingUp,
  HiOutlineUserGroup,
  HiOutlineBookOpen,
} from 'react-icons/hi';

const EarningsPage = () => {
  const { user } = useAuth();
  const [earnings, setEarnings] = useState({ total: 0, thisMonth: 0, activeStudents: 0, coursesSold: 0 });
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchEarnings = async () => {
      try {
        const [earningsRes, transactionsRes] = await Promise.all([
          api.get('/earnings/stats').catch(() => ({ data: { total: 0, thisMonth: 0, activeStudents: 0, coursesSold: 0 } })),
          api.get('/earnings/transactions').catch(() => ({ data: [] })),
        ]);
        setEarnings(earningsRes.data || { total: 0, thisMonth: 0, activeStudents: 0, coursesSold: 0 });
        setTransactions(transactionsRes.data || []);
      } catch (error) {
        console.error('Error fetching earnings:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchEarnings();
  }, []);

  const stats = [
    { title: 'Total Earnings', value: `₹${earnings.total.toLocaleString()}`, icon: HiOutlineCurrencyDollar, color: 'primary' },
    { title: 'This Month', value: `₹${earnings.thisMonth.toLocaleString()}`, icon: HiOutlineTrendingUp, color: 'success' },
    { title: 'Active Students', value: earnings.activeStudents.toString(), icon: HiOutlineUserGroup, color: 'info' },
    { title: 'Courses Sold', value: earnings.coursesSold.toString(), icon: HiOutlineBookOpen, color: 'warning' },
  ];

  const columns = [
    {
      header: 'Description',
      render: (row) => (
        <div>
          <p className="font-medium text-navy-800 text-sm">{row.description}</p>
          <p className="text-xs text-text-muted">{row.student}</p>
        </div>
      ),
    },
    { header: 'Amount', render: (row) => <span className="font-semibold text-navy-800">{row.amount}</span> },
    {
      header: 'Date',
      render: (row) => (
        <span className="text-sm text-navy-600">
          {new Date(row.date).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' })}
        </span>
      ),
    },
    {
      header: 'Status',
      render: (row) => (
        <Badge variant={row.status === 'completed' ? 'success' : 'warning'}>
          {row.status.charAt(0).toUpperCase() + row.status.slice(1)}
        </Badge>
      ),
    },
  ];

  if (loading) {
    return (
      <div className="space-y-6">
        <PageHeader title="Earnings" subtitle="Track your revenue and transactions" />
        <div className="flex items-center justify-center py-12">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-brand-primary"></div>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <PageHeader title="Earnings" subtitle="Track your revenue and transactions" />

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
        {stats.map((stat, i) => (
          <StatsCard key={i} {...stat} />
        ))}
      </div>

      {/* Getting Started Message for teachers with no earnings */}
      {earnings.total === 0 && (
        <Card>
          <div className="text-center py-12">
            <HiOutlineCurrencyDollar className="mx-auto h-16 w-16 text-brand-primary mb-4" />
            <h3 className="text-lg font-semibold text-navy-800 mb-2">Start Earning</h3>
            <p className="text-text-muted mb-6 max-w-md mx-auto">
              Create courses and attract students to start earning. Your earnings will be tracked here.
            </p>
          </div>
        </Card>
      )}

      {/* Transactions */}
      {transactions.length > 0 && (
        <div>
          <h2 className="text-lg font-semibold text-navy-800 mb-4">Recent Transactions</h2>
          <DataTable columns={columns} data={transactions} emptyMessage="No transactions yet" />
        </div>
      )}
    </div>
  );
};

export default EarningsPage;
