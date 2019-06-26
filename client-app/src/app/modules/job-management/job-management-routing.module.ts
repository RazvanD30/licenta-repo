import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {JobTableComponent} from './pages/job-table/job-table.component';

const routes: Routes = [
  {path: 'dashboard', component: JobTableComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class JobManagementRoutingModule {
}
