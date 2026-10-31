import { useEffect, useState } from 'react'
import { MapPin } from 'lucide-react'
import { api } from '../api'

export default function BranchSelector({ selectedBranch, onSelectBranch, forceOpen, onClose }) {
  const [branches, setBranches] = useState([])
  const [show, setShow] = useState(false)
  
  useEffect(() => {
    api.branches().then(setBranches).catch(console.error)
  }, [])
  
  // Auto-detect branch from URL query parameter, or default to Bole Medhanialem
  useEffect(() => {
    if (branches.length === 0) return
    const params = new URLSearchParams(window.location.search)
    const branchCode = params.get('branch')
    if (branchCode) {
      const branch = branches.find(b => b.code === branchCode.toUpperCase())
      if (branch) { onSelectBranch(branch); return }
    }
    // Set default branch if none selected yet
    if (!selectedBranch) {
      const def = branches.find(b => b.code === 'BOLE_MEDHANIALEM') || branches.find(b => b.active)
      if (def) onSelectBranch(def)
    }
  }, [branches])
  
  // Handle force open from navbar
  useEffect(() => {
    if (forceOpen) {
      setShow(true)
    }
  }, [forceOpen])
  
  const handleClose = () => {
    setShow(false)
    if (onClose) onClose()
  }
  
  return (
    <div className="branch-selector">
      <button className="branch-button" onClick={() => setShow(!show)}>
        <MapPin size={16} />
        {selectedBranch ? selectedBranch.name : 'Select Branch'}
      </button>
      
      {show && (
        <>
          <div className="branch-overlay" onClick={handleClose} />
          <div className="branch-dropdown">
            <h3>Choose Your Branch</h3>
            <div className="branch-list">
              {branches.filter(b => b.active).map(branch => (
                <button
                  key={branch.id}
                  className={`branch-option ${selectedBranch?.id === branch.id ? 'active' : ''}`}
                  onClick={() => {
                    onSelectBranch(branch)
                    handleClose()
                  }}
                >
                  <div className="branch-info">
                    <strong>{branch.name}</strong>
                    <small>{branch.address}</small>
                    {branch.phone && <small>{branch.phone}</small>}
                  </div>
                  {selectedBranch?.id === branch.id && <span className="check">✓</span>}
                </button>
              ))}
            </div>
          </div>
        </>
      )}
    </div>
  )
}
