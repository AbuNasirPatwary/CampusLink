import { useState } from 'react'
import { reportService } from '../services/reportService'

export default function ReportButton({ module, targetType = 'POST', targetId }) {
  const [done, setDone] = useState(false)
  async function report() {
    const reason = window.prompt('Why are you reporting this content?')
    if (!reason?.trim()) return
    try {
      await reportService.report({ module, targetType, targetId, reason: reason.trim() })
      setDone(true)
    } catch (e) {
      window.alert(e.message)
    }
  }
  return <button type="button" onClick={report} className="text-xs font-semibold text-red-600 hover:underline">{done ? 'Reported' : 'Report'}</button>
}
