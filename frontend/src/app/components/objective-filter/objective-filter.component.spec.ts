import { ComponentFixture, fakeAsync, TestBed, tick, flushMicrotasks } from '@angular/core/testing';
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
import { authGuard } from '../../guards/auth.guard';
import { OverviewComponent } from '../overview/overview.component';
import { OAuthService } from 'angular-oauth2-oidc';
import { BehaviorSubject } from 'rxjs';

describe('ObjectiveFilterComponent', () => {
  let component: ObjectiveFilterComponent;
  let fixture: ComponentFixture<ObjectiveFilterComponent>;
  let loader: HarnessLoader;
  let router: Router;
  let mockActivatedRoute: { queryParams: BehaviorSubject<any> };

  const authGuardMock = () => {
    return Promise.resolve(true);
  };

  const oAuthServiceMock = {
    hasValidIdToken: jest.fn()
  };

  beforeEach(() => {
    mockActivatedRoute = {
      queryParams: new BehaviorSubject<any>({})
    };

    TestBed.configureTestingModule({
      declarations: [ObjectiveFilterComponent,
        OverviewComponent],
      providers: [{ provide: authGuard,
        useValue: authGuardMock },
      { provide: OAuthService,
        useValue: oAuthServiceMock },
      { provide: ActivatedRoute,
        useValue: mockActivatedRoute }],
      imports: [
        HttpClientTestingModule,
        AppRoutingModule,
        MatFormFieldModule,
        MatIconModule,
        FormsModule,
        MatInputModule,
        NoopAnimationsModule
      ]
    });

    fixture = TestBed.createComponent(ObjectiveFilterComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
    router = TestBed.inject(Router);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should route correctly', fakeAsync(() => {
    let search!: MatInputHarness;

    loader.getHarness(MatInputHarness)
      .then((h) => search = h);
    flushMicrotasks();

    jest.spyOn(router, 'navigate')
      .mockResolvedValue(true);
    jest.spyOn(component, 'updateUrl');

    search.setValue('this is a test');
    flushMicrotasks();

    fixture.detectChanges();
    component.refresh.next();

    tick(200);
    expect(component.updateUrl)
      .toHaveBeenCalledTimes(0);

    tick(200);
    expect(router.navigate)
      .toHaveBeenCalledWith([], { queryParams: { objectiveQuery: 'this is a test' } });
  }));

  it('should read from query correctly', () => {
    mockActivatedRoute.queryParams.next({ objectiveQuery: 'this is a test' });
    fixture.detectChanges();

    expect(component.query)
      .toBe('this is a test');
  });
});
