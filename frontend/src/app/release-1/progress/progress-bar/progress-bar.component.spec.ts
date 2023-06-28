import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgressBarComponent } from './progress-bar.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('ProgressBarComponent', () => {
  let component: ProgressBarComponent;
  let fixture: ComponentFixture<ProgressBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [],
      declarations: [ProgressBarComponent],
      imports: [HttpClientTestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(ProgressBarComponent);
    component = fixture.componentInstance;
    component.limitHigh = 70;
    component.limitLow = 30;
    component.colorHigh = '#29DF0B';
    component.colorMiddle = '#FFA012';
    component.colorLow = '#FF1A0C';
    fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should detect colorHigh', () => {
    component.value = 80;

    component.setColor();

    expect(component.color).toEqual('#29DF0B');
  });

  test('should detect colorLow', () => {
    component.value = 20;

    component.setColor();

    expect(component.color).toEqual('#FF1A0C');
  });

  test('should detect colorMiddle', () => {
    component.value = 50;

    component.setColor();

    expect(component.color).toEqual('#FFA012');
  });
});
