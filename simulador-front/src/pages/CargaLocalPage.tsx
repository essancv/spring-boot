import React, { useState } from "react";
import { useAuth } from "../AuthContext";

type Cotizacion = [number, number, number];

export default function CargaLocalPage() {
  const { token, id: userId } = useAuth();

  const [mensaje, setMensaje] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(false);

  const handleFileSelected = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    setMensaje("Leyendo fichero...");
    setLoading(true);

    const text = await file.text();

    // 1. Parsear el contenido
    const cotizaciones = parseCotizaciones(text);

    if (!cotizaciones) {
      setMensaje("Error: formato de fichero no válido");
      setLoading(false);
      return;
    }

    try {
      // 2. Borrar cotizaciones existentes
      await fetch(`/api/user/${userId}/quotations`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      // 3. Crear cotizaciones una a una
      for (const [anio, mes, valor] of cotizaciones) {
        await fetch(`/api/user/${userId}/quotations`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`
          },
          body: JSON.stringify({
            year: anio,
            month: mes,
            amount: valor
          })
        });
      }

      setMensaje("Carga completada con éxito");
    } catch (error) {
      console.error(error);
      setMensaje("Error durante la carga");
    }

    setLoading(false);
  };

  // -------------------------------
  // Función que extrae el array del texto
  // -------------------------------
  function parseCotizaciones(text: string): Cotizacion[] | null {
    try {
      // Buscar el contenido entre corchetes [...]
     const match = text.match(/\[[\s\S]*\]/);
      if (!match) return null;

      const json = match[0];

      const parsed = JSON.parse(json);

      if (!Array.isArray(parsed)) return null;

      return parsed.map((c: any) => {
        if (!Array.isArray(c) || c.length !== 3) throw new Error();
        return [Number(c[0]), Number(c[1]), Number(c[2])] as Cotizacion;
      });
    } catch (e) {
      console.error("Error parseando fichero:", e);
      return null;
    }
  }

  return (
    <div style={{ padding: "20px" }}>
      <h2>Seleccione el fichero a cargar</h2>

      <input type="file" accept=".txt,.js,.json" onChange={handleFileSelected} />

      {loading && <p>Cargando datos...</p>}
      {mensaje && <p>{mensaje}</p>}
    </div>
  );
}
