import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';

import { ApplicationBannerComponent } from './application-banner.component';
import { By } from '@angular/platform-browser';
import { RefreshDataService } from '../../services/refresh-data.service';
import { PUZZLE_TOP_BAR_HEIGHT } from '../../shared/constantLibary';
import { TeamFilterComponent } from '../team-filter/team-filter.component';
import { QuarterFilterComponent } from '../quarter-filter/quarter-filter.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { ObjectiveFilterComponent } from '../objective-filter/objective-filter.component';
import { of } from 'rxjs';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatChipsModule } from '@angular/material/chips';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { OkrTangramComponent } from '../../shared/custom/okr-tangram/okr-tangram.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';

class ResizeObserverMock {
  observe() {}

  unobserve() {}

  disconnect() {}
}

const refreshDataServiceMock = {
  okrBannerHeightSubject: {
    next: jest.fn()
  },
  reloadOverviewSubject: of(null)
};

const routeMock = {
  queryParams: of(null)
};

describe('ApplicationBannerComponent', () => {
  // @ts-ignore
  global.ResizeObserver = ResizeObserverMock;
  let component: ApplicationBannerComponent;
  let fixture: ComponentFixture<ApplicationBannerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        MatExpansionModule,
        MatFormFieldModule,
        MatChipsModule,
        MatSelectModule,
        MatIconModule,
        NoopAnimationsModule,
        FormsModule,
        ReactiveFormsModule,
        MatInputModule
      ],
      declarations: [
        ApplicationBannerComponent,
        TeamFilterComponent,
        QuarterFilterComponent,
        ObjectiveFilterComponent,
        OkrTangramComponent
      ],
      providers: [{ provide: RefreshDataService,
        useValue: refreshDataServiceMock },
      { provide: ActivatedRoute,
        useValue: routeMock }]
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
