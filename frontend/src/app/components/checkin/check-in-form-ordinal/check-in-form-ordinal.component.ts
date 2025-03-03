import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { KeyResultOrdinal } from '../../../shared/types/model/key-result-ordinal';
import { Zone } from '../../../shared/types/enums/zone';

@Component({
  selector: 'app-check-in-form-ordinal',
  templateUrl: './check-in-form-ordinal.component.html',
  styleUrls: ['./check-in-form-ordinal.component.scss'],
  standalone: false
})
export class CheckInFormOrdinalComponent {
  @Input()
  keyResult!: KeyResultOrdinal;

  /*
   * @Input()
   * checkIn!: CheckInOrdinalMin;
   */

  @Input()
  dialogForm!: FormGroup;

  protected readonly Zone = Zone;

  getCurrentZone(): Zone {
    if (this.keyResult.lastCheckIn?.zone) {
      return Zone[this.keyResult.lastCheckIn.zone as keyof typeof Zone];
    } else {
      return Zone.FAIL;
    }
  }
}
