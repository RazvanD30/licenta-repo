import {NgModule} from '@angular/core';
import {UserService} from '../../core/services/user.service';
import {SharedModule} from '../../shared/shared.module';

@NgModule({
  declarations: [],
  imports: [
    SharedModule
  ],
  providers: [
    UserService
  ]
})
export class UserManagementModule {
}
