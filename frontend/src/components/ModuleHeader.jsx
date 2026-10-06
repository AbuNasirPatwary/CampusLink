import AppIcon from './AppIcon'
export default function ModuleHeader({ label, title, description, icon, children }) {
  return <section className="module-page-head">
    <div><div className="eyebrow"><span/>{icon&&<AppIcon name={icon} size={15}/>} {label}</div><div className="hero-title">{title}</div>{description&&<p>{description}</p>}</div>
    {children&&<div className="module-head-actions">{children}</div>}
  </section>
}
