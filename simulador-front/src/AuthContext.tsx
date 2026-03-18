// AuthContext.tsx
import React, { createContext, useContext, useState } from "react";
import { useEffect } from "react";

type UserData = {
  username: string;
  nombre: string;
  apellidos: string;
  email: string;
  fechaNacimiento: string;
  fechaInicioTrabajo: string;
  telefono: string;
};

type AuthContextType = {
  token: string | null;
  id: string | null;
  username: string | null;
  userData: UserData | null;
  login: (username: string, password: string) => Promise<boolean>;
  logout: () => void;
  setUserData: (data: UserData | null) => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {

  const [id, setId] = useState<string | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [username, setUsername] = useState<string | null>(null);
  const [userData, setUserDataState] = useState<UserData | null>(null);

  useEffect(() => {
    if (!token) {
      setUserData(null);
      return;
    }
  
    console.log ('Vamos a cargar los datos de usuario')
    const cargarUsuario = async () => {
    if (!id) {
      throw new Error("id is null");
    }
    const res = await fetch(`/api/user/${encodeURIComponent(id)}`, {
      headers: { Authorization: `Bearer ${token}` }
    });

    const data = await res.json();
    setUserData(data);
  
  };

  cargarUsuario();
}, [token]);

  const login = async (usernameInput: string, password: string) => {
    const res = await fetch("/api/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username: usernameInput, password }),
    });

    if (!res.ok) return false;

    const data = await res.json(); // { token: "..." }
    setId (data.id)
    setToken(data.token);
    setUsername(usernameInput);
    return true;
  };

  const logout = () => {
    setToken(null);
    setUsername(null);
    setUserDataState(null);
  };

  const setUserData = (data: UserData | null) => setUserDataState(data);

  return (
    <AuthContext.Provider value={{ token, id, username, userData, login, logout, setUserData }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used within AuthProvider");
  return ctx;
};
