import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OverviewComponent } from './overview.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { overViewEntity1 } from '../shared/testData';
import { of } from 'rxjs';
import { OverviewService } from '../shared/services/overview.service';
import { AppRoutingModule } from '../app-routing.module';
import { RouterTestingHarness } from '@angular/router/testing';

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
      imports: [HttpClientTestingModule, AppRoutingModule],
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

  it('should load default overview when no quarter is defined in route-params', () => {
    jest.spyOn(overviewService, 'getOverview');
    component.ngOnInit();
    expect(overviewService.getOverview).toHaveBeenCalled();
  });

  it('should load overview according to quarter-id when valid quarterId is defined in route-params', async () => {
    jest.spyOn(overviewService, 'getOverview');
    const routerHarness = await RouterTestingHarness.create();
    await routerHarness.navigateByUrl('/?quarter=7');
    routerHarness.detectChanges();
    component.ngOnInit();
    expect(overviewService.getOverview).toHaveBeenCalledWith(7);
  });
});
