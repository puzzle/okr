import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import errorMessages from '../../../../../assets/errors/error-messages.json';
import { KeyResult } from '../../../types/model/KeyResult';
import { formInputCheck } from '../../../common';

@Component({
  selector: 'app-check-in-base-informations',
  templateUrl: './check-in-base-informations.component.html',
  styleUrls: ['./check-in-base-informations.component.scss'],
})
export class CheckInBaseInformationsComponent {
  @Input()
  dialogForm!: FormGroup;
  @Input() keyresult!: KeyResult;
  protected readonly errorMessages: any = errorMessages;
  protected readonly formInputCheck = formInputCheck;

  isTouchedOrDirty(name: string) {
    return this.dialogForm.get(name)?.dirty || this.dialogForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string): string[] {
    const errors = this.dialogForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }

  changeIsChecked(event: any, index: number) {
    const actions = this.dialogForm.value.actionList;
    actions[index] = { ...actions[index], isChecked: event.checked };
    this.dialogForm.patchValue({ actionList: actions });
  }
}
