import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { KeyresultService } from '../../services/keyresult.service';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatInputModule } from '@angular/material/input';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { MatIconModule } from '@angular/material/icon';
import { keyResultOrdinal, testUser, users } from '../../shared/testData';
import { State } from '../../shared/types/enums/State';
import { Observable, of } from 'rxjs';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { KeyResultObjective } from '../../shared/types/model/KeyResultObjective';
import { User } from '../../shared/types/model/User';
import { OAuthService } from 'angular-oauth2-oidc';
import { KeyResultTypeComponent } from '../key-result-type/key-result-type.component';
import { ActionPlanComponent } from '../action-plan/action-plan.component';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { UserService } from '../../services/user.service';
import { KeyResultFormComponent } from './key-result-form.component';
import { Action } from '../../shared/types/model/Action';
import { KeyResultMetric } from '../../shared/types/model/KeyResultMetric';
import { KeyResultOrdinal } from '../../shared/types/model/KeyResultOrdinal';
import { TranslateTestingModule } from 'ngx-translate-testing';
// @ts-ignore
import * as de from '../../../assets/i18n/de.json';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { DialogTemplateCoreComponent } from '../../shared/custom/dialog-template-core/dialog-template-core.component';
import { Quarter } from '../../shared/types/model/Quarter';

