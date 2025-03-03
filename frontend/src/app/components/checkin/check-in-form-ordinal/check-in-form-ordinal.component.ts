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

  @Input()
  dialogForm!: FormGroup;

  protected readonly Zone = Zone;
}
