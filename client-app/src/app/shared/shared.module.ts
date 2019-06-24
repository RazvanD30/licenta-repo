import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MaterialModule} from '../modules/material/material.module';
import {FlexLayoutModule} from '@angular/flex-layout';
import {HttpClientModule} from '@angular/common/http';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {ScrollingModule} from '@angular/cdk/scrolling';
import {HeaderComponent} from '../core/header/header.component';
import {AuthenticationComponent} from '../core/authentication/authentication.component';
import {LoadingComponent} from '../core/loading/loading.component';
import {FooterComponent} from '../core/footer/footer.component';
import {LoginComponent} from '../core/authentication/partial-components/login/login.component';
import {RegisterComponent} from '../core/authentication/partial-components/register/register.component';

@NgModule({
  declarations: [
    FooterComponent,
    HeaderComponent,
    LoginComponent,
    RegisterComponent,
    AuthenticationComponent,
    LoadingComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    FlexLayoutModule,
    HttpClientModule,
    FontAwesomeModule,
    ScrollingModule
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
    ScrollingModule,
    FontAwesomeModule
  ],
  bootstrap: [
    AuthenticationComponent,
    LoadingComponent
  ]
})
export class SharedModule {
}
