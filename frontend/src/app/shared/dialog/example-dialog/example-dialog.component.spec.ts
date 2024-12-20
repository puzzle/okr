import { ComponentFixture, fakeAsync, TestBed, tick, waitForAsync } from "@angular/core/testing";
import { ExampleDialogComponent } from "./example-dialog.component";
import { HarnessLoader } from "@angular/cdk/testing";
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from "@angular/material/dialog";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { TestbedHarnessEnvironment } from "@angular/cdk/testing/testbed";
import { MatSelectModule } from "@angular/material/select";
import { MatInputModule } from "@angular/material/input";
import { MatRadioModule } from "@angular/material/radio";
import { ReactiveFormsModule } from "@angular/forms";
import { MatRadioButtonHarness } from "@angular/material/radio/testing";
import { MatInputHarness } from "@angular/material/input/testing";
import { MatSelectHarness } from "@angular/material/select/testing";
import { By } from "@angular/platform-browser";
// @ts-ignore
import * as errorData from "../../../../assets/errors/error-messages.json";
import { ObjectiveComponent } from "../../../components/objective/objective.component";
import { MatOptionHarness } from "@angular/material/core/testing";

describe("ExampleDialogComponent", () => {
  let component: ExampleDialogComponent;
  let fixture: ComponentFixture<ExampleDialogComponent>;
  let loader: HarnessLoader;

  const errors = errorData;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        MatDialogModule,
        NoopAnimationsModule,
        MatSelectModule,
        MatInputModule,
        MatRadioModule,
        ReactiveFormsModule
      ],
      providers: [{
        provide: MatDialogRef,
        useValue: {}
      },
      {
        provide: MAT_DIALOG_DATA,
        useValue: {}
      }],
      declarations: [ExampleDialogComponent,
        ObjectiveComponent]
    })
      .compileComponents();
    fixture = TestBed.createComponent(ExampleDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it("should create", fakeAsync(() => {
    expect(component)
      .toBeTruthy();
  }));

  it("should be able to set name", waitForAsync(async() => {
    // Insert values into name input
    const nameInput = await loader.getHarness(MatInputHarness);
    await nameInput.setValue("Name");

    // Check if save button is disabled
    const submitButton = fixture.debugElement.query(By.css("[data-testId=\"submit\"]"));
    expect(await submitButton.nativeElement.disabled)
      .toBeTruthy();

    // Validate if object was created correctly
    const formObject = fixture.componentInstance.dialogForm.value;
    expect(formObject.name)
      .toBe("Name");
  }));

  it("should be able to set buttons", waitForAsync(async() => {
    // Check radio button
    const buttons = await loader.getAllHarnesses(MatRadioButtonHarness);
    await buttons[0].check();

    // Check if save button is disabled
    const submitButton = fixture.debugElement.query(By.css("[data-testId=\"submit\"]"));
    expect(await submitButton.nativeElement.disabled)
      .toBeTruthy();

    // Validate if object was created correctly
    const formObject = fixture.componentInstance.dialogForm.value;
    expect(formObject.name)
      .toBe("");
    expect(formObject.gender)
      .toBe(await buttons[0].getValue());
  }));

  it("should be able to set select", async() => {
    let option = "";
    // Get mat-select element and click it (dropdown)
    await loader.getHarness(MatSelectHarness)
      .then(fakeAsync((matSelect: MatSelectHarness) => {
        matSelect.open();
        advance();
        matSelect.getOptions()
          .then((selectOptions) => {
            selectOptions[0].click();
            advance();
            selectOptions[0].getText()
              .then((value) => option = value);
          });
      }));

    // Check if save button is disabled
    const submitButton = fixture.debugElement.query(By.css("[data-testId=\"submit\"]"));
    expect(await submitButton.nativeElement.disabled)
      .toBeTruthy();

    // Validate if object was created correctly
    const formObject = fixture.componentInstance.dialogForm.value;
    expect(formObject.name)
      .toBe("");
    expect(option)
      .toBe(formObject.hobby);
  });

  it("should display error message of too short input", waitForAsync(async() => {
    // Insert values into name input which don't match length validator
    const nameInput = await loader.getHarness(MatInputHarness);
    await nameInput.setValue("Na");

    // Verify error message
    const errorMessage = fixture.debugElement.query(By.css("mat-error"));
    expect(errorMessage.nativeElement.textContent)
      .toContain(errors.MINLENGTH);

    // Check if submit button is disabled
    const submitButton = fixture.debugElement.query(By.css("[data-testId=\"submit\"]"));
    expect(submitButton.nativeElement.disabled)
      .toBeTruthy();
  }));

  it("should display error message of required dropdown", waitForAsync(async() => {
    // Open and close mat-select element to trigger validation
    const matSelect = await loader.getHarness(MatSelectHarness);
    await matSelect.open();
    await matSelect.close();

    // Verify error message
    const errorMessage = fixture.debugElement.query(By.css("mat-error"));
    expect(errorMessage.nativeElement.textContent)
      .toContain(errors.REQUIRED);

    // Check if submit button is disabled
    const submitButton = fixture.debugElement.query(By.css("[data-testId=\"submit\"]"));
    expect(submitButton.nativeElement.disabled)
      .toBeTruthy();
  }));

  it("should not save form unless radio button is checked", async() => {
    // Insert value into input
    const nameInput = loader.getHarness(MatInputHarness);
    const matSelect = loader.getHarness(MatSelectHarness);
    const radioButtons = loader.getAllHarnesses(MatRadioButtonHarness);

    // Verify that the submit button is disabled because the radio button is not checked yet
    const submitButton = fixture.debugElement.query(By.css("[data-testId=\"submit\"]"));
    expect(submitButton.nativeElement.disabled)
      .toBeTruthy();

    await Promise.all([nameInput,
      matSelect,
      radioButtons])
      .then(fakeAsync(([nameInput,
        matSelect,
        radioButtons]: [MatInputHarness, MatSelectHarness, MatRadioButtonHarness[]]) => {
        nameInput.setValue("Name");
        advance();
        matSelect.open()
          .then(() => {
            matSelect.getOptions()
              .then((options: MatOptionHarness[]) => {
                options[1].click();
                radioButtons[1].check();
                advance();
                // Check submit button and form output
                Promise.all([radioButtons[1].getValue(),
                  options[1].getText()])
                  .then(([gender,
                    hobby]) => {
                    const formObject = fixture.componentInstance.dialogForm.value;
                    expect(submitButton.nativeElement.disabled)
                      .toBeFalsy();
                    expect(formObject.name)
                      .toBe("Name");
                    expect(gender)
                      .toBe("female");
                    expect(hobby)
                      .toBe(formObject.hobby);
                  });
              });
          });
      }));
  });

  function advance(duration = 100) {
    tick(duration);
    fixture.detectChanges();
    tick(duration);
  }
});
