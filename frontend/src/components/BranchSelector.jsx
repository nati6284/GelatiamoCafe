import { useEffect, useRef, useState } from 'react'
import { MapPin } from 'lucide-react'

export default function BranchSelector({ selectedBranch, onSelectBranch, branches = [], forceOpen, onClose }) {
  const [show, setShow] = useState(false)
  const containerRef = useRef(null)

  useEffect(() => {
    if (branches.length === 0) return
    const params = new URLSearchParams(window.location.search)
    const branchCode = params.get('branch')
    if (branchCode) {
      const branch = branches.find(b => b.code === branchCode.toUpperCase())
      if (branch) { onSelectBranch(branch); return }
    }
    if (selectedBranch) {
      const fresh = branches.find(b => b.id === selectedBranch.id)
      if (fresh && fresh.name !== selectedBranch.name) onSelectBranch(fresh)
      return
    }
    const def = branches.find(b => b.code === 'BOLE_MEDHANIALEM') || branches.find(b => b.active)
    if (def) onSelectBranch(def)
  }, [branches])

  useEffect(() => {
    if (forceOpen) setShow(true)
  }, [forceOpen])

  // Close when tapping outside
  useEffect(() => {
    if (!show) return
    const handleOutside = (e) => {
      if (containerRef.current && !containerRef.current.contains(e.target)) {
        setShow(false)
        if (onClose) onClose()
      }
    }
    document.addEventListener('pointerdown', handleOutside)
    return () => document.removeEventListener('pointerdown', handleOutside)
  }, [show])

  const handleSelect = (branch) => {
    onSelectBranch(branch)
    setShow(false)
    if (onClose) onClose()
  }

  return (
    <div className="branch-selector" ref={containerRef}>
      <button className="branch-button" onClick={() => setShow(s => !s)}>
        <MapPin size={16} />
        {selectedBranch ? selectedBranch.name : 'Select Branch'}
      </button>

      {show && (
        <div className="branch-dropdown">
          <h3>Choose Your Branch</h3>
          <div className="branch-list">
            {branches.filter(b => b.active).map(branch => (
              <button
                key={branch.id}
                className={`branch-option ${selectedBranch?.id === branch.id ? 'active' : ''}`}
                onPointerDown={(e) => { e.stopPropagation(); handleSelect(branch) }}
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
      )}
    </div>
  )
}
