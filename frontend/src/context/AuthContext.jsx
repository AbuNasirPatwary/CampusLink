import { createContext, useContext, useEffect, useState } from 'react'
import { authService } from '../services/authService'
import { userService } from '../services/userService'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const token = localStorage.getItem('campuslink_token')
    if (!token) { setLoading(false); return }
    userService.me().then(setUser).catch(() => {
      localStorage.removeItem('campuslink_token')
    }).finally(() => setLoading(false))
  }, [])

  async function login(email, password) {
    const result = await authService.login({ email, password })
    localStorage.setItem('campuslink_token', result.token)
    setUser(result.user)
    return result.user
  }

  async function register(data) {
    return authService.register(data)
  }

  function logout() {
    localStorage.removeItem('campuslink_token')
    setUser(null)
  }

  async function refreshUser() {
    const fresh = await userService.me()
    setUser(fresh)
    return fresh
  }

  return <AuthContext.Provider value={{ user, loading, login, register, logout, refreshUser }}>{children}</AuthContext.Provider>
}

export const useAuth = () => useContext(AuthContext)
