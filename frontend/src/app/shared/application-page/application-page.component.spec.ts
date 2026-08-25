import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ApplicationPageComponent } from './application-page.component';
import { ActivatedRoute } from '@angular/router';
import { Observable, of } from 'rxjs';
import { ConfigService } from '../../services/config.service';
import { RefreshDataService } from '../../services/refresh-data.service';
import { getValueFromQuery } from '../common';

jest.mock('../common', () => ({
  getValueFromQuery: jest.fn(),
  isMobileDevice: jest.fn()
}));

describe('ApplicationPageComponent', () => {
  let component: ApplicationPageComponent;
  let fixture: ComponentFixture<ApplicationPageComponent>;

  let mockRefreshDataService: { okrBannerHeightSubject: Observable<number> };
  let mockConfigService: { config$: Observable<any> };
  let mockActivatedRoute: { queryParams: Observable<any> };

  beforeEach(async() => {
    mockRefreshDataService = { okrBannerHeightSubject: of(42) };
    mockConfigService = { config$: of({}) };
    mockActivatedRoute = { queryParams: of({}) };

    (getValueFromQuery as jest.Mock).mockReset();

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

    fixture = TestBed.createComponent(ApplicationPageComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should accept the isEmpty signal input', () => {
    fixture.componentRef.setInput('isEmpty', true);
    expect(component.isEmpty())
      .toBe(true);
  });

  describe('ngOnInit behavior', () => {
    it('should push new banner height to overviewPadding and detect changes', () => {
      mockRefreshDataService.okrBannerHeightSubject = of(42);

      const cdSpy = jest.spyOn((component as any).changeDetector, 'detectChanges');
      const paddingSpy = jest.spyOn(component.overviewPadding, 'next');

      fixture.detectChanges();

      expect(paddingSpy)
        .toHaveBeenCalledWith(42);
      expect(cdSpy)
        .toHaveBeenCalled();
    });

    it('should update backgroundLogoSrc$ when config enables triangles', () => {
      mockConfigService.config$ = of({ triangles: true,
        backgroundLogo: 'assets/custom.svg' });
      const logoSpy = jest.spyOn(component.backgroundLogoSrc$, 'next');

      fixture.detectChanges();

      expect(logoSpy)
        .toHaveBeenCalledWith('assets/custom.svg');
    });

    it('should NOT update backgroundLogoSrc$ when config disables triangles', () => {
      mockConfigService.config$ = of({ triangles: false,
        backgroundLogo: 'assets/custom.svg' });
      const logoSpy = jest.spyOn(component.backgroundLogoSrc$, 'next');

      fixture.detectChanges();

      expect(logoSpy).not.toHaveBeenCalled();
    });
  });
});
