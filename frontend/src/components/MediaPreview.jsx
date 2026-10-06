const BACKEND = import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080'
export function mediaUrl(url) { return !url ? '' : url.startsWith('http') ? url : `${BACKEND}${url}` }
export default function MediaPreview({ url, type = 'image', className = '' }) {
  if (!url) return null
  return type === 'video'
    ? <video controls className={`w-full rounded-xl ${className}`} src={mediaUrl(url)} />
    : <img className={`w-full rounded-xl object-cover ${className}`} src={mediaUrl(url)} alt="Uploaded media" />
}
