import { TestBed } from '@angular/core/testing';
import { convertToParamMap, Router } from '@angular/router';
import { QuarterService } from '../services/quarter.service';
import { UserService } from '../services/user.service';
import { TeamStateService } from '../services/team.state.service';
import { defaultQueryParamsGuard } from './default-query-params.guard';
import { isObservable, lastValueFrom, of } from 'rxjs';
import { quarter1, teamList, testUser } from '../shared/test-data';

describe('DefaultQueryParamsGuard', () => {
  let quarterServiceMock: Partial<QuarterService>;
  let userServiceMock: Partial<UserService>;
  let teamStateServiceMock: Partial<TeamStateService>;
  let routerMock: Partial<Router>;

  const mockUser = testUser;
  const mockCurrentQuarter = quarter1;
  const mockUserTeamIdsString = mockUser.userTeamList.map((team) => team.id)
    .join(',');

  const executeGuard = async(queryParams: { quarter: string;
    teams: string; } = {
    quarter: '',
    teams: ''
  }, url = '', navigated = false) => {
    const route = { queryParamMap: convertToParamMap(queryParams) } as any;
    const state = { url } as any;

    jest.replaceProperty(routerMock, 'navigated', navigated);

    const guardResult = TestBed.runInInjectionContext(() => defaultQueryParamsGuard(route, state));

    if (isObservable(guardResult)) {
      return await lastValueFrom(guardResult);
    }
    return guardResult;
  };

  beforeEach(() => {
    quarterServiceMock = {
      getCurrentQuarter: jest.fn()
        .mockReturnValue(of(mockCurrentQuarter))
    };

    userServiceMock = {
      getOrInitCurrentUser: jest.fn()
        .mockReturnValue(of(mockUser))
    };

    teamStateServiceMock = {
      loadTeams: jest.fn()
        .mockReturnValue(of(teamList))
    };

    routerMock = {
      parseUrl: jest.fn()
        .mockReturnValue({ queryParams: {} }),
      navigated: false
    };

    TestBed.configureTestingModule({
      providers: [
        { provide: Router,
          useValue: routerMock },
        { provide: QuarterService,
          useValue: quarterServiceMock },
        { provide: UserService,
          useValue: userServiceMock },
        { provide: TeamStateService,
          useValue: teamStateServiceMock }
      ]
    });
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should be created', () => {
    expect(executeGuard)
      .toBeTruthy();
  });

  it('should redirect with default quarter and user teams when no query params exist (initial load)', async() => {
    const result = await executeGuard();

    const injectedTeamStateService = TestBed.inject(TeamStateService);
    const injectedRouter = TestBed.inject(Router);

    expect(injectedTeamStateService.loadTeams)
      .toHaveBeenCalledWith({ quarterId: mockCurrentQuarter.id });
    expect(injectedRouter.parseUrl)
      .toHaveBeenCalledWith('');

    expect(result)
      .toEqual({
        queryParams: {
          quarter: mockCurrentQuarter.id,
          teams: mockUserTeamIdsString
        }
      });
  });

  describe('Invalid or missing queryParams when user get redirected', () => {
    it('should return true when query params exactly math the stata', async() => {
      const result = await executeGuard({ quarter: mockCurrentQuarter.id.toString(),
        teams: mockUserTeamIdsString });

      expect(result)
        .toBe(true);
      expect(routerMock.parseUrl).not.toHaveBeenCalled();
    });

    it('should filter out invalid team ids', async() => {
      const result = await executeGuard({ quarter: mockCurrentQuarter.id.toString(),
        teams: '1,9999,-1,2,3' });

      expect(result)
        .toEqual({ queryParams: { quarter: 1,
          teams: '1,2,3' } });
    });

    it('should update missing teams', async() => {
      const result = await executeGuard({ quarter: mockCurrentQuarter.id.toString(),
        teams: '' });

      expect(result)
        .toEqual({ queryParams: { quarter: 1,
          teams: mockUserTeamIdsString } });
    });

    it('should update missing quarter', async() => {
      const result = await executeGuard({ quarter: '',
        teams: mockUserTeamIdsString });

      expect(result)
        .toEqual({ queryParams: { quarter: 1,
          teams: mockUserTeamIdsString } });
    });

    // todo at the moment there is a bug that the quarter is not being updated when it is invalid
    it('should update invalid quarter', async() => {
      const result = await executeGuard({ quarter: '2',
        teams: mockUserTeamIdsString });

      expect(result)
        .toEqual({ queryParams: { quarter: 1,
          teams: mockUserTeamIdsString } });
    });

    it('should allow empty teams when user is already on page', async() => {
      const result = await executeGuard({ quarter: mockCurrentQuarter.id.toString(),
        teams: '' }, '', true);

      expect(result)
        .toBe(true);
    });
  });
});
