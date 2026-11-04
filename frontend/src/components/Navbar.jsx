import { useState } from 'react'
import { Link, NavLink, useNavigate } from 'react-router-dom'
import { Menu as MenuIcon, X, LogOut, Settings, QrCode, LayoutDashboard } from 'lucide-react'
import { useI18n } from '../i18n'
import { cafeConfig } from '../config'
import BranchSelector from './BranchSelector'
import { api } from '../api'

export default function Navbar({ selectedBranch, onSelectBranch, branches, isAdmin }) {
  const {t, lang, setLang} = useI18n()
  const [menuOpen, setMenuOpen] = useState(false)
  const navigate = useNavigate()

  const handleAdminLogout = async () => {
    const token = sessionStorage.getItem('adminToken')
    try { await api.adminLogout(token) } catch {}
    sessionStorage.removeItem('adminToken')
    window.dispatchEvent(new Event('adminAuthChange'))
    navigate('/')
  }

  if (isAdmin) {
    return (
      <header className="site-header admin-header">
        <Link to="/" className="brand" aria-label="Home">
          <img src={cafeConfig.logo} alt="GELATIAMO" className="brand-logo"/>
        </Link>

        {/* Mobile hamburger */}
        <button
          className="mobile-menu icon-button admin-mobile-toggle"
          onClick={() => setMenuOpen(!menuOpen)}
          aria-label="Toggle navigation"
        >
          {menuOpen ? <X/> : <MenuIcon/>}
        </button>

        {/* Desktop nav */}
        <nav className={`main-nav admin-main-nav${menuOpen ? ' open' : ''}`}>
          {/* Mobile: branch selector inside drawer */}
          <div className="mobile-branch-wrapper">
            <BranchSelector
              selectedBranch={selectedBranch}
              onSelectBranch={(b) => { onSelectBranch(b); setMenuOpen(false) }}
              branches={branches}
            />
          </div>

          <NavLink to="/admin" onClick={() => setMenuOpen(false)}>
            <LayoutDashboard size={15}/> Dashboard
          </NavLink>
          <NavLink to="/menu" onClick={() => setMenuOpen(false)}>
            <Settings size={15}/> Menu Preview
          </NavLink>
          <NavLink to="/qr" onClick={() => setMenuOpen(false)}>
            <QrCode size={15}/> QR Codes
          </NavLink>

          {/* Mobile: sign out inside drawer */}
          <button className="admin-drawer-logout" onClick={handleAdminLogout}>
            <LogOut size={15}/> Sign Out
          </button>
        </nav>

        {/* Desktop: actions */}
        <div className="header-actions admin-desktop-actions">
          <BranchSelector selectedBranch={selectedBranch} onSelectBranch={onSelectBranch} branches={branches} />
          <span className="admin-badge">Admin</span>
          <button className="admin-logout-btn" onClick={handleAdminLogout}>
            <LogOut size={15}/> Sign Out
          </button>
        </div>
      </header>
    )
  }

  return (
    <header className="site-header">
      <Link to="/" className="brand" aria-label="Home">
        <img src={cafeConfig.logo} alt="GELATIAMO" className="brand-logo"/>
      </Link>
      
      <button 
        className="mobile-menu icon-button" 
        onClick={() => setMenuOpen(!menuOpen)} 
        aria-label="Toggle navigation"
      >
        {menuOpen ? <X/> : <MenuIcon/>}
      </button>
      
      <nav className={menuOpen ? 'main-nav open' : 'main-nav'}>
         <div className="mobile-branch-wrapper">
          <BranchSelector selectedBranch={selectedBranch} onSelectBranch={(b) => { onSelectBranch(b); setMenuOpen(false) }} branches={branches} />
        </div>
        <NavLink to="/" onClick={() => setMenuOpen(false)}>{t('home')}</NavLink>
        <NavLink to="/menu" onClick={() => setMenuOpen(false)}>{t('menu')}</NavLink>
        <NavLink to="/contact" onClick={() => setMenuOpen(false)}>{t('contact')}</NavLink>
        <NavLink to="/feedback" onClick={() => setMenuOpen(false)}>Feedback</NavLink>
        <button 
          className="mobile-lang-toggle" 
          onClick={() => setLang(lang === 'en' ? 'am' : 'en')}
        >
          {lang === 'en' ? 'አማ' : 'EN'}
        </button>
      </nav>
      
      <div className="header-actions">
        <BranchSelector selectedBranch={selectedBranch} onSelectBranch={onSelectBranch} branches={branches} />
        <button 
          className="lang-toggle" 
          onClick={() => setLang(lang === 'en' ? 'am' : 'en')}
        >
          {lang === 'en' ? 'አማ' : 'EN'}
        </button>
      </div>
    </header>
  )
}
