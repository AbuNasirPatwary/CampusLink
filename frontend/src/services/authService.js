import { apiFetch, jsonBody } from './api'
export const authService = {
  login: (data) => apiFetch('/auth/login', { method: 'POST', body: jsonBody(data) }),
  register: (data) => apiFetch('/auth/register', { method: 'POST', body: jsonBody(data) }),
}
