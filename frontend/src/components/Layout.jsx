import { NavLink, Outlet, useLocation, useNavigate } from 'react-router-dom'
import { useState } from 'react'
import { useAuth } from '../context/AuthContext'
import Brand from './Brand'
import AppIcon from './AppIcon'

const normalNav=[['Home','home','/dashboard'],['FindIt','find','/findit'],['BorrowHub','borrow','/borrowhub'],['Complaints','complaint','/complaints'],['TalentHub','talent','/talent']]
const adminNav=[['Dashboard','grid','/admin'],['User Verification','users','/admin/verification'],['FindIt Claims','find','/admin/findit-claims'],['Complaints','complaint','/admin/complaints'],['TalentHub','talent','/admin/talent']]

function activeFor(path,target){if(target==='/dashboard'||target==='/admin') return path===target;return path===target||path.startsWith(target+'/')}

export default function Layout(){
  const {user,logout}=useAuth(); const nav=useNavigate(); const loc=useLocation(); const[menuOpen,setMenuOpen]=useState(false)
  const admin=user?.role==='ADMIN'; const items=admin?adminNav:normalNav
  const active=items.find(([, ,to])=>activeFor(loc.pathname,to))?.[0] || (loc.pathname.startsWith('/profile')?'Profile':loc.pathname.startsWith('/notifications')?'Notifications':'CampusLink')
  const initials=(user?.fullName||'Campus User').split(/\s+/).map(x=>x[0]).join('').slice(0,2).toUpperCase()
  const meta=admin?'Platform administrator':user?.role==='ALUMNI'?'Approved alumni':(user?.department?`${user.department}${user.semester?` · ${user.semester}`:''}`:'Current student')
  function signOut(){logout();nav('/login')}
  return <div className={`app-shell dashboard-shell ${active==='FindIt'?'findit-shell':''} ${admin?'admin-shell':''}`}>
    <aside className={`sidebar ${menuOpen?'sidebar-open':''}`}>
      <div className="sidebar-brand"><Brand compact/><button className="mobile-close" onClick={()=>setMenuOpen(false)} aria-label="Close navigation">×</button></div>
      <nav className="side-nav"><span className="nav-label">{admin?'Admin':'Workspace'}</span>
        {items.map(([label,icon,to])=><NavLink key={label} to={to} onClick={()=>setMenuOpen(false)} className={`nav-item ${activeFor(loc.pathname,to)?'active':''}`}><AppIcon name={icon} size={20}/><span>{label}</span>{activeFor(loc.pathname,to)&&<i/>}</NavLink>)}
        <span className="nav-label second">Account</span>
        {!admin&&<NavLink to="/notifications" onClick={()=>setMenuOpen(false)} className={`nav-item ${loc.pathname.startsWith('/notifications')?'active':''}`}><AppIcon name="bell" size={20}/><span>Notifications</span>{loc.pathname.startsWith('/notifications')&&<i/>}</NavLink>}
        <NavLink to="/profile" onClick={()=>setMenuOpen(false)} className={`nav-item ${loc.pathname.startsWith('/profile')?'active':''}`}><AppIcon name="profile" size={20}/><span>Profile</span>{loc.pathname.startsWith('/profile')&&<i/>}</NavLink>
      </nav>
      <div className="sidebar-foot"><div className="profile-mini"><div className="avatar">{initials}</div><div><strong>{user?.fullName}</strong><span>{meta}</span></div><button onClick={signOut} aria-label="Log out"><AppIcon name="logout" size={18}/></button></div></div>
    </aside>
    <div className="app-main">
      <header className="topbar"><button className="menu-button" onClick={()=>setMenuOpen(true)} aria-label="Open navigation"><AppIcon name="menu"/></button><div className="topbar-context"><span>{admin?'Admin console':'Campus community portal'}</span><strong>{active}</strong></div><div className="topbar-actions">{!admin&&<button className="icon-button" onClick={()=>nav('/notifications')} aria-label="Notifications"><AppIcon name="bell" size={20}/></button>}<div className="top-avatar">{initials}</div></div></header>
      <Outlet/>
    </div>
    {menuOpen&&<button className="sidebar-scrim" onClick={()=>setMenuOpen(false)} aria-label="Close navigation"/>}
  </div>
}
