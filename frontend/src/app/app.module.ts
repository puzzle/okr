import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { TeamDetailComponent } from './team-detail/team-detail.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [AppComponent, DashboardComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NoopAnimationsModule,
    MatFormFieldModule,
    MatSelectModule,
    ReactiveFormsModule,
  ],
  declarations: [AppComponent, TeamDetailComponent],
  imports: [BrowserModule, AppRoutingModule, NoopAnimationsModule],
  providers: [],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule {}
