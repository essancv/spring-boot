// App.tsx
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider, useAuth } from "./AuthContext";
import LoginPage from "./pages/LoginPage";
import ProtectedLayout from "./pages/ProtectedLayout";
import UsuarioPage from "./pages/UsuarioPage";
import CotizacionesPage from "./pages/CotizacionesPage";
import SimulacionPage from "./pages/SimulacionPage";
import './styles.css';
const PrivateRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { token } = useAuth();
  if (!token) return <Navigate to="/login" replace />;
  return children;
};

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route
            path="/app"
            element={
              <PrivateRoute>
                <ProtectedLayout />
              </PrivateRoute>
            }
          >
            <Route path="usuario" element={<UsuarioPage />} />
            <Route path="cotizaciones" element={<CotizacionesPage />} />
            <Route path="simulacion" element={<SimulacionPage />} />
          </Route>
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
