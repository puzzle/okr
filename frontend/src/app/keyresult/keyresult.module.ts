import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { KeyResultRowComponent } from './key-result-row/key-result-row.component';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { KeyResultService } from '../shared/services/key-result.service';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { KeyResultDetailComponent } from './key-result-detail/key-result-detail.component';
import { MatCardModule } from '@angular/material/card';
import { KeyresultFormComponent } from './keyresult-form/keyresult-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatListModule } from '@angular/material/list';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { DiagramComponent } from './diagram/diagram.component';
import { ProgressModule } from '../progress/progress.module';
import { MatDialogModule } from '@angular/material/dialog';

@NgModule({
  declarations: [
    KeyResultRowComponent,
    KeyResultDetailComponent,
    KeyresultFormComponent,
    DiagramComponent,
  ],
  providers: [KeyResultService, DatePipe],
  exports: [KeyResultRowComponent, KeyResultDetailComponent, DiagramComponent],
  imports: [
    CommonModule,
    MatExpansionModule,
    MatProgressBarModule,
    MatIconModule,
    MatMenuModule,
    RouterLink,
    MatDialogModule,
    MatButtonModule,
    MatCardModule,
    ReactiveFormsModule,
    MatListModule,
    MatInputModule,
    MatSelectModule,
    ProgressModule,
  ],
})
export class KeyresultModule {}
