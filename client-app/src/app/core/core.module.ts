import {NgModule, Optional, SkipSelf} from '@angular/core';
import {BranchService} from '../shared/services/branch.service';

@NgModule({
  declarations: [],
  imports: [],
  providers: [
    BranchService
  ]
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    if (parentModule) {
      throw new Error(
        'CoreModule is already loaded. Import it in the AppModule only');
    }
  }

  /*static forRoot(): ModuleWithProviders {
    return {
      ngModule: CoreModule,
      providers: [BranchService]
    };
  }
  */
}
