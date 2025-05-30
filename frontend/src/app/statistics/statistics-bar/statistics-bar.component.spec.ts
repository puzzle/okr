import { ComponentFixture, TestBed } from '@angular/core/testing';
import { StatisticsBarComponent } from './statistics-bar.component';
import { MatProgressBarModule } from '@angular/material/progress-bar';

describe('StatisticsBarComponent', () => {
  let component: StatisticsBarComponent;
  let fixture: ComponentFixture<StatisticsBarComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [StatisticsBarComponent],
      imports: [MatProgressBarModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(StatisticsBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  describe('getBarClass', () => {
    test.each([[undefined,
      'progress-bar-color__progress'],
    ['relation',
      'progress-bar-color__relation']])('should return correct class when colorPreset is %s', (preset, expected) => {
      component.colorPreset = preset;
      expect(component.getBarClass())
        .toBe(expected);
    });
  });
});
