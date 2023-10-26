import { ComponentFixture, TestBed, tick } from '@angular/core/testing';
import { QuarterFilterComponent } from './quarter-filter.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { OverviewService } from '../shared/services/overview.service';
import { quarter } from '../shared/testData';
import { Observable, of } from 'rxjs';
import { Quarter } from '../shared/types/model/Quarter';
import { QuarterService } from '../shared/services/quarter.service';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatSelectHarness } from '@angular/material/select/testing';

const overviewService = {
  getOverview: jest.fn(),
};

const quarters = [
  { ...quarter, id: 2 },
  { ...quarter, id: 5 },
  { ...quarter, id: 7 },
];

const quarterService = {
  getAllQuarters(): Observable<Quarter[]> {
    return of(quarters);
  },
};

describe('QuarterFilterComponent', () => {
  let component: QuarterFilterComponent;
  let fixture: ComponentFixture<QuarterFilterComponent>;
  let loader: HarnessLoader;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QuarterFilterComponent],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        FormsModule,
        MatSelectModule,
        MatFormFieldModule,
        NoopAnimationsModule,
      ],
      providers: [
        { provide: OverviewService, useValue: overviewService },
        { provide: QuarterService, useValue: quarterService },
      ],
    });
    fixture = TestBed.createComponent(QuarterFilterComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set correct default quarter if no route param is defined', async () => {
    jest.spyOn(component, 'changeDisplayedQuarter');
    const quarterSelect = await loader.getHarness(MatSelectHarness);
    expect(quarterSelect).toBeTruthy();
    component.ngOnInit();
    fixture.detectChanges();
    expect(component.quarterId).toBe(quarters[0].id);
    expect(await quarterSelect.getValueText()).toBe(quarters[0].label);
    expect(component.changeDisplayedQuarter).toHaveBeenCalledTimes(0);
  });

  it('should set correct value in form according to route param', async () => {
    jest.spyOn(component, 'changeDisplayedQuarter');
    const routerHarnessPromise = RouterTestingHarness.create();
    const quarterSelectPromise = loader.getHarness(MatSelectHarness);
    await Promise.all([routerHarnessPromise, quarterSelectPromise]).then(async ([routerHarness, quarterSelect]) => {
      await routerHarness.navigateByUrl('/?quarter=' + quarters[2].id);

      expect(quarterSelect).toBeTruthy();
      routerHarness.detectChanges();
      component.ngOnInit();
      fixture.detectChanges();

      expect(component.quarterId).toBe(quarters[2].id);
      expect(await quarterSelect.getValueText()).toBe(quarters[2].label);
      expect(component.changeDisplayedQuarter).toHaveBeenCalledTimes(1);
    });
  });

  it('should set default quarter if quarter id in route params does not exist', async () => {
    jest.spyOn(component, 'changeDisplayedQuarter');
    const quarterSelect = await loader.getHarness(MatSelectHarness);

    const routerHarness = await RouterTestingHarness.create();
    await routerHarness.navigateByUrl('/?quarter=10');

    expect(quarterSelect).toBeTruthy();
    routerHarness.detectChanges();
    component.ngOnInit();
    fixture.detectChanges();
    expect(component.quarterId).toBe(quarters[0].id);
    expect(await quarterSelect.getValueText()).toBe(quarters[0].label);
    expect(component.changeDisplayedQuarter).toHaveBeenCalledTimes(1);
  });
});
