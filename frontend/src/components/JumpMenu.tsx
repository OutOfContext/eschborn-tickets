import type { CurrentUserResponse } from '../types/auth'

interface JumpMenuProps {
  user: CurrentUserResponse
  onOpenMediaDemo: () => void
  onLogout: () => Promise<void>
  isLoggingOut: boolean
  errorMessage: string
}

export function JumpMenu({
  user,
  onOpenMediaDemo,
  onLogout,
  isLoggingOut,
  errorMessage,
}: JumpMenuProps) {
  return (
    <section className="menuCard">
      <div className="menuHeader">
        <h1>Absprung-Menue</h1>
        <p>Angemeldet als <strong>{user.username}</strong></p>
      </div>

      <div className="roleBadges">
        {user.roles.length > 0 ? user.roles.map((role) => (
          <span key={role} className="badge">{role}</span>
        )) : <span className="badge">ohne Rolle</span>}
      </div>

      <div className="menuGrid">
        <button className="menuAction" type="button" onClick={onOpenMediaDemo}>
          <span className="menuTitle">MediaHandler-Demo</span>
          <span className="menuSubtitle">Upload testen, Metadaten pruefen, Embed-Link kopieren</span>
        </button>
      </div>

      <div className="menuFooter">
        <button type="button" className="secondaryButton" onClick={onLogout} disabled={isLoggingOut}>
          {isLoggingOut ? 'Melde ab...' : 'Abmelden'}
        </button>
      </div>

      {errorMessage && <p className="error">{errorMessage}</p>}
    </section>
  )
}

