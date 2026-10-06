import { useState } from 'react'
import { useAuth } from '../../context/AuthContext'
import { userService } from '../../services/userService'
import { mediaService } from '../../services/mediaService'
import ModuleHeader from '../../components/ModuleHeader'
import AppButton from '../../components/AppButton'
import AppIcon from '../../components/AppIcon'
import { mediaUrl } from '../../components/MediaPreview'

export default function ProfilePage(){
  const {user,refreshUser}=useAuth(); const[editing,setEditing]=useState(false); const[error,setError]=useState(''); const[msg,setMsg]=useState(''); const[passwordOpen,setPasswordOpen]=useState(false); const[pw,setPw]=useState({currentPassword:'',newPassword:''})
  const[form,setForm]=useState({fullName:user.fullName||'',department:user.department||'',semester:user.semester||'',graduationYear:user.graduationYear||'',bio:user.bio||'',phone:user.phone||'',profileImageUrl:user.profileImageUrl||''})
  const initials=(user.fullName||'Campus User').split(/\s+/).map(x=>x[0]).join('').slice(0,2).toUpperCase(); const roleLabel=user.role==='ADMIN'?'Platform administrator':user.role==='ALUMNI'?'Alumni':'Current student'
  async function upload(e){const f=e.target.files?.[0];if(!f)return;setError('');try{const r=await mediaService.upload(f,'profiles');setForm(v=>({...v,profileImageUrl:r.url}));setMsg('Photo uploaded. Save changes to update your profile.')}catch(e){setError(e.message)}}
  async function save(){setError('');setMsg('');try{await userService.updateMe({...form,graduationYear:user.role==='ALUMNI'&&form.graduationYear?Number(form.graduationYear):null,semester:user.role==='STUDENT'?form.semester:null});await refreshUser();setEditing(false);setMsg('Profile updated successfully.')}catch(e){setError(e.message)}}
  async function changePassword(e){e.preventDefault();setError('');setMsg('');try{await userService.changePassword(pw);setPw({currentPassword:'',newPassword:''});setPasswordOpen(false);setMsg('Password changed successfully.')}catch(e){setError(e.message)}}
  return <main className="content module-page profile-page"><ModuleHeader label="Account" title={user.role==='ADMIN'?'Admin account':'My profile'} description="Manage your CampusLink account and university information."><AppButton icon={editing?'check':'profile'} onClick={()=>editing?save():setEditing(true)}>{editing?'Save changes':'Edit profile'}</AppButton></ModuleHeader>
    {error&&<div className="auth-error">{error}</div>}{msg&&<div className="success-banner">{msg}</div>}
    <section className="profile-summary-card"><div className="profile-photo-wrap"><div className="profile-photo">{form.profileImageUrl?<img src={mediaUrl(form.profileImageUrl)} alt="Profile"/>:initials}</div><label><input type="file" accept="image/*" onChange={upload}/><AppIcon name="plus" size={14}/> Change photo</label></div><div className="profile-summary-copy"><span>{roleLabel}</span><div>{user.fullName}</div><p>{user.role==='ADMIN'?'CampusLink Administration':`${user.department||'UIU Community'}${user.role==='ALUMNI'?(user.graduationYear?` · Alumni ${user.graduationYear}`:' · Alumni'):(user.semester?` · ${user.semester}`:'')}`}</p><small>{user.email}</small></div><div className="profile-summary-status"><i/><span>Account {String(user.status).toLowerCase()}</span></div></section>
    <div className="profile-layout"><section className="profile-info-card"><div className="profile-card-head"><div><span>Personal information</span><strong>University profile</strong></div><AppIcon name="profile" size={20}/></div><div className="profile-fields">
      <label><span>Full name</span><input disabled={!editing} value={form.fullName} onChange={e=>setForm({...form,fullName:e.target.value})}/></label>
      {user.role!=='ADMIN'&&<label><span>Student ID</span><input disabled value={user.studentId||''}/><small>University identity field cannot be changed here.</small></label>}
      <label><span>{user.role==='ADMIN'?'Administrative unit':'Department / program'}</span><input disabled={!editing} value={user.role==='ADMIN'?'CampusLink Administration':form.department} onChange={e=>setForm({...form,department:e.target.value})}/></label>
      {user.role==='STUDENT'&&<label><span>Semester</span><input disabled={!editing} value={form.semester} onChange={e=>setForm({...form,semester:e.target.value})}/></label>}
      {user.role==='ALUMNI'&&<label><span>Graduation year</span><input disabled={!editing} type="number" value={form.graduationYear} onChange={e=>setForm({...form,graduationYear:e.target.value})}/></label>}
      <label className="verified-field"><span>Email <small><AppIcon name="check" size={12}/> Verified</small></span><div><AppIcon name="mail" size={17}/><input disabled value={user.email}/></div><small>Account email cannot be changed here.</small></label>
      {user.role!=='ADMIN'&&<label><span>Bio <small>Optional</small></span><textarea disabled={!editing} value={form.bio} onChange={e=>setForm({...form,bio:e.target.value})}/></label>}
      {user.role!=='ADMIN'&&<label><span>Phone <small>Optional</small></span><input disabled={!editing} value={form.phone} onChange={e=>setForm({...form,phone:e.target.value})}/></label>}
    </div></section>
    <aside className="profile-side"><section className="account-status-card"><div className="profile-card-head"><div><span>Account status</span><strong>CampusLink access</strong></div><AppIcon name="shield" size={20}/></div><dl><div><dt>Account status</dt><dd><i/>{user.status}</dd></div><div><dt>Account type</dt><dd>{roleLabel}</dd></div></dl></section><section className="security-card"><div className="security-icon"><AppIcon name="shield" size={20}/></div><div><strong>Password &amp; security</strong><p>Use a strong password for your CampusLink account.</p></div><AppButton variant="secondary" onClick={()=>setPasswordOpen(v=>!v)}>{passwordOpen?'Cancel':'Change password'}</AppButton></section>{passwordOpen&&<form className="password-card" onSubmit={changePassword}><label><span>Current password</span><input type="password" required value={pw.currentPassword} onChange={e=>setPw({...pw,currentPassword:e.target.value})}/></label><label><span>New password</span><input type="password" minLength={8} required value={pw.newPassword} onChange={e=>setPw({...pw,newPassword:e.target.value})}/></label><AppButton type="submit">Update password</AppButton></form>}</aside></div>
  </main>
}