describe('KeyResultFormComponent', () => {
  let component: KeyResultFormComponent;
  let fixture: ComponentFixture<KeyResultFormComponent>;

  const oAuthMockService = {
    getIdentityClaims() {
      return { name: users[1].firstName + ' ' + users[1].lastName };
    }
  };

  const userService = {
    getUsers() {
      return of(users);
    },
    getCurrentUser: jest.fn()
  };

  const matDialogRefMock = {
    close: jest.fn()
  };

  const mockUserService = {
    getUsers: jest.fn()
  };

  const keyResultForm = {
    owner: null,
    actionList: [],
    title: 'Title',
    baseline: 0,
    stretchZone: null,
    targetZone: null,
    commitZone: null,
    unit: 'FTE',
    description: null,
    stretchGoal: 0,
    keyResultType: 'metric'
  };

  const keyResultObjective: KeyResultObjective = {
    id: 2,
    state: State.ONGOING,
    quarter: new Quarter(
      1, 'GJ 22/23-Q2', new Date(), new Date()
    )
  };

  const keyResultFormGroup = new FormGroup({
    title: new FormControl<string>('', [Validators.required,
      Validators.minLength(2),
      Validators.maxLength(250)]),
    description: new FormControl<string>('', [Validators.maxLength(4096)]),
    owner: new FormControl<User | string | null>(null, [Validators.required,
      Validators.nullValidator]),
    actionList: new FormControl<Action[]>([]),
    unit: new FormControl<string | null>(null),
    baseline: new FormControl<number | null>(null),
    stretchGoal: new FormControl<number | null>(null),
    commitZone: new FormControl<string | null>(null),
    targetZone: new FormControl<string | null>(null),
    stretchZone: new FormControl<string | null>(null),
    keyResultType: new FormControl<string>('metric')
  });

  describe('New KeyResult', () => {
    beforeEach(() => {
      mockUserService.getUsers.mockReturnValue(users);
      TestBed.configureTestingModule({
        imports: [
          MatDialogModule,
          NoopAnimationsModule,
          MatSelectModule,
          MatInputModule,
          MatRadioModule,
          ReactiveFormsModule,
          MatAutocompleteModule,
          MatIconModule,
          TranslateModule.forRoot(),
          DragDropModule,
          TranslateTestingModule.withTranslations({
            de: de
          })
        ],
        providers: [
          KeyresultService,
          TranslateService,
          { provide: UserService,
            useValue: userService },
          {
            provide: MatDialogRef,
            useValue: matDialogRefMock
          },
          {
            provide: OAuthService,
            useValue: oAuthMockService
          },
          provideRouter([]),
          provideHttpClient(),
          provideHttpClientTesting()
        ],
        declarations: [
          KeyResultFormComponent,
          DialogTemplateCoreComponent,
          KeyResultTypeComponent,
          ActionPlanComponent
        ]
      })
        .compileComponents();

      fixture = TestBed.createComponent(KeyResultFormComponent);
      component = fixture.componentInstance;
      component.keyResultForm = keyResultFormGroup;
      userService.getCurrentUser.mockReturnValue(testUser);
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component)
        .toBeTruthy();
    });

    it('should have logged in user as owner', waitForAsync(async() => {
      const userServiceSpy = jest.spyOn(userService, 'getUsers');
      component.keyResultForm.setValue(keyResultForm);
      component.ngOnInit();
      fixture.detectChanges();

      const formObject = component.keyResultForm.value;
      expect(formObject.title)
        .toBe('Title');
      expect(formObject.description)
        .toBe(null);
      expect(userServiceSpy)
        .toHaveBeenCalled();
      expect(component.keyResultForm.controls['owner'].value)
        .toBe(testUser);
      expect(component.keyResultForm.invalid)
        .toBeFalsy();
    }));

    it('should return correct filtered user', () => {
      let userObservable: Observable<User[]> = component.filter('baum');

      userObservable.subscribe((userList) => {
        expect(userList.length)
          .toEqual(1);
      });
      userObservable = component.filter('ob');

      userObservable.subscribe((userList) => {
        expect(userList.length)
          .toEqual(2);
      });
    });

    it('should get full name of user', () => {
      const fullName: string = component.getFullNameOfUser(testUser);
      expect(fullName)
        .toEqual('Bob Baumeister');
    });

    it('should set metric values', () => {
      const fullKeyResultMetric: KeyResultMetric = {
        id: 3,
        version: 2,
        title: 'Der Titel ist hier',
        description: 'Die Beschreibung',
        owner: testUser,
        objective: keyResultObjective,
        baseline: 3,
        keyResultType: 'metric',
        lastCheckIn: null,
        actionList: null,
        stretchGoal: 25,
        unit: 'CHF',
        createdOn: new Date(),
        modifiedOn: new Date(),
        isWriteable: true
      };
      component.setMetricValuesInForm(fullKeyResultMetric);

      expect(component.keyResultForm.controls['baseline'].value)
        .toEqual(3);
      expect(component.keyResultForm.controls['stretchGoal'].value)
        .toEqual(25);
      expect(component.keyResultForm.controls['unit'].value)
        .toEqual('CHF');
    });

    it('should set ordinal values', () => {
      const fullKeyResultOrdinal: KeyResultOrdinal = {
        id: 3,
        version: 2,
        title: 'Der Titel ist hier',
        description: 'Die Beschreibung',
        owner: testUser,
        objective: keyResultObjective,
        commitZone: 'Eine Kuh',
        keyResultType: 'metric',
        lastCheckIn: null,
        actionList: null,
        targetZone: 'Ein Schaf',
        stretchZone: 'Eine Ziege',
        createdOn: new Date(),
        modifiedOn: new Date(),
        isWriteable: true
      };
      component.setOrdinalValuesInForm(fullKeyResultOrdinal);

      expect(component.keyResultForm.controls['commitZone'].value)
        .toEqual('Eine Kuh');
      expect(component.keyResultForm.controls['targetZone'].value)
        .toEqual('Ein Schaf');
      expect(component.keyResultForm.controls['stretchZone'].value)
        .toEqual('Eine Ziege');
    });

    it('should correctly check if key-result is metric or ordinal', () => {
      expect(component.isMetricKeyResult())
        .toBeTruthy();
      component.keyResultForm.patchValue({ keyResultType: 'ordinal' });
      expect(component.isMetricKeyResult())
        .toBeFalsy();
    });

    it('should get correct full name from user object', () => {
      const user = users[0];
      expect(component.getFullNameOfUser(user))
        .toEqual('Bob Baumeister');
      expect(component.getFullNameOfUser(null!))
        .toEqual('');
    });

    it('should get correct key-result id', () => {
      expect(component.getKeyResultId())
        .toEqual(null);
      component.keyResult = keyResultOrdinal;
      expect(component.getKeyResultId())
        .toEqual(101);
    });

    it('should get correct username from o-auth-service', () => {
      expect(component.getFullNameOfLoggedInUser())
        .toEqual(testUser.firstName + ' ' + testUser.lastName);
    });
  });
});
