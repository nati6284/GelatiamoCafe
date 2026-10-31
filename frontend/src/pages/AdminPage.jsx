import { useState, useEffect } from 'react'
import { Plus, Edit, Trash2, Upload, Link as LinkIcon, LogOut, Lock, QrCode, DollarSign, ChevronLeft } from 'lucide-react'
import { api } from '../api'
import { API_BASE } from '../config'
import { CATEGORY_ORDER } from '../data'
import { QRPrintCard } from '../pages/QRPage'

// ── Login Screen ──────────────────────────────────────────────────────────────
function LoginScreen({ onLogin }) {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError]       = useState('')
  const [loading, setLoading]   = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const data = await api.adminLogin({ username, password })
      onLogin(data.token)
    } catch (err) {
      setError(err.message || 'Invalid credentials')
    } finally {
      setLoading(false)
    }
  }

  return (
    <>
      <section className="page-banner">
        <span className="eyebrow">ADMIN PANEL</span>
        <h1>Admin Login</h1>
        <p>Sign in to manage your content.</p>
      </section>
      <section className="section" style={{ display: 'flex', justifyContent: 'center' }}>
        <div style={{ width: '100%', maxWidth: '400px' }}>
          <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            <div className="field">
              <label>Username</label>
              <input
                type="text"
                value={username}
                onChange={e => setUsername(e.target.value)}
                required
                autoFocus
              />
            </div>
            <div className="field">
              <label>Password</label>
              <input
                type="password"
                value={password}
                onChange={e => setPassword(e.target.value)}
                required
              />
            </div>
            {error && <p style={{ color: '#e53e3e', margin: 0 }}>{error}</p>}
            <button type="submit" className="button" disabled={loading}>
              <Lock size={16} /> {loading ? 'Signing in…' : 'Sign In'}
            </button>
          </form>
        </div>
      </section>
    </>
  )
}

