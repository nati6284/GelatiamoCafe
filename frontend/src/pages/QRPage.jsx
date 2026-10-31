import { useEffect, useState } from 'react'
import { useI18n } from '../i18n'
import { cafeConfig } from '../config'
import { api } from '../api'

export default function QRPage() {
  const {t} = useI18n()
  const [branches, setBranches] = useState([])
  
  useEffect(() => {
    api.branches().then(setBranches).catch(console.error)
  }, [])
  
  const getQRUrl = (branchCode) => {
    const menuUrl = `${window.location.origin}/menu?branch=${branchCode}`
    return `https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=${encodeURIComponent(menuUrl)}`
  }
  
  const downloadQR = (branchCode, branchName) => {
    const link = document.createElement('a')
    link.href = getQRUrl(branchCode)
    link.download = `${branchName.replace(/\s+/g, '_')}_QR.png`
    link.click()
  }
  
  return <>
    <section className="page-banner no-print">
      <span className="eyebrow">DIGITAL MENU</span>
      <h1>{t('qrTitle')}</h1>
      <p>Branch-specific QR codes for location-based pricing</p>
      <p>Print QR codes for each branch location</p>
    </section>
    
    <section className="section qr-section no-print">
      <div className="qr-grid">
        {branches.filter(b => b.active).map(branch => (
          <div key={branch.id} className="qr-card">
            <h3>{branch.name}</h3>
            <p className="qr-address">{branch.address}</p>
            
            <div className="qr-code-wrapper">
              <img 
                src={getQRUrl(branch.code)} 
                alt={`QR Code for ${branch.name}`}
                className="qr-image"
              />
            </div>
            
            <div className="qr-info">
              <p><strong>Branch Code:</strong> {branch.code}</p>
              <p className="qr-url">
                {window.location.origin}/menu?branch={branch.code}
              </p>
            </div>
            
            <button 
              className="button"
              onClick={() => downloadQR(branch.code, branch.name)}
            >
              Download QR Code
            </button>
          </div>
        ))}
      </div>
      
      {branches.length === 0 && (
        <div className="empty-state">
          <p>No branches available. Please add branches in the admin panel.</p>
        </div>
      )}
      
      <div className="qr-instructions">
        <h2>How It Works</h2>
        <ol>
          <li>Each branch has its own unique QR code</li>
          <li>Customers scan the QR code at their branch</li>
          <li>They see the menu with prices specific to that location</li>
          <li>You can set different prices per branch in the admin panel</li>
        </ol>
      </div>
    </section>
  </>
}
