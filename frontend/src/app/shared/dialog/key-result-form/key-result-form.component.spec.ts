import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { KeyresultService } from '../../services/keyresult.service';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatInputModule } from '@angular/material/input';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatIconModule } from '@angular/material/icon';
import { keyResultOrdinal, testUser, users } from '../../testData';
import { State } from '../../types/enums/State';
import { Observable, of } from 'rxjs';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { KeyResultObjective } from '../../types/model/KeyResultObjective';
import { User } from '../../types/model/User';
import { DialogHeaderComponent } from '../../custom/dialog-header/dialog-header.component';
import { OAuthService } from 'angular-oauth2-oidc';
import { KeyresultTypeComponent } from '../../../keyresult-type/keyresult-type.component';
import { ActionPlanComponent } from '../../../action-plan/action-plan.component';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { UserService } from '../../services/user.service';
import { KeyResultFormComponent } from '../key-result-form/key-result-form.component';
import { Action } from '../../types/model/Action';
import { KeyResultMetric } from '../../types/model/KeyResultMetric';
import { KeyResultOrdinal } from '../../types/model/KeyResultOrdinal';

describe('KeyResultFormComponent', () => {
  let component: KeyResultFormComponent;
  let fixture: ComponentFixture<KeyResultFormComponent>;

  const oauthMockService = {
    getIdentityClaims() {
      return { name: users[1].firstname + ' ' + users[1].lastname };
    },
  };

  const userService = {
    getUsers() {
      return of(users);
    },
  };

  const matDialogRefMock = {
    close: jest.fn(),
  };

  const mockUserService = {
    getUsers: jest.fn(),
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
    keyResultType: 'metric',
  };

  const keyResultObjective: KeyResultObjective = {
    id: 2,
    state: State.ONGOING,
    quarter: { id: 1, label: 'GJ 22/23-Q2', endDate: new Date(), startDate: new Date() },
  };

  const keyResultFormGroup = new FormGroup({
    title: new FormControl<string>('', [Validators.required, Validators.minLength(2), Validators.maxLength(250)]),
    description: new FormControl<string>('', [Validators.maxLength(4096)]),
    owner: new FormControl<User | string | null>(null, [Validators.required, Validators.nullValidator]),
    actionList: new FormControl<Action[]>([]),
    unit: new FormControl<string | null>(null),
    baseline: new FormControl<number | null>(null),
    stretchGoal: new FormControl<number | null>(null),
    commitZone: new FormControl<string | null>(null),
    targetZone: new FormControl<string | null>(null),
    stretchZone: new FormControl<string | null>(null),
    keyResultType: new FormControl<string>('metric'),
  });

  describe('New KeyResult', () => {
    beforeEach(() => {
      mockUserService.getUsers.mockReturnValue(users);
      TestBed.configureTestingModule({
        imports: [
          HttpClientTestingModule,
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
        ],
        providers: [
          KeyresultService,
          TranslateService,
          { provide: UserService, useValue: userService },
          {
            provide: MatDialogRef,
            useValue: matDialogRefMock,
          },
          {
            provide: OAuthService,
            useValue: oauthMockService,
          },
        ],
        declarations: [KeyResultFormComponent, DialogHeaderComponent, KeyresultTypeComponent, ActionPlanComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyResultFormComponent);
      component = fixture.componentInstance;
      component.keyResultForm = keyResultFormGroup;
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should have logged in user as owner', waitForAsync(async () => {
      const userServiceSpy = jest.spyOn(userService, 'getUsers');
      component.keyResultForm.setValue(keyResultForm);
      component.ngOnInit();
      fixture.detectChanges();

      const formObject = component.keyResultForm.value;
      expect(formObject.title).toBe('Title');
      expect(formObject.description).toBe(null);
      expect(userServiceSpy).toHaveBeenCalled();
      expect(component.keyResultForm.controls['owner'].value).toBe(users[1]);
      expect(component.keyResultForm.invalid).toBeFalsy();
    }));

    it('should return right filtered user', () => {
      let userObservable: Observable<User[]> = component.filter('baum');

      userObservable.subscribe((userList) => {
        expect(userList.length).toEqual(1);
      });
      userObservable = component.filter('ob');

      userObservable.subscribe((userList) => {
        expect(userList.length).toEqual(2);
      });
    });

    it('should return label from user', () => {
      let userName: string = component.getUserNameById(testUser);
      expect(userName).toEqual('Bob Baumeister');
    });

    it('should set metric values', () => {
      let fullKeyResultMetric: KeyResultMetric = {
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
        writeable: true,
      };
      component.setMetricValuesInForm(fullKeyResultMetric);

      expect(component.keyResultForm.controls['baseline'].value).toEqual(3);
      expect(component.keyResultForm.controls['stretchGoal'].value).toEqual(25);
      expect(component.keyResultForm.controls['unit'].value).toEqual('CHF');
    });

    it('should set ordinal values', () => {
      let fullKeyResultOrdinal: KeyResultOrdinal = {
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
        writeable: true,
      };
      component.setOrdinalValuesInForm(fullKeyResultOrdinal);

      expect(component.keyResultForm.controls['commitZone'].value).toEqual('Eine Kuh');
      expect(component.keyResultForm.controls['targetZone'].value).toEqual('Ein Schaf');
      expect(component.keyResultForm.controls['stretchZone'].value).toEqual('Eine Ziege');
    });

    it('should get metric value right', () => {
      expect(component.isMetricKeyResult()).toBeTruthy();
      component.keyResultForm.patchValue({ keyResultType: 'ordinal' });
      expect(component.isMetricKeyResult()).toBeFalsy();
    });

    it('should get username from user right', () => {
      let user = users[0];
      expect(component.getUserNameById(user)).toEqual('Bob Baumeister');
      expect(component.getUserNameById(null!)).toEqual('');
    });

    it('should get keyresult id right', () => {
      expect(component.getKeyResultId()).toEqual(null);
      component.keyResult = keyResultOrdinal;
      expect(component.getKeyResultId()).toEqual(101);
    });

    it('should get username from oauthService  right', () => {
      expect(component.getUserName()).toEqual('Paco Egiman');
    });
  });
});
