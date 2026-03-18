import { http, HttpResponse } from "msw";

// -----------------------------
// Tipos opcionales (puedes ajustarlos)
// -----------------------------
interface Usuario {
  username: string;
  nombre: string;
  apellidos: string;
  email: string;
  fechaNacimiento: string;
  fechaInicioTrabajo: string;
  telefono: string;
}

type Cotizacion = [number, number, number];

// -----------------------------
// Datos mock
// -----------------------------
let usuarioMock: Usuario = {
  username: "Mock",
  nombre: "Juan",
  apellidos: "Pérez García",
  email: "juan@example.com",
  fechaNacimiento: "1966-03-29",
  fechaInicioTrabajo: "2000-11-11",
  telefono: "600123123",
};

let cotizacionesMock: Cotizacion[] = [
  [2026, 1, 4503.47],
  ...crearArrayMeses(1989, 10, 2, 1000.7),
  [1994, 6, 205.55],
  [1994, 7, 560.74],
  [1994, 8, 560.74],
  [1994, 9, 560.74],
  [1994, 10, 560.74],
  [1994, 11, 560.74],
  [1994, 12, 355.20],
];

// -----------------------------
// Función auxiliar tipada
// -----------------------------
function crearArrayMeses(
  año: number,
  start: number,
  numMeses: number,
  valor: number
): Cotizacion[] {
  const resultado: Cotizacion[] = [];

  for (let i = 0; i < numMeses; i++) {
    resultado.push([año, start + i + 1, valor]);
  }

  console.log("Resultado", resultado);
  return resultado;
}

// -----------------------------
// Versión extendida
// -----------------------------
/*
let cotizacionesMock_V2: Cotizacion[] = [
  ...crearArrayMeses(1989, 10, 2, 1000.7),
  ...crearArrayMeses(1990, 0, 12, 1000.7),
  ...crearArrayMeses(1991, 0, 12, 1000.7),
  ...crearArrayMeses(1992, 0, 12, 1000.7),
  ...crearArrayMeses(1993, 0, 12, 1000.7),
  ...crearArrayMeses(2024, 0, 12, 4720.5),
  ...crearArrayMeses(2025, 0, 12, 49098.5),
  ...crearArrayMeses(2026, 0, 3, 5007.69),
];
*/

// -----------------------------
// Handlers MSW
// -----------------------------
export const handlers = [
  // LOGIN
  http.post("/api/login", async ({ request }) => {
    const { username, password } = await request.json() as {username: string;password:string;};
    console.log("Login con usuario", username);

    if (username === "demo" && password === "demo") {
      return HttpResponse.json({ token: "FAKE_JWT_TOKEN_123" });
    }

    return HttpResponse.json(
      { error: "Usuario o contraseña incorrectos" },
      { status: 401 }
    );
  }),

  // DATOS DE USUARIO
  http.get("/api/usuario", ({ request }) => {
    const url = new URL(request.url);
    const username = url.searchParams.get("username");
    console.log("Petición usuario:", username);

    return HttpResponse.json(usuarioMock);
  }),

  // ACTUALIZAR USUARIO
  http.post("/api/usuario", async ({ request }) => {
    const data = (await request.json()) as Usuario;
    usuarioMock = data;
    return HttpResponse.json(data);
  }),

  // COTIZACIONES
  http.get("/api/cotizacion", () => {
    return HttpResponse.json(cotizacionesMock);
  }),

  http.post("/api/cotizacion", async ({ request }) => {
    const data = (await request.json()) as Cotizacion[];
    cotizacionesMock = data;
    return HttpResponse.json({ ok: true, data });
  }),

  // CONFIGURACIÓN
  http.get("/api/configuration", () => {
    return HttpResponse.json({
      baseMinimaCotizacion: 1381.20,
      aniosSimular: 25,
      pensionMaxima: 3359.60,
      porcentajeConvenio: 0.75,
      ajusteConvenio: 1.02,
      flagParo24Meses: true,
    });
  }),
];