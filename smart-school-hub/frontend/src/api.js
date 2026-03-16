const API_BASE = 'http://localhost:8080/api'

export function getToken() {
  return localStorage.getItem('ssh_token')
}

export function setToken(token) {
  if (token) {
    localStorage.setItem('ssh_token', token)
  } else {
    localStorage.removeItem('ssh_token')
  }
}

export async function apiFetch(path, options = {}) {
  const token = getToken()
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {})
  }

  if (token) {
    headers.Authorization = `Bearer ${token}`
  }

  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers
  })

  if (!response.ok) {
    const text = await response.text()
    throw new Error(text || 'Request failed')
  }

  if (response.status === 204) {
    return null
  }

  return response.json()
}

export async function login(username, password) {
  return apiFetch('/auth/login', {
    method: 'POST',
    body: JSON.stringify({ username, password })
  })
}

export async function register(payload) {
  return apiFetch('/auth/register', {
    method: 'POST',
    body: JSON.stringify(payload)
  })
}
