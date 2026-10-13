import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { authInterceptor } from './interceptors/auth.interceptor';

import { Login } from './component/login/login';
import { Navbar } from './component/navbar/navbar';
import { Docentes } from './component/docentes/docentes';
import { Cursos } from './component/cursos/cursos';
import { Aulas } from './component/aulas/aulas';
import { Usuarios } from './component/usuarios/usuarios';
import { Grupos } from './component/grupos/grupos';
import { Habilitaciones } from './component/habilitaciones/habilitaciones';
import { Restricciones } from './component/restricciones/restricciones';
import { Horario } from './component/horario/horario';
import { Parametros } from './component/parametros/parametros';

@NgModule({
  declarations: [
    App, Login, Navbar,
    Docentes, Cursos, Aulas, Usuarios,
    Grupos, Habilitaciones, Restricciones,
    Horario, Parametros,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [
    provideHttpClient(withInterceptors([authInterceptor])),
  ],
  bootstrap: [App],
})
export class AppModule {}
