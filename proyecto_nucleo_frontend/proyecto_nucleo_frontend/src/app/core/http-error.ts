export function textoError(err: any, defecto: string): string {
  const e = err?.error;
  if (typeof e === 'string' && e) return e;
  if (Array.isArray(e?.errores) && e.errores.length) return e.errores.join(' · ');
  return e?.mensaje ?? e?.message ?? e?.error ?? defecto;
}
