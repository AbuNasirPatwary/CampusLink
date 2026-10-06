import { apiFetch, jsonBody } from './api'
export const complaintService = {
  all: () => apiFetch('/complaints'),
  get: (id) => apiFetch(`/complaints/${id}`),
  mine: () => apiFetch('/complaints/mine'),
  create: (data) => apiFetch('/complaints', { method: 'POST', body: jsonBody(data) }),
  update: (id, data) => apiFetch(`/complaints/${id}`, { method: 'PUT', body: jsonBody(data) }),
  remove: (id) => apiFetch(`/complaints/${id}`, { method: 'DELETE' }),
  comments: (id) => apiFetch(`/complaints/${id}/comments`),
  addComment: (id, text) => apiFetch(`/complaints/${id}/comments`, { method: 'POST', body: jsonBody({ text }) }),
  react: (id) => apiFetch(`/complaints/${id}/react`, { method: 'POST' }),
}
