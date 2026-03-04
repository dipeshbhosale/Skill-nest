const Badge = ({ children, variant = 'default', className = '' }) => {
  const variants = {
    default: 'bg-surface-bg text-navy-600',
    primary: 'bg-brand-light text-brand-primary',
    success: 'bg-green-50 text-success',
    warning: 'bg-amber-50 text-warning',
    error: 'bg-red-50 text-error',
    info: 'bg-blue-50 text-info',
  };

  return (
    <span
      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
        variants[variant] || variants.default
      } ${className}`}
    >
      {children}
    </span>
  );
};

export default Badge;
