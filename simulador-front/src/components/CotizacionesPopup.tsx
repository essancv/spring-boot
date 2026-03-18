import React from "react";
import TablaCotizaciones from "./TablaCotizaciones";
import type { CotizacionMes } from "./TablaCotizaciones";

interface Props {
  visible: boolean;
  onClose: () => void;
  cotizaciones: CotizacionMes[];
}

const CotizacionesPopup: React.FC<Props> = ({ visible, onClose, cotizaciones }) => {
  if (!visible) return null;

  const years = Array.from(new Set(cotizaciones.map(c => c.year))).sort();

  const getIndex = (y: number, m: number) =>
    cotizaciones.findIndex(c => c.year === y && c.month === m);

  return (
    <div className="modal-overlay">
      <div className="modal-content">

        <h2>Cotizaciones usadas en la simulación</h2>

        {/* ⭐ Leyenda de colores */}
        <div className="leyenda-colores">
          <div><span className="color-box paro"></span> Paro</div>
          <div><span className="color-box convenio"></span> Convenio</div>
          <div><span className="color-box relleno"></span> Relleno mínimo</div>
          <div><span className="color-box hueco"></span> Hueco sin datos</div>
          <div><span className="color-box real"></span> Cotización real</div>
        </div>

        <TablaCotizaciones
          cotizaciones={cotizaciones}
          onChangeValor={() => {}}
          onEnterValor={() => {}} 
          isLocked={() => true}
          isMesActual={() => false}
          years={years}
          getIndex={getIndex}
          estilosPorCelda={c => ({
            backgroundColor:
              c?.isParo ? "#e0f7ff" :
              c?.isConvenio ? "#ffe8a3" :
              c?.isRelleno ? "#fff4cc" :
              c?.isHueco ? "#f2f2f2" :
              "white"
          })}
        />

        <button onClick={onClose} style={{ marginTop: "20px" }}>
          Cerrar
        </button>

      </div>
    </div>
  );
};

export default CotizacionesPopup;
