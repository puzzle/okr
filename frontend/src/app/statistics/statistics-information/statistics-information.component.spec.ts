import { ComponentFixture, TestBed } from '@angular/core/testing';
import { StatisticsInformationComponent } from './statistics-information.component';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('StatisticsInformationComponent', () => {
  let component: StatisticsInformationComponent;
  let fixture: ComponentFixture<StatisticsInformationComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [StatisticsInformationComponent],
      providers: [provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting()]
    })
      .compileComponents();

    fixture = TestBed.createComponent(StatisticsInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  describe('chartHeight', () => {
    test.each([
      // [chartValue, maxHeightInPx, expectedHeight]
      [0,
        50,
        '0px'],
      [0.5,
        50,
        '25px'], // 0.5 * 50 = 25px
      [1,
        50,
        '50px'], // 1 * 50 = 50px
      [0.75,
        50,
        '37.5px'], // 0.75 * 50 = 37.5px
      [0.5,
        20,
        '10px'] // 0.5 * 20 = 10px
    ])('with chartValue %i and maxHeightInPx %i, should return %s', (chartValue, maxHeightInPx, expectedHeight) => {
      component.chartValue = chartValue;
      component.maxHeightInPx = maxHeightInPx;
      fixture.detectChanges();
      expect(component.chartHeight)
        .toBe(expectedHeight);
    });
  });
});
