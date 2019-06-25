import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';

import {LoaderComponent} from './shared/components/loader/loader.component';
import {AuthenticationGuard} from './core/guards/authentication.guard';
import {AuthenticationComponent} from './core/authentication/authentication.component';
import {AlertComponent} from './shared/components/alert/alert.component';

const routes: Routes = [
  {path: '', loadChildren: './modules/home/home.module#HomeModule', canLoad: [AuthenticationGuard], pathMatch: 'full'},
  {path: 'user-management', loadChildren: './modules/user-management/user-management.module#UserManagementModule'},
  {
    path: 'branch-management',
    loadChildren: './modules/branch-management/branch-management.module#BranchManagementModule',
    canLoad: [AuthenticationGuard]
  },
  {path: 'authenticate', component: AuthenticationComponent},
  {path: 'loader', component: LoaderComponent, canActivate: [AuthenticationGuard]},
  {path: 'alert', component: AlertComponent},
  {
    path: 'network-management',
    loadChildren: './modules/network-management/network-management.module#NetworkManagementModule',
    canLoad: [AuthenticationGuard]
  },
  {
    path: 'file-management',
    loadChildren: './modules/file-management/file-management.module#FileManagementModule',
    canLoad: [AuthenticationGuard]
  },
  {
    path: 'job-management',
    loadChildren: './modules/job-management/job-management.module#JobManagementModule',
    canLoad: [AuthenticationGuard]
  }


  // otherwise redirect to home
  // {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
