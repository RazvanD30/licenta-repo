import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoginComponent} from './pages/login/login.component';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AuthenticationService} from '../../core/services/authentication.service';
import {AuthenticationRoutingModule} from './authentication-routing.module';
import {RegisterComponent} from './pages/register/register.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatButtonModule, MatCheckboxModule, MatGridListModule, MatIconModule, MatInputModule} from '@angular/material';
import { TempLoginComponent } from './components/temp-login/temp-login.component';
import { AuthenticationComponent } from './pages/authentication/authentication.component';

@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent,
    TempLoginComponent,
    AuthenticationComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    MatIconModule,
    MatButtonModule,
    MatCheckboxModule,
    MatGridListModule,
    MatInputModule,
    MatIconModule,
    BrowserAnimationsModule,
    AuthenticationRoutingModule
  ],
  providers: [
    AuthenticationService
  ]
})
export class AuthenticationModule {
}
