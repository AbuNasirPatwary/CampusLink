import { apiFetch, jsonBody } from './api'
export const userService = {
  me: () => apiFetch('/users/me'),
  updateMe: (data) => apiFetch('/users/me', { method: 'PUT', body: jsonBody(data) }),
  changePassword: (data) => apiFetch('/users/me/password', { method: 'PUT', body: jsonBody(data) }),
}
