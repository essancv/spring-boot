// src/pages/CotizacionesPage.tsx
import React, { useEffect, useState } from "react";
import { useAuth } from "../AuthContext";
import TablaCotizaciones from "../components/TablaCotizaciones";
import type { CotizacionMes } from "../components/TablaCotizaciones";

const CotizacionesPage: React.FC = () => {
  const { token, id, userData } = useAuth();

  const [cotizaciones, setCotizaciones] = useState<CotizacionMes[]>([]);
  const [cargando, setCargando] = useState(true);

  const generarRangoAnios = (lista: CotizacionMes[]) => {
    if (lista.length === 0) return [];
    const minYear = Math.min(...lista.map(c => c.year));
    const maxYear = Math.max(...lista.map(c => c.year));
    const years: number[] = [];
    for (let y = minYear; y <= maxYear; y++) years.push(y);
    return years;
  };

  useEffect(() => {
    const cargar = async () => {
      // 👉 ahora también exigimos userData
      if (!token || !id || !userData) return;

      const res = await fetch(
        `/api/user/${encodeURIComponent(id)}/quotations`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      const data: [number, number, number][] = await res.json();

      const lista: CotizacionMes[] = data.map((q: any) => ({
        id: q.id,
        year: new Date(q.date).getFullYear(),
        month: new Date(q.date).getMonth() + 1,
        amount: q.amount
      }));

      const inicio = new Date(userData.fechaInicioTrabajo);
      const minYear = inicio.getFullYear();
      const maxYear =
        lista.length > 0 ? Math.max(...lista.map(c => c.year)) : minYear;

      const map = new Map<string, CotizacionMes>();
      lista.forEach(c => map.set(`${c.year}-${c.month}`, c));

      const hoy = new Date();
      const currentYear = hoy.getFullYear();
      const currentMonth = hoy.getMonth() + 1;

      const listaCompleta: CotizacionMes[] = [];

      for (let y = minYear; y <= maxYear; y++) {
        for (let m = 1; m <= 12; m++) {
          const esAntesDeInicio =
            y === minYear && m < inicio.getMonth() + 1;
          const esFuturo =
            y > currentYear || (y === currentYear && m > currentMonth);
          
          const locked = esAntesDeInicio || esFuturo;

          const key = `${y}-${m}`;
          const real = map.get(key);

          listaCompleta.push(
            real ?? {
              id: undefined,
              year: y,
              month: m,
              amount: null,
              isLocked: locked,
              isNew:true
            }
          );
        }
      }
      
console.log(
  "Meses marcados como isLocked",
  listaCompleta.filter(c => c.isLocked)
);
      setCotizaciones(listaCompleta);
      setCargando(false);
    };

    cargar();
    // 👉 añadimos userData como dependencia
  }, [token, id, userData]);

  const isLocked = (year: number, month: number) => {
      const c = cotizaciones.find(x => x.year === year && x.month === month);
      return c?.isLocked === true;
   };  
   const isMesActual = () => false;

  const globalIndex = (year: number, month: number) =>
    cotizaciones.findIndex(c => c.year === year && c.month === month);

    const handleChangeAmount = async (index: number, value: string) => {
      const num = Number(value.replace(",", "."));
      setCotizaciones(prev => {
        const copy = [...prev];
        copy[index] = { ...copy[index], amount: isNaN(num) ? null : num };
        return copy;
      });
  }

  const handleEnterAmount = async (index: number) => {
    // Actualización en BBDD si es preciso
    const c = cotizaciones[index];
    if (!c) return;

    const payload = {
      year: c.year,
      month: c.month,
      amount: c.amount ?? 0
    };

    if (c.isNew) {
      // 👉 POST (crear nueva)
      const res = await fetch(`/api/user/${id}/quotations`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(payload)
      });
      const created = await res.json();
      setCotizaciones(prev => {
        const copy = [...prev];
        copy[index] = {
          ...copy[index],
          id: created.id,   // asignamos el id real de BBDD
          isNew: false      // ya no es nueva
        };
        return copy;
      });
    
    } else {
      // 👉 PUT (actualizar existente)
      await fetch(`/api/user/${id}/quotations/${c.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(payload)
      });
    }
  };

  const guardar = async () => {
    if (!token || !id) return;

    const payload = cotizaciones.map(c => [c.year, c.month, c.amount ?? 0]);

    await fetch(`/api/user/¡${encodeURIComponent(id)}/quotations`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(payload),
    });

    alert("Cotizaciones guardadas");
  };

  if (cargando) return <div>Cargando cotizaciones…</div>;

  const years = generarRangoAnios(cotizaciones);

  return (
    <div>
      <h1>Cotizaciones</h1>

      <button onClick={guardar} style={{ marginBottom: "20px" }}>
        Guardar cambios
      </button>

      <TablaCotizaciones
        cotizaciones={cotizaciones}
        onChangeValor={handleChangeAmount}
        onEnterValor={handleEnterAmount}
        isLocked={isLocked}
        isMesActual={isMesActual}
        years={years}
        getIndex={globalIndex}
       />
    </div>
  );
};

export default CotizacionesPage;
