export default function PageHeader({ title, description, action }) {
  return <div className="mb-6 flex flex-col justify-between gap-3 sm:flex-row sm:items-end">
    <div><h1 className="text-3xl font-black text-slate-900">{title}</h1>{description && <p className="mt-1 text-slate-600">{description}</p>}</div>
    {action}
  </div>
}
