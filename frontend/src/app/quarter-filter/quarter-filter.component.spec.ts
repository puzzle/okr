import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuarterFilterComponent } from './quarter-filter.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { OverviewService } from '../shared/services/overview.service';
import { By } from '@angular/platform-browser';

const overviewService = {
  getOverview(id?: number) {},
};

describe('QuarterFilterComponent', () => {
  let component: QuarterFilterComponent;
  let fixture: ComponentFixture<QuarterFilterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QuarterFilterComponent],
      imports: [HttpClientTestingModule],
      providers: [{ provide: OverviewService, useValue: overviewService }],
    });
    fixture = TestBed.createComponent(QuarterFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get overview according to id set in quarter-filter', () => {});
});
