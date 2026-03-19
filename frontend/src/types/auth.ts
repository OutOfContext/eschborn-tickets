export interface LoginResponse {
  sessionId: string
  username: string
  roles: string[]
  tokenType: string
  expiresIn: number
}

export interface CurrentUserResponse {
  sessionId: string | null
  username: string
  roles: string[]
}

