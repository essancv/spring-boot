// src/pages/SimulacionPage.tsx
import React, { useEffect, useState } from "react";
import { useAuth } from "../AuthContext";
import SimuladorPension from "../services/SimuladorPension";
import TablaCotizaciones from "../components/TablaCotizaciones";
import type { CotizacionMes } from "../components/TablaCotizaciones";
import CotizacionesPopup from "../components/CotizacionesPopup";

const SimulacionPage: React.FC = () => {
  const { token, id, userData } = useAuth();

  const [historico, setHistorico] = useState<CotizacionMes[]>([]);
  const [cotizaciones, setCotizaciones] = useState<CotizacionMes[]>([]);
  const [usarSimulacion, setUsarSimulacion] = useState(true);
  const [baseConvenio, setBaseConvenio] = useState("");

  const [porcentajeManual, setPorcentajeManual] = useState<number | null>(null);
  const [resultado, setResultado] = useState<any>(null);

  const [config, setConfig] = useState<any>(null); // 👈 CONFIGURACIÓN CARGADA UNA VEZ

  const [mostrarPopup, setMostrarPopup] = useState(false);

  if (!userData) return null;

  const hoy = new Date();
  hoy.setDate(1);
  const yNow = hoy.getFullYear();
  const mNow = hoy.getMonth() + 1;

  const nacimiento = new Date(userData.fechaNacimiento);
  const fechaJubilacion = new Date(
    nacimiento.getFullYear() + 65,
    nacimiento.getMonth(),
    1
  );

  // 🔵 Cargar configuración SOLO UNA VEZ
  useEffect(() => {
    const cargarConfig = async () => {
      const res = await fetch("/api/config");
      const data = await res.json();
      setConfig(data);
    };
    cargarConfig();
  }, []);

  // 🔵 Cargar histórico y meses simulados
  useEffect(() => {
    const cargar = async () => {
      if (!token || !id) return;

      const res = await fetch(
        `/api/user/${encodeURIComponent(id)}/quotations`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      const data: [number, number, number][] = await res.json();

      /*
      const historicoTemp: CotizacionMes[] = data.map(([y, m, a]) => ({
        year: y,
        month: m,
        amount: a,
      }));
      */

      const historicoTemp: CotizacionMes[] = data.map((q: any) => ({
              id: q.id,
              year: new Date(q.date).getFullYear(),
              month: new Date(q.date).getMonth() + 1,
              amount: q.amount
            }));

      historicoTemp.sort((a, b) =>
        a.year === b.year ? a.month - b.month : a.year - b.year
      );
      setHistorico(historicoTemp);

      const meses: CotizacionMes[] = [];

      // Hoy suponemos que se ha cotizado , por lo tanto tiene que ser nextmonth
      const cursor = new Date(
            hoy.getFullYear(),
            hoy.getMonth() + 1,
            1
          );

      const cotMap = new Map<string, number>(
        historicoTemp.map(c => [`${c.year}-${c.month}`, c.amount ?? 0])
      );

      while (cursor <= fechaJubilacion) {
        const y = cursor.getFullYear();
        const m = cursor.getMonth() + 1;

        meses.push({
          year: y,
          month: m,
          amount:
            y === yNow && m === mNow
              ? null
              : cotMap.get(`${y}-${m}`) ?? null,
           isParo: false,
           isConvenio: false,
           isRelleno: false,
           isHueco: cotMap.get(`${y}-${m}`) == null
        });

        cursor.setMonth(cursor.getMonth() + 1);
      }

      setCotizaciones(meses);
    };

    cargar();
  }, [token, id]);

  // 🔵 Ejecutar simulación cuando cambien datos (pero solo si config está cargada)
  useEffect(() => {
    if (!config) return;
    if (historico.length === 0) return;
    if (cotizaciones.length === 0) return;

    console.log ('Fecha  de nacimiento : ' + nacimiento)
    const simulador = new SimuladorPension({
      nacimiento,
      historico,
      cotizacionesSimuladas: cotizaciones.filter(c => !c.isRelleno),  // Sólo las cotizadas .. los huecos los rellena el simulador
      usarSimulacion,
      porcentajeManual,
      config, // 👈 CONFIG PASADA AL SIMULADOR
    });

    // simulador.simular().then(setResultado);

    simulador.simular().then(res => {
      console.log ('Cotizaciones usadas para la simu : ' + res.cotizacionesSimulacion.length)
      const cotizacionesFinales = res.cotizacionesSimulacion.map(c => {
        const sim = cotizaciones.find(
          x => x.year === c.year && x.month === c.month
        );

        return {
          ...c,
          isParo: sim?.isParo ?? false,
          isConvenio: sim?.isConvenio ?? false,
          isRelleno: sim?.isRelleno ?? false,
          isHueco:
            sim?.isHueco ??
            (sim?.amount == null || sim?.amount === 0)
        };
      });

      console.log ('Cotizaciones finales : ' + res.cotizacionesSimulacion.length)
      console.log ('Cotizaciones finales  isParo : ' + cotizacionesFinales[0].isParo );
      console.log ('Cotizaciones finales  isConvenio : ' + cotizacionesFinales[0].isConvenio );
      console.log ('Cotizaciones finales  isRelleno : ' + cotizacionesFinales[0].isRelleno );
      console.log ('Cotizaciones finales  isHueco : ' + cotizacionesFinales[0].isHueco );

      setResultado({
        ...res,
        cotizacionesSimulacion: cotizacionesFinales
      });
    });

  }, [historico, cotizaciones, usarSimulacion, porcentajeManual, config]);

  // Helpers UI
  const isLocked = (year: number, month: number) => {
    const y65 = fechaJubilacion.getFullYear();
    const m65 = fechaJubilacion.getMonth() + 1;

    if (year === yNow && month <= mNow) return true;
    if (year === y65 && month > m65) return true;

    return false;
  };

  const isMesActual = (year: number, month: number) =>
    year === yNow && month === mNow;

  const globalIndex = (year: number, month: number) =>
    cotizaciones.findIndex(c => c.year === year && c.month === month);

  const handleChangeAmount = (index: number, value: string) => {
    const num = Number(value.replace(",", "."));
    setCotizaciones(prev => {
      const copy = [...prev];
      copy[index] = { ...copy[index], amount: isNaN(num) ? null : num };
      return copy;
    });
  };

  // 🔵 RELLENAR PARO (24 meses)
  const rellenarParo = () => {
    const ultimaBaseReal =
      historico.length > 0
        ? historico[historico.length - 1].amount ?? 0
        : 0;

    if (!ultimaBaseReal || ultimaBaseReal <= 0) return;

    setCotizaciones(prev => {
      const copy = [...prev];
      let count = 0;

      for (let i = 0; i < copy.length && count < 24; i++) {
        const c = copy[i];

        if (
          !isLocked(c.year, c.month) &&
          !isMesActual(c.year, c.month) &&
          !c.isParo &&
          (!c.amount || c.amount === 0)
        ) {
          copy[i] = {
            ...c,
            amount: ultimaBaseReal,
            isParo: true,
            isConvenio: false,
            isRelleno: false,
            isHueco: false
          };
          count++;
        }
      }

      return copy;
    });
  };

  // 🔵 RELLENAR MÍNIMO
  const rellenarMinimo = () => {
    if (!config) return;
    const base = config.baseMinimaCotizacion;

    setCotizaciones(prev =>
      prev.map(c =>
        c.isParo ||
        isLocked(c.year, c.month) ||
        isMesActual(c.year, c.month) ||
        (c.amount && c.amount > 0)
          ? c
          : { ...c, amount: base , isParo: false,
            isConvenio: false,
            isRelleno: true,
            isHueco: false }
      )
    );
  };

  // 🔵 RELLENAR CONVENIO
  const rellenarConvenio = () => {
    const base = Number(baseConvenio.replace(",", "."));
    if (isNaN(base) || base <= 0) return;

    setCotizaciones(prev =>
      prev.map(c =>
        c.isParo ||
        isLocked(c.year, c.month) ||
        isMesActual(c.year, c.month) ||
        (c.amount && c.amount > 0)
          ? c
          : { ...c, amount: base ,isParo: false,
             isConvenio: true,
             isRelleno: false,
            isHueco: false}
      )
    );
  };

  const limpiar = () => {
    setCotizaciones(prev =>
      prev.map(c =>
        isLocked(c.year, c.month)
          ? c
          : { ...c, amount: null, isParo: false }
      )
    );
  };

  if (!resultado) return <div>Cargando simulación…</div>;

  const years = Array.from(new Set(cotizaciones.map(c => c.year))).sort();

  return (
    <div>
      <h1>Simulación de Cotizaciones</h1>

      <div style={{ marginBottom: "10px", fontSize: "18px" }}>
        <strong>Fecha de Jubilación:</strong>{" "}
        {fechaJubilacion.toLocaleDateString("es-ES", {
          year: "numeric",
          month: "long",
        })}
      </div>

      {/* RESULTADOS */}
      <div style={{ marginBottom: "20px", fontSize: "16px" }}>
        <strong>Días cotizados reales:</strong>{" "}
        {resultado.diasCotizadosReales}
        <span>
          {" "}
          ({resultado.rangoReal.desde} – {resultado.rangoReal.hasta})
        </span>

        {resultado.hayHuecosReales && (
          <div style={{ color: "red" }}>
            ⚠ Existen meses sin cotización en la simulación
          </div>
        )}

        <br />
        <strong>Días simulados cotizados:</strong>{" "}
        {resultado.diasSimulados}

        <br />
        <strong>Base media (25 años):</strong>{" "}
        {resultado.baseMedia.toFixed(2)}

        <br />
        <strong>Días totales para cálculo:</strong> 9000
        <span>
          {" "}
          ({resultado.rango25Años.desde} – {resultado.rango25Años.hasta})
        </span>

        <br />
        <strong>Base reguladora estimada:</strong>{" "}
        {resultado.baseReguladora.toFixed(2)}
        <br />

        <strong>Años Cotizados : </strong>{" "}
        {resultado.añosCotizados.toFixed(2)}

        <br />

        <div style={{ marginTop: "10px" }}>
          <strong>Porcentaje de pensión:</strong>{" "}
          <input
            type="number"
            step="0.01"
            value={
              porcentajeManual !== null
                ? porcentajeManual
                : resultado.porcentajePension.toFixed(2)
            }
            onChange={e => {
              const v = e.target.value;
              setPorcentajeManual(v === "" ? null : Number(v));
            }}
            style={{ width: "80px", marginLeft: "10px" }}
          />
          %
        </div>

        <br />
        <strong>Pensión estimada mensual:</strong>{" "}
        {resultado.pensionEstimada.toFixed(2)}
        {resultado.indicadorPensionMaxima && (
          <span
            style={{
              color: resultado.indicadorPensionMaxima.color,
              marginLeft: "8px",
            }}
          >
            {resultado.indicadorPensionMaxima.texto}
          </span>
        )}        

      </div>

      {/* CONTROLES */}
      <div style={{ marginBottom: "20px" }}>
        <label>
          <input
            type="checkbox"
            checked={usarSimulacion}
            onChange={() => setUsarSimulacion(!usarSimulacion)}
          />
          &nbsp;Usar días simulados para el cálculo de pensión
        </label>
      </div>

      <div style={{ marginBottom: "20px" }}>
        <button onClick={rellenarParo}>Rellenar con Paro (24 meses)</button>
        <button onClick={rellenarMinimo}>Rellenar con Mínimo</button>

        <input
          type="text"
          placeholder="Base convenio"
          value={baseConvenio}
          onChange={e => setBaseConvenio(e.target.value)}
          style={{ marginLeft: "10px", width: "120px" }}
        />

        <button onClick={rellenarConvenio}>Rellenar con Convenio</button>

        <button
          onClick={limpiar}
          style={{
            marginLeft: "20px",
            backgroundColor: "#d9534f",
            color: "white",
          }}
        >
          Limpiar
        </button>
        <button
          onClick={() => setMostrarPopup(true)}
          style={{ marginLeft: "10px" }}
        >
          Ver Cotizaciones Para el Cálculo
      </button>

      </div>

      {/* TABLA */}
      <TablaCotizaciones
        cotizaciones={cotizaciones}
        onChangeValor={handleChangeAmount}
        onEnterValor={() => {}}
        isLocked={isLocked}
        isMesActual={isMesActual}
        years={years}
        getIndex={globalIndex}
        estilosPorCelda={c => ({
          backgroundColor: c?.isParo ? "#e0f7ff" : "transparent"
        })}
      />
      {/* ⭐ POPUP */}
      <CotizacionesPopup
        visible={mostrarPopup}
        onClose={() => setMostrarPopup(false)}
        cotizaciones={resultado.cotizacionesSimulacion}
      />
    </div>
  );
};

export default SimulacionPage;

