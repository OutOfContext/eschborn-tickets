import type { CurrentUserResponse, LoginResponse } from '../types/auth'

const AUTH_ENDPOINT = '/api/auth'

async function parseError(response: Response): Promise<string> {
  const fallback = `Request fehlgeschlagen (${response.status})`

  try {
    const text = await response.text()
    return text ? `${fallback}: ${text}` : fallback
  } catch {
    return fallback
  }
}

export async function login(username: string, password: string): Promise<LoginResponse> {
  const response = await fetch(`${AUTH_ENDPOINT}/login`, {
    method: 'POST',
    credentials: 'include',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ username, password }),
  })

  if (!response.ok) {
    throw new Error(await parseError(response))
  }

  return (await response.json()) as LoginResponse
}

export async function fetchCurrentUser(): Promise<CurrentUserResponse> {
  const response = await fetch(`${AUTH_ENDPOINT}/me`, {
    credentials: 'include',
  })

  if (!response.ok) {
    throw new Error(await parseError(response))
  }

  return (await response.json()) as CurrentUserResponse
}

export async function logout(): Promise<void> {
  const response = await fetch(`${AUTH_ENDPOINT}/logout`, {
    method: 'DELETE',
    credentials: 'include',
  })

  if (!response.ok) {
    throw new Error(await parseError(response))
  }
}

