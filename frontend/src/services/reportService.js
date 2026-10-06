import { apiFetch, jsonBody } from './api'
export const reportService = {
  report: (data) => apiFetch('/reports', { method: 'POST', body: jsonBody(data) }),
}
