import { ComponentFixture, TestBed } from '@angular/core/testing';
import { KeyResultService } from '../../services/key-result.service';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { MatIconModule } from '@angular/material/icon';
import { testUser, UNIT_CHF, users } from '../../shared/test-data';
import { State } from '../../shared/types/enums/state';
import { Observable, of } from 'rxjs';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { KeyResultObjective } from '../../shared/types/model/key-result-objective';
import { User } from '../../shared/types/model/user';
import { OAuthService } from 'angular-oauth2-oidc';
import { KeyResultTypeComponent } from '../key-result-type/key-result-type.component';
import { ActionPlanComponent } from '../action-plan/action-plan.component';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { UserService } from '../../services/user.service';
import { KeyResultFormComponent } from './key-result-form.component';
import { KeyResultMetric } from '../../shared/types/model/key-result-metric';
import { KeyResultOrdinal } from '../../shared/types/model/key-result-ordinal';
import { TranslateTestingModule } from 'ngx-translate-testing';
// @ts-ignore
import * as de from '../../../assets/i18n/de.json';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { DialogTemplateCoreComponent } from '../../shared/custom/dialog-template-core/dialog-template-core.component';
import { Quarter } from '../../shared/types/model/quarter';
import { getKeyResultForm } from '../../shared/constant-library';
import { getValueOfForm } from '../../shared/common';
import { ErrorComponent } from '../../shared/custom/error/error.component';

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

  const keyResultObjective: KeyResultObjective = {
    id: 2,
    state: State.ONGOING,
    quarter: new Quarter(
      1, 'GJ 22/23-Q2', new Date(), new Date(), false
    )
  };

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
          KeyResultService,
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
          ActionPlanComponent,
          ErrorComponent
        ]
      })
        .compileComponents();

      fixture = TestBed.createComponent(KeyResultFormComponent);
      component = fixture.componentInstance;
      component.keyResultForm = getKeyResultForm();
      userService.getCurrentUser.mockReturnValue(testUser);
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component)
        .toBeTruthy();
    });


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
        actionList: [],
        stretchGoal: 25,
        unit: UNIT_CHF,
        createdOn: new Date(),
        modifiedOn: new Date(),
        isWriteable: true
      };
      component.setMetricValuesInForm(fullKeyResultMetric);

      expect(getValueOfForm(component.keyResultForm, ['metric',
        'baseline']))
        .toEqual(3);

      expect(getValueOfForm(component.keyResultForm, ['metric',
        'stretchGoal']))
        .toEqual(25);

      expect(getValueOfForm(component.keyResultForm, ['metric',
        'unit']))
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
        actionList: [],
        targetZone: 'Ein Schaf',
        stretchZone: 'Eine Ziege',
        createdOn: new Date(),
        modifiedOn: new Date(),
        isWriteable: true
      };
      component.setOrdinalValuesInForm(fullKeyResultOrdinal);

      expect(getValueOfForm(component.keyResultForm, ['ordinal',
        'commitZone']))
        .toEqual('Eine Kuh');
      expect(getValueOfForm(component.keyResultForm, ['ordinal',
        'targetZone']))
        .toEqual('Ein Schaf');
      expect(getValueOfForm(component.keyResultForm, ['ordinal',
        'stretchZone']))
        .toEqual('Eine Ziege');
    });

    it('should correctly check if key-result is metric or ordinal', () => {
      expect(component.isMetricKeyResult())
        .toBeTruthy();
      component.keyResultForm.patchValue({ keyResultType: 'ordinal' });
      expect(component.isMetricKeyResult())
        .toBeFalsy();
    });

    it('should get correct username from o-auth-service', () => {
      expect(component.getFullNameOfLoggedInUser())
        .toEqual(testUser.firstName + ' ' + testUser.lastName);
    });
  });
});
