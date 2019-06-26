import {NgModule} from '@angular/core';
import {JobTableComponent} from './pages/job-table/job-table.component';
import {JobManagementRoutingModule} from './job-management-routing.module';
import {SharedModule} from '../../shared/shared.module';

@NgModule({
  declarations: [JobTableComponent],
  imports: [
    SharedModule,
    JobManagementRoutingModule
  ]
})
export class JobManagementModule {
}
