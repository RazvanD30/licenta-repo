import {NgModule} from '@angular/core';
import {UserService} from '../../core/services/user.service';
import {UsersTableComponent} from './partial-components/users-table/users-table.component';

@NgModule({
  declarations: [],
  imports: [
    UsersTableComponent
  ],
  providers: [
    UserService
  ]
})
export class UserManagementModule {
}
