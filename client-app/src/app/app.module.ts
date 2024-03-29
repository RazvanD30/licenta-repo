import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {CoreModule} from './core/core.module';
import {AlertComponent} from './shared/components/alert/alert.component';
import {AuthenticationGuard} from './core/guards/authentication.guard';
import {AlertService} from './core/services/alert.service';
import {AuthenticationService} from './core/services/authentication.service';
import {UserService} from './core/services/user.service';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {HttpTokenInterceptor} from './core/interceptors/http-token.interceptor';
import {ErrorHandlerInterceptor} from './core/interceptors/error-handler.interceptor';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {LoaderComponent} from './shared/components/loader/loader.component';
import {MDBBootstrapModule} from 'angular-bootstrap-md';
import {JwtHelper} from 'angular2-jwt';
import {SharedModule} from './shared/shared.module';
import {CustomPreloader} from './shared/config/custom-preloader';

@NgModule({
  declarations: [
    AppComponent,
    AlertComponent,
    LoaderComponent
  ],
  imports: [
    MDBBootstrapModule.forRoot(),
    BrowserAnimationsModule,
    CoreModule,
    SharedModule,
    AppRoutingModule // MUST BE AFTER EVERY LAZY LOADED MODULE
  ],
  providers: [
    AuthenticationGuard,
    CustomPreloader,
    AlertService,
    UserService,
    AuthenticationService,
    JwtHelper,

    {provide: HTTP_INTERCEPTORS, useClass: HttpTokenInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorHandlerInterceptor, multi: true},
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
