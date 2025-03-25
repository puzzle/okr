import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationPageComponent } from './application-page.component';
import { provideRouter, RouterModule } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { BehaviorSubject, Subject } from 'rxjs';
import { RefreshDataService } from '../../services/refresh-data.service';
import { RouterTestingHarness } from '@angular/router/testing';

const refreshDataServiceMock = {
  teamFilterReady: new Subject<any>(),
  quarterFilterReady: new Subject<any>(),
  reloadOverviewSubject: new Subject<any>(),
  okrBannerHeightSubject: new BehaviorSubject(5)
};

describe('ApplicationPageComponent', () => {
  let component: ApplicationPageComponent;
  let fixture: ComponentFixture<ApplicationPageComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [RouterModule],
      declarations: [ApplicationPageComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: RefreshDataService,
          useValue: refreshDataServiceMock
        }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ApplicationPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    jest.spyOn(component.reloadPage, 'next');
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should emit page reloader if all filters are ready', () => {
    refreshDataServiceMock.quarterFilterReady.next(true);
    refreshDataServiceMock.teamFilterReady.next(true);
    expect(component.reloadPage.next)
      .toHaveBeenCalled();
  });

  it('should emit page reloader reloadOverviewSubject is called', () => {
    refreshDataServiceMock.reloadOverviewSubject.next(true);
    expect(component.reloadPage.next)
      .toHaveBeenCalled();
  });

  it.each([
    [
      '?quarter=7',
      7,
      [],
      ''
    ],
    [
      '?teams=1,2',
      undefined,
      [1,
        2],
      ''
    ],
    [
      '?objectiveQuery=a%20a',
      undefined,
      [],
      'a a'
    ],
    [
      '?teams=1,2&objectiveQuery=a%20a',
      undefined,
      [1,
        2],
      'a a'
    ],
    [
      '?teams=1,2&quarter=7',
      7,
      [1,
        2],
      ''
    ],
    [
      '?quarter=7&objectiveQuery=a%20a',
      7,
      [],
      'a a'
    ]
  ])('should call service method with correct params overview based on query-params', async(
    query: string, quarterParam?: number, teamsParam?: number[], objectiveParam?: string
  ) => {
    const routerHarness = await RouterTestingHarness.create();
    await routerHarness.navigateByUrl('/' + query);
    routerHarness.detectChanges();
    const filterPageChange = component.getFilterPageChange();
    expect(filterPageChange.quarterId)
      .toEqual(quarterParam);
    expect(filterPageChange.objectiveQueryString)
      .toEqual(objectiveParam);
    expect(filterPageChange.teamIds)
      .toEqual(teamsParam);
  });
});
