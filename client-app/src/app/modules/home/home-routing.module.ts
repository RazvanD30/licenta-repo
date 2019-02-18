import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {HomeComponent} from './pages/home/home.component';
import {AuthenticationGuard} from '../../core/guards/authentication.guard';
import {AuthenticationModule} from '../authentication/authentication.module';

const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthenticationGuard]},
  { path: 'authentication', component: AuthenticationModule}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
