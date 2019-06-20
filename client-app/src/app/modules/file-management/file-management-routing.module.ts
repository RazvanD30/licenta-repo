import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {FileDashboardComponent} from './pages/file-dashboard/file-dashboard.component';

const routes: Routes = [
  {path: 'dashboard', component: FileDashboardComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FileManagementRoutingModule {
}
