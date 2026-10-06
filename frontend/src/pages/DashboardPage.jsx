import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { findItService } from '../services/findItService'
import { borrowHubService } from '../services/borrowHubService'
import { complaintService } from '../services/complaintService'
import { talentService } from '../services/talentService'
import { notificationService } from '../services/notificationService'
import AppIcon from '../components/AppIcon'

const modules=[
  {title:'FindIt',description:'Search the campus lost-and-found board or help an item make its way home.',icon:'find',tone:'mint',action:'Explore FindIt',to:'/findit'},
  {title:'BorrowHub',description:'Request books, notes, electronics, and components from the UIU community.',icon:'borrow',tone:'violet',action:'Browse requests',to:'/borrowhub'},
  {title:'Complaints',description:'Raise campus concerns and join constructive community discussions.',icon:'complaint',tone:'amber',action:'View discussions',to:'/complaints'},
  {title:'TalentHub',description:'Discover and share original writing, photography, video, art, and design.',icon:'talent',tone:'rose',action:'See new work',to:'/talent'},
]

export default function DashboardPage(){
  const {user}=useAuth(); const nav=useNavigate(); const[counts,setCounts]=useState({}); const[notifications,setNotifications]=useState([])
  useEffect(()=>{let live=true;Promise.allSettled([findItService.all(),borrowHubService.all(),complaintService.all(),talentService.all(),notificationService.all()]).then(r=>{if(!live)return;setCounts({findit:r[0].status==='fulfilled'?r[0].value.length:null,borrow:r[1].status==='fulfilled'?r[1].value.length:null,complaint:r[2].status==='fulfilled'?r[2].value.length:null,talent:r[3].status==='fulfilled'?r[3].value.length:null});if(r[4].status==='fulfilled')setNotifications(r[4].value.slice(0,3))});return()=>{live=false}},[])
  const firstName=user.fullName?.split(' ')[0]||'there'; const initials=user.fullName?.split(/\s+/).map(x=>x[0]).join('').slice(0,2).toUpperCase();
  const today=useMemo(()=>new Intl.DateTimeFormat('en-US',{weekday:'long',month:'long',day:'numeric'}).format(new Date()),[])
  const meta=[counts.findit!=null?`${counts.findit} FindIt posts`:'FindIt ready',counts.borrow!=null?`${counts.borrow} borrow requests`:'BorrowHub ready',counts.complaint!=null?`${counts.complaint} concerns`:'Complaints ready',counts.talent!=null?`${counts.talent} works`:'TalentHub ready']
  return <main className="content dashboard">
    <section className="welcome"><div><div className="eyebrow"><AppIcon name="spark" size={16}/> {today}</div><div className="hero-title">Welcome, {firstName}.</div><p>Everything you need around the UIU community is available from the same workspace.</p></div><button className="profile-chip" onClick={()=>nav('/profile')}><div className="avatar large">{initials}</div><div><span>{user.department||'UIU Community'}</span><strong>{user.role==='ALUMNI'?`Alumni${user.graduationYear?` · ${user.graduationYear}`:''}`:(user.semester||'Current student')}</strong></div><AppIcon name="chevron" size={18}/></button></section>
    <section className="dashboard-grid"><div className="services-area"><div className="section-head"><div><div className="section-title">Campus services</div><p>Students and approved alumni have the same Phase-One access.</p></div><span className="section-count">4 services</span></div><div className="service-grid">{modules.map((m,i)=><article className={`service-card ${m.tone} ${i===0?'featured':''}`} key={m.title}><div className="service-top"><div className="service-icon"><AppIcon name={m.icon} size={23}/></div><span>{meta[i]}</span></div><div className="service-title">{m.title}</div><p>{m.description}</p><button onClick={()=>nav(m.to)}>{m.action}<AppIcon name="arrow" size={17}/></button>{i===0&&<div className="card-orbit"><span/><span/><span/></div>}</article>)}</div></div>
      <aside className="dashboard-rail"><div className="rail-card activity-card"><div className="rail-title"><div><span>Recent activity</span><strong>Your updates</strong></div><button onClick={()=>nav('/notifications')}>View all</button></div><div className="activity-list">{notifications.length?notifications.map((n,i)=><div className="activity-item" key={n.id}><div className="activity-icon"><AppIcon name={i%2?'complaint':'bell'} size={18}/></div><div><strong>{n.message}</strong><span>{new Date(n.createdAt).toLocaleString()}</span></div></div>):<div className="activity-empty">No notifications yet.</div>}</div></div>
      <div className="rail-card pulse-card"><div className="pulse-top"><div><span>Account access</span><strong>CampusLink Phase One</strong></div><div className="pulse-icon"><AppIcon name="users" size={20}/></div></div><div className="stats"><div><strong>4</strong><span>services</span></div><div><strong>1</strong><span>workspace</span></div><div><strong>100%</strong><span>shared access</span></div></div><div className="progress"><i/></div><small>{user.role==='ALUMNI'?'Alumni account approved and active':'Student account active'}</small></div></aside>
    </section>
  </main>
}
