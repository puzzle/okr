import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-example-dialog',
  templateUrl: './example-dialog.component.html',
  styleUrls: ['./example-dialog.component.scss'],
})
export class ExampleDialogComponent {
  hobbies = ['Fishing', 'Football', 'Videogames', 'Tennis', 'Other'];
  selected = '';
  dialogForm = new FormGroup({
    name: new FormControl<string>('', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]),
    gender: new FormControl<string>('', [Validators.required]),
    hobby: new FormControl<string>('', [Validators.required]),
  });

  constructor(public dialog: MatDialogRef<ExampleDialogComponent>, @Inject(MAT_DIALOG_DATA) private data: any) {}

  save() {
    return this.dialog.close(this.dialogForm.value);
  }
}
