import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';

import { ObjectiveFilterComponent } from './objective-filter.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AppRoutingModule } from '../../app-routing.module';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatInputHarness } from '@angular/material/input/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { authGuard } from '../../guards/auth.guard';
import { OverviewComponent } from '../overview/overview.component';
import {
  AbstractLoggerService,
  AuthModule,
  AutoLoginPartialRoutesGuard,
  StsConfigLoader,
} from 'angular-auth-oidc-client';
import { BehaviorSubject, of } from 'rxjs';
import { UserService } from '../../services/user.service';
import { testUser } from '../../shared/testData';
import { NgZone } from '@angular/core';
import { ApplicationTopBarComponent } from '../application-top-bar/application-top-bar.component';

describe('ObjectiveFilterComponent', () => {
  let component: ObjectiveFilterComponent;
  let fixture: ComponentFixture<ObjectiveFilterComponent>;
  let loader: HarnessLoader;
  let router: Router;
  let ngZone: NgZone;
  let queryParamsSubject: BehaviorSubject<any>;

  const userServiceMock = {
    getOrInitCurrentUser: jest.fn(),
  };

  beforeEach(() => {
    queryParamsSubject = new BehaviorSubject<any>({});

    TestBed.configureTestingModule({
      declarations: [ObjectiveFilterComponent, OverviewComponent, ApplicationTopBarComponent],
      providers: [
        {
          provide: StsConfigLoader,
          useValue: {
            loadConfig: () => of({}),
            loadConfigs: () => of([{}]),
          },
        },
        {
          provide: AbstractLoggerService,
          useValue: {
            logError: () => of({}),
          },
        },
        {
          provide: AutoLoginPartialRoutesGuard,
          useValue: {
            canActivate: () => true, // or `false`, depending on what you want to simulate
          },
        },
        {
          provide: ActivatedRoute,
          useValue: {
            queryParams: queryParamsSubject.asObservable(),
          },
        },
      ],
      imports: [
        HttpClientTestingModule,
        AppRoutingModule,
        MatFormFieldModule,
        MatIconModule,
        FormsModule,
        MatInputModule,
        NoopAnimationsModule,
      ],
    });
    fixture = TestBed.createComponent(ObjectiveFilterComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
    router = TestBed.inject(Router);
    userServiceMock.getOrInitCurrentUser.mockReturnValue(of(testUser));
    ngZone = TestBed.inject(NgZone);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should route correctly', fakeAsync(() => {
    loader.getHarness(MatInputHarness).then((search) => {
      ngZone.run(() => {
        jest.spyOn(router, 'navigate');
        jest.spyOn(component, 'updateURL');
        search.setValue('this is a test');
        fixture.detectChanges();
        component.refresh.next();
        tick(200);
        expect(component.updateURL).toHaveBeenCalledTimes(0);
        tick(200);
        expect(router.navigate).toHaveBeenCalledWith([], { queryParams: { objectiveQuery: 'this is a test' } });
      });
    });
  }));

  it('should read from query  correctly', fakeAsync(() => {
    queryParamsSubject.next({ objectiveQuery: 'this is a test' });
    tick(500);
    fixture.detectChanges();
    expect(component.query).toBe('this is a test');
  }));
});
