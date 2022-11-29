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
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { ObjectiveRowComponent } from './team-detail/objective-row/objective-row.component';
import { KeyresultDetailComponent } from './keyresult-detail/keyresult-detail.component';
import { ObjectiveDetailComponent } from './team-detail/objective-row/objective-detail/objective-detail.component';
import { MatCardModule } from '@angular/material/card';

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    TeamDetailComponent,
    ObjectiveRowComponent,
    ObjectiveDetailComponent,
    KeyresultDetailComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NoopAnimationsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatIconModule,
    MatMenuModule,
    MatButtonModule,
    MatProgressBarModule,
    ReactiveFormsModule,
    MatCardModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule {}
