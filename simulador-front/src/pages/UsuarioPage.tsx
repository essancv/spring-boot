// pages/UsuarioPage.tsx
import { useEffect, useState } from "react";
import { useAuth } from "../AuthContext";

export default function UsuarioPage() {
  const { token, id, userData, setUserData } = useAuth();
  const [form, setForm] = useState(userData);

  // Cuando userData cambie (por ejemplo, tras login), sincronizamos el formulario
  useEffect(() => {
    setForm(userData);
  }, [userData]);

  const handleChange = (field: string, value: string) => {
    setForm(prev => prev ? { ...prev, [field]: value } : prev);
  };

  const handleSave = async () => {
    if (!form || !token || !id ) return;
    const res = await fetch(`/api/user/${encodeURIComponent(id)}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(form),
    });
    if (res.ok) {
      const updated = await res.json();
      setUserData(updated);
      setForm(updated);
    }
  };

  if (!form) return <div>Cargando datos de usuario...</div>;

  return (
    <div className="usuario-page">
      <h2>Datos de usuario</h2>
      <div>
        <label>
          Username (no editable)
          <input value={form.username} disabled />
        </label>
        <label>
          Nombre
          <input
            value={form.nombre}
            onChange={e => handleChange("nombre", e.target.value)}
          />
        </label>
        <label>
          Apellidos
          <input
            value={form.apellidos}
            onChange={e => handleChange("apellidos", e.target.value)}
          />
        </label>
        <label>
          Email
          <input
            value={form.email}
            onChange={e => handleChange("email", e.target.value)}
          />
        </label>
        <label>
          Fecha nacimiento
          <input
            type="date"
            value={form.fechaNacimiento}
            onChange={e => handleChange("fechaNacimiento", e.target.value)}
          />
        </label>
        <label>
          Fecha inicio trabajo
          <input
            type="date"
            value={form.fechaInicioTrabajo}
            onChange={e => handleChange("fechaInicioTrabajo", e.target.value)}
          />
        </label>
        <label>
          Teléfono
          <input
            value={form.telefono}
            onChange={e => handleChange("telefono", e.target.value)}
          />
        </label>
      </div>
      <button onClick={handleSave}>Guardar</button>
    </div>
  );
}
