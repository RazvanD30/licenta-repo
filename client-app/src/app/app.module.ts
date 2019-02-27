import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {AuthenticationDirective} from './shared/directives/authentication.directive';
import {CapitalizePipe} from './shared/pipes/capitalize.pipe';
import {SafePipe} from './shared/pipes/safe.pipe';
import {CoreModule} from './core/core.module';
import {FlexLayoutModule} from '@angular/flex-layout';
import {AlertComponent} from './shared/components/alert/alert.component';
import {AuthenticationGuard} from './core/guards/authentication.guard';
import {AlertService} from './core/services/alert.service';
import {AuthenticationService} from './core/services/authentication.service';
import {UserService} from './core/services/user.service';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {HttpTokenInterceptor} from './core/interceptors/http-token.interceptor';
import {ErrorHandlerInterceptor} from './core/interceptors/error-handler.interceptor';
import {AuthenticateMock, authenticateMockProvider} from './core/mocks/authenticate.mock';
import {AuthenticationComponent} from './core/authentication/authentication.component';
import {LoginComponent} from './core/authentication/partial-components/login/login.component';
import {MaterialModule} from './modules/material/material.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {RegisterComponent} from './core/authentication/partial-components/register/register.component';
import {LoaderComponent} from './shared/components/loader/loader.component';
import {HomeModule} from './modules/home/home.module';
import {UserManagementModule} from './modules/user-management/user-management.module';

@NgModule({
  declarations: [
    AppComponent,
    AuthenticationDirective,
    AuthenticationComponent,
    CapitalizePipe,
    LoginComponent,
    RegisterComponent,
    SafePipe,
    AlertComponent,
    LoaderComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    CoreModule,
    FlexLayoutModule,
    HttpClientModule,
    MaterialModule,


    AppRoutingModule // MUST BE AFTER EVERY LAZY LOADED MODULE
  ],
  providers: [
    AuthenticationGuard,
    AlertService,
    UserService,
    AuthenticationService,

    {provide: HTTP_INTERCEPTORS, useClass: HttpTokenInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorHandlerInterceptor, multi: true},

    // mock backend
    authenticateMockProvider
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
