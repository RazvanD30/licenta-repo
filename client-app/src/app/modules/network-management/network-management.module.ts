import {NgModule} from '@angular/core';
import {NetworkManagementRoutingModule} from './network-management-routing.module';
import {AuthenticationGuard} from '../../core/guards/authentication.guard';
import {CytoscapeComponent} from '../../shared/components/cytoscape/cytoscape.component';
import {NetworkSpecialRoutineComponent} from './partial-components/network-special-routine/network-special-routine.component';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {HttpTokenInterceptor} from '../../core/interceptors/http-token.interceptor';
import {ErrorHandlerInterceptor} from '../../core/interceptors/error-handler.interceptor';
import {NetworkDebuggingComponent} from './partial-components/network-debugging/network-debugging.component';
import {NetworkConsoleComponent} from './pages/network-console/network-console.component';
import {NetworkInitializationComponent} from './pages/network-initialization/network-initialization.component';
import {UserService} from '../../core/services/user.service';
import {NetworkTableComponent} from './partial-components/network-table/network-table.component';
import {NodeDetailsComponent} from './partial-components/node-details/node-details.component';
import {NetworkInitService} from '../../shared/services/network-init.service';
import {LayerTableComponent} from './partial-components/layer-table/layer-table.component';
import {LogsTableComponent} from './partial-components/logs-table/logs-table.component';
import {GlobalTableComponent} from './partial-components/global-table/global-table.component';
import {NetworkCreateComponent} from './partial-components/network-create/network-create.component';
import {NetworkTablesComponent} from './pages/network-tables/network-tables.component';
import {LinkTableComponent} from './partial-components/link-table/link-table.component';
import {NodeTableComponent} from './partial-components/node-table/node-table.component';
import {SharedModule} from '../../shared/shared.module';

@NgModule({
  declarations: [
    CytoscapeComponent,
    NetworkSpecialRoutineComponent,
    NetworkDebuggingComponent,
    NetworkConsoleComponent,
    NetworkInitializationComponent,
    NetworkTableComponent,
    LayerTableComponent,
    NodeDetailsComponent,
    LayerTableComponent,
    LogsTableComponent,
    GlobalTableComponent,
    NetworkCreateComponent,
    NetworkTablesComponent,
    LinkTableComponent,
    NodeTableComponent
  ],
  imports: [
    SharedModule,
    NetworkManagementRoutingModule
  ],
  providers: [
    UserService,
    NetworkInitService,

    AuthenticationGuard,
    {provide: HTTP_INTERCEPTORS, useClass: HttpTokenInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorHandlerInterceptor, multi: true}

    // mock backend
  ],
  entryComponents: [
    NetworkSpecialRoutineComponent,
    NodeDetailsComponent
  ]
})
export class NetworkManagementModule {
}
