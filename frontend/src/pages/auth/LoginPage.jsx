import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import uiuLoginPhoto from '../../assets/UIU2.jpg'
import Brand from '../../components/Brand'
import AppIcon from '../../components/AppIcon'
import AppButton from '../../components/AppButton'

export default function LoginPage(){
  const {login}=useAuth(); const nav=useNavigate()
  const[form,setForm]=useState({email:'',password:''}); const[error,setError]=useState(''); const[busy,setBusy]=useState(false)
  async function submit(e){e.preventDefault();setError('');setBusy(true);try{const user=await login(form.email,form.password);nav(user.role==='ADMIN'?'/admin':'/dashboard')}catch(e){if(e.status===403 && /waiting for administrator approval/i.test(e.message)) nav('/pending',{state:{email:form.email}}); else setError(e.message)}finally{setBusy(false)}}
  function fillAdmin(){setForm({email:'admin@campuslink.edu',password:'Admin@123'})}
  return <main className="login-screen">
    <section className="login-story"><img src={uiuLoginPhoto} alt="United International University campus entrance and university buses"/><div className="photo-shade"/><div className="story-top"><Brand/></div><div className="story-copy"><div className="eyebrow light"><AppIcon name="spark" size={16}/> Your campus, connected</div><div className="display-title">Everything campus life<br/>needs, in one place.</div><p>University assistance, community, and talent sharing—made simpler for everyone.</p><div className="story-features"><div><span><AppIcon name="find" size={19}/></span>Find what matters</div><div><span><AppIcon name="users" size={19}/></span>Help your community</div><div><span><AppIcon name="talent" size={19}/></span>Share your talent</div></div></div></section>
    <section className="login-panel">
      <button className="login-back" type="button" onClick={()=>nav('/')}><AppIcon name="arrow" size={16}/> Back to CampusLink</button>
      <div className="mobile-brand"><Brand/></div>
      <div className="login-form-wrap"><div className="eyebrow"><span className="live-dot"/> Campus community portal</div><div className="page-title">Welcome back</div><p className="page-subtitle">Sign in to continue to your CampusLink account.</p>
        <form onSubmit={submit}>
          {error&&<div className="auth-error">{error}</div>}
          <label className="field-label" htmlFor="email">Email</label><div className="field"><AppIcon name="mail" size={19}/><input id="email" name="email" type="email" value={form.email} onChange={e=>setForm({...form,email:e.target.value})} placeholder="you@example.com" required/></div>
          <div className="password-row"><label className="field-label" htmlFor="password">Password</label><button type="button" className="text-action" onClick={()=>setError('Password reset is outside the Phase-One demo scope.')}>Forgot password?</button></div>
          <div className="field"><AppIcon name="shield" size={19}/><input id="password" type="password" value={form.password} onChange={e=>setForm({...form,password:e.target.value})} placeholder="Your password" required/></div>
          <AppButton type="submit" className="login-btn" disabled={busy}>{busy?'Signing in...':'Sign in to CampusLink'} <AppIcon name="arrow" size={18}/></AppButton>
        </form>
        <div className="register-row">New to CampusLink? <button type="button" className="text-action strong" onClick={()=>nav('/register')}>Create your account</button></div>
        <div className="demo-note"><div className="demo-icon"><AppIcon name="shield" size={18}/></div><div><strong>Demo administrator</strong><span>admin@campuslink.edu · Admin@123</span></div><button type="button" onClick={fillAdmin}>Use</button></div>
        <div className="login-footer">Phase-One university project prototype.</div>
      </div>
    </section>
  </main>
}
