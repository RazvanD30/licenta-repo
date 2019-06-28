import {NgModule} from '@angular/core';
import {JobTableComponent} from './partial-components/job-table/job-table.component';
import {JobManagementRoutingModule} from './job-management-routing.module';
import {SharedModule} from '../../shared/shared.module';
import { JobDashboardComponent } from './pages/job-dashboard/job-dashboard.component';

@NgModule({
  declarations: [JobTableComponent, JobDashboardComponent],
  imports: [
    SharedModule,
    JobManagementRoutingModule
  ]
})
export class JobManagementModule {
}
