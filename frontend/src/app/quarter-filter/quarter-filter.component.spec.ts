import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuarterFilterComponent } from './quarter-filter.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('QuarterFilterComponent', () => {
  let component: QuarterFilterComponent;
  let fixture: ComponentFixture<QuarterFilterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QuarterFilterComponent],
      imports: [HttpClientTestingModule],
    });
    fixture = TestBed.createComponent(QuarterFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
