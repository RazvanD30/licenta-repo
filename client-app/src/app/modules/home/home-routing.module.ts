import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {HomeComponent} from './pages/home/home.component';
import {AuthenticationGuard} from '../../core/guards/authentication.guard';

const routes: Routes = [
  // {path: '', redirectTo: 'home', canActivate: [AuthenticationGuard], pathMatch: 'full'},
  // {path: 'home', component: HomeComponent, canActivate: [AuthenticationGuard]},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule {
}
