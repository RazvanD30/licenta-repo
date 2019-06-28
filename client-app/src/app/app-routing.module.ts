import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {LoaderComponent} from './shared/components/loader/loader.component';
import {AuthenticationGuard} from './core/guards/authentication.guard';
import {AuthenticationComponent} from './core/authentication/authentication.component';
import {AlertComponent} from './shared/components/alert/alert.component';
import {CustomPreloader} from './shared/config/custom-preloader';

const routes: Routes = [
  {
    path: '', loadChildren: './modules/home/home.module#HomeModule',
    pathMatch: 'full',
    canActivate: [AuthenticationGuard],
    data: {preload: true}
  },
  {
    path: 'branch-management',
    loadChildren: './modules/branch-management/branch-management.module#BranchManagementModule',
    canActivate: [AuthenticationGuard],
    data: {preload: true}
  },
  {path: 'authenticate', component: AuthenticationComponent},
  {path: 'loader', component: LoaderComponent, canActivate: [AuthenticationGuard]},
  {path: 'alert', component: AlertComponent},
  {
    path: 'network-management',
    loadChildren: './modules/network-management/network-management.module#NetworkManagementModule',
    canActivate: [AuthenticationGuard],
    data: {preload: true}
  },
  {
    path: 'file-management',
    loadChildren: './modules/file-management/file-management.module#FileManagementModule',
    canActivate: [AuthenticationGuard],
    data: {preload: true}
  },
  {
    path: 'job-management',
    loadChildren: './modules/job-management/job-management.module#JobManagementModule',
    canActivate: [AuthenticationGuard],
    data: {preload: true}
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes,
    {preloadingStrategy: CustomPreloader, enableTracing: false, useHash: false})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
