import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HarnessLoader } from '@angular/cdk/testing';

import { CustomInputComponent } from './custom-input.component';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { checkInMetric } from '../../testData';
import { By } from '@angular/platform-browser';

describe('CustomInputComponent', () => {
  let component: CustomInputComponent;
  let fixture: ComponentFixture<CustomInputComponent>;
  let loader: HarnessLoader;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        MatDialogModule,
        NoopAnimationsModule,
        MatSelectModule,
        MatInputModule,
        MatRadioModule,
        ReactiveFormsModule,
      ],
      declarations: [CustomInputComponent],
    });
    fixture = TestBed.createComponent(CustomInputComponent);
    component = fixture.componentInstance;
    component.formControlNameGiven = 'value';
    component.formGroup = new FormGroup<any>({
      value: new FormControl('', [Validators.required]),
    });
    fixture.detectChanges();
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should save given text in input to form-group in typescript', async () => {
    const input = fixture.debugElement.query(By.css('input')).nativeElement;
    input.value = checkInMetric.value;
    input.dispatchEvent(new Event('input'));
    expect(component.formGroup.controls['value'].value).toBe(checkInMetric.value!.toString());
  });
});
