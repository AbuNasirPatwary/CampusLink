import { apiFetch, apiFetchBlob, jsonBody } from './api'
export const adminService={
 users:()=>apiFetch('/admin/users'),pendingAlumni:()=>apiFetch('/admin/users/pending-alumni'),setUserStatus:(id,status)=>apiFetch(`/admin/users/${id}/status?status=${status}`,{method:'PATCH'}),
 findItClaims:()=>apiFetch('/findit/admin/claims'),reviewClaim:(id,status)=>apiFetch(`/findit/admin/claims/${id}?status=${status}`,{method:'PATCH'}),markDelivered:(id)=>apiFetch(`/findit/admin/posts/${id}/delivered`,{method:'PATCH'}),privateMedia:(key)=>apiFetchBlob(`/media/private/${key}`),
 updateComplaint:(id,data)=>apiFetch(`/complaints/admin/${id}`,{method:'PATCH',body:jsonBody(data)}),featureTalent:(id,value)=>apiFetch(`/talent/admin/${id}/featured?value=${value}`,{method:'PATCH'}),reports:()=>apiFetch('/reports'),activityLogs:()=>apiFetch('/admin/activity-logs'),updateReport:(id,status)=>apiFetch(`/reports/${id}?status=${status}`,{method:'PATCH'})
}
