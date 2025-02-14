import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { CheckInFormOrdinalComponent } from './check-in-form-ordinal.component';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { keyResultOrdinalMin } from '../../../shared/test-data';
import { KeyResultOrdinal } from '../../../shared/types/model/key-result-ordinal';
import { MatDialogModule } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { Zone } from '../../../shared/types/enums/zone';
import { MatRadioButtonHarness } from '@angular/material/radio/testing';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';

describe('CheckInFormOrdinalComponent', () => {
  let component: CheckInFormOrdinalComponent;
  let fixture: ComponentFixture<CheckInFormOrdinalComponent>;
  let loader: HarnessLoader;

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
      declarations: [CheckInFormOrdinalComponent]
    });
    fixture = TestBed.createComponent(CheckInFormOrdinalComponent);
    component = fixture.componentInstance;
    component.keyResult = keyResultOrdinalMin as unknown as KeyResultOrdinal;
    component.dialogForm = new FormGroup({
      ordinalZone: new FormControl<string>('', [Validators.required]),
      confidence: new FormControl<number>(5, [Validators.required,
        Validators.min(1),
        Validators.max(10)])
    });
    fixture.detectChanges();
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should set zone of check-in to fail if value is empty', waitForAsync(async() => {
    expect(component.dialogForm.controls['ordinalZone'].value)
      .toBe('');
  }));

  it('should be able to set zone to fail', waitForAsync(async() => {
    const radioButtons = await loader.getAllHarnesses(MatRadioButtonHarness);
    await radioButtons[0].check();
    expect(component.dialogForm.controls['ordinalZone'].value)
      .toBe(Zone.FAIL);
  }));

  it('should be able to set zone to commit', waitForAsync(async() => {
    const radioButtons = await loader.getAllHarnesses(MatRadioButtonHarness);
    await radioButtons[1].check();
    expect(component.dialogForm.controls['ordinalZone'].value)
      .toBe(Zone.COMMIT);
  }));

  it('should be able to set zone to target', waitForAsync(async() => {
    const radioButtons = await loader.getAllHarnesses(MatRadioButtonHarness);
    await radioButtons[2].check();
    expect(component.dialogForm.controls['ordinalZone'].value)
      .toBe(Zone.TARGET);
  }));

  it('should be able to set zone to stretch', waitForAsync(async() => {
    const radioButtons = await loader.getAllHarnesses(MatRadioButtonHarness);
    await radioButtons[3].check();
    expect(component.dialogForm.controls['ordinalZone'].value)
      .toBe(Zone.STRETCH);
  }));

  it('should be able to switch options', waitForAsync(async() => {
    const radioButtons = await loader.getAllHarnesses(MatRadioButtonHarness);
    await radioButtons[3].check();
    await radioButtons[1].check();
    expect(component.dialogForm.controls['ordinalZone'].value)
      .toBe(Zone.COMMIT);
  }));
});
