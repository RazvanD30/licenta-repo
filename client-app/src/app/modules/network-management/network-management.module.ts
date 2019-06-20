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
import {NetworkManagementComponent} from './pages/network-management/network-management.component';
import {UserService} from '../../core/services/user.service';
import {CoreModule} from '../../core/core.module';
import { NetworkTableComponent } from './partial-components/network-table/network-table.component';
import {MaterialModule} from '../material/material.module';
import {ScrollingModule} from '@angular/cdk/scrolling';
import { NodeDetailsComponent } from './partial-components/node-details/node-details.component';
import {NetworkInitService} from '../../shared/services/network-init.service';
import { LayerTableComponent } from './partial-components/layer-table/layer-table.component';
import { LogsTableComponent } from './partial-components/logs-table/logs-table.component';
import { GlobalTableComponent } from './partial-components/global-table/global-table.component';
import { NetworkCreateComponent } from './partial-components/network-create/network-create.component';
import { NetworkTablesComponent } from './pages/network-tables/network-tables.component';
@NgModule({
  declarations: [
    CytoscapeComponent,
    NetworkSpecialRoutineComponent,
    NetworkDebuggingComponent,
    NetworkConsoleComponent,
    NetworkManagementComponent,
    NetworkTableComponent,
    LayerTableComponent,
    NodeDetailsComponent,
    LayerTableComponent,
    LogsTableComponent,
    GlobalTableComponent,
    NetworkCreateComponent,
    NetworkTablesComponent
  ],
  imports: [
    CoreModule,
    MaterialModule,
    ScrollingModule,
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
