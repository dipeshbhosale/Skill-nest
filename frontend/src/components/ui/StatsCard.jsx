const StatsCard = ({ title, value, change, icon: Icon, color = 'primary' }) => {
  const colorClasses = {
    primary: 'from-brand-primary to-brand-secondary',
    success: 'from-success to-emerald-400',
    warning: 'from-warning to-orange-400',
    error: 'from-error to-rose-400',
    info: 'from-info to-blue-400',
  };

  return (
    <div className="bg-surface-card rounded-2xl p-6 border border-navy-600/20 hover:border-navy-600/40 transition-all duration-300">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm text-text-secondary mb-1">{title}</p>
          <h3 className="text-2xl font-bold text-white">{value}</h3>
          {change && (
            <p className={`text-xs mt-1 ${change > 0 ? 'text-success' : 'text-error'}`}>
              {change > 0 ? '+' : ''}{change}% from last month
            </p>
          )}
        </div>
        {Icon && (
          <div className={`w-12 h-12 rounded-xl bg-gradient-to-br ${colorClasses[color]} flex items-center justify-center`}>
            <Icon className="w-6 h-6 text-white" />
          </div>
        )}
      </div>
    </div>
  );
};

export default StatsCard;
