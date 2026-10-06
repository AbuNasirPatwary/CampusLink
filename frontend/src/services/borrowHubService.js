import { apiFetch, jsonBody } from './api'
export const borrowHubService = {
  all: () => apiFetch('/borrowhub'),
  get: (id) => apiFetch(`/borrowhub/${id}`),
  mine: () => apiFetch('/borrowhub/mine'),
  create: (data) => apiFetch('/borrowhub', { method: 'POST', body: jsonBody(data) }),
  update: (id, data) => apiFetch(`/borrowhub/${id}`, { method: 'PUT', body: jsonBody(data) }),
  complete: (id) => apiFetch(`/borrowhub/${id}/complete`, { method: 'PATCH' }),
  remove: (id) => apiFetch(`/borrowhub/${id}`, { method: 'DELETE' }),
  comments: (id) => apiFetch(`/borrowhub/${id}/comments`),
  addComment: (id, text) => apiFetch(`/borrowhub/${id}/comments`, { method: 'POST', body: jsonBody({ text }) }),
}
