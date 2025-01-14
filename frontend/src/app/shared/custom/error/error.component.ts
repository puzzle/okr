import { Component, Input } from '@angular/core';
import { MatError } from '@angular/material/form-field';
import { NgForOf } from '@angular/common';
import { FormGroup, ValidationErrors } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-error',
  imports: [MatError,
    NgForOf],
  templateUrl: './error.component.html',
  styleUrl: './error.component.scss'
})
export class ErrorComponent {
  @Input() form?: FormGroup;

  @Input() controlPath: string[] = [];

  @Input() name?: string;


  constructor(private translate: TranslateService) {
  }

  getErrorMessages() {
    const displayName = this.name || this.controlPath[this.controlPath.length - 1];
    let formField = this.form;
    for (const key of this.controlPath) {
      formField = formField?.get(key) as FormGroup;
    }
    if (!formField?.errors || !formField?.dirty || !formField?.touched) {
      return;
    }
    return Object.keys(formField.errors)
      .map((errorKey: any) => this.translate.instant('DIALOG_ERRORS.' + errorKey.toUpperCase(), { fieldName: displayName,
        ...formField.errors?.[errorKey] || { error: {} } as ValidationErrors }));
  }
}
