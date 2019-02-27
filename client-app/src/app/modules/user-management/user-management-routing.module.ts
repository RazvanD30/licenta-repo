import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {UsersViewComponent} from './pages/users-view/users-view.component';

const routes: Routes = [
  {path: '', component: UsersViewComponent, pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserManagementRoutingModule {
}
