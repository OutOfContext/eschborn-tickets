import { useState } from 'react'
import { login, logout } from './api/auth'
import { JumpMenu } from './components/JumpMenu'
import { LoginForm } from './components/LoginForm'
import { MediaHandlerDemoPage } from './pages/MediaHandlerDemoPage'
import type { CurrentUserResponse } from './types/auth'
import './App.css'

type AppView = 'login' | 'menu' | 'media-demo'

function App() {
  const [view, setView] = useState<AppView>('login')
  const [currentUser, setCurrentUser] = useState<CurrentUserResponse | null>(null)
  const [isLoggingIn, setIsLoggingIn] = useState(false)
  const [isLoggingOut, setIsLoggingOut] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')

  const onLogin = async (username: string, password: string) => {
    setErrorMessage('')
    setIsLoggingIn(true)
    try {
      const response = await login(username, password)
      setCurrentUser({
        sessionId: response.sessionId,
        username: response.username,
        roles: response.roles,
      })
      setView('menu')
    } catch (error) {
      setErrorMessage(error instanceof Error ? error.message : 'Anmeldung fehlgeschlagen.')
    } finally {
      setIsLoggingIn(false)
    }
  }

  const onLogout = async () => {
    setErrorMessage('')
    setIsLoggingOut(true)
    try {
      await logout()
      setCurrentUser(null)
      setView('login')
    } catch (error) {
      setErrorMessage(error instanceof Error ? error.message : 'Abmeldung fehlgeschlagen.')
    } finally {
      setIsLoggingOut(false)
    }
  }


  if (view === 'media-demo') {
    return <MediaHandlerDemoPage onBack={() => setView('menu')} />
  }

  if (view === 'menu' && currentUser) {
    return (
      <main className="shell">
        <JumpMenu
          user={currentUser}
          onOpenMediaDemo={() => setView('media-demo')}
          onLogout={onLogout}
          isLoggingOut={isLoggingOut}
          errorMessage={errorMessage}
        />
      </main>
    )
  }

  return (
    <main className="shell">
      <LoginForm
        onLogin={onLogin}
        isSubmitting={isLoggingIn}
        errorMessage={errorMessage}
      />
    </main>
  )
}

export default App
