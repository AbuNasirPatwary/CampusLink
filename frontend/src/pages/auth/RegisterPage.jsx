import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import Brand from '../../components/Brand'
import AppIcon from '../../components/AppIcon'
import AppButton from '../../components/AppButton'

const initial={role:'STUDENT',studentId:'',fullName:'',email:'',department:'',semester:'',graduationYear:'',password:'',confirmPassword:''}

export default function RegisterPage(){
  const {register}=useAuth(); const nav=useNavigate(); const[form,setForm]=useState(initial); const[error,setError]=useState(''); const[busy,setBusy]=useState(false); const[result,setResult]=useState(null)
  const alumni=form.role==='ALUMNI'
  const set=(key,value)=>setForm(v=>({...v,[key]:value}))
  async function submit(e){e.preventDefault();setError('');if(form.password!==form.confirmPassword){setError('Passwords do not match.');return}setBusy(true);try{const payload={role:form.role,studentId:form.studentId.trim(),fullName:form.fullName.trim(),email:form.email.trim(),department:form.department.trim()||null,semester:alumni?null:(form.semester.trim()||null),graduationYear:alumni&&form.graduationYear?Number(form.graduationYear):null,password:form.password};const user=await register(payload);setResult(user)}catch(e){setError(e.message)}finally{setBusy(false)}}

  if(result) return <main className="registration-screen"><section className="registration-brand-panel"><Brand/><div><div className="eyebrow light"><AppIcon name="shield" size={16}/> UIU community access</div><div className="registration-brand-title">One account.<br/>Your campus community.</div><p>CampusLink connects university assistance, conversation, and creativity in one trusted place.</p></div></section><section className="registration-panel"><button className="login-back" onClick={()=>nav('/')}><AppIcon name="arrow" size={16}/> Back to CampusLink</button><div className="registration-success"><span><AppIcon name={alumni?'clock':'check'} size={29}/></span><div className="registration-status-label">{alumni?'Pending approval':'Account created'}</div><div className="registration-title">{alumni?'Registration submitted':'Welcome to CampusLink'}</div><p>{alumni?'Your alumni registration is waiting for administrator approval. Your previous UIU Student ID will be used for the Phase-One manual verification.':'Your student account is active and ready to use.'}</p><div className="registration-pending"><span>Status</span><strong>{result.status?.replaceAll('_',' ')}</strong></div><div className="registration-success-actions"><AppButton onClick={()=>nav('/login')}>Sign In</AppButton><AppButton variant="secondary" onClick={()=>nav('/')}>Back to CampusLink</AppButton></div></div></section></main>

  return <main className="registration-screen">
    <section className="registration-brand-panel"><Brand/><div><div className="eyebrow light"><AppIcon name="spark" size={16}/> United International University</div><div className="registration-brand-title">Join the community<br/>behind the campus.</div><p>Students and approved alumni use the same CampusLink workspace. Registration only differs in how university identity is verified.</p></div></section>
    <section className="registration-panel"><button className="login-back" onClick={()=>nav('/')}><AppIcon name="arrow" size={16}/> Back to CampusLink</button>
      <form className="registration-form" onSubmit={submit}><div className="eyebrow"><span className="live-dot"/> Create account</div><div className="registration-title">Create your CampusLink account</div><p>Join the United International University community.</p>
        {error&&<div className="auth-error">{error}</div>}
        <div className="registration-step"><span>Step 1</span><strong>Choose your account type</strong></div>
        <div className="registration-types">
          <button type="button" className={!alumni?'selected':''} onClick={()=>set('role','STUDENT')}><span><AppIcon name="profile" size={21}/></span><div><strong>Current student</strong><small>Student ID + UIU university email.</small></div><i>{!alumni&&<AppIcon name="check" size={13}/>}</i></button>
          <button type="button" className={alumni?'selected':''} onClick={()=>set('role','ALUMNI')}><span><AppIcon name="users" size={21}/></span><div><strong>Alumni</strong><small>Previous Student ID + any valid email.</small></div><i>{alumni&&<AppIcon name="check" size={13}/>}</i></button>
        </div>
        {alumni&&<div className="registration-info"><AppIcon name="shield" size={18}/><p>Alumni accounts require administrator approval. For this Phase-One university project, the administrator manually checks the previous UIU Student ID.</p></div>}
        <div className="registration-step second"><span>Step 2</span><strong>Your information</strong></div>
        <div className="registration-fields">
          <label><span>Full name</span><input required value={form.fullName} onChange={e=>set('fullName',e.target.value)} placeholder={alumni?'Name used at UIU':'Your full name'}/></label>
          <label><span>Student ID</span><input required value={form.studentId} onChange={e=>set('studentId',e.target.value)} placeholder={alumni?'Your previous UIU student ID':'e.g. 011XXXXXXXX'}/></label>
          <label><span>{alumni?'Email':'UIU University Email'}</span><input required type="email" value={form.email} onChange={e=>set('email',e.target.value)} placeholder={alumni?'you@gmail.com':'name@uiu.ac.bd'}/></label>
          <label><span>Department / Program <small>Optional</small></span><input value={form.department} onChange={e=>set('department',e.target.value)} placeholder="e.g. Computer Science"/></label>
          {!alumni?<label><span>Semester <small>Optional</small></span><input value={form.semester} onChange={e=>set('semester',e.target.value)} placeholder="e.g. Semester 5"/></label>:<label><span>Graduation year <small>Optional</small></span><input type="number" min="1980" max="2100" value={form.graduationYear} onChange={e=>set('graduationYear',e.target.value)} placeholder="e.g. 2022"/></label>}
          <label><span>Password</span><input required minLength={8} type="password" value={form.password} onChange={e=>set('password',e.target.value)} placeholder="At least 8 characters"/></label>
          <label><span>Confirm password</span><input required minLength={8} type="password" value={form.confirmPassword} onChange={e=>set('confirmPassword',e.target.value)} placeholder="Repeat your password"/></label>
        </div>
        <AppButton type="submit" className="registration-submit" disabled={busy}>{busy?'Submitting...':alumni?'Submit alumni registration':'Create student account'} <AppIcon name="arrow" size={17}/></AppButton>
        <div className="registration-signin">Already have an account? <button type="button" onClick={()=>nav('/login')}>Sign In</button></div>
      </form>
    </section>
  </main>
}
