import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckInBaseInformationsComponent } from './check-in-base-informations.component';
import { FormControl, FormGroup, Validators } from '@angular/forms';

describe('CheckInBaseInformationsComponent', () => {
  let component: CheckInBaseInformationsComponent;
  let fixture: ComponentFixture<CheckInBaseInformationsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [],
      declarations: [CheckInBaseInformationsComponent],
    });
    fixture = TestBed.createComponent(CheckInBaseInformationsComponent);
    component = fixture.componentInstance;
    component.dialogForm = new FormGroup({
      changeInfo: new FormControl<string>('', [Validators.maxLength(4096)]),
      initiatives: new FormControl<string>('', [Validators.maxLength(4096)]),
    });
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
