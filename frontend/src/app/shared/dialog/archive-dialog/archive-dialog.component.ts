import { Component, inject } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { AsyncPipe } from '@angular/common';
import { QuarterService } from '../../../services/quarter.service';
import { Observable } from 'rxjs';
import { Quarter } from '../../types/model/quarter';
import { SharedModule } from '../../shared.module';

@Component({
  selector: 'app-archive-team-dialog',
  templateUrl: './archive-dialog.component.html',
  styleUrls: ['./archive-dialog.component.scss'],
  standalone: true,
  imports: [
    MatButtonModule,
    MatSelectModule,
    MatFormFieldModule,
    FormsModule,
    AsyncPipe,
    SharedModule
  ]
})
export class ArchiveTeamDialogComponent {
  private readonly quarterService = inject(QuarterService);

  private readonly dialogRef = inject(MatDialogRef<ArchiveTeamDialogComponent>);

  selectedQuarter: Quarter | undefined;

  availableQuarters$: Observable<Quarter[]> = this.quarterService.getAllQuarters();

  onSave(): void {
    this.dialogRef.close(this.selectedQuarter);
  }
}
