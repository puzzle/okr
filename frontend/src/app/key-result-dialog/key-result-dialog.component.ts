import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { User } from '../shared/types/model/User';
import { KeyResult } from '../shared/types/model/KeyResult';

@Component({
  selector: 'app-key-result-dialog',
  templateUrl: './key-result-dialog.component.html',
  styleUrls: ['./key-result-dialog.component.scss'],
})
export class KeyResultDialogComponent implements OnInit {
  keyResultForm = new FormGroup({
    title: new FormControl<string>('', [Validators.minLength(2), Validators.maxLength(250)]),
    description: new FormControl<string>('', [Validators.minLength(2), Validators.maxLength(4096)]),
    // TODO add owner from Dropdown and [Validators.required, Validators.nullValidator]
    owner: new FormControl<User | null>(null),
    // TODO remove the preset values when adding metric ordinal component
    unit: new FormControl<string | null>('CHF'),
    baseLine: new FormControl<number | null>(3),
    stretchGoal: new FormControl<number | null>(25),
    commitZone: new FormControl<string | null>(null),
    targetZone: new FormControl<string | null>(null),
    stretchZone: new FormControl<string | null>(null),
  });

  constructor(
    @Inject(MAT_DIALOG_DATA) private objective: any,
    @Inject(MAT_DIALOG_DATA) private keyResult: KeyResult,
  ) {}

  ngOnInit(): void {
    if (this.keyResult) {
      // TODO set form values
    }
  }

  saveKeyResult() {
    if (this.objective) {
      // NEW
      // TODO set values from keyResultForm to new KeyResult
      //  and do keyResult.objective = this.objective;
    } else {
      // EDIT
      // TODO set changed values from keyResultForm
    }
    // TODO call service with createdKR
  }
}
