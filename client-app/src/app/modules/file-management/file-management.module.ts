import {NgModule} from '@angular/core';
import {FileDashboardComponent} from './pages/file-dashboard/file-dashboard.component';
import {CoreModule} from '../../core/core.module';
import {MaterialModule} from '../material/material.module';
import {ScrollingModule} from '@angular/cdk/scrolling';
import {AuthenticationGuard} from '../../core/guards/authentication.guard';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {HttpTokenInterceptor} from '../../core/interceptors/http-token.interceptor';
import {ErrorHandlerInterceptor} from '../../core/interceptors/error-handler.interceptor';
import {FileManagementRoutingModule} from './file-management-routing.module';
import {FileService} from '../../shared/services/file.service';
import {FileUploadComponent} from './partial-components/file-upload/file-upload.component';

@NgModule({
  declarations: [
    FileDashboardComponent,
    FileUploadComponent
  ],
  imports: [
    CoreModule,
    MaterialModule,
    ScrollingModule,
    FileManagementRoutingModule
  ],
  providers: [
    FileService,

    AuthenticationGuard,
    {provide: HTTP_INTERCEPTORS, useClass: HttpTokenInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorHandlerInterceptor, multi: true}
  ]
})
export class FileManagementModule {
}
