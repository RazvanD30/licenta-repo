import {NgModule} from '@angular/core';
import {FileDashboardComponent} from './pages/file-dashboard/file-dashboard.component';
import {AuthenticationGuard} from '../../core/guards/authentication.guard';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {HttpTokenInterceptor} from '../../core/interceptors/http-token.interceptor';
import {ErrorHandlerInterceptor} from '../../core/interceptors/error-handler.interceptor';
import {FileManagementRoutingModule} from './file-management-routing.module';
import {FileService} from '../../shared/services/file.service';
import {FileUploadComponent} from './partial-components/file-upload/file-upload.component';
import {FileLinkComponent} from './partial-components/file-link/file-link.component';
import {SharedModule} from '../../shared/shared.module';
import { FileTableComponent } from './partial-components/file-table/file-table.component';

@NgModule({
  declarations: [
    FileDashboardComponent,
    FileUploadComponent,
    FileLinkComponent,
    FileTableComponent
  ],
  imports: [
    SharedModule,
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
