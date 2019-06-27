import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {NetworkDebuggingComponent} from './partial-components/network-debugging/network-debugging.component';
import {NetworkInitializationComponent} from './pages/network-initialization/network-initialization.component';
import {NetworkTablesComponent} from './pages/network-tables/network-tables.component';

const routes: Routes = [
  {path: 'debug', component: NetworkDebuggingComponent},
  {path: 'init', component: NetworkInitializationComponent},
  {path: 'config', component: NetworkTablesComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class NetworkManagementRoutingModule {
}
