// src/components/TablaCotizaciones.tsx
import React from "react";

export interface CotizacionMes {
  id?: number;
  year: number;
  month: number;
  amount: number | null;
  isParo?: boolean;
  isLocked?: boolean;
  isConvenio?:boolean;
  isRelleno?:boolean;
  isHueco?:boolean;
  isNew?:boolean;
}

interface Props {
  cotizaciones: CotizacionMes[];
  onChangeValor: (index: number, nuevoValor: string) => void;
  onEnterValor: (index: number) => void; 
  isLocked: (year: number, month: number) => boolean;
  isMesActual: (year: number, month: number) => boolean;
  years: number[];
  getIndex: (year: number, month: number) => number;
  estilosPorCelda?: (c: CotizacionMes | undefined) => React.CSSProperties;
}

const TablaCotizaciones: React.FC<Props> = ({
  cotizaciones,
  onChangeValor,
  onEnterValor,
  isLocked,
  isMesActual,
  years,
  getIndex,
  estilosPorCelda
}) => {
  const handleEnter = (index: number, e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      const next = document.querySelector<HTMLInputElement>(
        `input[data-index="${index + 1}"]`
      );
      next?.focus();
    }
  };

  const cotPorAño: Record<number, CotizacionMes[]> = {};
  years.forEach(y => {
    cotPorAño[y] = cotizaciones
      .filter(c => c.year === y)
      .sort((a, b) => a.month - b.month);
  });

  return (
    <table className="cotizaciones-table">
      <thead>
        <tr>
          <th>Mes</th>
          {years.map(year => (
            <th key={year}>{year}</th>
          ))}
        </tr>
      </thead>

      <tbody>
        {Array.from({ length: 12 }).map((_, i) => {
          const mes = i + 1;

          return (
            <tr key={mes}>
              <td>{mes}</td>

              {years.map(year => {
                const c = cotPorAño[year].find(x => x.month === mes);
                const index = getIndex(year, mes);

                const locked = isLocked(year, mes);
                const esActual = isMesActual(year, mes);

                const estilos = estilosPorCelda ? estilosPorCelda(c) : {};

                return (
                  <td
                    key={`${year}-${mes}`}
                    className={locked ? "locked-cell" : ""}
                    style={estilos}
                  >
                    <input
                      data-index={index}
                      disabled={locked}
                      value={esActual ? "" : (c?.amount ?? "")}
                      onChange={e => onChangeValor(index, e.target.value)}
                      onKeyDown={e => {
                        if (e.key === "Enter") {
                          onEnterValor(index);   // 👈 aquí llamamos al backend
                          handleEnter(index, e);
                        }
                      }}
                    />
                  </td>
                );
              })}
            </tr>
          );
        })}
      </tbody>
    </table>
  );
};

export default TablaCotizaciones;
