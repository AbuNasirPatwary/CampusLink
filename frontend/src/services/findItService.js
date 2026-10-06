import { apiFetch, jsonBody } from './api'
export const findItService = {
  all: () => apiFetch('/findit'),
  get: (id) => apiFetch(`/findit/${id}`),
  mine: () => apiFetch('/findit/mine/posts'),
  myClaims: () => apiFetch('/findit/mine/claims'),
  create: (data) => apiFetch('/findit', { method: 'POST', body: jsonBody(data) }),
  update: (id, data) => apiFetch(`/findit/${id}`, { method: 'PUT', body: jsonBody(data) }),
  remove: (id) => apiFetch(`/findit/${id}`, { method: 'DELETE' }),
  claim: (id, ownershipDescription) => apiFetch(`/findit/${id}/claims`, { method: 'POST', body: jsonBody({ ownershipDescription }) }),
}
