import { Injectable } from '@angular/core';

export type Tema = 'claro' | 'oscuro';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  tema: Tema = 'claro';

  constructor() {
    let guardado: string | null = null;
    try { guardado = localStorage.getItem('tema'); } catch {}
    const inicial: Tema =
      guardado === 'oscuro' || guardado === 'claro'
        ? guardado
        : window.matchMedia('(prefers-color-scheme: dark)').matches ? 'oscuro' : 'claro';
    this.aplicar(inicial);
  }

  get esOscuro(): boolean {
    return this.tema === 'oscuro';
  }

  alternar(): void {
    this.aplicar(this.esOscuro ? 'claro' : 'oscuro');
  }

  private aplicar(t: Tema): void {
    this.tema = t;
    document.documentElement.setAttribute('data-theme', t === 'oscuro' ? 'dark' : 'light');
    try { localStorage.setItem('tema', t); } catch {}
  }
}
