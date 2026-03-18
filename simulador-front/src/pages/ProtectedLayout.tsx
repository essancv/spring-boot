// pages/ProtectedLayout.tsx
import { Link, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../AuthContext";

export default function ProtectedLayout() {
  const { logout } = useAuth();
  const navigate = useNavigate();

  const handleSalir = () => {
    logout();
    navigate("/login");
  };

  return (
    <div className="app-layout">
      <nav className="sidebar">
        <ul>
          <li><Link to="usuario">1. Modificar datos de usuario</Link></li>
          <li><Link to="cotizaciones">2. Actualizar cotizaciones</Link></li>
          <li><Link to="simulacion">3. Simular</Link></li>
          <li><button onClick={handleSalir}>4. Salir</button></li>
        </ul>
      </nav>
      <main className="content">
        <Outlet />
      </main>
    </div>
  );
}
