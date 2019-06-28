import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {JobDashboardComponent} from './pages/job-dashboard/job-dashboard.component';

const routes: Routes = [
  {path: 'dashboard', component: JobDashboardComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class JobManagementRoutingModule {
}
