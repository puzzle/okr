import { TestBed } from '@angular/core/testing';
import { Location } from '@angular/common';
import { RouteService } from './route.service';
import {
  ActivatedRoute,
  convertToParamMap,
  NavigationEnd,
  Router,
} from '@angular/router';
import { of } from 'rxjs';

describe('RouteService', () => {
  let service: RouteService;
  let location: Location;
  let activatedRoute: ActivatedRoute;
  let router: Router;

  const mockActivatedRoute = {
    queryParams: of({}),
  };
  beforeEach(() => {
    const mockRouter = {
      navigate: jest.fn(),
      events: of(
        new NavigationEnd(0, 'http://localhost:4200', 'http://localhost:4200')
      ),
      url: 'http://localhost:4200',
    };
    TestBed.configureTestingModule({
      providers: [
        RouteService,
        {
          provide: ActivatedRoute,
          useValue: {
            paramMap: of(
              convertToParamMap({ objectives: '1,2', keyresults: '1' })
            ),
          },
        },
        { provide: Location, useValue: location },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: Router, useValue: mockRouter },
      ],
    });
    service = TestBed.inject(RouteService);
    router = TestBed.inject(Router);
    location = TestBed.inject(Location);
    activatedRoute = TestBed.inject(ActivatedRoute);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should add objective to selected objectives', () => {
    mockActivatedRoute.queryParams = of({ objectives: '1,2,3' });

    service.addToSelectedObjectives(4);

    expect(router.navigate).toHaveBeenCalledWith([], {
      relativeTo: mockActivatedRoute,
      queryParams: {
        objectives: '1,2,3,4',
        keyresults: undefined,
        quarterFilter: undefined,
        teamFilter: undefined,
      },
    });
  });

  it('should remove objective from selected objectives', () => {
    mockActivatedRoute.queryParams = of({ objectives: '1,2,3,4' });

    service.removeFromSelectedObjectives(4);

    expect(router.navigate).toHaveBeenCalledWith([], {
      relativeTo: mockActivatedRoute,
      queryParams: {
        objectives: '1,2,3',
        keyresults: undefined,
        quarterFilter: undefined,
        teamFilter: undefined,
      },
    });
  });

  it('should redirect to root if removing last selected objective', () => {
    mockActivatedRoute.queryParams = of({ objectives: '4' });

    const queryParams = {
      objectives: undefined,
      keyresults: undefined,
      quarterFilter: undefined,
      teamFilter: undefined,
    };

    service.removeFromSelectedObjectives(4);

    expect(router.navigate).toHaveBeenCalledWith(['/'], {
      queryParams: queryParams,
    });
  });

  it('should not remove objective if objective is not in selected objectives', () => {
    mockActivatedRoute.queryParams = of({ objectives: '1,2,3' });

    service.removeFromSelectedObjectives(4);

    expect(router.navigate).toHaveBeenCalledWith([], {
      relativeTo: mockActivatedRoute,
      queryParams: {
        objectives: '1,2,3',
        keyresults: undefined,
        quarterFilter: undefined,
        teamFilter: undefined,
      },
    });
  });

  it('should add keyresult to selected keyresults', () => {
    mockActivatedRoute.queryParams = of({ keyresults: '1,2,3' });

    service.addToSelectedKeyresults(4);

    expect(router.navigate).toHaveBeenCalledWith([], {
      relativeTo: mockActivatedRoute,
      queryParams: {
        objectives: undefined,
        keyresults: '1,2,3,4',
        quarterFilter: undefined,
        teamFilter: undefined,
      },
    });
  });

  it('should remove keyresult from selected keyresults', () => {
    mockActivatedRoute.queryParams = of({ keyresults: '1,2,3,4' });

    service.removeFromSelectedKeyresult(4);

    expect(router.navigate).toHaveBeenCalledWith([], {
      relativeTo: mockActivatedRoute,
      queryParams: {
        objectives: undefined,
        keyresults: '1,2,3',
        quarterFilter: undefined,
        teamFilter: undefined,
      },
    });
  });

  it('should redirect to root if removing last selected keyresults', () => {
    mockActivatedRoute.queryParams = of({ keyresults: '4' });

    const queryParams = {
      objectives: undefined,
      keyresults: undefined,
      quarterFilter: undefined,
      teamFilter: undefined,
    };

    service.removeFromSelectedKeyresult(4);

    expect(router.navigate).toHaveBeenCalledWith(['/'], {
      queryParams: queryParams,
    });
  });

  it('should not remove keyresult if keyresult is not in selected keyresults', () => {
    mockActivatedRoute.queryParams = of({ keyresults: '1,2,3' });

    service.removeFromSelectedKeyresult(4);

    expect(router.navigate).toHaveBeenCalledWith([], {
      relativeTo: mockActivatedRoute,
      queryParams: {
        objectives: undefined,
        keyresults: '1,2,3',
        quarterFilter: undefined,
        teamFilter: undefined,
      },
    });
  });

  it('should navigate to the given location with the current query params', () => {
    const queryParams = {
      objectives: '1,2,3',
      keyresults: '1',
      teamFilter: '1',
    };
    mockActivatedRoute.queryParams = of(queryParams);

    const location = '/some-location';
    service.navigate(location);

    expect(router.navigate).toHaveBeenCalledWith([location], {
      queryParams: queryParams,
    });
  });

  it('should navigate back to "/" when back() method is called', () => {
    mockActivatedRoute.queryParams = of({});
    const queryParams = {
      objectives: undefined,
      keyresults: undefined,
      teamFilter: undefined,
      quarterFilter: undefined,
    };

    service.back();
    expect(router.navigate).toHaveBeenCalledTimes(1);
    expect(router.navigate).toHaveBeenCalledWith(['/'], {
      queryParams: queryParams,
    });
  });

  it('should navigate to previousURL if previousUrl is available', () => {
    mockActivatedRoute.queryParams = of({});
    const queryParams = {
      objectives: undefined,
      keyresults: undefined,
      teamFilter: undefined,
      quarterFilter: undefined,
    };

    service.setPreviousUrl('http://localhost:4200/?objectives=1');
    const location = service.getPreviousUrl();
    service.back();
    expect(router.navigate).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith([location?.split('?')[0]], {
      queryParams: queryParams,
    });
  });

  it('should change quarterfilter', () => {
    mockActivatedRoute.queryParams = of({});
    const queryParams = {
      objectives: undefined,
      keyresults: undefined,
      teamFilter: undefined,
      quarterFilter: '1',
    };

    const newQuarterFilter$ = service.changeQuarterFilter(1);

    newQuarterFilter$.subscribe((newQuarterFilter) => {
      expect(newQuarterFilter).toBeInstanceOf(router.navigate);
      expect(router.navigate).toHaveBeenCalledWith('/', {
        queryParams: queryParams,
      });
    });

    const wrongFilter$ = service.changeQuarterFilter(undefined);

    wrongFilter$.subscribe((wrongFilter) => {
      expect(wrongFilter).toBeInstanceOf(router.navigate);
      expect(router.navigate).toHaveBeenCalledWith('/');
    });
  });

  it('should change team filter', () => {
    mockActivatedRoute.queryParams = of({});
    const queryParams = {
      objectives: undefined,
      keyresults: undefined,
      teamFilter: '1,2,3',
      quarterFilter: undefined,
    };

    const newTeamFilter$ = service.changeTeamFilter([1, 2, 3]);

    newTeamFilter$.subscribe((newTeamFilter) => {
      expect(newTeamFilter).toBeInstanceOf(router.navigate);
      expect(router.navigate).toHaveBeenCalledWith('/', {
        queryParams: queryParams,
      });
    });

    const wrongTeamFilter$ = service.changeQuarterFilter(undefined);

    wrongTeamFilter$.subscribe((wrongTeamFilter) => {
      expect(wrongTeamFilter).toBeInstanceOf(router.navigate);
      expect(router.navigate).toHaveBeenCalledWith('/');
    });
  });
});
