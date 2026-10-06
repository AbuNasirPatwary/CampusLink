export default function ErrorMessage({ error }) { return error ? <div className="rounded-xl bg-red-50 p-3 text-sm text-red-700">{error}</div> : null }
