import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import errorMessages from '../../../../../assets/errors/error-messages.json';
import { KeyResult } from '../../../types/model/KeyResult';
import { ActionService } from '../../../services/action.service';
import { Action } from '../../../types/model/Action';
import { Observable } from 'rxjs';
import { formInputCheck } from '../../../common';

@Component({
  selector: 'app-check-in-base-informations',
  templateUrl: './check-in-base-informations.component.html',
})
export class CheckInBaseInformationsComponent implements OnInit {
  @Input()
  dialogForm!: FormGroup;
  @Input() keyresult!: KeyResult;
  actionPoints!: Observable<Action[]>;
  protected readonly errorMessages: any = errorMessages;
  protected readonly formInputCheck = formInputCheck;

  constructor(private actionService: ActionService) {}

  ngOnInit() {
    this.actionPoints = this.actionService.getActionsFromKeyResult(this.keyresult.id);
  }

  isTouchedOrDirty(name: string) {
    return this.dialogForm.get(name)?.dirty || this.dialogForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string): string[] {
    const errors = this.dialogForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }
}
