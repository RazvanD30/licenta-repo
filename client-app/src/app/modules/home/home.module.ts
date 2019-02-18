import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HomeComponent} from './pages/home/home.component';
import {HomeRoutingModule} from './home-routing.module';
import {AuthenticationModule} from '../authentication/authentication.module';

@NgModule({
  declarations: [
    HomeComponent
    ],
  imports: [
    CommonModule,
    HomeRoutingModule,
    AuthenticationModule
  ]
})
export class HomeModule {
}
