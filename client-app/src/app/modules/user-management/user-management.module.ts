import {NgModule} from '@angular/core';
import {UserService} from '../../core/services/user.service';
import {SharedModule} from '../../shared/shared.module';
import {UsersViewComponent} from './pages/users-view/users-view.component';
import {UsersTableComponent} from './partial-components/users-table/users-table.component';

@NgModule({
  declarations: [
    UsersViewComponent,
    UsersTableComponent
  ],
  imports: [
    SharedModule
  ],
  providers: [
    UserService
  ]
})
export class UserManagementModule {
}
