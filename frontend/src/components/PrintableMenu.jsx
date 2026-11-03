import { cafeConfig } from '../config'

export default function PrintableMenu({ items, branch, categories }) {
  // Group items by category in order
  const categoryOrder = categories && categories.length > 0
    ? categories.map(c => c.name)
    : []

  const grouped = {}
  items.forEach(item => {
    if (item.available === false) return
    if (!grouped[item.category]) grouped[item.category] = []
    grouped[item.category].push(item)
  })

  const sortedCategories = Object.keys(grouped).sort((a, b) => {
    const ai = categoryOrder.indexOf(a)
    const bi = categoryOrder.indexOf(b)
    if (ai === -1 && bi === -1) return a.localeCompare(b)
    if (ai === -1) return 1
    if (bi === -1) return -1
    return ai - bi
  })

  const handlePrint = () => window.print()

  return (
    <>
      <button className="print-trigger-btn no-print" onClick={handlePrint}>
        🖨 Print
      </button>
      <div className="printable-menu" id="printable-menu">
        {/* Header */}
        <div className="pm-header">
          <div className="pm-header-left">
            <img src={cafeConfig.logo} alt={cafeConfig.name} className="pm-logo" />
          </div>
          <div className="pm-header-center">
            <h1 className="pm-header-name">{cafeConfig.name}</h1>
            <p className="pm-header-tagline">{cafeConfig.tagline}</p>
            <div className="pm-header-divider"><span>✦</span></div>
            {branch && <p className="pm-header-branch">{branch.name}</p>}
          </div>
          <div className="pm-header-right">
            <div className="pm-header-badge">
              <span>Good Food</span>
              <span>·</span>
              <span>Great Moments</span>
            </div>
          </div>
        </div>

        {/* Categories */}
        {sortedCategories.map(cat => (
          <div key={cat} className="pm-category">
            <div className="pm-category-title">
              <span>{cat}</span>
            </div>
            <div className="pm-grid">
              {grouped[cat].map(item => (
                <div key={item.id} className="pm-item">
                  <div className="pm-item-img">
                    {item.image
                      ? <img src={item.image.startsWith('http') ? item.image : `/${item.image}`} alt={item.name} />
                      : <div className="pm-item-img-placeholder" />}
                  </div>
                  <div className="pm-item-info">
                    <span className="pm-item-name">{item.name}</span>
                    <span className="pm-item-price">
                      {Number(item.price).toLocaleString()} Br
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        ))}

        {/* Footer */}
        <div className="pm-footer">
          <p className="pm-thank">Thank You <span>♥</span> for your support!</p>
          <p className="pm-note">All prices are not inclusive of 5% service charge</p>
        </div>
      </div>
    </>
  )
}
