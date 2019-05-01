import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HomeComponent} from './pages/home/home.component';
import {HomeRoutingModule} from './home-routing.module';
import {AuthenticationGuard} from '../../core/guards/authentication.guard';
import {NetworkDebugService} from '../../core/services/network-debug';
import {ButtonsModule, CardsFreeModule, WavesModule} from 'angular-bootstrap-md';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';

@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    HomeRoutingModule,
    CardsFreeModule,
    WavesModule,
    ButtonsModule,
    FontAwesomeModule
  ],
  providers: [
    NetworkDebugService,
    AuthenticationGuard
  ]
})
export class HomeModule {
}
