import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckInBaseInformationsComponent } from './check-in-base-informations.component';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatInputHarness } from '@angular/material/input/testing';
import { By } from '@angular/platform-browser';
// @ts-ignore
import * as errorData from '../../../../../assets/errors/error-messages.json';
import { MatInputModule } from '@angular/material/input';
import { MatDialogModule } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { Action } from '../../../types/model/Action';
import { action1, action2 } from '../../../testData';

describe('CheckInBaseInformationsComponent', () => {
  let component: CheckInBaseInformationsComponent;
  let fixture: ComponentFixture<CheckInBaseInformationsComponent>;
  let loader: HarnessLoader;
  let errors = errorData;

  let changeInfoText = 'ChangeInfo';
  let initiativesText = 'Initiatives';

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
      declarations: [CheckInBaseInformationsComponent],
    });
    fixture = TestBed.createComponent(CheckInBaseInformationsComponent);
    component = fixture.componentInstance;
    component.dialogForm = new FormGroup({
      changeInfo: new FormControl<string>('', [Validators.maxLength(4096)]),
      initiatives: new FormControl<string>('', [Validators.maxLength(4096)]),
      actionList: new FormControl<Action[]>([action1, action2]),
    });
    fixture.detectChanges();
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should save given text in input to form-group in typescript', async () => {
    const changeInfoTextbox = fixture.debugElement.query(By.css('[data-testId="changeInfo"]')).nativeElement;
    const initiativesTextbox = fixture.debugElement.query(By.css('[data-testId="initiatives"]')).nativeElement;
    changeInfoTextbox.value = changeInfoText;
    initiativesTextbox.value = initiativesText;

    //Update values in form
    initiativesTextbox.dispatchEvent(new Event('input'));
    changeInfoTextbox.dispatchEvent(new Event('input'));

    expect(component.dialogForm.controls['changeInfo'].value).toBe(changeInfoText);
    expect(component.dialogForm.controls['initiatives'].value).toBe(initiativesText);
  });

  it('should display error message if text input too long', () => {
    //Insert values into name input which don't match length validator
    loader.getAllHarnesses(MatInputHarness).then(async (inputs) => {
      const changeInfoTextbox = inputs[0];
      const initiativesTextbox = inputs[1];
      await changeInfoTextbox.setValue('x'.repeat(4097));
      await initiativesTextbox.setValue('x'.repeat(4097));

      //Verify error message
      const errorMessage = fixture.debugElement.query(By.css('mat-error'));
      expect(errorMessage.nativeElement.textContent).toContain(errors.MAXLENGTH);
    });
  });

  it('should display action list', () => {
    let checkBoxes = document.querySelectorAll('mat-checkbox');
    expect(checkBoxes.length).toEqual(2);
    expect(checkBoxes[1].getAttribute('checked')).toBeTruthy();
  });
});
