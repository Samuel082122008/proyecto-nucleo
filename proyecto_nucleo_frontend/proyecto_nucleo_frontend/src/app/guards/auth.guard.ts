import { inject } from '@angular/core';
import { CanActivateFn, Router, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from '../services/auth';
import { Rol } from '../models/enums';

export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  return auth.isLoggedIn() ? true : router.createUrlTree(['/login']);
};

// Uso en rutas: data: { roles: ['DIRECTOR', 'COORDINADOR'] }
export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const roles = (route.data['roles'] ?? []) as Rol[];

  if (!auth.isLoggedIn()) return router.createUrlTree(['/login']);
  if (roles.length === 0 || auth.hasRole(...roles)) return true;
  return router.createUrlTree(['/horario']);
};
