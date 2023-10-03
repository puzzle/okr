import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckInFormOrdinalComponent } from './check-in-form-ordinal.component';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { keyResultOrdinalMin } from '../../../testData';
import { KeyResultOrdinal } from '../../../types/model/KeyResultOrdinal';

describe('CheckInFormOrdinalComponent', () => {
  let component: CheckInFormOrdinalComponent;
  let fixture: ComponentFixture<CheckInFormOrdinalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CheckInFormOrdinalComponent],
    });
    fixture = TestBed.createComponent(CheckInFormOrdinalComponent);
    component = fixture.componentInstance;
    component.keyResult = keyResultOrdinalMin as unknown as KeyResultOrdinal;
    component.dialogForm = new FormGroup({
      value: new FormControl<string>('', [Validators.required]),
      confidence: new FormControl<number>(5, [Validators.required, Validators.min(1), Validators.max(10)]),
    });
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
