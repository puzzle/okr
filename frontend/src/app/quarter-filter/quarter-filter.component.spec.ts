import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuarterFilterComponent } from './quarter-filter.component';

describe('QuarterFilterComponent', () => {
  let component: QuarterFilterComponent;
  let fixture: ComponentFixture<QuarterFilterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QuarterFilterComponent],
    });
    fixture = TestBed.createComponent(QuarterFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
