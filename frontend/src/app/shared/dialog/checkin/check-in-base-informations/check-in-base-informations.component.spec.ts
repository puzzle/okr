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

  let action3: Action = { id: 3, version: 1, action: '', priority: 3, isChecked: true, keyResultId: 1 };

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
      actionList: new FormControl<Action[]>([action1, action2, action3]),
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

  it('should display action list', async () => {
    const checkboxes = fixture.nativeElement.querySelectorAll('mat-checkbox');
    expect(checkboxes.length).toEqual(3);
    expect(checkboxes[0].checked!).toBe(action1.isChecked);
    expect(checkboxes[1].checked!).toBe(action2.isChecked);
  });

  it('should change state of action isChecked', async () => {
    expect(component.dialogForm.value.actionList[0].isChecked).toBeFalsy();
    let event = { checked: true };
    component.changeIsChecked(event, 0);
    expect(component.dialogForm.value.actionList[0].isChecked).toBeTruthy();

    event = { checked: false };
    component.changeIsChecked(event, 0);
    expect(component.dialogForm.value.actionList[0].isChecked).toBeFalsy();
  });
});
