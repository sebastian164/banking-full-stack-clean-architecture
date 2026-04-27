import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { routes } from './app-routing.module';

import { SharedModule } from './shared.module';

import { BankPageComponent } from './pages/bank-page/bank-page.component';
@NgModule({
  declarations: [],
  imports: [
    BankPageComponent,
    /* ------------------- */
    RouterModule.forChild(routes),
    SharedModule,
  ],
  providers: [],
})
export class MFModule {}
