import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ApplicationPageComponent } from './application-page.component';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject, Subject } from 'rxjs';
import { ConfigService } from '../../services/config.service';
import { RefreshDataService } from '../../services/refresh-data.service';
import { getValueFromQuery, isMobileDevice } from '../common';

jest.mock('../common', () => ({
  getValueFromQuery: jest.fn(),
  isMobileDevice: jest.fn()
}));

describe('ApplicationPageComponent', () => {
  let component: ApplicationPageComponent;
  let fixture: ComponentFixture<ApplicationPageComponent>;

  let mockRefreshDataService: { okrBannerHeightSubject: Subject<number> };
  let mockConfigService: { config$: Subject<any> };
  let mockActivatedRoute: { queryParams: BehaviorSubject<any> };

  beforeEach(async() => {
    mockRefreshDataService = { okrBannerHeightSubject: new Subject<number>() };
    mockConfigService = { config$: new Subject<any>() };
    mockActivatedRoute = { queryParams: new BehaviorSubject<any>({}) };

    (getValueFromQuery as jest.Mock).mockReset();
    (isMobileDevice as jest.Mock).mockReset();

    await TestBed.configureTestingModule({
      declarations: [ApplicationPageComponent],
      providers: [{ provide: RefreshDataService,
        useValue: mockRefreshDataService },
      { provide: ConfigService,
        useValue: mockConfigService },
      { provide: ActivatedRoute,
        useValue: mockActivatedRoute }]
    })
      .compileComponents();
  });

  const setupComponent = () => {
    fixture = TestBed.createComponent(ApplicationPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  };

  it('should create', () => {
    setupComponent();
    expect(component)
      .toBeTruthy();
  });

  describe('Initialization (Constructor & Route Params)', () => {
    it('should set hasSelectedTeams to true if teams exist in query params', () => {
      (getValueFromQuery as jest.Mock).mockReturnValue(['team1',
        'team2']);
      mockActivatedRoute.queryParams.next({ teams: 'team1,team2' });

      setupComponent();

      expect(getValueFromQuery)
        .toHaveBeenCalledWith('team1,team2');
      expect(component.hasSelectedTeams)
        .toBe(true);
    });

    it('should set hasSelectedTeams to false if no teams are in query params', () => {
      (getValueFromQuery as jest.Mock).mockReturnValue([]);
      mockActivatedRoute.queryParams.next({ teams: '' });

      setupComponent();

      expect(component.hasSelectedTeams)
        .toBe(false);
    });
  });

  describe('ngOnInit behavior', () => {
    it('should push new banner height to overviewPadding and detect changes', () => {
      setupComponent();

      const cdSpy = jest.spyOn((component as any).changeDetector, 'detectChanges');
      const paddingSpy = jest.spyOn(component.overviewPadding, 'next');

      mockRefreshDataService.okrBannerHeightSubject.next(42);

      expect(paddingSpy)
        .toHaveBeenCalledWith(42);
      expect(cdSpy)
        .toHaveBeenCalled();
    });

    it('should update backgroundLogoSrc$ when config enables triangles', () => {
      setupComponent();
      const logoSpy = jest.spyOn(component.backgroundLogoSrc$, 'next');

      mockConfigService.config$.next({ triangles: true,
        backgroundLogo: 'assets/custom.svg' });

      expect(logoSpy)
        .toHaveBeenCalledWith('assets/custom.svg');
    });

    it('should NOT update backgroundLogoSrc$ when config disables triangles', () => {
      setupComponent();
      const logoSpy = jest.spyOn(component.backgroundLogoSrc$, 'next');

      mockConfigService.config$.next({ triangles: false,
        backgroundLogo: 'assets/custom.svg' });

      expect(logoSpy).not.toHaveBeenCalled();
    });
  });

  describe('DOM Manipulation', () => {
    let mockElement: HTMLElement;

    beforeEach(() => {
      mockElement = document.createElement('div');
      mockElement.id = 'overview';
      document.body.appendChild(mockElement);
    });

    afterEach(() => {
      document.body.removeChild(mockElement);
    });

    it('should add bottom-shadow-space class if NOT a mobile device', () => {
      (isMobileDevice as jest.Mock).mockReturnValue(false);

      setupComponent();

      expect(mockElement.classList.contains('bottom-shadow-space'))
        .toBe(true);
    });

    it('should NOT add bottom-shadow-space class if it IS a mobile device', () => {
      (isMobileDevice as jest.Mock).mockReturnValue(true);

      setupComponent();

      expect(mockElement.classList.contains('bottom-shadow-space'))
        .toBe(false);
    });
  });
});
