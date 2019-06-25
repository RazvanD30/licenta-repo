import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { JobTableComponent } from './pages/job-table/job-table.component';
import {JobManagementRoutingModule} from "./job-management-routing.module";

@NgModule({
  declarations: [JobTableComponent],
  imports: [
    CommonModule,
    JobManagementRoutingModule
  ]
})
export class JobManagementModule { }
