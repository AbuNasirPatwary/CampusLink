import AppIcon from './AppIcon'
export default function AppButton({ children, variant='primary', icon, type='button', className='', ...props }) {
  return <button type={type} className={`btn btn-${variant} ${className}`} {...props}>{icon && <AppIcon name={icon} size={18}/>}<span>{children}</span></button>
}
