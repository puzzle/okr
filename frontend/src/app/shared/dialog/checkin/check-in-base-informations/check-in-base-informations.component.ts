import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import errorMessages from '../../../../../assets/errors/error-messages.json';
import { formInputCheck } from '../../../common';

@Component({
  selector: 'app-check-in-base-informations',
  templateUrl: './check-in-base-informations.component.html',
})
export class CheckInBaseInformationsComponent {
  @Input()
  dialogForm!: FormGroup;
  protected readonly errorMessages: any = errorMessages;

  isTouchedOrDirty(name: string) {
    return this.dialogForm.get(name)?.dirty || this.dialogForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string): string[] {
    const errors = this.dialogForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }

  protected readonly formInputCheck = formInputCheck;
}
