import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { HarnessLoader } from '@angular/cdk/testing';

import { CustomInputComponent } from './custom-input.component';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatInputHarness } from '@angular/material/input/testing';
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

  //ToDo: All mat-input fields are now native input fields and therefore harness does not work anymore.
  xit('should save given text in input to form-group in typescript', waitForAsync(async () => {
    const input = await loader.getHarness(MatInputHarness);
    await input.setValue(checkInMetric.value!.toString());

    expect(component.formGroup.controls['value'].value).toBe(checkInMetric.value!.toString());
  }));
});
