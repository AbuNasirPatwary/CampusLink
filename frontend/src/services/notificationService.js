import { apiFetch } from './api'
export const notificationService={all:()=>apiFetch('/notifications'),markRead:(id)=>apiFetch(`/notifications/${id}/read`,{method:'PATCH'}),markAllRead:()=>apiFetch('/notifications/read-all',{method:'PATCH'})}
