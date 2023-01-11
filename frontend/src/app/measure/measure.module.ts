import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MeasureFormComponent } from './measure-form/measure-form.component';
import { MatIconModule } from '@angular/material/icon';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MAT_DATE_LOCALE, MatNativeDateModule } from '@angular/material/core';
import { MeasureRowComponent } from './measure-row/measure-row.component';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatCardModule } from '@angular/material/card';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [MeasureFormComponent, MeasureRowComponent],
  imports: [
    CommonModule,
    MatIconModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatExpansionModule,
    MatCardModule,
    RouterModule,
  ],
  providers: [{ provide: MAT_DATE_LOCALE, useValue: 'de-DE' }],
})
export class MeasureModule {}
