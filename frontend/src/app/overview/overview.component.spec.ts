import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OverviewComponent } from './overview.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { overViewEntity1 } from '../shared/testData';
import { of } from 'rxjs';
import { OverviewService } from '../shared/services/overview.service';

const overviewService = {
  getOverview() {
    return of(overViewEntity1);
  },
};

describe('OverviewComponent', () => {
  let component: OverviewComponent;
  let fixture: ComponentFixture<OverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [OverviewComponent],
      providers: [{ provide: OverviewService, useValue: overviewService }],
    }).compileComponents();

    fixture = TestBed.createComponent(OverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load default overview', () => {
    jest.spyOn(overviewService, 'getOverview');
    component.ngOnInit();
    expect(overviewService.getOverview).toHaveBeenCalled();
  });
});
