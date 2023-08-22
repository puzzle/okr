import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExampleDialogComponent } from './example-dialog.component';
import { HarnessLoader } from '@angular/cdk/testing';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { ReactiveFormsModule } from '@angular/forms';
import { MatRadioButtonHarness } from '@angular/material/radio/testing';
import { MatInputHarness } from '@angular/material/input/testing';
import { MatSelectHarness } from '@angular/material/select/testing';
import { By } from '@angular/platform-browser';
import errorMessages from '../../../../../assets/errors/error-messages.json';

describe('ExampleDialogComponent', () => {
  let component: ExampleDialogComponent;
  let fixture: ComponentFixture<ExampleDialogComponent>;
  let loader: HarnessLoader;
  let errors = errorMessages;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        MatDialogModule,
        NoopAnimationsModule,
        MatSelectModule,
        MatInputModule,
        MatRadioModule,
        ReactiveFormsModule,
      ],
      providers: [
        {
          provide: MatDialogRef,
          useValue: {},
        },
        {
          provide: MAT_DIALOG_DATA,
          useValue: {},
        },
      ],
      declarations: [ExampleDialogComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ExampleDialogComponent);
    loader = TestbedHarnessEnvironment.loader(fixture);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be able to save form', async () => {
    //Insert values into name input
    const nameInput = await loader.getHarness(MatInputHarness);
    await nameInput.setValue('Name');

    //Check radio button
    const buttons = await loader.getAllHarnesses(MatRadioButtonHarness);
    await buttons[0].check();

    //Get mat-select element and click it (dropdown)
    const matSelect = await loader.getHarness(MatSelectHarness);
    await matSelect.open();
    const selectOptions = await matSelect.getOptions();
    await selectOptions[0].click();

    //Check if save button is disabled
    const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
    expect(await submitButton.nativeElement.disabled).toBeFalsy();

    //Validate if object was created correctly
    const formObject = fixture.componentInstance.dialogForm.value;
    expect(formObject.name).toBe('Name');
    expect(formObject.gender).toBe(await buttons[0].getValue());
    expect(formObject.hobby).toBe(await selectOptions[0].getText());
  });

  it('should display error message of too short input', async () => {
    //Insert values into name input which don't match length validator
    const nameInput = await loader.getHarness(MatInputHarness);
    await nameInput.setValue('Na');

    //Verify error message
    const errorMessage = fixture.debugElement.query(By.css('mat-error'));
    expect(errorMessage.nativeElement.textContent).toContain(errors.MINLENGTH);

    //Check if submit button is disabled
    const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
    expect(submitButton.nativeElement.disabled).toBeTruthy();
  });

  it('should display error message of required dropdown', async () => {
    //Open and close mat-select element to trigger validation
    const matSelect = await loader.getHarness(MatSelectHarness);
    await matSelect.open();
    await matSelect.close();

    //Verify error message
    const errorMessage = fixture.debugElement.query(By.css('mat-error'));
    expect(errorMessage.nativeElement.textContent).toContain(errors.REQUIRED);

    //Check if submit button is disabled
    const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
    expect(submitButton.nativeElement.disabled).toBeTruthy();
  });

  it('should not save form unless radio button is checked', async () => {
    //Insert value into input
    const nameInput = await loader.getHarness(MatInputHarness);
    await nameInput.setValue('Name');

    //Select value from dropdown
    const matSelect = await loader.getHarness(MatSelectHarness);
    await matSelect.open();
    const selectOptions = await matSelect.getOptions();
    await selectOptions[1].click();

    //Verify that the submit button is disabled because the radio button is not checked yet
    const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
    expect(await submitButton.nativeElement.disabled).toBeTruthy();

    //Check radio button
    const buttons = await loader.getAllHarnesses(MatRadioButtonHarness);
    await buttons[1].check();

    //Check submit button and form output
    expect(await submitButton.nativeElement.disabled).toBeFalsy();
    const formObject = fixture.componentInstance.dialogForm.value;
    expect(formObject.name).toBe('Name');
    expect(formObject.gender).toBe(await buttons[1].getValue());
    expect(formObject.hobby).toBe(await selectOptions[1].getText());
  });
});
