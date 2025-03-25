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
      [10,
        50,
        '1000px'], // 10 * 100 = 1000px
      [1,
        50,
        '100px'], // 1 * 100 = 100px
      [25,
        50,
        '2500px'], // 25 * 100 = 2500px
      [10,
        100,
        '1000px'], // 10 * 100 = 1000px (maxHeightInPx doesn't affect the result)
      [5,
        20,
        '500px'] // 5 * 100 = 500px
    ])('with chartValue %i and maxHeightInPx %i, should return %s', (chartValue, maxHeightInPx, expectedHeight) => {
      component.chartValue = chartValue;
      component.maxHeightInPx = maxHeightInPx;
      fixture.detectChanges();
      expect(component.chartHeight)
        .toBe(expectedHeight);
    });
  });
});
