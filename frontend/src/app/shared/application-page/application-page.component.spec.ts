import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationPageComponent } from './application-page.component';
import { provideRouter, RouterModule } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { BehaviorSubject, Subject } from 'rxjs';
import { RefreshDataService } from '../../services/refresh-data.service';

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
});
