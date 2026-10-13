import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Login } from './component/login/login';
import { authGuard, roleGuard } from './guards/auth.guard';
import { Docentes } from './component/docentes/docentes';
import { Cursos } from './component/cursos/cursos';
import { Aulas } from './component/aulas/aulas';
import { Usuarios } from './component/usuarios/usuarios';
import { Grupos } from './component/grupos/grupos';
import { Habilitaciones } from './component/habilitaciones/habilitaciones';
import { Restricciones } from './component/restricciones/restricciones';
import { Horario } from './component/horario/horario';
import { Parametros } from './component/parametros/parametros';



const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: Login },

  { path: 'horario',        component: Horario,        canActivate: [authGuard] },
  { path: 'restricciones',  component: Restricciones,  canActivate: [authGuard] },

  { path: 'docentes',       component: Docentes,       canActivate: [roleGuard], data: { roles: ['DIRECTOR', 'COORDINADOR'] } },
  { path: 'cursos',         component: Cursos,         canActivate: [roleGuard], data: { roles: ['DIRECTOR', 'COORDINADOR'] } },
  { path: 'aulas',          component: Aulas,          canActivate: [roleGuard], data: { roles: ['DIRECTOR', 'COORDINADOR'] } },
  { path: 'grupos',         component: Grupos,         canActivate: [roleGuard], data: { roles: ['DIRECTOR', 'COORDINADOR'] } },
  { path: 'habilitaciones', component: Habilitaciones, canActivate: [roleGuard], data: { roles: ['DIRECTOR', 'COORDINADOR'] } },
  { path: 'parametros',     component: Parametros,     canActivate: [roleGuard], data: { roles: ['DIRECTOR', 'COORDINADOR'] } },
  { path: 'usuarios',       component: Usuarios,       canActivate: [roleGuard], data: { roles: ['DIRECTOR'] } },

  { path: '**', redirectTo: 'login' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
