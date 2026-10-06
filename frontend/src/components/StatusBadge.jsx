export default function StatusBadge({ value }) {
  return <span className="badge">{String(value || '').replaceAll('_', ' ')}</span>
}
