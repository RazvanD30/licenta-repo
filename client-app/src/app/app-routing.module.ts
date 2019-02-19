import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';

import {LoaderComponent} from './shared/components/loader/loader.component';
import {AuthenticationGuard} from './core/guards/authentication.guard';
import {AuthenticationComponent} from './core/authentication/authentication.component';

const routes: Routes = [
  {path: 'authenticate', component: AuthenticationComponent},
  {path: 'loader', component: LoaderComponent},


  // otherwise redirect to home
  // {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
