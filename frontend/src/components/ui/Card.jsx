const Card = ({ children, className = '', noPadding = false }) => (
  <div
    className={`bg-white rounded-2xl border border-surface-border card-shadow ${
      noPadding ? '' : 'p-6'
    } ${className}`}
  >
    {children}
  </div>
);

export default Card;
