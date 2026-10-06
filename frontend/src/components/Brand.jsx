export default function Brand({ compact = false }) {
  return <div className={`brand ${compact ? 'brand-compact' : ''}`}>
    <div className="brand-mark"><span /><span /><span /></div>
    <div className="brand-name">Campus<span>Link</span></div>
  </div>
}
