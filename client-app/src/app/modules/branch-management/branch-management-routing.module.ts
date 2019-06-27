import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {BranchDashboardComponent} from './pages/branch-dashboard/branch-dashboard.component';

const routes: Routes = [
  {path: 'dashboard', component: BranchDashboardComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BranchManagementRoutingModule {
}
