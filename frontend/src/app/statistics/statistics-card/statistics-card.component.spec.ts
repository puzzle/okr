import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StatisticsCardComponent } from './statistics-card.component';

describe('StatisticsCardComponent', () => {
  let component: StatisticsCardComponent;
  let fixture: ComponentFixture<StatisticsCardComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [],
      declarations: [StatisticsCardComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(StatisticsCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
