import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {AuthenticationComponent} from './authentication/authentication.component';
import {LoginComponent} from './authentication/partial-components/login/login.component';
import {RegisterComponent} from './authentication/partial-components/register/register.component';
import {MaterialModule} from '../modules/material/material.module';
import {FlexLayoutModule} from '@angular/flex-layout';
import {HttpClientModule} from '@angular/common/http';
import { LoadingComponent } from './loading/loading.component';

@NgModule({
  declarations: [
    FooterComponent,
    HeaderComponent,
    LoginComponent,
    RegisterComponent,
    AuthenticationComponent,
    LoadingComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    FlexLayoutModule,
    HttpClientModule,
    FontAwesomeModule
  ],
  exports: [
    HeaderComponent,
    AuthenticationComponent,
    LoadingComponent,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    FlexLayoutModule,
    HttpClientModule,
    FontAwesomeModule,
  ],
  bootstrap: [
    AuthenticationComponent,
    LoadingComponent
  ]
})
export class CoreModule {
}
