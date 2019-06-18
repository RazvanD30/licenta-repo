import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {BranchTableComponent} from './partial-components/branch-table/branch-table.component';

const routes: Routes = [
  {path: 'dashboard', component: BranchTableComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BranchManagementRoutingModule {
}