// ── Main Admin Page ───────────────────────────────────────────────────────────
export default function AdminPage() {
  const [token, setToken]           = useState(() => sessionStorage.getItem('adminToken') || '')
  const [activeTab, setActiveTab]   = useState('menu') // 'menu' | 'branches' | 'qr'
  const [menuItems, setMenuItems]   = useState([])
  const [branches, setBranches]     = useState([])
  const [showModal, setShowModal]   = useState(false)
  const [editingItem, setEditingItem] = useState(null)
  const [imageMode, setImageMode]   = useState('url')
  const [loading, setLoading]       = useState(false)
  const [error, setError]           = useState('')
  const [menuCatFilter, setMenuCatFilter]   = useState('')
  const [priceCatFilter, setPriceCatFilter] = useState('')

  // Branch prices panel
  const [pricingBranch, setPricingBranch] = useState(null)
  const [prices, setPrices]               = useState([])
  const [priceEdits, setPriceEdits]       = useState({})      // { menuItemId: newPrice }
  const [savingPrice, setSavingPrice]     = useState(null)    // menuItemId currently saving

  const handleLogin = (t) => {
    sessionStorage.setItem('adminToken', t)
    setToken(t)
  }

  const handleLogout = async () => {
    try {
      await fetch(`${API_BASE}/admin/logout`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${token}` },
      })
    } catch {}
    sessionStorage.removeItem('adminToken')
    setToken('')
  }

  // Load data whenever tab changes (or after login)
  useEffect(() => {
    if (!token) return
    if (activeTab !== 'branches') setPricingBranch(null)
    if (activeTab === 'menu') loadMenu()
    else loadBranches()
  }, [token, activeTab])

  const loadMenu = async () => {
    setLoading(true); setError('')
    try {
      const data = await api.menu()
      setMenuItems(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const loadBranches = async () => {
    setLoading(true); setError('')
    try {
      const data = await api.adminBranches(token)
      setBranches(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const openPricing = async (branch) => {
    setPricingBranch(branch)
    setPriceEdits({})
    setPriceCatFilter('')
    try {
      const data = await api.getBranchPrices(branch.id, token)
      setPrices(data)
      // seed edits with current prices
      const edits = {}
      data.forEach(p => { edits[p.menuItem.id] = p.price })
      setPriceEdits(edits)
    } catch (err) {
      alert(err.message)
    }
  }

  const closePricing = () => { setPricingBranch(null); setPrices([]) }

  const savePrice = async (menuItemId) => {
    setSavingPrice(menuItemId)
    try {
      await api.updateBranchPrice(pricingBranch.id, menuItemId, priceEdits[menuItemId], token)
    } catch (err) {
      alert(err.message)
    } finally {
      setSavingPrice(null)
    }
  }

  // ── Menu handlers ────────────────────────────────────────────────────────
  const handleAddMenu = () => {
    setEditingItem({ name: '', description: '', category: '', price: '', image: '', available: true })
    setImageMode('url')
    setShowModal(true)
  }

  const handleEditMenu = (item) => {
    setEditingItem({ ...item })
    setImageMode('url')
    setShowModal(true)
  }

  const handleDeleteMenu = async (id) => {
    if (!window.confirm('Delete this menu item?')) return
    try {
      await api.deleteMenuItem(id, token)
      setMenuItems(menuItems.filter(i => i.id !== id))
    } catch (err) {
      alert(err.message)
    }
  }

  const handleSaveMenu = async (e) => {
    e.preventDefault()
    try {
      if (editingItem.id) {
        const updated = await api.updateMenuItem(editingItem.id, editingItem, token)
        setMenuItems(menuItems.map(i => i.id === updated.id ? updated : i))
      } else {
        const created = await api.createMenuItem(editingItem, token)
        setMenuItems([...menuItems, created])
      }
      setShowModal(false); setEditingItem(null)
    } catch (err) {
      alert(err.message)
    }
  }

  const handleImageUpload = (e) => {
    const file = e.target.files[0]
    if (!file) return
    if (file.size > 10 * 1024 * 1024) {
      alert('Image is too large. Please use an image URL instead, or choose a file under 1.5 MB.')
      e.target.value = ''
      return
    }
    const reader = new FileReader()
    reader.onloadend = () => setEditingItem({ ...editingItem, image: reader.result })
    reader.readAsDataURL(file)
  }


  // ── Branch handlers ──────────────────────────────────────────────────────
  const handleAddBranch = () => {
    setEditingItem({ name: '', address: '', phone: '', code: '', mapUrl: '', active: true })
    setShowModal(true)
  }

  const handleEditBranch = (branch) => {
    setEditingItem({ ...branch })
    setShowModal(true)
  }

  const handleDeleteBranch = async (id) => {
    if (!window.confirm('Delete this branch?')) return
    try {
      await api.deleteBranch(id, token)
      setBranches(branches.filter(b => b.id !== id))
    } catch (err) {
      alert(err.message)
    }
  }

  const handleSaveBranch = async (e) => {
    e.preventDefault()
    try {
      if (editingItem.id) {
        const updated = await api.updateBranch(editingItem.id, editingItem, token)
        setBranches(branches.map(b => b.id === updated.id ? updated : b))
      } else {
        const created = await api.createBranch(editingItem, token)
        setBranches([...branches, created])
      }
      setShowModal(false); setEditingItem(null)
    } catch (err) {
      alert(err.message)
    }
  }

  // ── Not logged in ────────────────────────────────────────────────────────
  if (!token) return <LoginScreen onLogin={handleLogin} />

  // ── Logged in ────────────────────────────────────────────────────────────
  return (
    <>
      <section className="page-banner">
        <span className="eyebrow">ADMIN PANEL</span>
        <h1>Manage Your Content</h1>
        <p>Add, edit, or remove menu items and branch locations.</p>
      </section>

      <section className="section admin-section">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
          <div className="admin-tabs" style={{ margin: 0 }}>
            <button className={activeTab === 'menu' ? 'active' : ''} onClick={() => setActiveTab('menu')}>Menu Items</button>
            <button className={activeTab === 'branches' ? 'active' : ''} onClick={() => setActiveTab('branches')}>Branches</button>
            <button className={activeTab === 'qr' ? 'active' : ''} onClick={() => setActiveTab('qr')}><QrCode size={14} /> QR Codes</button>
          </div>
          <button className="button button-outline-dark" onClick={handleLogout} style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
            <LogOut size={16} /> Sign Out
          </button>
        </div>

        {error && <p style={{ color: '#e53e3e' }}>{error}</p>}

        {activeTab === 'menu' ? (
          <div className="admin-content">
            <div className="admin-heading">
              <h2>Menu Management</h2>
              <button className="button" onClick={handleAddMenu}><Plus size={16} /> Add Menu Item</button>
            </div>
            {/* Category filter */}
            <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap', marginBottom: '16px' }}>
              <button
                className={menuCatFilter === '' ? 'button button-small' : 'button button-small button-outline-dark'}
                onClick={() => setMenuCatFilter('')}
              >All</button>
              {CATEGORY_ORDER.filter(cat => menuItems.some(i => i.category === cat)).map(cat => (
                <button
                  key={cat}
                  className={menuCatFilter === cat ? 'button button-small' : 'button button-small button-outline-dark'}
                  onClick={() => setMenuCatFilter(cat)}
                >{cat}</button>
              ))}
            </div>
            <div className="admin-list">
              {loading ? (
                <p className="empty-state">Loading…</p>
              ) : menuItems.length === 0 ? (
                <p className="empty-state">No menu items yet. Add your first item!</p>
              ) : (
                menuItems
                  .filter(item => menuCatFilter === '' || item.category === menuCatFilter)
                  .map(item => (
                  <div key={item.id} className="admin-row">
                    <div className="admin-item-content">
                      {item.image && (
                        <img src={item.image} alt={item.name}
                          style={{ width: '80px', height: '80px', objectFit: 'cover', borderRadius: '8px' }} />
                      )}
                      <div>
                        <strong>{item.name}</strong>
                        <p>{item.category} • ETB {item.price}</p>
                        <small>{item.description}</small>
                      </div>
                    </div>
                    <div style={{ display: 'flex', gap: '10px' }}>
                      <button className="button button-small" onClick={() => handleEditMenu(item)}><Edit size={14} /> Edit</button>
                      <button className="danger-button" onClick={() => handleDeleteMenu(item.id)}><Trash2 size={14} /> Delete</button>
                    </div>
                  </div>
                  ))
              )}
            </div>
          </div>
        ) : activeTab === 'branches' ? (
          <div className="admin-content">
            {pricingBranch ? (
              <>
                <div className="admin-heading">
                  <button className="button button-outline-dark" onClick={closePricing} style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                    <ChevronLeft size={16} /> Back to Branches
                  </button>
                  <h2 style={{ margin: 0 }}>Prices — {pricingBranch.name}</h2>
                </div>
                <p style={{ color: '#666', marginBottom: '16px', fontSize: '14px' }}>
                  Edit the price for each item at this branch, then click Save.
                </p>
                {/* Category filter for prices */}
                <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap', marginBottom: '16px' }}>
                  <button
                    className={priceCatFilter === '' ? 'button button-small' : 'button button-small button-outline-dark'}
                    onClick={() => setPriceCatFilter('')}
                  >All</button>
                  {CATEGORY_ORDER.filter(cat => prices.some(p => p.menuItem.category === cat)).map(cat => (
                    <button
                      key={cat}
                      className={priceCatFilter === cat ? 'button button-small' : 'button button-small button-outline-dark'}
                      onClick={() => setPriceCatFilter(cat)}
                    >{cat}</button>
                  ))}
                </div>
                {/* Group by category */}
                {Object.entries(
                  prices.reduce((acc, p) => {
                    const cat = p.menuItem.category
                    if (!acc[cat]) acc[cat] = []
                    acc[cat].push(p)
                    return acc
                  }, {})
                ).sort(([a], [b]) => {
                  const ai = CATEGORY_ORDER.indexOf(a)
                  const bi = CATEGORY_ORDER.indexOf(b)
                  if (ai === -1 && bi === -1) return a.localeCompare(b)
                  if (ai === -1) return 1
                  if (bi === -1) return -1
                  return ai - bi
                }).filter(([cat]) => priceCatFilter === '' || cat === priceCatFilter).map(([category, items]) => (
                  <div key={category} style={{ marginBottom: '24px' }}>
                    <h3 style={{ fontSize: '13px', textTransform: 'uppercase', letterSpacing: '1px', color: '#888', marginBottom: '8px' }}>
                      {category}
                    </h3>
                    {items.map(p => (
                      <div key={p.menuItem.id} className="admin-row" style={{ alignItems: 'center' }}>
                        <div style={{ flex: 1 }}>
                          <strong>{p.menuItem.name}</strong>
                          <small style={{ marginLeft: '8px', color: '#888' }}>base: ETB {p.menuItem.price}</small>
                        </div>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                          <span style={{ color: '#888', fontSize: '13px' }}>ETB</span>
                          <input
                            type="number"
                            step="0.01"
                            min="0"
                            value={priceEdits[p.menuItem.id] ?? p.price}
                            onChange={e => setPriceEdits({ ...priceEdits, [p.menuItem.id]: parseFloat(e.target.value) })}
                            style={{ width: '100px', padding: '6px 8px', border: '1px solid #ddd', borderRadius: '6px' }}
                          />
                          <button
                            className="button button-small"
                            onClick={() => savePrice(p.menuItem.id)}
                            disabled={savingPrice === p.menuItem.id}
                          >
                            {savingPrice === p.menuItem.id ? '…' : 'Save'}
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                ))}
              </>
            ) : (
              <>
                <div className="admin-heading">
                  <h2>Branch Management</h2>
                  <button className="button" onClick={handleAddBranch}><Plus size={16} /> Add Branch</button>
                </div>
                <div className="admin-list">
                  {loading ? (
                    <p className="empty-state">Loading…</p>
                  ) : branches.length === 0 ? (
                    <p className="empty-state">No branches yet. Add your first branch!</p>
                  ) : (
                    branches.map(branch => (
                      <div key={branch.id} className="admin-row">
                        <div>
                          <strong>{branch.name}</strong>
                          <p>{branch.address}</p>
                          <small>{branch.phone}</small>
                        </div>
                        <div style={{ display: 'flex', gap: '10px' }}>
                          <button className="button button-small" onClick={() => openPricing(branch)} style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                            <DollarSign size={14} /> Prices
                          </button>
                          <button className="button button-small" onClick={() => handleEditBranch(branch)}><Edit size={14} /> Edit</button>
                          <button className="danger-button" onClick={() => handleDeleteBranch(branch.id)}><Trash2 size={14} /> Delete</button>
                        </div>
                      </div>
                    ))
                  )}
                </div>
              </>
            )}
          </div>
        ) : (
          <div className="admin-content">
            <div className="admin-heading">
              <h2>QR Codes</h2>
            </div>
            {loading ? (
              <p className="empty-state">Loading…</p>
            ) : branches.filter(b => b.active).length === 0 ? (
              <p className="empty-state">No active branches. Add branches first.</p>
            ) : (
              <div className="qr-print-grid">
                {branches.filter(b => b.active).map(branch => (
                  <QRPrintCard key={branch.id} branch={branch} />
                ))}
              </div>
            )}
          </div>
        )}
      </section>

      {/* Modal for Add/Edit */}
      {showModal && editingItem && (
        <div className="modal-overlay" onClick={() => setShowModal(false)}>
          <div className="modal-content-side admin-modal" onClick={e => e.stopPropagation()}>
            <div className="modal-header-section">
              <h2 className="modal-title">
                {editingItem.id ? 'Edit' : 'Add'} {activeTab === 'menu' ? 'Menu Item' : 'Branch'}
              </h2>
            </div>

            <div className="modal-body">
              {activeTab === 'menu' ? (
                <form onSubmit={handleSaveMenu}>
                  <div className="field">
                    <label>Item Name *</label>
                    <input type="text" value={editingItem.name}
                      onChange={e => setEditingItem({ ...editingItem, name: e.target.value })} required />
                  </div>
                  <div className="field">
                    <label>Description</label>
                    <textarea value={editingItem.description} rows="3"
                      onChange={e => setEditingItem({ ...editingItem, description: e.target.value })} />
                  </div>
                  <div className="form-row">
                    <div className="field">
                      <label>Category *</label>
                      <select value={editingItem.category}
                        onChange={e => setEditingItem({ ...editingItem, category: e.target.value })} required>
                        <option value="">Select Category</option>
                        <option value="Breakfast">Breakfast</option>
                        <option value="Lunch">Lunch</option>
                        <option value="Cake">Cakes</option>
                        <option value="Ice Cream">Ice Cream</option>
                        <option value="Torta">Torta</option>
                        <option value="Cold Drinks">Cold Drinks</option>
                        <option value="Hot Drinks">Hot Drinks</option>
                        <option value="Virgin Cocktail">Virgin Cocktail</option>
                        <option value="Sandwiches & Wraps">Sandwiches &amp; Wraps</option>
                        <option value="Extra">Extra</option>
                        <option value="Boxes">Boxes</option>
                      </select>
                    </div>
                    <div className="field">
                      <label>Price (ETB) *</label>
                      <input type="number" step="0.01" value={editingItem.price}
                        onChange={e => setEditingItem({ ...editingItem, price: e.target.value })} required />
                    </div>
                  </div>

                  <div className="field">
                    <label>Image</label>
                    <div className="image-mode-toggle">
                      <button type="button" className={imageMode === 'url' ? 'active' : ''} onClick={() => setImageMode('url')}>
                        <LinkIcon size={16} /> URL
                      </button>
                      <button type="button" className={imageMode === 'upload' ? 'active' : ''} onClick={() => setImageMode('upload')}>
                        <Upload size={16} /> Upload
                      </button>
                    </div>
                    {imageMode === 'url' ? (
                      <input type="url" placeholder="https://example.com/image.jpg"
                        value={editingItem.image}
                        onChange={e => setEditingItem({ ...editingItem, image: e.target.value })} />
                    ) : (
                      <div className="file-upload">
                        <input type="file" accept="image/*" onChange={handleImageUpload} id="imageUpload" />
                        <label htmlFor="imageUpload" className="file-upload-label">
                          <Upload size={20} /><span>Choose image file</span>
                        </label>
                      </div>
                    )}
                    {editingItem.image && (
                      <div className="image-preview">
                        <img src={editingItem.image} alt="Preview" />
                      </div>
                    )}
                  </div>

                  <div className="field">
                    <label style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                      <input type="checkbox" checked={editingItem.available}
                        onChange={e => setEditingItem({ ...editingItem, available: e.target.checked })} />
                      Available
                    </label>
                  </div>

                  <div className="form-actions">
                    <button type="submit" className="button">Save Item</button>
                    <button type="button" className="button button-outline-dark" onClick={() => setShowModal(false)}>Cancel</button>
                  </div>
                </form>
              ) : (
                <form onSubmit={handleSaveBranch}>
                  <div className="field">
                    <label>Branch Name *</label>
                    <input type="text" value={editingItem.name}
                      onChange={e => setEditingItem({ ...editingItem, name: e.target.value })} required />
                  </div>
                  <div className="field">
                    <label>Branch Code *</label>
                    <input type="text" placeholder="e.g. BOLE" value={editingItem.code || ''}
                      onChange={e => setEditingItem({ ...editingItem, code: e.target.value })} required />
                  </div>
                  <div className="field">
                    <label>Address *</label>
                    <textarea value={editingItem.address} rows="2"
                      onChange={e => setEditingItem({ ...editingItem, address: e.target.value })} required />
                  </div>
                  <div className="field">
                    <label>Phone</label>
                    <input type="tel" value={editingItem.phone || ''}
                      onChange={e => setEditingItem({ ...editingItem, phone: e.target.value })} />
                  </div>
                  <div className="field">
                    <label>Google Maps URL</label>
                    <input type="url" placeholder="https://maps.app.goo.gl/…"
                      value={editingItem.mapUrl || ''}
                      onChange={e => setEditingItem({ ...editingItem, mapUrl: e.target.value })} />
                  </div>
                  <div className="field">
                    <label style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                      <input type="checkbox" checked={editingItem.active}
                        onChange={e => setEditingItem({ ...editingItem, active: e.target.checked })} />
                      Active
                    </label>
                  </div>
                  <div className="form-actions">
                    <button type="submit" className="button">Save Branch</button>
                    <button type="button" className="button button-outline-dark" onClick={() => setShowModal(false)}>Cancel</button>
                  </div>
                </form>
              )}
            </div>
          </div>
        </div>
      )}
    </>
  )
}
