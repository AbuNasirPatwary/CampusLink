import { apiFetch, jsonBody } from './api'
export const talentService = {
  all: () => apiFetch('/talent'),
  get: (id) => apiFetch(`/talent/${id}`),
  mine: () => apiFetch('/talent/mine'),
  bookmarks: () => apiFetch('/talent/bookmarks/mine'),
  create: (data) => apiFetch('/talent', { method: 'POST', body: jsonBody(data) }),
  update: (id, data) => apiFetch(`/talent/${id}`, { method: 'PUT', body: jsonBody(data) }),
  remove: (id) => apiFetch(`/talent/${id}`, { method: 'DELETE' }),
  comments: (id) => apiFetch(`/talent/${id}/comments`),
  addComment: (id, text) => apiFetch(`/talent/${id}/comments`, { method: 'POST', body: jsonBody({ text }) }),
  like: (id) => apiFetch(`/talent/${id}/like`, { method: 'POST' }),
  bookmark: (id) => apiFetch(`/talent/${id}/bookmark`, { method: 'POST' }),
}
