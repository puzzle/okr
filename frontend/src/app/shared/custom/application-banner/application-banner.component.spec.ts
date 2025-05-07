import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';

import { ApplicationBannerComponent } from './application-banner.component';
import { By } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { of, Subject } from 'rxjs';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatChipsModule } from '@angular/material/chips';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TeamFilterComponent } from '../../filter/team-filter/team-filter.component';
import { QuarterFilterComponent } from '../../filter/quarter-filter/quarter-filter.component';
import { ObjectiveFilterComponent } from '../../../components/objective-filter/objective-filter.component';
import { RefreshDataService } from '../../../services/refresh-data.service';
import { PUZZLE_TOP_BAR_HEIGHT } from '../../constant-library';
import { NgOptimizedImage } from '@angular/common';
import { ConfigService } from '../../../services/config.service';


const refreshDataServiceMock = {
  okrBannerHeightSubject: {
    next: jest.fn()
  },
  reloadOverviewSubject: of(null)
};

const configServiceMock = {
  config$: new Subject<any>()
};

describe('ApplicationBannerComponent', () => {
  window.ResizeObserver =
      window.ResizeObserver ||
      jest.fn()
        .mockImplementation(() => ({
          disconnect: jest.fn(),
          observe: jest.fn(),
          unobserve: jest.fn()
        }));
  let component: ApplicationBannerComponent;
  let fixture: ComponentFixture<ApplicationBannerComponent>;

  beforeEach(() => {
    configServiceMock.config$.next({ triangles: '/assets/images/okr-add-objective-icon.svg' });
    TestBed.configureTestingModule({
      imports: [
        MatExpansionModule,
        MatFormFieldModule,
        MatChipsModule,
        MatSelectModule,
        MatIconModule,
        NoopAnimationsModule,
        FormsModule,
        ReactiveFormsModule,
        MatInputModule,
        NgOptimizedImage
      ],
      declarations: [
        ApplicationBannerComponent,
        TeamFilterComponent,
        QuarterFilterComponent,
        ObjectiveFilterComponent
      ],
      providers: [
        { provide: RefreshDataService,
          useValue: refreshDataServiceMock },
        { provide: ConfigService,
          useValue: configServiceMock },
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    fixture = TestBed.createComponent(ApplicationBannerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should hide banner if scrolled down', fakeAsync(() => {
    // Set bannerHeight to default
    const bannerHeight = 160;
    // Scroll more than the height of the banner
    const scrollTop = 180;
    // Set lastScrollPosition to smaller than scrollTop => user scrolls down
    component.lastScrollPosition = 160;
    component.bannerHeight = bannerHeight;

    // Set banner style
    component.refreshBanner(scrollTop);
    tick(600);

    // Assert that banner is hidden was changed
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('#okr-banner')).attributes['style'])
      .toContain('top: -' + (PUZZLE_TOP_BAR_HEIGHT + bannerHeight));
  }));

  it('should show banner if scrolled up', fakeAsync(() => {
    // Scroll more than the height of the banner
    const scrollTop = 180;
    // Set lastScrollPosition to bigger than scrollTop => user scrolls up
    component.lastScrollPosition = 200;

    // Set banner style
    component.refreshBanner(scrollTop);
    tick(600);

    // Assert that banner is visible
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('#okr-banner')).attributes['style'])
      .toContain('top: ' + PUZZLE_TOP_BAR_HEIGHT);
  }));

  it('should call refreshBanner() when changing header appearance', () => {
    jest.spyOn(component, 'refreshBanner')
      .mockReturnValue();

    // Set bannerHeight to default and execute header appearance change
    component.bannerHeight = 160;
    component.changeHeaderAppearance();

    // Assert that banner is visible
    fixture.detectChanges();
    expect(component.refreshBanner)
      .toHaveBeenCalled();
  });

  it('should call changeHeaderAppearance() after calling scroll()', () => {
    jest.spyOn(component, 'changeHeaderAppearance');

    component.scroll();

    expect(component.changeHeaderAppearance)
      .toHaveBeenCalled();
  });
});
