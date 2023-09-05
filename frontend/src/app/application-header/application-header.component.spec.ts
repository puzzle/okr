import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';

import { ApplicationHeaderComponent } from './application-header.component';
import { By } from '@angular/platform-browser';

class ResizeObserverMock {
  observe() {}
  unobserve() {}
}

describe('ApplicationHeaderComponent', () => {
  //@ts-ignore
  global.ResizeObserver = ResizeObserverMock;
  let component: ApplicationHeaderComponent;
  let fixture: ComponentFixture<ApplicationHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ApplicationHeaderComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set banner position to fixed when scrolled more than the height of the banner', fakeAsync(() => {
    //Set bannerHeight bigger than the default banner
    let bannerHeight: number = 200;
    //Scroll more than the banner height
    let scrollTop: number = bannerHeight + 20;

    //Set banner style
    component.setOKRBannerStyle(bannerHeight, scrollTop);
    tick(200);

    //Assert that position was changed
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('#okrBanner')).attributes['style']).toContain('position: fixed');
  }));

  it('should set banner position to relative if user is at top of page', fakeAsync(() => {
    //Set bannerHeight to default height
    let bannerHeight: number = 160;
    //Set user to top of page
    let scrollTop: number = 0;

    //Set banner style
    component.setOKRBannerStyle(bannerHeight, scrollTop);
    tick(200);

    //Assert that position was changed
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('#okrBanner')).attributes['style']).toContain('position: relative');
  }));

  it('should should hide banner if scrolled down', fakeAsync(() => {
    //Set bannerHeight to default
    let bannerHeight: number = 160;
    //Scroll more than the height of the banner
    let scrollTop: number = 180;
    //Set lastScrollPosition to smaller than scrollTop => user scrolls down
    component.lastScrollPosition = 160;

    //Set banner style
    component.setOKRBannerStyle(bannerHeight, scrollTop);
    tick(200);

    //Assert that banner is hidden was changed
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('#okrBanner')).attributes['style']).toContain(
      'top: -' + (component.PUZZLE_TOP_BAR_HEIGHT + bannerHeight)
    );
  }));

  it('should show banner if scrolled up', fakeAsync(() => {
    //Set bannerHeight to default
    let bannerHeight: number = 160;
    //Scroll more than the height of the banner
    let scrollTop: number = 180;
    //Set lastScrollPosition to bigger than scrollTop => user scrolls up
    component.lastScrollPosition = 200;

    //Set banner style
    component.setOKRBannerStyle(bannerHeight, scrollTop);
    tick(200);

    //Assert that banner is visible
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('#okrBanner')).attributes['style']).toContain(
      'top: ' + component.PUZZLE_TOP_BAR_HEIGHT
    );
  }));
});
