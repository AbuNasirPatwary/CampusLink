import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import uiuCampusPhoto from '../assets/UIU1.jpg'
import AppIcon from '../components/AppIcon'
import AppButton from '../components/AppButton'

const services = [
  ['FindIt','Report lost or found items and help belongings return to their owners.','find','01'],
  ['BorrowHub','Request books, notes, electronics, project components, and other temporary items from the UIU community.','borrow','02'],
  ['Complaint Board','Raise campus concerns, join discussions, and follow issues as they are addressed.','complaint','03'],
  ['TalentHub','Share and discover writing, photography, video, art, and design from the UIU community.','talent','04'],
]

const roles = [
  ['Students & Alumni','Active accounts use the same complete CampusLink workspace.','Full access','users'],
  ['Administrators','Handle alumni verification, FindIt claims, complaint updates, moderation, and platform oversight.','Platform access','shield'],
]

export default function LandingPage(){
  const nav=useNavigate(); const[menuOpen,setMenuOpen]=useState(false)
  const go=(id)=>{document.getElementById(id)?.scrollIntoView({behavior:'smooth'});setMenuOpen(false)}
  return <main className="landing-page" id="home">
    <section className="landing-hero">
      <img className="landing-hero-image" src={uiuCampusPhoto} alt="United International University campus"/>
      <div className="landing-hero-shade"/>
      <header className="landing-nav">
        <button className="landing-logo" onClick={()=>go('home')}><span className="landing-logo-mark"><span/><span/><span/></span><span className="landing-logo-name">Campus<span>Link</span></span></button>
        <nav className={`landing-nav-links ${menuOpen?'open':''}`}>
          <button onClick={()=>go('home')}>Home</button><button onClick={()=>go('community')}>About</button><button onClick={()=>go('services')}>Services</button>
          <div className="landing-mobile-actions"><button className="landing-signin" onClick={()=>nav('/login')}>Sign In</button><button className="landing-create" onClick={()=>nav('/register')}>Create Account</button></div>
        </nav>
        <div className="landing-nav-actions"><button className="landing-signin" onClick={()=>nav('/login')}>Sign In</button><button className="landing-create" onClick={()=>nav('/register')}>Create Account</button></div>
        <button className="landing-menu" onClick={()=>setMenuOpen(v=>!v)} aria-label="Open navigation"><AppIcon name="menu" size={22}/></button>
      </header>
      <div className="landing-hero-content">
        <div className="landing-eyebrow"><span/> United International University Community</div>
        <div className="landing-display">Your Campus.<br/><span>Connected.</span></div>
        <p>CampusLink brings university assistance, community interaction, and student talent together in one connected platform.</p>
        <div className="landing-hero-actions"><AppButton className="landing-primary" onClick={()=>nav('/register')}>Get Started <AppIcon name="arrow" size={18}/></AppButton><AppButton className="landing-secondary" onClick={()=>nav('/login')}>Sign In</AppButton></div>
        <div className="landing-built"><AppIcon name="shield" size={15}/> Built for the UIU community</div>
      </div>
      <button className="landing-scroll" onClick={()=>go('services')}><span>Explore CampusLink</span><AppIcon name="arrow" size={17}/></button>
    </section>

    <section className="landing-services landing-section" id="services">
      <div className="landing-section-intro"><div><div className="landing-kicker">Campus services</div><div className="landing-heading">Everything you need<br/>around campus.</div></div><p>One platform for everyday university assistance, community participation, and creative expression.</p></div>
      <div className="landing-service-grid">{services.map(([title,description,icon,number])=><article className="landing-service-card" key={title}><div className="landing-card-top"><span className="landing-card-icon"><AppIcon name={icon} size={24}/></span><span className="landing-card-number">{number}</span></div><div className="landing-card-title">{title}</div><p>{description}</p></article>)}</div>
    </section>

    <section className="landing-community" id="community">
      <div className="landing-community-copy"><div className="landing-kicker light">One university. One community.</div><div className="landing-heading light">Built for the<br/>UIU community.</div><p>Students and approved alumni share one simple CampusLink workspace. Administrators handle only the verification and moderation tasks that need them.</p><div className="landing-community-seal"><span>UIU</span><div>United International<br/>University</div></div></div>
      <div className="landing-role-list">{roles.map(([title,description,label,icon])=><article className="landing-role" key={title}><span className="landing-role-icon"><AppIcon name={icon} size={22}/></span><div><span>{label}</span><div className="landing-role-title">{title}</div><p>{description}</p></div><span className="landing-role-arrow"><AppIcon name="arrow" size={18}/></span></article>)}</div>
    </section>

    <section className="landing-process landing-section"><div className="landing-process-head"><div className="landing-kicker">How it works</div><div className="landing-heading">Simple from the start.</div></div><div className="landing-steps"><article><span>01</span><div className="landing-step-title">Join CampusLink</div><p>Register with your UIU student ID and the email appropriate for your account type.</p></article><article><span>02</span><div className="landing-step-title">Choose a service</div><p>Use FindIt, BorrowHub, Complaint Board, or TalentHub from one workspace.</p></article><article><span>03</span><div className="landing-step-title">Connect with your community</div><p>Ask for help, contribute, solve problems, or share your work.</p></article></div></section>

    <section className="landing-cta-wrap"><div className="landing-cta"><div><div className="landing-kicker light">Start your CampusLink journey</div><div className="landing-cta-title">Ready to connect<br/>with your campus?</div></div><div className="landing-cta-copy"><p>Join CampusLink and become part of a more connected UIU community.</p><div><AppButton className="landing-primary" onClick={()=>nav('/register')}>Create Account <AppIcon name="arrow" size={18}/></AppButton><AppButton className="landing-cta-signin" onClick={()=>nav('/login')}>Sign In</AppButton></div></div></div></section>
    <footer className="landing-footer"><div className="landing-footer-main"><div><div className="landing-footer-brand">Campus<span>Link</span></div><p>United International University</p><small>University Assistance, Community and Talent-Sharing Platform</small></div><nav><button onClick={()=>go('home')}>Home</button><button onClick={()=>go('community')}>About</button><button onClick={()=>go('services')}>Services</button><button onClick={()=>nav('/login')}>Sign In</button></nav></div><div className="landing-footer-bottom"><span>© 2026 CampusLink</span><span>Made for the UIU community</span></div></footer>
  </main>
}
