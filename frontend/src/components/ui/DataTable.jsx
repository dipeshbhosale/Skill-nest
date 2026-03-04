const DataTable = ({ columns, data, emptyMessage = 'No data available.' }) => {
  if (!data || data.length === 0) {
    return (
      <div className="bg-white rounded-2xl border border-surface-border p-8 text-center">
        <p className="text-text-muted text-sm">{emptyMessage}</p>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-2xl border border-surface-border overflow-hidden card-shadow">
      <div className="overflow-x-auto">
        <table className="w-full">
          <thead>
            <tr className="border-b border-surface-border">
              {columns.map((col, i) => (
                <th
                  key={i}
                  className="px-6 py-4 text-left text-xs font-semibold text-text-muted uppercase tracking-wider"
                >
                  {col.header}
                </th>
              ))}
            </tr>
          </thead>
          <tbody className="divide-y divide-surface-border">
            {data.map((row, rowIdx) => (
              <tr
                key={row.id || rowIdx}
                className="hover:bg-surface-bg transition-colors"
              >
                {columns.map((col, colIdx) => (
                  <td key={colIdx} className="px-6 py-4 text-sm text-navy-600">
                    {col.render ? col.render(row) : row[col.accessor]}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default DataTable;
