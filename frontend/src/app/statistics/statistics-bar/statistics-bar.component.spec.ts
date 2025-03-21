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
});
