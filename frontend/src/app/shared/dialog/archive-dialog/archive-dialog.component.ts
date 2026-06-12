import { Component, effect, inject, signal } from '@angular/core';
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

  readonly availableQuarters = toSignal(this.quarterService.getAllQuarters(), { initialValue: [] });

  readonly currentQuarter = toSignal(this.quarterService.getCurrentQuarter(), { initialValue: undefined });

  readonly selectedQuarter = signal<Quarter | undefined>(undefined);

  private readonly initializeSelection = effect(() => {
    const current = this.currentQuarter();
    const quarters = this.availableQuarters();

    if (!current || this.selectedQuarter()) {
      return;
    }

    const matchingQuarter = quarters.find((q) => q.id === current.id);

    if (matchingQuarter) {
      this.selectedQuarter.set(matchingQuarter);
    }
  });

  compareById(q1: Quarter | null, q2: Quarter | null): boolean {
    return q1?.id === q2?.id;
  }

  onSave(): void {
    this.dialogRef.close(this.selectedQuarter());
  }
}
