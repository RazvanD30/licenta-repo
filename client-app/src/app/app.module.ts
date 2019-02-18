import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoaderComponent} from './shared/components/loader/loader.component';
import {AuthenticationDirective} from './shared/directives/authentication.directive';
import {CapitalizePipe} from './shared/pipes/capitalize.pipe';
import {SafePipe} from './shared/pipes/safe.pipe';
import {HomeModule} from './modules/home/home.module';
import {CoreModule} from './core/core.module';
import {MatCard} from '@angular/material';
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
import {AuthenticationModule} from './modules/authentication/authentication.module';

@NgModule({
  declarations: [
    AppComponent,
    LoaderComponent,
    AuthenticationDirective,
    CapitalizePipe,
    SafePipe,
    MatCard,
    AlertComponent
  ],
  imports: [
    BrowserModule,
    CoreModule,
    FlexLayoutModule,
    HomeModule,
    AuthenticationModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [
    AuthenticationGuard,
    AlertService,
    UserService,

    {provide: HTTP_INTERCEPTORS, useClass: HttpTokenInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorHandlerInterceptor, multi: true},

    // mock backend
    authenticateMockProvider
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
