import { Component, inject, OnInit } from '@angular/core';
import { MatDialogClose, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { AsyncPipe } from '@angular/common';
import { QuarterService } from '../../../services/quarter.service';
import { Observable, take } from 'rxjs';
import { Quarter } from '../../types/model/quarter';
import { SharedModule } from '../../shared.module';

@Component({
  selector: 'app-archive-team-dialog',
  templateUrl: './archive-dialog.component.html',
  styleUrls: ['./archive-dialog.component.scss'],
  standalone: true,
  imports: [
    MatButtonModule,
    FormsModule,
    AsyncPipe,
    SharedModule,
    MatDialogClose
  ]
})
export class ArchiveTeamDialogComponent implements OnInit {
  private readonly quarterService = inject(QuarterService);

  private readonly dialogRef = inject(MatDialogRef<ArchiveTeamDialogComponent>);

  selectedQuarter: Quarter | undefined;

  availableQuarters$!: Observable<Quarter[]>;

  ngOnInit(): void {
    this.availableQuarters$ = this.quarterService.getAllQuarters();

    this.quarterService.getCurrentQuarter()
      .pipe(take(1))
      .subscribe((quarter) => {
        this.selectedQuarter = quarter;
      });
  }

  compareById(q1: Quarter, q2: Quarter): boolean {
    return q1 && q2 ? q1.id === q2.id : q1 === q2;
  }

  onSave(): void {
    this.dialogRef.close(this.selectedQuarter);
  }
}
