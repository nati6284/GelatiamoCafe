import { useState } from 'react'
import { Link, NavLink } from 'react-router-dom'
import { Menu as MenuIcon, MapPin, X } from 'lucide-react'
import { useI18n } from '../i18n'
import { cafeConfig } from '../config'
import BranchSelector from './BranchSelector'

export default function Navbar({ selectedBranch, onSelectBranch, branches }) {
  const {t, lang, setLang} = useI18n()
  const [menuOpen, setMenuOpen] = useState(false)
  
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
