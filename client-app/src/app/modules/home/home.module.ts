import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HomeComponent} from './pages/home/home.component';
import {HomeRoutingModule} from './home-routing.module';
import {AuthenticationGuard} from '../../core/guards/authentication.guard';

@NgModule({
  declarations: [
    HomeComponent
    ],
  imports: [
    CommonModule,
    HomeRoutingModule
  ],
  providers: [
    AuthenticationGuard
  ]
})
export class HomeModule {
}
