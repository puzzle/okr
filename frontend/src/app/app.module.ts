import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DashboardComponent } from './dashboard/dashboard.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatInputModule } from '@angular/material/input';
import { TeamModule } from './team/team.module';
import { MatDialogModule } from '@angular/material/dialog';
import { KeyresultDeleteDialogComponent } from './shared/dialog/keyresult-delete-dialog/keyresult-delete-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    KeyresultDeleteDialogComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    MatFormFieldModule,
    MatSelectModule,
    MatIconModule,
    MatMenuModule,
    MatButtonModule,
    MatProgressBarModule,
    MatDialogModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatExpansionModule,
    MatInputModule,
    TeamModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule {}
