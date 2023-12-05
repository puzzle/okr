import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';

import { ApplicationBannerComponent } from './application-banner.component';
import { By } from '@angular/platform-browser';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { PUZZLE_TOP_BAR_HEIGHT } from '../shared/constantLibary';

class ResizeObserverMock {
  observe() {}
  unobserve() {}
  disconnect() {}
}

const refreshDataServiceMock = {
  okrBannerHeightSubject: {
    next: jest.fn(),
  },
};

describe('ApplicationBannerComponent', () => {
  //@ts-ignore
  global.ResizeObserver = ResizeObserverMock;
  let component: ApplicationBannerComponent;
  let fixture: ComponentFixture<ApplicationBannerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ApplicationBannerComponent],
      providers: [{ provide: RefreshDataService, useValue: refreshDataServiceMock }],
    });

    fixture = TestBed.createComponent(ApplicationBannerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should should hide banner if scrolled down', fakeAsync(() => {
    //Set bannerHeight to default
    let bannerHeight: number = 160;
    //Scroll more than the height of the banner
    let scrollTop: number = 180;
    //Set lastScrollPosition to smaller than scrollTop => user scrolls down
    component.lastScrollPosition = 160;
    component.bannerHeight = bannerHeight;

    //Set banner style
    component.refreshBanner(scrollTop);
    tick(600);

    //Assert that banner is hidden was changed
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('#okrBanner')).attributes['style']).toContain(
      'top: -' + (PUZZLE_TOP_BAR_HEIGHT + bannerHeight),
    );
  }));

  it('should show banner if scrolled up', fakeAsync(() => {
    //Scroll more than the height of the banner
    let scrollTop: number = 180;
    //Set lastScrollPosition to bigger than scrollTop => user scrolls up
    component.lastScrollPosition = 200;

    //Set banner style
    component.refreshBanner(scrollTop);
    tick(600);

    //Assert that banner is visible
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('#okrBanner')).attributes['style']).toContain(
      'top: ' + PUZZLE_TOP_BAR_HEIGHT,
    );
  }));

  it('should call setOKRBannerStyle() when changing header appearance', () => {
    jest.spyOn(component, 'refreshBanner').mockReturnValue();

    //Set bannerHeight to default and execute header appearance change
    component.bannerHeight = 160;
    component.changeHeaderAppearance();

    //Assert that banner is visible
    fixture.detectChanges();
    expect(component.refreshBanner).toHaveBeenCalled();
  });

  it('should call correct method after call scroll()', () => {
    jest.spyOn(component, 'changeHeaderAppearance');

    component.scroll();

    expect(component.changeHeaderAppearance).toHaveBeenCalled();
  });
});
