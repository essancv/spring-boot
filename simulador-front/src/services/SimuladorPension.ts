export interface Cotizacion {
  year: number;
  month: number;
  amount: number | null;
}

export interface Configuracion {
  baseMinimaCotizacion: number;
  aniosSimular: number;
  pensionMaxima: number;
  porcentajeConvenio: number;
  ajusteConvenio: number;
  flagParo24Meses: boolean;
}

export interface SimuladorParams {
  nacimiento: Date;
  historico: Cotizacion[];
  cotizacionesSimuladas: Cotizacion[];
  usarSimulacion: boolean;
  porcentajeManual: number | null;
  config: Configuracion;
}

export default class SimuladorPension {
  fechaJubilacion: Date;
  historico: Cotizacion[];
  cotizacionesSimuladas: Cotizacion[];
  usarSimulacion: boolean;
  porcentajeManual: number | null;
  config: Configuracion;
  cotizaciones: Cotizacion[];

  constructor({
    nacimiento,
    historico,
    cotizacionesSimuladas,
    usarSimulacion,
    porcentajeManual,
    config
  }: SimuladorParams) {
    this.fechaJubilacion = new Date(
      nacimiento.getFullYear() + 65,
      nacimiento.getMonth(),
      1
    );

    console.log("SimuladorPension - fecha de jubilación:", this.fechaJubilacion);

    this.historico = historico;
    this.cotizacionesSimuladas = cotizacionesSimuladas;
    this.usarSimulacion = usarSimulacion;
    this.porcentajeManual = porcentajeManual;
    this.config = config;

    this.cotizaciones = usarSimulacion
      ? [...historico, ...cotizacionesSimuladas]
      : [...historico];
  }

  ordenarAsc(lista: Cotizacion[]) {
    return [...lista].sort((a, b) =>
      a.year === b.year ? a.month - b.month : a.year - b.year
    );
  }

  ordenarDesc(lista: Cotizacion[]) {
    return [...lista].sort((a, b) =>
      a.year === b.year ? b.month - a.month : b.year - a.year
    );
  }

  generarMesesEntre(y1: number, m1: number, y2: number, m2: number) {
    const meses: { year: number; month: number }[] = [];
    let y = y1;
    let m = m1;

    while (y < y2 || (y === y2 && m <= m2)) {
      meses.push({ year: y, month: m });
      m++;
      if (m > 12) {
        m = 1;
        y++;
      }
    }
    return meses;
  }

  calcularDiasCotizadosReales() {
    const hist = this.ordenarAsc(this.cotizaciones);

    const realesValidos = hist.filter(c => c.amount && c.amount > 0);

    console.log(
      "calcularDiasCotizadosReales - Meses NO cotizados:",
      hist.length - realesValidos.length
    );

    const dias = realesValidos.length * 30;

    let fechaDesde = "";
    let fechaHasta = "";

    if (hist.length > 0) {
      fechaDesde = `${hist[0].month}/${hist[0].year}`;
      const ult = hist[hist.length - 1];
      fechaHasta = `${ult.month}/${ult.year}`;
    }

    return {
      dias,
      rango: { desde: fechaDesde, hasta: fechaHasta }
    };
  }

  detectarHuecosHistorico() {
    const hist = this.ordenarAsc(this.cotizaciones);
    if (hist.length === 0) return false;

    const ultimo = hist[hist.length - 1];

    const todos = this.generarMesesEntre(
      this.fechaJubilacion.getFullYear(),
      this.fechaJubilacion.getMonth(),
      ultimo.year,
      ultimo.month
    );

    const setHist = new Set(hist.map(c => `${c.year}-${c.month}`));

    const faltantes = todos.filter(
      m => !setHist.has(`${m.year}-${m.month}`)
    );

    console.log("Meses faltantes en histórico:", faltantes.length);

    return faltantes;
  }

  rellenaHuecosConBaseMinima(lista: Cotizacion[]) {
    const baseMinima = this.config.baseMinimaCotizacion;
    let contadorHuecos = 0;

    return lista.map(mes => {
      const sinCotizar = mes.amount == null || mes.amount === 0;

      if (!sinCotizar) return mes;

      contadorHuecos++;

      if (contadorHuecos <= 48) {
        return { ...mes, amount: baseMinima };
      }

      return { ...mes, amount: baseMinima * 0.5 };
    });
  }

  construirMeses25Años() {
    const cotizaciones = this.ordenarDesc(this.cotizacionesSimuladas);
    const huecos = this.ordenarDesc(this.historico);

    const map = new Map<string, Cotizacion>();

    cotizaciones.forEach(c => map.set(`${c.year}-${c.month}`, c));
    huecos.forEach(c => {
      const key = `${c.year}-${c.month}`;
      if (!map.has(key)) map.set(key, c);
    });

    let lista = Array.from(map.values());
    lista = this.ordenarDesc(lista);

    return lista.slice(0, 300);
  }

  calcularBaseReguladora(meses25: Cotizacion[]) {
    const suma = meses25.reduce((acc, c) => acc + (c.amount ?? 0), 0);
    const baseMedia = suma / (meses25.length * 30);
    const baseReg = suma / 350;

    const desde = `${meses25[0].month}/${meses25[0].year}`;
    const hasta = `${meses25[meses25.length - 1].month}/${meses25[meses25.length - 1].year}`;

    return { baseMedia, baseReg, rango: { desde, hasta } };
  }

  calcularAniosCotizados(diasReales: number) {
    return diasReales / 365;
  }

  calcularPorcentajePension(anios: number) {
    if (anios < 15) return 0;

    const mesesExtra = (anios - 15) * 12;
    const maxMeses = 21.5 * 12;

    const extra = Math.min(Math.max(mesesExtra, 0), maxMeses);

    return 50 + (extra * (50 / maxMeses));
  }

  calcularIndicadorPensionMaxima(pension: number) {
    const max = this.config.pensionMaxima;

    if (pension > max) {
      return { texto: "🟢 supera la pensión máxima", color: "green" };
    }
    if (pension < max) {
      return { texto: "🔴 por debajo de la pensión máxima", color: "red" };
    }
    return null;
  }

  async simular() {
    const diasReales = this.calcularDiasCotizadosReales();
    const huecos = this.detectarHuecosHistorico();

    const meses25 = this.construirMeses25Años();
    const meses25rellenos = this.rellenaHuecosConBaseMinima(meses25);

    const { baseMedia, baseReg, rango } =
      this.calcularBaseReguladora(meses25rellenos);

    const anios = this.calcularAniosCotizados(diasReales.dias);

    const porcentaje =
      this.porcentajeManual ?? this.calcularPorcentajePension(anios);

    const pension = (baseReg * porcentaje) / 100;
    const indicador = this.calcularIndicadorPensionMaxima(pension);

    return {
      diasCotizadosReales: diasReales.dias,
      rangoReal: diasReales.rango,
      hayHuecosReales: huecos,
      diasSimulados: 300 * 30,
      baseMedia,
      baseReguladora: baseReg,
      rango25Años: rango,
      añosCotizados: anios,
      porcentajePension: porcentaje,
      pensionEstimada: pension,
      indicadorPensionMaxima: indicador,
      cotizacionesSimulacion: meses25rellenos
    };
  }
}
