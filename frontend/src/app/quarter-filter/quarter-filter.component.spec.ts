import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuarterFilterComponent } from './quarter-filter.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { OverviewService } from '../shared/services/overview.service';
import { By } from '@angular/platform-browser';
import { quarter } from '../shared/testData';
import { Observable, of, Subject } from 'rxjs';
import { Quarter } from '../shared/types/model/Quarter';
import { QuarterService } from '../shared/services/quarter.service';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';

const overviewService = {
  getOverview: jest.fn(),
};

const quarterService = {
  getAllQuarters(): Observable<Quarter[]> {
    return of([quarter]);
  },
};

describe('QuarterFilterComponent', () => {
  let component: QuarterFilterComponent;
  let fixture: ComponentFixture<QuarterFilterComponent>;
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QuarterFilterComponent],
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [
        { provide: OverviewService, useValue: overviewService },
        { provide: QuarterService, useValue: quarterService },
      ],
    });
    fixture = TestBed.createComponent(QuarterFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set correct default value in form and set quarterId variable according to value in component', () => {
    const quarterSelect = <HTMLSelectElement>document.getElementById('quarter-select');
    expect(quarterSelect).toBeTruthy();
    component.ngOnInit();
    fixture.detectChanges();
    expect(component.quarterId).toBe(quarter.id);
    expect(+quarterSelect.options[quarterSelect.selectedIndex].value).toBe(quarter.id);
  });
});
