const Button = ({
  children,
  variant = 'primary',
  size = 'md',
  className = '',
  icon: Icon,
  ...props
}) => {
  const variants = {
    primary: 'bg-brand-primary hover:bg-brand-secondary text-white shadow-md shadow-brand-primary/25',
    secondary: 'bg-surface-bg hover:bg-surface-hover text-navy-700 border border-surface-border',
    success: 'bg-success hover:bg-success/90 text-white shadow-md shadow-success/25',
    danger: 'bg-error hover:bg-error/90 text-white shadow-md shadow-error/25',
    ghost: 'bg-transparent hover:bg-surface-hover text-navy-600',
    outline: 'border-2 border-brand-primary text-brand-primary hover:bg-brand-light',
  };

  const sizes = {
    sm: 'px-3 py-1.5 text-xs',
    md: 'px-5 py-2.5 text-sm',
    lg: 'px-6 py-3 text-base',
  };

  return (
    <button
      className={`inline-flex items-center justify-center gap-2 rounded-xl font-semibold transition-all duration-200 ${variants[variant]} ${sizes[size]} ${className}`}
      {...props}
    >
      {Icon && <Icon className="w-4 h-4" />}
      {children}
    </button>
  );
};

export default Button;
