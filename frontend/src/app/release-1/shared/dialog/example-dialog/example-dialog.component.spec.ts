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
import { MatButtonHarness } from '@angular/material/button/testing';
import { MatDialogHarness } from '@angular/material/dialog/testing';

describe('ExampleDialogComponent', () => {
  let component: ExampleDialogComponent;
  let fixture: ComponentFixture<ExampleDialogComponent>;
  let loader: HarnessLoader;

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
    await nameInput.setValue('Tom');

    //Check radio button
    const buttons = await loader.getAllHarnesses(MatRadioButtonHarness);
    await buttons[0].check();

    //Get mat-select element and click it (dropdown)
    const matSelect = await loader.getHarness(MatSelectHarness);
    await matSelect.open();
    const selectOption = await matSelect.getOptions({ text: 'football' });
    await selectOption[0].click();

    //Check if save button is disabled
    const saveButton = await loader.getHarness(MatButtonHarness);
    expect(await saveButton.isDisabled()).toBe(false);

    //Validate if object was created correctly
    const formObject = fixture.componentInstance.dialogForm.value;
    expect(formObject.name).toBe('Tom');
    expect(formObject.gender).toBe('male');
    expect(formObject.hobby).toBe('football');
  });

  it('should display error message of too short input', () => {});

  it('should display error message of required dropdown', () => {});

  it('should save form after checking radio button', () => {});
});
