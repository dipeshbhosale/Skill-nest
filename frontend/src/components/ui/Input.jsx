const Input = ({
  label,
  type = 'text',
  placeholder,
  value,
  onChange,
  error,
  icon: Icon,
  required = false,
  disabled = false,
  className = '',
  ...props
}) => {
  return (
    <div className={`space-y-1.5 ${className}`}>
      {label && (
        <label className="block text-sm font-medium text-text-secondary">
          {label} {required && <span className="text-error">*</span>}
        </label>
      )}
      <div className="relative">
        {Icon && (
          <div className="absolute left-3 top-1/2 -translate-y-1/2 text-text-muted">
            <Icon className="w-5 h-5" />
          </div>
        )}
        <input
          type={type}
          placeholder={placeholder}
          value={value}
          onChange={onChange}
          disabled={disabled}
          required={required}
          className={`w-full bg-navy-700 border border-navy-600/30 rounded-xl px-4 py-3 text-sm text-white placeholder-text-muted
            focus:outline-none focus:ring-2 focus:ring-brand-primary/50 focus:border-brand-primary
            disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200
            ${Icon ? 'pl-10' : ''}
            ${error ? 'border-error focus:ring-error/50' : ''}`}
          {...props}
        />
      </div>
      {error && <p className="text-xs text-error">{error}</p>}
    </div>
  );
};

export default Input;
