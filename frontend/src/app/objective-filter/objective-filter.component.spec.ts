import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';

import { ObjectiveFilterComponent } from './objective-filter.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AppRoutingModule } from '../app-routing.module';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatInputHarness } from '@angular/material/input/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { authGuard } from '../shared/guards/auth.guard';
import { OverviewComponent } from '../overview/overview.component';

describe('ObjectiveFilterComponent', () => {
  let component: ObjectiveFilterComponent;
  let fixture: ComponentFixture<ObjectiveFilterComponent>;
  let loader: HarnessLoader;
  let router: Router;

  const authGuardMock = () => {
    return Promise.resolve(true);
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ObjectiveFilterComponent, OverviewComponent],
      providers: [
        {
          provide: authGuard,
          useValue: authGuardMock,
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

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should route correctly', fakeAsync(() => {
    loader.getHarness(MatInputHarness).then((search) => {
      jest.spyOn(router, 'navigate');
      jest.spyOn(component, 'updateURL');
      search.setValue('this is a test');
      fixture.detectChanges();
      component.refresh.next();
      tick(200);
      expect(component.updateURL).toHaveBeenCalledTimes(0);
      tick(200);
      expect(router.navigate).toHaveBeenCalledWith([], { queryParams: { objectiveQuery: 'this is a test' } });
      expect(router.url).toBe('/?objectiveQuery=this%20is%20a%20test');
    });
  }));

  it('should read from query  correctly', fakeAsync(() => {
    const searchPromise = loader.getHarness(MatInputHarness);
    const routerPromise = RouterTestingHarness.create();

    Promise.all([searchPromise, routerPromise]).then(([search, router]: [MatInputHarness, RouterTestingHarness]) => {
      router.navigateByUrl('/?objectiveQuery=this%2520is%2520a%2520test');
      tick(500);
      fixture.detectChanges();
      expect(component.query).toBe('this is a test');
    });
  }));
});
