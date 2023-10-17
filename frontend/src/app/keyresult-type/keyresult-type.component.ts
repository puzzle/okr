import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { KeyResult } from '../shared/types/model/KeyResult';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { KeyResultMetric } from '../shared/types/model/KeyResultMetric';
import { KeyResultOrdinal } from '../shared/types/model/KeyResultOrdinal';
import errorMessages from '../../assets/errors/error-messages.json';
import { KeyResultEmitMetricDTO } from '../shared/types/DTOs/KeyResultEmitMetricDTO';
import { KeyResultEmitOrdinalDTO } from '../shared/types/DTOs/KeyResultEmitOrdinalDTO';
import { KeyResultEmitDTO } from '../shared/types/DTOs/KeyResultEmitDTO';
import { Unit } from '../shared/types/enums/Unit';

@Component({
  selector: 'app-keyresult-type',
  templateUrl: './keyresult-type.component.html',
  styleUrls: ['./keyresult-type.component.scss'],
})
export class KeyresultTypeComponent implements OnInit {
  @Input() keyResultForm!: FormGroup
  @Input() keyresult!: KeyResult;
  @Output() newKeyresultEvent = new EventEmitter<KeyResultEmitDTO>();
  isMetric: boolean = true;
  typeChangeAllowed: boolean = true;

  ngOnInit(): void {
    if (this.keyresult) {
      // this.typeChangeAllowed = this.keyresult.lastCheckIn?.id == null;
      if (this.keyresult.keyResultType == 'metric') {
        this.isMetric = true;
        let keyresultMetric: KeyResultMetric = this.castToMetric(this.keyresult);
        this.keyResultForm.setValue({
          unit: keyresultMetric.unit,
          baseline: keyresultMetric.baseline,
          stretchGoal: keyresultMetric.stretchGoal,
          commitZone: null,
          targetZone: null,
          stretchZone: null,
        });
      } else {
        this.isMetric = false;
        let keyresultOrdinal: KeyResultOrdinal = this.castToOrdinal(this.keyresult);
        this.keyResultForm.setValue({
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

  switchKeyResultType(type: String) {
    if ((type == 'metric' && this.isMetric) || (type == 'ordinal' && !this.isMetric) || !this.typeChangeAllowed) {
      return;
    } else {
      this.isMetric = !this.isMetric;
    }
  }

  isTouchedOrDirty(name: string) {
    return this.keyResultForm.get(name)?.dirty || this.keyResultForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string) {
    const errors = this.keyResultForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }

  protected readonly errorMessages: any = errorMessages;
  protected readonly Unit = Unit;
}
