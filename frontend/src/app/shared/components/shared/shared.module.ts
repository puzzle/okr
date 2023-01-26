import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
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
import { KeyResultDetailComponent } from './key-result-detail/key-result-detail.component';
import { KeyResultDescriptionComponent } from './key-result-description/key-result-description.component';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { KeyresultFormComponent } from './keyresult-form/keyresult-form.component';
import { MatSelectModule } from '@angular/material/select';
import { MeasureValueValidator } from '../../validators';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  declarations: [
    MeasureFormComponent,
    MeasureRowComponent,
    KeyResultDetailComponent,
    KeyResultDescriptionComponent,
    MeasureValueValidator,
    KeyresultFormComponent,
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
    MatSlideToggleModule,
    MatSelectModule,
    TranslateModule,
  ],
  exports: [MeasureValueValidator],
})
export class SharedModule {}
