import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ObjectiveDetailComponent } from './objective-detail/objective-detail.component';
import { MatExpansionModule } from '@angular/material/expansion';
import { ObjectiveRowComponent } from './objective-row/objective-row.component';
import { ObjectiveService } from '../shared/services/objective.service';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { KeyresultModule } from '../keyresult/keyresult.module';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { ObjectiveFormComponent } from './objective-form/objective-form.component';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { ProgressModule } from '../progress/progress.module';

@NgModule({
  declarations: [
    ObjectiveDetailComponent,
    ObjectiveRowComponent,
    ObjectiveFormComponent,
  ],
  providers: [ObjectiveService],
  exports: [
    ObjectiveRowComponent,
    ObjectiveDetailComponent,
    ObjectiveFormComponent,
  ],
  imports: [
    CommonModule,
    MatExpansionModule,
    MatProgressBarModule,
    MatIconModule,
    MatMenuModule,
    KeyresultModule,
    MatButtonModule,
    RouterLink,
    MatDividerModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatInputModule,
    ProgressModule,
  ],
})
export class ObjectiveModule {}
