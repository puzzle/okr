import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfidenceComponent } from './confidence.component';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatSliderHarness } from '@angular/material/slider/testing';
import { keyResultMetricMin } from '../shared/testData';
import { MatSliderModule } from '@angular/material/slider';
import { CheckInMin } from '../shared/types/model/CheckInMin';
import { FormsModule } from '@angular/forms';
import { SimpleChange } from '@angular/core';
import { By } from '@angular/platform-browser';

describe('ConfidenceComponent', () => {
  let component: ConfidenceComponent;
  let fixture: ComponentFixture<ConfidenceComponent>;
  let loader: HarnessLoader;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConfidenceComponent],
      imports: [MatSliderModule, FormsModule],
    }).compileComponents();

    fixture = TestBed.createComponent(ConfidenceComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
    component.keyResult = keyResultMetricMin;
    component.edit = true;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it.each([
    [{ confidence: 8 } as CheckInMin, '8'],
    [null, '5'],
  ])('should set confidence of component with right value', async (checkIn: CheckInMin | null, expected: string) => {
    component.keyResult.lastCheckIn = checkIn;
    component.ngOnChanges({
      keyResult: new SimpleChange(null, component.keyResult, true),
    });
    fixture.detectChanges();
    await fixture.whenStable();
    const textField = fixture.debugElement.query(By.css("[data-testid='confidence']"));
    const expectedLabel = expected + '/' + component.max;
    const sliderInputField = fixture.debugElement.query(By.css('mat-slider > input '));

    expect(await sliderInputField.nativeElement.value).toBe(expected);
    expect(textField.nativeElement.innerHTML).toContain(expectedLabel);
  });

  it.each([[true], [false]])('should show slider on based on input var', async (editable) => {
    component.edit = editable;
    fixture.detectChanges();
    const slider = fixture.debugElement.query(By.css('mat-slider'));

    expect(!!slider).toBe(editable);
  });
});
