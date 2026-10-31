import { useEffect, useMemo, useState } from 'react'
import { Link, Route, Routes, useLocation } from 'react-router-dom'
import { Check } from 'lucide-react'
import { I18nProvider, useI18n } from './i18n'
import { locations, menuData } from './data'
import { api } from './api'

// Import Pages
import Home from './pages/Home'
import MenuPage from './pages/MenuPage'
import LocationsPage from './pages/LocationsPage'
import ContactPage from './pages/ContactPage'
import QRPage from './pages/QRPage'
import AdminPage from './pages/AdminPage'
import FeedbackPage from './pages/FeedbackPage'

// Import Components
import Navbar from './components/Navbar'
import Footer from './components/Footer'
import MenuDetailModal from './components/MenuDetailModal'

// Convert menuData object to flat array for display
const convertMenuToArray = () => {
  const result = []
  let id = 1
  const categoryMapping = {
    'iceCream': 'Ice Cream',
    'coldDrinks': 'Cold Drinks',
    'hotDrinks': 'Hot Drinks',
    'virginCocktail': 'Virgin Cocktail',
    'sandwichesWraps': 'Sandwiches Wraps',
    'breakfast': 'Breakfast',
    'cakes': 'Cakes',
    'torta': 'Torta',
    'lunch': 'Lunch',
    'extra': 'Extra',
    'boxes': 'Boxes'
  }
  
  Object.entries(menuData).forEach(([category, items]) => {
    const categoryName = categoryMapping[category] || category.charAt(0).toUpperCase() + category.slice(1)
    items.forEach(item => {
      result.push({
        id: String(id++),
        name: item.name,
        description: item.description,
        category: categoryName,
        price: parseFloat(item.price),
        image: item.image,
        available: true,
        popular: item.popular,
        new: item.new
      })
    })
  })
  return result
}

function AppShell() {
  const {lang} = useI18n()
  const [toast, setToast] = useState('')
  const [selectedItem, setSelectedItem] = useState(null)
  const [selectedBranch, setSelectedBranch] = useState(null)
  const [menuItems, setMenuItems] = useState([])
  const [loading, setLoading] = useState(true)
  const staticItems = useMemo(() => convertMenuToArray(), [])
  const branches = locations.map((loc, i) => ({...loc, id: i + 1}))
  const location = useLocation()
  
  // Load menu based on branch selection
  useEffect(() => {
    setLoading(true)
    if (selectedBranch) {
      api.menuByBranch(selectedBranch.code)
        .then(setMenuItems)
        .catch(err => {
          console.error('Failed to load branch menu:', err)
          setMenuItems(staticItems)
        })
        .finally(() => setLoading(false))
    } else {
      // Load default menu from API or fallback to static
      api.menu()
        .then(setMenuItems)
        .catch(err => {
          console.error('Failed to load menu:', err)
          setMenuItems(staticItems)
        })
        .finally(() => setLoading(false))
    }
  }, [selectedBranch, staticItems])
  
  // Scroll to top on route change
  useEffect(() => {
    window.scrollTo(0, 0)
  }, [location.pathname])
  
  // Auto-hide toast after 4 seconds
  useEffect(() => {
    if (!toast) return
    const id = setTimeout(() => setToast(''), 4000)
    return () => clearTimeout(id)
  }, [toast])
  
  const items = menuItems.length > 0 ? menuItems : staticItems
  
  return (
    <div className={lang === 'am' ? 'app amharic' : 'app'}>
      <Navbar selectedBranch={selectedBranch} onSelectBranch={setSelectedBranch} />

      <main>
        <Routes>
          <Route path="/" element={<Home items={items} onSelectItem={setSelectedItem} loading={loading}/>}/>
          <Route path="/menu" element={<MenuPage items={items} onSelectItem={setSelectedItem} loading={loading}/>}/>
          <Route path="/locations" element={<LocationsPage branches={branches}/>}/>
          <Route path="/contact" element={<ContactPage selectedBranch={selectedBranch}/>}/>
          <Route path="/feedback" element={<FeedbackPage/>}/>
          <Route path="/qr" element={<QRPage/>}/>
          <Route path="/admin" element={<AdminPage/>}/>
          <Route path="*" element={
            <section className="section">
              <h1>Page not found</h1>
              <Link className="button" to="/">Return home</Link>
            </section>
          }/>
        </Routes>
      </main>
      
      {selectedItem && <MenuDetailModal item={selectedItem} onClose={() => setSelectedItem(null)}/>}
      
      <Footer />
      
      {toast && (
        <div className="toast" role="status">
          <Check size={18}/>
          {toast}
        </div>
      )}
    </div>
  )
}

export default function App() {
  return <I18nProvider><AppShell/></I18nProvider>
}

