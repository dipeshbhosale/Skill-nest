const colorMap = {
  primary: {
    bg: 'bg-brand-light',
    icon: 'bg-brand-primary',
    text: 'text-brand-primary',
  },
  success: {
    bg: 'bg-green-50',
    icon: 'bg-success',
    text: 'text-success',
  },
  warning: {
    bg: 'bg-amber-50',
    icon: 'bg-warning',
    text: 'text-warning',
  },
  info: {
    bg: 'bg-blue-50',
    icon: 'bg-info',
    text: 'text-info',
  },
  error: {
    bg: 'bg-red-50',
    icon: 'bg-error',
    text: 'text-error',
  },
};

const StatsCard = ({ title, value, change, icon: Icon, color = 'primary' }) => {
  const palette = colorMap[color] || colorMap.primary;
  const isPositive = change > 0;

  return (
    <div className="bg-white rounded-2xl p-5 card-shadow border border-surface-border">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm text-text-muted font-medium">{title}</p>
          <p className="text-2xl font-bold text-navy-800 mt-1">{value}</p>
          {change !== undefined && change !== 0 && (
            <p className={`text-xs mt-1 font-medium ${isPositive ? 'text-success' : 'text-error'}`}>
              {isPositive ? '+' : ''}{change}%{' '}
              <span className="text-text-muted">from last month</span>
            </p>
          )}
        </div>
        {Icon && (
          <div className={`w-14 h-14 rounded-2xl ${palette.bg} flex items-center justify-center`}>
            <Icon className={`w-7 h-7 ${palette.text}`} />
          </div>
        )}
      </div>
    </div>
  );
};

export default StatsCard;
