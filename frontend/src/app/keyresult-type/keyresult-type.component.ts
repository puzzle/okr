import { Component, Input, OnInit } from '@angular/core';
import { KeyResult } from '../shared/types/model/KeyResult';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { User } from '../shared/types/model/User';
import { KeyResultMetric } from '../shared/types/model/KeyResultMetric';
import { KeyResultOrdinal } from '../shared/types/model/KeyResultOrdinal';
import errorMessages from '../../assets/errors/error-messages.json';

@Component({
  selector: 'app-keyresult-type',
  templateUrl: './keyresult-type.component.html',
  styleUrls: ['./keyresult-type.component.scss'],
})
export class KeyresultTypeComponent implements OnInit {
  @Input() keyresult!: KeyResult;
  isMetric: boolean = true;

  typeForm = new FormGroup({
    owner: new FormControl<User | null>(null),
    unit: new FormControl<String | null>(null, [Validators.required]),
    baseline: new FormControl<number | null>(null, [Validators.required, Validators.pattern('^[0-9]*$')]),
    stretchGoal: new FormControl<number | null>(null, [Validators.required, Validators.pattern('^[0-9]*$')]),
    commitZone: new FormControl<string | null>(null),
    targetZone: new FormControl<string | null>(null),
    stretchZone: new FormControl<string | null>(null),
  });

  ngOnInit(): void {
    if (this.keyresult) {
      if (this.keyresult.keyResultType == 'metric') {
        let keyresultMetric: KeyResultMetric = this.castToMetric(this.keyresult);
        this.typeForm.setValue({
          owner: keyresultMetric.owner,
          unit: keyresultMetric.unit,
          baseline: keyresultMetric.baseline,
          stretchGoal: keyresultMetric.stretchGoal,
          commitZone: null,
          targetZone: null,
          stretchZone: null,
        });
      } else {
        let keyresultOrdinal: KeyResultOrdinal = this.castToOrdinal(this.keyresult);
        this.typeForm.setValue({
          owner: keyresultOrdinal.owner,
          unit: null,
          baseline: null,
          stretchGoal: null,
          commitZone: keyresultOrdinal.commitZone,
          targetZone: keyresultOrdinal.targetZone,
          stretchZone: keyresultOrdinal.stretchZone,
        });
      }
    }
  }

  castToMetric(keyResult: KeyResult) {
    return keyResult as KeyResultMetric;
  }

  castToOrdinal(keyResult: KeyResult) {
    return keyResult as KeyResultOrdinal;
  }

  switchKeyResultType() {
    this.isMetric = !this.isMetric;
  }

  isTouchedOrDirty(name: string) {
    return this.typeForm.get(name)?.dirty || this.typeForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string) {
    const errors = this.typeForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }

  protected readonly errorMessages: any = errorMessages;
}
