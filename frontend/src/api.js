import { API_BASE } from './config'

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: { 'Content-Type': 'application/json', ...(options.token ? { Authorization: `Bearer ${options.token}` } : {}), ...options.headers },
    ...options,
  })
  const text = await response.text()
  const data = text ? (() => { try { return JSON.parse(text) } catch { return text } })() : null
  if (!response.ok) throw new Error(data?.message || data?.error || `Request failed (${response.status})`)
  return data
}
export const api = {
  menu: () => request('/menu'),
  menuByBranch: (branchCode) => request(`/menu/branch/${branchCode}`),
  popularByBranch: (branchCode) => request(`/menu/branch/${branchCode}/popular`),
  branches: () => request('/branches'),
  adminLogin: payload => request('/admin/login', { method: 'POST', body: JSON.stringify(payload) }),
  createMenuItem: (payload, token) => request('/admin/menu', { method: 'POST', token, body: JSON.stringify(payload) }),
  updateMenuItem: (id, payload, token) => request(`/admin/menu/${id}`, { method: 'PUT', token, body: JSON.stringify(payload) }),
  deleteMenuItem: (id, token) => request(`/admin/menu/${id}`, { method: 'DELETE', token }),
  // Branch admin
  adminBranches: (token) => request('/admin/branches', { token }),
  createBranch: (payload, token) => request('/admin/branches', { method: 'POST', token, body: JSON.stringify(payload) }),
  updateBranch: (id, payload, token) => request(`/admin/branches/${id}`, { method: 'PUT', token, body: JSON.stringify(payload) }),
  deleteBranch: (id, token) => request(`/admin/branches/${id}`, { method: 'DELETE', token }),
  getBranchPrices: (branchId, token) => request(`/admin/branches/${branchId}/prices`, { token }),
  updateBranchPrice: (branchId, menuItemId, price, token) => request(`/admin/branches/${branchId}/prices/${menuItemId}`, { method: 'PUT', token, body: JSON.stringify({price}) }),
}

