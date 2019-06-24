import {NgModule} from '@angular/core';
import {CoreModule} from '../../core/core.module';
import {BranchManagementRoutingModule} from './branch-management-routing.module';
import {AuthenticationGuard} from '../../core/guards/authentication.guard';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {HttpTokenInterceptor} from '../../core/interceptors/http-token.interceptor';
import {ErrorHandlerInterceptor} from '../../core/interceptors/error-handler.interceptor';
import {BranchTableComponent} from './partial-components/branch-table/branch-table.component';
import {SharedModule} from '../../shared/shared.module';

@NgModule({
  declarations: [
    BranchTableComponent
  ],
  imports: [
    SharedModule,
    BranchManagementRoutingModule
  ],
  providers: [
    AuthenticationGuard,
    {provide: HTTP_INTERCEPTORS, useClass: HttpTokenInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorHandlerInterceptor, multi: true}
  ]
})
export class BranchManagementModule {
}
