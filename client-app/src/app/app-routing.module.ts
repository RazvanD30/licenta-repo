import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';

import {LoaderComponent} from './shared/components/loader/loader.component';
import {AuthenticationGuard} from './core/guards/authentication.guard';

const routes: Routes = [
  {path: '', loadChildren: './modules/home/home.module#HomeModule'},
  {path: 'authentication', loadChildren: './modules/authentication/authentication.module#AuthenticationModule'},
  {path: 'loader', component: LoaderComponent},

  // otherwise redirect to home
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
