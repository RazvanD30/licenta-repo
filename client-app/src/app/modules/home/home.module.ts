import {NgModule} from '@angular/core';
import {HomeComponent} from './pages/home/home.component';
import {HomeRoutingModule} from './home-routing.module';
import {AuthenticationGuard} from '../../core/guards/authentication.guard';
import {SharedModule} from '../../shared/shared.module';

@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    HomeRoutingModule,
    SharedModule
  ],
  providers: [
    AuthenticationGuard
  ]
})
export class HomeModule {
}
