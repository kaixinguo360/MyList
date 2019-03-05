import { Injectable, NgModule } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate, Router,
  RouterModule,
  RouterStateSnapshot,
  Routes
} from '@angular/router';

import { AuthService } from './service/auth.service';
import { LoginComponent } from './login/login.component';
import { ListComponent } from './list/list.component';
import { ListDetailsComponent } from './list-details/list-details.component';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard implements CanActivate {
  
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return this.authService.isLogin() ? true : this.router.navigate(['']);
  }
  
  constructor(
    private router: Router,
    private authService: AuthService
  ) { }
}

const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'list', component: ListComponent, canActivate: [ LoginGuard ] },
  { path: 'list/:id', component: ListDetailsComponent, canActivate: [ LoginGuard ] }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
