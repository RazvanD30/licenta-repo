import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {UsersTableComponent} from './partial-components/users-table/users-table.component';
import {UsersViewComponent} from './pages/users-view/users-view.component';
import {MaterialModule} from '../material/material.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {UserManagementRoutingModule} from './user-management-routing.module';
import {UserService} from '../../core/services/user.service';

@NgModule({
  declarations: [UsersTableComponent, UsersViewComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    UserManagementRoutingModule
  ],
  providers: [
    UserService
  ]
})
export class UserManagementModule {
}
