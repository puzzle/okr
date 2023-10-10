import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {KeyResult} from '../shared/types/model/KeyResult';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {User} from '../shared/types/model/User';
import {KeyResultMetric} from '../shared/types/model/KeyResultMetric';
import {KeyResultOrdinal} from '../shared/types/model/KeyResultOrdinal';
import errorMessages from '../../assets/errors/error-messages.json';
import {KeyResultEmitMetricDTO} from "../shared/types/DTOs/KeyResultEmitMetricDTO";
import {KeyResultEmitOrdinalDTO} from "../shared/types/DTOs/KeyResultEmitOrdinalDTO";
import {KeyResultEmitDTO} from "../shared/types/DTOs/KeyResultEmitDTO";

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
    owner: new FormControl<User | null>(null),
    unit: new FormControl<string | null>(null, [Validators.required]),
    baseline: new FormControl<number | null>(null, [Validators.required, Validators.pattern('^[0-9]*$')]),
    stretchGoal: new FormControl<number | null>(null, [Validators.required, Validators.pattern('^[0-9]*$')]),
    commitZone: new FormControl<string | null>(null),
    targetZone: new FormControl<string | null>(null),
    stretchZone: new FormControl<string | null>(null),
  });

  ngOnInit(): void {
    if (this.keyresult) {
      this.typeChangeAllowed = this.keyresult.lastCheckIn?.id == null;
      if (this.keyresult.keyResultType == 'metric') {
        this.isMetric = true;
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
        this.isMetric = false;
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

  switchKeyResultType(type: String) {
    if ((type == "metric" && this.isMetric) || (type == "ordinal" && !this.isMetric) || (!this.typeChangeAllowed)) {
      return
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
        keyresultType: "metric",
        owner: this.typeForm.value.owner,
        unit: this.typeForm.value.unit,
        baseline: this.typeForm.value.baseline,
        stretchGoal: this.typeForm.value.stretchGoal
      }
      this.newKeyresultEvent.emit(keyresultEmit);
    } else {
      let keyresultEmit: KeyResultEmitOrdinalDTO = {
        keyresultType: "ordinal",
        owner: this.typeForm.value.owner,
        commitZone: this.typeForm.value.commitZone,
        targetZone: this.typeForm.value.targetZone,
        stretchZone: this.typeForm.value.stretchZone
      }
      this.newKeyresultEvent.emit(keyresultEmit);
    }
  }

  protected readonly errorMessages: any = errorMessages;
}
