export function generarMeses(fechaInicio: string) {
  const inicio = new Date(fechaInicio);
  inicio.setDate(1);

  const hoy = new Date();
  hoy.setDate(1);

  const meses: { year: number; month: number }[] = [];

  const cursor = new Date(inicio);

  while (cursor <= hoy) {
    meses.push({
      year: cursor.getFullYear(),
      month: cursor.getMonth() + 1,
    });
    cursor.setMonth(cursor.getMonth() + 1);
  }

  return meses;
}
