import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {NetworkDebuggingComponent} from './partial-components/network-debugging/network-debugging.component';
import {NetworkManagementComponent} from './pages/network-management/network-management.component';

const routes: Routes = [
  // {path: '', redirectTo: 'home', canActivate: [AuthenticationGuard], pathMatch: 'full'},
  {path: 'debug', component: NetworkDebuggingComponent},
  {path: 'dashboard', component: NetworkManagementComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class NetworkManagementRoutingModule {
}
