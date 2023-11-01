import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import errorMessages from '../../../../../assets/errors/error-messages.json';
import { formInputCheck } from '../../../common';
import { Action } from '../../../types/model/Action';

@Component({
  selector: 'app-check-in-base-informations',
  templateUrl: './check-in-base-informations.component.html',
  styleUrls: ['./check-in-base-informations.component.scss'],
})
export class CheckInBaseInformationsComponent {
  @Input()
  dialogForm!: FormGroup;
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

  getActionsWithText(): Action[] {
    return this.dialogForm.controls['actionList'].value.filter((action: Action) => action.action !== '');
  }
}
