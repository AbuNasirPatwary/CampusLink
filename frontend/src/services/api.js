const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

export class ApiError extends Error {
  constructor(message, status, data) {
    super(message)
    this.name = 'ApiError'
    this.status = status
    this.data = data
  }
}

function authHeaders(headers = {}) {
  const result = new Headers(headers)
  const token = localStorage.getItem('campuslink_token')
  if (token) result.set('Authorization', `Bearer ${token}`)
  return result
}

export async function apiFetch(path, options = {}) {
  const headers = authHeaders(options.headers || {})
  if (!(options.body instanceof FormData) && options.body !== undefined) headers.set('Content-Type', 'application/json')
  const response = await fetch(`${API_URL}${path}`, { ...options, headers })
  const text = await response.text()
  let data = null
  if (text) { try { data = JSON.parse(text) } catch { data = text } }
  if (!response.ok) throw new ApiError(data?.message || `Request failed (${response.status})`, response.status, data)
  return data
}

export async function apiFetchBlob(path, options = {}) {
  const response = await fetch(`${API_URL}${path}`, { ...options, headers: authHeaders(options.headers || {}) })
  if (!response.ok) {
    let message = `Request failed (${response.status})`
    try { const data = await response.json(); message = data?.message || message } catch { /* ignore */ }
    throw new ApiError(message, response.status)
  }
  return response.blob()
}

export function jsonBody(data) { return JSON.stringify(data) }
export const apiBaseUrl = API_URL
