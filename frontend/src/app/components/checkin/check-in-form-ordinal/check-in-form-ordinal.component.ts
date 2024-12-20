import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { KeyResultOrdinal } from '../../../shared/types/model/KeyResultOrdinal';
import { Zone } from '../../../shared/types/enums/Zone';
import { CheckInMin } from '../../../shared/types/model/CheckInMin';

@Component({
  selector: 'app-check-in-form-ordinal',
  templateUrl: './check-in-form-ordinal.component.html',
  styleUrls: ['./check-in-form-ordinal.component.scss']
})
export class CheckInFormOrdinalComponent {
  @Input()
  keyResult!: KeyResultOrdinal;

  @Input()
  checkIn!: CheckInMin;

  @Input()
  dialogForm!: FormGroup;

  protected readonly Zone = Zone;
}
