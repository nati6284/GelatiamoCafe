import { useEffect, useState } from 'react'
import { cafeConfig } from '../config'
import { api } from '../api'

export function QRPrintCard({ branch }) {
  const menuUrl = `${window.location.origin}/menu?branch=${branch.code}`
  const qrSrc = `https://api.qrserver.com/v1/create-qr-code/?size=500x500&data=${encodeURIComponent(menuUrl)}`
  const logoSrc = `${window.location.origin}/logo.png`

  const handlePrint = () => {
    const win = window.open('', '_blank', 'width=800,height=900')
    win.document.write(`<!DOCTYPE html><html><head>
      <title>${branch.name} — QR Code</title>
      <style>
        @page { size: A4; margin: 0 }
        * { margin: 0; padding: 0; box-sizing: border-box }
        body {
          font-family: 'Helvetica Neue', Arial, sans-serif;
          background: #fff;
          color: #1a1a1a;
          width: 210mm;
          min-height: 297mm;
          padding: 48px 52px;
        }
        .header { display: flex; align-items: center; gap: 20px; margin-bottom: 28px }
        .header img { height: 72px; object-fit: contain }
        .brand h1 { font-size: 36px; font-weight: 800; letter-spacing: 1.5px; line-height: 1 }
        .brand p { font-size: 14px; color: #555; margin-top: 4px }
        hr { border: none; border-top: 2.5px solid #009246; margin: 0 0 52px }
        .scan-title { font-size: 30px; font-weight: 700; color: #009246; text-align: center; margin-bottom: 10px }
        .branch-name { font-size: 15px; color: #666; text-align: center; font-style: italic; margin-bottom: 28px }
        .qr-box { border: 2.5px solid #009246; border-radius: 18px; padding: 32px; background: #fff; width: 360px; margin: 0 auto }
        .qr-box img { display: block; width: 100%; height: auto }
        .hint { font-size: 13px; color: #777; text-align: center; line-height: 1.6; margin-top: 28px }
      </style>
    </head><body>
      <div class="header">
        <img id="logo" src="${logoSrc}" alt="logo" />
        <div class="brand"><h1>${cafeConfig.name}</h1><p>${cafeConfig.tagline}</p></div>
      </div>
      <hr/>
      <p class="scan-title">Scan for Our Digital Menu</p>
      <p class="branch-name">${branch.name}</p>
      <div class="qr-box"><img id="qr" src="${qrSrc}" alt="QR Code" /></div>
      <p class="hint">Point your camera at the QR code to view our complete menu</p>
      <script>
        var logo = document.getElementById('logo');
        var qr = document.getElementById('qr');
        var loaded = 0;
        function tryPrint() { loaded++; if (loaded === 2) { window.focus(); window.print(); window.close(); } }
        logo.onload = tryPrint; logo.onerror = tryPrint;
        qr.onload = tryPrint; qr.onerror = tryPrint;
        if (logo.complete) tryPrint();
        if (qr.complete) tryPrint();
      </script>
    </body></html>`)
    win.document.close()
  }

  return (
    <div className="qr-print-card">

      {/* Preview card */}
      <div className="qr-print-preview">
        <div className="qr-print-header">
          <img src="/logo.png" alt="logo" className="qr-print-logo" />
          <div>
            <strong>{cafeConfig.name}</strong>
            <span>{cafeConfig.tagline}</span>
          </div>
        </div>
        <div className="qr-print-divider" />
        <p className="qr-print-title">Scan for Our Digital Menu</p>
        <p className="qr-print-branch">{branch.name}</p>
        <div className="qr-print-box">
          <img src={qrSrc} alt={`QR for ${branch.name}`} />
        </div>
        <p className="qr-print-hint">Point your camera at the QR code to view our complete menu</p>
      </div>

      <button className="button" onClick={handlePrint}>
        Print QR — {branch.name}
      </button>
    </div>
  )
}

export default function QRPage() {
  const [branches, setBranches] = useState([])

  useEffect(() => {
    api.branches().then(setBranches).catch(console.error)
  }, [])

  return <>
    <section className="page-banner">
      <span className="eyebrow eyebrow-dark">DIGITAL MENU</span>
      <h1>Branch QR Codes</h1>
      <p>Printable QR codes for each branch location</p>
    </section>

    <section className="section">
      <div className="qr-print-grid">
        {branches.filter(b => b.active).map(branch => (
          <QRPrintCard key={branch.id} branch={branch} />
        ))}
        {branches.length === 0 && (
          <p className="empty-state">No branches available.</p>
        )}
      </div>
    </section>
  </>
}
