import { apiFetch } from './api'
export const mediaService = {
  upload: async (file, folder) => {
    const body = new FormData()
    body.append('file', file)
    body.append('folder', folder)
    return apiFetch('/media/upload', { method: 'POST', body })
  }
}
