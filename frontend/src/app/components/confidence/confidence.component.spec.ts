import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfidenceComponent } from './confidence.component';
import { checkInMetric } from '../../shared/test-data';
import { MatSliderModule } from '@angular/material/slider';
import { CheckInMin } from '../../shared/types/model/check-in-min';
import { FormsModule } from '@angular/forms';
import { SimpleChange } from '@angular/core';
import { By } from '@angular/platform-browser';

describe('ConfidenceComponent', () => {
  let component: ConfidenceComponent;
  let fixture: ComponentFixture<ConfidenceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConfidenceComponent],
      imports: [MatSliderModule,
        FormsModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ConfidenceComponent);
    component = fixture.componentInstance;
    component.checkIn = checkInMetric;
    component.isEdit = true;
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it.each([[{ confidence: 8 } as CheckInMin,
    '8'],
  [null,
    '5']])('should set confidence of component with correct value', async(checkIn: CheckInMin | null, expected: string) => {
    component.checkIn = checkIn!;
    component.ngOnChanges({
      checkIn: new SimpleChange(null, component.checkIn, true)
    });
    fixture.detectChanges();
    await fixture.whenStable();
    const textField = fixture.debugElement.query(By.css('[data-testId=\'confidence\']'));
    const expectedLabel = expected + '/' + component.max;
    const sliderInputField = fixture.debugElement.query(By.css('mat-slider > input '));

    expect(await sliderInputField.nativeElement.value)
      .toBe(expected);
    expect(textField.nativeElement.innerHTML)
      .toContain(expectedLabel);
  });

  it.each([[true],
    [false]])('should show slider on based on input var', async(editable) => {
    component.isEdit = editable;
    fixture.detectChanges();
    const slider = fixture.debugElement.query(By.css('mat-slider'));

    expect(!!slider)
      .toBe(editable);
  });
});
