import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { routes } from './app-routing.module';
import { BankPageComponent } from './pages/bank-page/bank-page.component';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    CommonModule,
    FormsModule,
    HttpClientModule,
    BankPageComponent,
    RouterModule.forRoot(routes),
  ],
  providers: [],
  schemas: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
