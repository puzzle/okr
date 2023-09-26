import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { KeyResult } from '../../types/model/KeyResult';
import { FormGroup } from '@angular/forms';
import errorMessages from '../../../../assets/errors/error-messages.json';

@Component({
  selector: 'app-check-in-form',
  templateUrl: './check-in-form.component.html',
  styleUrls: ['./check-in-form.component.scss'],
})
export class CheckInFormComponent implements OnInit {
  keyResult: KeyResult;
  currentDate: Date;

  dialogForm = new FormGroup({});

  constructor(
    public dialogRef: MatDialogRef<CheckInFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.keyResult = data.keyResult;
    this.currentDate = new Date();
  }

  ngOnInit(): void {
    console.log(this.keyResult.description);
  }

  isTouchedOrDirty(name: string) {
    return this.dialogForm.get(name)?.dirty || this.dialogForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string) {
    const errors = this.dialogForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }

  protected readonly errorMessages: any = errorMessages;
}
