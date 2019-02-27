import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';

import {LoaderComponent} from './shared/components/loader/loader.component';
import {AuthenticationGuard} from './core/guards/authentication.guard';
import {AuthenticationComponent} from './core/authentication/authentication.component';
import {AlertComponent} from './shared/components/alert/alert.component';

const routes: Routes = [
  {path: '', loadChildren: './modules/home/home.module#HomeModule', canLoad: [AuthenticationGuard], pathMatch: 'full'},
  {path: 'user-management', loadChildren: './modules/user-management/user-management.module#UserManagementModule'},
  {path: 'authenticate', component: AuthenticationComponent},
  {path: 'loader', component: LoaderComponent, canActivate: [AuthenticationGuard]},
  {path: 'alert', component: AlertComponent}


  // otherwise redirect to home
  // {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
