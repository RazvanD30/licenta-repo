import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {NetworkDebuggingComponent} from './partial-components/network-debugging/network-debugging.component';
import {NetworkManagementComponent} from './pages/network-create/network-management.component';
import {UsersViewComponent} from '../user-management/pages/users-view/users-view.component';

const routes: Routes = [
  // {path: '', redirectTo: 'home', canActivate: [AuthenticationGuard], pathMatch: 'full'},
  {path: 'debug', component: NetworkDebuggingComponent},
  {path: 'create', component: NetworkManagementComponent},
  {path: 'users', component: UsersViewComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class NetworkManagementRoutingModule {
}
