import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KeyResultOverviewComponent } from './key-result-overview/key-result-overview.component';
import { MeasureFormComponent } from './measure-form/measure-form.component';
import { MeasureRowComponent } from './measure-row/measure-row.component';
import { MatIconModule } from '@angular/material/icon';
import { KeyresultModule } from '../../../keyresult/keyresult.module';
import { MatDividerModule } from '@angular/material/divider';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatCardModule } from '@angular/material/card';
import { RouterLinkWithHref, RouterModule } from '@angular/router';
import { MatNativeDateModule } from '@angular/material/core';

@NgModule({
  declarations: [
    KeyResultOverviewComponent,
    MeasureFormComponent,
    MeasureRowComponent,
  ],
  imports: [
    CommonModule,
    MatIconModule,
    KeyresultModule,
    MatDividerModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatInputModule,
    MatButtonModule,
    MatExpansionModule,
    MatCardModule,
    RouterLinkWithHref,
    MatNativeDateModule,
    RouterModule,
  ],
})
export class SharedModule {}
