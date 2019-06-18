import { NgModule } from '@angular/core';
import {CoreModule} from '../../core/core.module';
import {MaterialModule} from '../material/material.module';
import {ScrollingModule} from '@angular/cdk/scrolling';
import {BranchManagementRoutingModule} from './branch-management-routing.module';
import {BranchService} from '../../shared/services/branch.service';
import {AuthenticationGuard} from '../../core/guards/authentication.guard';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {HttpTokenInterceptor} from '../../core/interceptors/http-token.interceptor';
import {ErrorHandlerInterceptor} from '../../core/interceptors/error-handler.interceptor';
import {BranchTableComponent} from './partial-components/branch-table/branch-table.component';

@NgModule({
  declarations: [
    BranchTableComponent
  ],
  imports: [
    CoreModule,
    MaterialModule,
    ScrollingModule,
    BranchManagementRoutingModule
  ],
  providers: [
    BranchService,

    AuthenticationGuard,
    {provide: HTTP_INTERCEPTORS, useClass: HttpTokenInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorHandlerInterceptor, multi: true}
  ]
})
export class BranchManagementModule { }
