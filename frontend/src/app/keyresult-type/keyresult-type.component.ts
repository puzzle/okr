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
  @Input() keyresult!: KeyResult;
  @Output() newKeyresultEvent = new EventEmitter<KeyResultEmitDTO>();
  isMetric: boolean = true;
  typeChangeAllowed: boolean = true;

  typeForm = new FormGroup({
    unit: new FormControl<string | null>(null, [Validators.required]),
    baseline: new FormControl<number | null>(null, [Validators.required, Validators.pattern('^\\d*\\.?\\d*$')]),
    stretchGoal: new FormControl<number | null>(null, [Validators.required, Validators.pattern('^\\d*\\.?\\d*$')]),
    commitZone: new FormControl<string | null>(null, [Validators.required, Validators.maxLength(400)]),
    targetZone: new FormControl<string | null>(null, [Validators.required, Validators.maxLength(400)]),
    stretchZone: new FormControl<string | null>(null, [Validators.required, Validators.maxLength(400)]),
  });

  ngOnInit(): void {
    if (this.keyresult) {
      this.typeChangeAllowed = this.keyresult.lastCheckIn?.id == null;
      if (this.keyresult.keyResultType == 'metric') {
        this.isMetric = true;
        let keyresultMetric: KeyResultMetric = this.castToMetric(this.keyresult);
        this.typeForm.setValue({
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
        this.typeForm.setValue({
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
    return this.typeForm.get(name)?.dirty || this.typeForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string) {
    const errors = this.typeForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }

  emitData() {
    if (this.isMetric) {
      let keyresultEmit: KeyResultEmitMetricDTO = {
        keyresultType: 'metric',
        unit: this.typeForm.value.unit,
        baseline: this.typeForm.value.baseline,
        stretchGoal: this.typeForm.value.stretchGoal,
      };
      this.newKeyresultEvent.emit(keyresultEmit);
    } else {
      let keyresultEmit: KeyResultEmitOrdinalDTO = {
        keyresultType: 'ordinal',
        commitZone: this.typeForm.value.commitZone,
        targetZone: this.typeForm.value.targetZone,
        stretchZone: this.typeForm.value.stretchZone,
      };
      this.newKeyresultEvent.emit(keyresultEmit);
    }
  }

  protected readonly errorMessages: any = errorMessages;
  protected readonly Unit = Unit;
}
