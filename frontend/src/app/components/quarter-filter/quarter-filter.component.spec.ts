import { ComponentFixture, TestBed } from '@angular/core/testing';
import { QuarterFilterComponent } from './quarter-filter.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { OverviewService } from '../../services/overview.service';
import { Observable, of } from 'rxjs';
import { Quarter } from '../../shared/types/model/Quarter';
import { QuarterService } from '../../services/quarter.service';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatSelectHarness } from '@angular/material/select/testing';
import { Router } from '@angular/router';

const overviewService = {
  getOverview: jest.fn()
};

const quarters = [
  new Quarter(
    999, 'Backlog', null, null
  ),
  new Quarter(
    2, '23.02.2025', new Date(), new Date()
  ),
  new Quarter(
    5, '23.02.2025', new Date(), new Date()
  ),
  new Quarter(
    7, '23.02.2025', new Date(), new Date()
  )
];

const quarterService = {
  getAllQuarters (): Observable<Quarter[]> {
    return of(quarters);
  },
  getCurrentQuarter (): Observable<Quarter> {
    return of(quarters[2]);
  }
};

describe('QuarterFilterComponent', () => {
  let component: QuarterFilterComponent;
  let fixture: ComponentFixture<QuarterFilterComponent>;
  let loader: HarnessLoader;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QuarterFilterComponent],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        FormsModule,
        MatSelectModule,
        MatFormFieldModule,
        NoopAnimationsModule
      ],
      providers: [{ provide: OverviewService,
        useValue: overviewService },
      { provide: QuarterService,
        useValue: quarterService }]
    });
    fixture = TestBed.createComponent(QuarterFilterComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should set correct default quarter if no route param is defined', async () => {
    jest.spyOn(component, 'changeDisplayedQuarter');
    jest.spyOn(quarters[2] as any, 'isCurrent')
      .mockReturnValue(true);
    const quarterSelect = await loader.getHarness(MatSelectHarness);
    expect(quarterSelect)
      .toBeTruthy();
    component.ngOnInit();
    fixture.detectChanges();
    expect(component.currentQuarterId)
      .toBe(quarters[2].id);
    expect(await quarterSelect.getValueText())
      .toBe(quarters[2].label + ' Aktuell');
    expect(component.changeDisplayedQuarter)
      .toHaveBeenCalledTimes(1);
  });

  it('should set correct value in form according to route param', async () => {
    jest.spyOn(component, 'changeDisplayedQuarter');
    const routerHarnessPromise = RouterTestingHarness.create();
    const quarterSelectPromise = loader.getHarness(MatSelectHarness);
    await Promise.all([routerHarnessPromise,
      quarterSelectPromise])
      .then(async ([routerHarness,
        quarterSelect]) => {
        await routerHarness.navigateByUrl('/?quarter=' + quarters[3].id);

        expect(quarterSelect)
          .toBeTruthy();
        routerHarness.detectChanges();
        component.ngOnInit();
        fixture.detectChanges();

        expect(component.currentQuarterId)
          .toBe(quarters[3].id);
        expect(await quarterSelect.getValueText())
          .toBe(quarters[3].label);
        expect(component.changeDisplayedQuarter)
          .toHaveBeenCalledTimes(1);
      });
  });

  it('should set default quarter if quarter id in route params does not exist', async () => {
    jest.spyOn(component, 'changeDisplayedQuarter');
    const quarterSelect = await loader.getHarness(MatSelectHarness);

    const routerHarness = await RouterTestingHarness.create();
    await routerHarness.navigateByUrl('/?quarter=1000');

    expect(quarterSelect)
      .toBeTruthy();
    routerHarness.detectChanges();
    component.ngOnInit();
    fixture.detectChanges();
    expect(component.currentQuarterId)
      .toBe(quarters[2].id);
    expect(await quarterSelect.getValueText())
      .toBe(quarters[2].label + ' Aktuell');
    expect(component.changeDisplayedQuarter)
      .toHaveBeenCalledTimes(1);
    expect(router.url)
      .toBe('/?quarter=' + quarters[2].id);
  });
});
