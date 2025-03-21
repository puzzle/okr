import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StatisticsInformationComponent } from './statistics-information.component';

describe('StatisticsInformationComponent', () => {
  let component: StatisticsInformationComponent;
  let fixture: ComponentFixture<StatisticsInformationComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [StatisticsInformationComponent]
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
});
