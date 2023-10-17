import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { RefreshDataService } from '../../services/refresh-data.service';

@Component({
  selector: 'app-complete-dialog',
  templateUrl: './complete-dialog.component.html',
  styleUrls: ['./complete-dialog.component.scss'],
})
export class CompleteDialogComponent {
  completeForm = new FormGroup({
    isSuccessful: new FormControl<string>('1', [Validators.required]),
    comment: new FormControl<string>('', [Validators.maxLength(4096)]),
  });

  constructor(
    public dialogRef: MatDialogRef<CompleteDialogComponent>,
    private refreshDataService: RefreshDataService,
  ) {}

  closeDialog() {
    this.dialogRef.close({
      endState: Boolean(+!this.completeForm.value.isSuccessful) ? 'SUCCESSFUL' : 'NOTSUCCESSFUL',
      comment: this.completeForm.value.comment,
    });
  }
}
