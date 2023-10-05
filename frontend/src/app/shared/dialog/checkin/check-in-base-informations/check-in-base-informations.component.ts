import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import errorMessages from '../../../../../assets/errors/error-messages.json';

@Component({
  selector: 'app-check-in-base-informations',
  templateUrl: './check-in-base-informations.component.html',
  styleUrls: ['./check-in-base-informations.component.scss'],
})
export class CheckInBaseInformationsComponent {
  @Input()
  dialogForm!: FormGroup;

  isTouchedOrDirty(name: string) {
    return this.dialogForm.get(name)?.dirty || this.dialogForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string): string[] {
    const errors = this.dialogForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }

  getErrorMessage(key: string) {
    return errorMessages[key.toUpperCase() as keyof typeof errorMessages];
  }

  protected readonly errorMessages = errorMessages;
}
