import { useState } from 'react'
import type { FormEvent } from 'react'

interface LoginFormProps {
  onLogin: (username: string, password: string) => Promise<void>
  isSubmitting: boolean
  errorMessage: string
}

export function LoginForm({ onLogin, isSubmitting, errorMessage }: LoginFormProps) {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [localError, setLocalError] = useState('')

  const onSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setLocalError('')

    if (!username.trim() || !password.trim()) {
      setLocalError('Bitte Benutzername und Passwort eingeben.')
      return
    }

    await onLogin(username.trim(), password)
  }

  return (
    <section className="authCard">
      <h1>Willkommen zur Ticket-Demo</h1>
      <p>Bitte melde dich an, um ins Absprung-Menue zu gelangen.</p>

      <form className="authForm" onSubmit={onSubmit}>
        <label htmlFor="username">Benutzername</label>
        <input
          id="username"
          type="text"
          value={username}
          onChange={(event) => setUsername(event.target.value)}
          placeholder="z. B. admin"
          autoComplete="username"
        />

        <label htmlFor="password">Passwort</label>
        <input
          id="password"
          type="password"
          value={password}
          onChange={(event) => setPassword(event.target.value)}
          placeholder="Passwort"
          autoComplete="current-password"
        />

        <button type="submit" disabled={isSubmitting}>
          {isSubmitting ? 'Anmeldung laeuft...' : 'Anmelden'}
        </button>
      </form>

      {(localError || errorMessage) && (
        <p className="error">{localError || errorMessage}</p>
      )}
    </section>
  )
}

