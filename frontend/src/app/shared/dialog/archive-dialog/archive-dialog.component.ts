import { Component, inject } from '@angular/core';
import { MatDialogClose, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { QuarterService } from '../../../services/quarter.service';
import { Quarter } from '../../types/model/quarter';
import { SharedModule } from '../../shared.module';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-archive-team-dialog',
  templateUrl: './archive-dialog.component.html',
  styleUrls: ['./archive-dialog.component.scss'],
  standalone: true,
  imports: [
    MatButtonModule,
    FormsModule,
    SharedModule,
    MatDialogClose
  ]
})
export class ArchiveTeamDialogComponent {
  private readonly quarterService = inject(QuarterService);

  private readonly dialogRef = inject(MatDialogRef<ArchiveTeamDialogComponent>);

  availableQuarters = toSignal(this.quarterService.getAllQuarters(), { initialValue: [] });

  selectedQuarter = toSignal(this.quarterService.getCurrentQuarter(), { initialValue: undefined });

  compareById(q1: Quarter, q2: Quarter): boolean {
    return q1 && q2 ? q1.id === q2.id : q1 === q2;
  }

  onSave(): void {
    this.dialogRef.close(this.selectedQuarter);
  }
}
