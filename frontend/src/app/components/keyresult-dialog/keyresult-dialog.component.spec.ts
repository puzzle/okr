import { KeyresultDialogComponent } from './keyresult-dialog.component';
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { KeyresultService } from '../../services/keyresult.service';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { By } from '@angular/platform-browser';
import { testUser, users } from '../../shared/testData';
import { State } from '../../shared/types/enums/State';
import { KeyResult } from '../../shared/types/model/KeyResult';
import { of } from 'rxjs';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { KeyResultObjective } from '../../shared/types/model/KeyResultObjective';
import { OAuthService } from 'angular-oauth2-oidc';
import { KeyresultTypeComponent } from '../keyresult-type/keyresult-type.component';
import { ActionPlanComponent } from '../action-plan/action-plan.component';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { UserService } from '../../services/user.service';
import { KeyResultFormComponent } from '../key-result-form/key-result-form.component';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { MatDividerModule } from '@angular/material/divider';
import { DialogTemplateCoreComponent } from '../../shared/custom/dialog-template-core/dialog-template-core.component';
import { Quarter } from '../../shared/types/model/Quarter';

describe('KeyresultDialogComponent', () => {
  let component: KeyresultDialogComponent;
  let fixture: ComponentFixture<KeyresultDialogComponent>;
  let keyResultService: KeyresultService;

  const oauthMockService = {
    getIdentityClaims() {
      return { name: users[1].firstname + ' ' + users[1].lastname };
    },
  };

  const userService = {
    getUsers() {
      return of(users);
    },
    getCurrentUser: jest.fn(),
  };

  const fullObjective = {
    id: 1,
    title: 'Das ist ein Objective',
    description: 'Das ist die Beschreibung',
    state: State.ONGOING,
    team: { id: 1, name: 'Das Puzzle Team' },
    quarter: { id: 1, label: 'GJ 22/23-Q2' },
  };

  const keyResultObjective: KeyResultObjective = {
    id: 2,
    state: State.ONGOING,
    quarter: new Quarter(1, 'GJ 22/23-Q2', new Date(), new Date()),
  };

  const fullKeyResultMetric = {
    id: 3,
    version: 2,
    actionList: [
      {
        id: 1,
        action: 'Test',
        isChecked: false,
        keyResultId: 3,
        priority: 0,
      },
      {
        id: 2,
        action: 'Katze',
        isChecked: false,
        keyResultId: 3,
        priority: 1,
      },
      {
        id: 3,
        action: 'Hund',
        isChecked: true,
        keyResultId: 3,
        priority: 2,
      },
    ],
    title: 'Der Titel ist hier',
    description: 'Die Beschreibung',
    owner: testUser,
    objective: keyResultObjective,
    baseline: 3,
    keyResultType: 'metric',
    stretchGoal: 25,
    unit: 'CHF',
  };

  const receivedKeyResultMetric = {
    id: 3,
    version: 2,
    actionList: [
      {
        id: 1,
        action: 'Test',
        isChecked: false,
        keyResultId: 3,
        priority: 0,
      },
      {
        id: 2,
        action: 'Katze',
        isChecked: false,
        keyResultId: 3,
        priority: 1,
      },
      {
        id: 3,
        action: 'Hund',
        isChecked: true,
        keyResultId: 3,
        priority: 2,
      },
    ],
    title: 'Der Titel ist hier',
    description: 'Die Beschreibung',
    owner: testUser,
    objective: keyResultObjective,
    baseline: 3,
    keyResultType: 'metric',
    stretchGoal: 25,
    unit: 'CHF',
    commitZone: null,
    targetZone: null,
    stretchZone: null,
  };

  const fullKeyResultOrdinal = {
    id: 6,
    version: 2,
    actionList: [
      {
        id: 1,
        action: 'Test',
        isChecked: false,
        keyResultId: 3,
        priority: 0,
      },
      {
        id: 2,
        action: 'Katze',
        isChecked: false,
        keyResultId: 3,
        priority: 1,
      },
      {
        id: 3,
        action: 'Hund',
        isChecked: true,
        keyResultId: 3,
        priority: 2,
      },
    ],
    title: 'Der Titel ist hier',
    description: 'Die Beschreibung',
    owner: testUser,
    objective: keyResultObjective,
    keyResultType: 'ordinal',
    commitZone: 'Commit zone',
    targetZone: 'Target zone',
    stretchZone: 'Stretch goal',
  };

  const receivedKeyResultOrdinal = {
    id: 6,
    version: 2,
    actionList: [
      {
        id: 1,
        action: 'Test',
        isChecked: false,
        keyResultId: 3,
        priority: 0,
      },
      {
        id: 2,
        action: 'Katze',
        isChecked: false,
        keyResultId: 3,
        priority: 1,
      },
      {
        id: 3,
        action: 'Hund',
        isChecked: true,
        keyResultId: 3,
        priority: 2,
      },
    ],
    title: 'Der Titel ist hier',
    description: 'Die Beschreibung',
    owner: testUser,
    objective: keyResultObjective,
    keyResultType: 'ordinal',
    commitZone: 'Commit zone',
    targetZone: 'Target zone',
    stretchZone: 'Stretch goal',
    baseline: null,
    stretchGoal: null,
    unit: null,
  };

  const initKeyResult = {
    id: undefined,
    title: '',
    description: '',
    owner: testUser,
    objective: fullObjective,
    baseline: 3,
    keyResultType: 'metric',
    stretchGoal: 25,
    unit: 'CHF',
    commitZone: null,
    targetZone: null,
    stretchZone: null,
    actionList: [],
  };

  const savedKeyResult = {
    id: undefined,
    version: undefined,
    title: 'Neuer Titel',
    description: 'Description',
    owner: testUser,
    objective: fullObjective,
    baseline: 3,
    keyResultType: 'metric',
    stretchGoal: 25,
    unit: 'CHF',
    commitZone: null,
    targetZone: null,
    stretchZone: null,
    actionList: [],
  };

  const matDialogRefMock = {
    close: jest.fn(),
  };

  const mockUserService = {
    getUsers: jest.fn(),
  };

  describe('New KeyResult', () => {
    beforeEach(() => {
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
          MatDividerModule,
        ],
        providers: [
          provideRouter([]),
          provideHttpClient(),
          provideHttpClientTesting(),
          KeyresultService,
          TranslateService,
          { provide: UserService, useValue: userService },
          {
            provide: MatDialogRef,
            useValue: matDialogRefMock,
          },
          {
            provide: MAT_DIALOG_DATA,
            useValue: { objective: fullObjective, keyResult: undefined },
          },
          {
            provide: OAuthService,
            useValue: oauthMockService,
          },
        ],
        declarations: [
          KeyresultDialogComponent,
          KeyResultFormComponent,
          KeyresultTypeComponent,
          ActionPlanComponent,
          DialogTemplateCoreComponent,
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyresultDialogComponent);
      component = fixture.componentInstance;

      userService.getCurrentUser.mockReturnValue(testUser);

      fixture.detectChanges();
      keyResultService = TestBed.inject(KeyresultService);
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should be able to set title', waitForAsync(async () => {
      component.keyResultForm.setValue({
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
      });
      fixture.detectChanges();
      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(await submitButton.nativeElement.getAttribute('disabled')).toBeFalsy();

      const formObject = component.keyResultForm.value;
      expect(formObject.title).toBe('Title');
      expect(formObject.description).toBe(null);
    }));

    it('should display error message of too short input', waitForAsync(async () => {
      component.keyResultForm.setValue({
        owner: testUser,
        actionList: [],
        title: 'T',
        baseline: null,
        stretchZone: null,
        targetZone: null,
        commitZone: null,
        unit: null,
        description: '',
        stretchGoal: null,
        keyResultType: null,
      });
      fixture.detectChanges();

      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(await submitButton.nativeElement.getAttribute('disabled')).toEqual('');
      expect(component.keyResultForm.invalid).toBeTruthy();
      expect(component.keyResultForm.get('title')!.errors?.['minlength']).toBeTruthy();
    }));

    it('should display error message of required', waitForAsync(async () => {
      component.keyResultForm.setValue({
        owner: testUser,
        actionList: [],
        title: null,
        baseline: null,
        stretchZone: null,
        targetZone: null,
        commitZone: null,
        unit: null,
        description: '',
        stretchGoal: null,
        keyResultType: null,
      });
      fixture.detectChanges();

      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(await submitButton.nativeElement.getAttribute('disabled')).toEqual('');
      expect(component.keyResultForm.invalid).toBeTruthy();
      expect(component.keyResultForm.get('title')!.errors?.['required']).toBeTruthy();
    }));

    it('should call service save method', waitForAsync(() => {
      const spy = jest.spyOn(keyResultService, 'saveKeyResult');
      spy.mockImplementation(() => of({ id: 2 } as KeyResult));

      component.keyResultForm.setValue({
        owner: testUser,
        actionList: [],
        title: 'Neuer Titel',
        baseline: 3,
        stretchZone: null,
        targetZone: null,
        commitZone: null,
        unit: 'CHF',
        description: 'Description',
        stretchGoal: 25,
        keyResultType: 'metric',
      });

      initKeyResult.title = 'Neuer Titel';
      initKeyResult.description = 'Description';

      component.saveKeyResult();

      expect(spy).toBeCalledTimes(1);
      expect(spy).toHaveBeenCalledWith(savedKeyResult);
    }));
  });

  describe('Edit KeyResult Metric', () => {
    beforeEach(() => {
      mockUserService.getUsers.mockReturnValue(users);
      TestBed.configureTestingModule({
        imports: [
          MatDialogModule,
          NoopAnimationsModule,
          MatInputModule,
          ReactiveFormsModule,
          MatIconModule,
          MatAutocompleteModule,
          DragDropModule,
          TranslateModule.forRoot(),
          MatDividerModule,
        ],
        providers: [
          provideRouter([]),
          provideHttpClient(),
          provideHttpClientTesting(),
          KeyresultService,
          {
            provide: MatDialogRef,
            useValue: {
              close: () => {},
            },
          },
          {
            provide: OAuthService,
            useValue: oauthMockService,
          },
          {
            provide: MAT_DIALOG_DATA,
            useValue: { keyResult: fullKeyResultMetric, objective: keyResultObjective },
          },
        ],
        declarations: [
          KeyresultDialogComponent,
          KeyResultFormComponent,
          DialogTemplateCoreComponent,
          ActionPlanComponent,
          KeyresultTypeComponent,
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyresultDialogComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      keyResultService = TestBed.inject(KeyresultService);
      fullKeyResultMetric.id = 3;
    });

    afterEach(() => {
      mockUserService.getUsers.mockReset();
    });

    it('should use KeyResult value from data input', waitForAsync(() => {
      const formObject = fixture.componentInstance.keyResultForm.value;
      expect(formObject.title).toBe('Der Titel ist hier');
      expect(formObject.description).toBe('Die Beschreibung');
      expect(formObject.owner).toBe(testUser);
    }));

    it('should be able to set title and description', waitForAsync(async () => {
      expect(component.keyResultForm.value.title).toEqual('Der Titel ist hier');
      expect(component.keyResultForm.value.description).toEqual('Die Beschreibung');

      component.keyResultForm.setValue({
        owner: testUser,
        actionList: [],
        title: 'Title',
        baseline: 0,
        stretchZone: '',
        targetZone: '',
        commitZone: '',
        unit: 'FTE',
        description: 'Description',
        stretchGoal: 0,
        keyResultType: 'metric',
      });
      fixture.detectChanges();
      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(await submitButton.nativeElement.getAttribute('disabled')).toBeFalsy();

      const formObject = fixture.componentInstance.keyResultForm.value;
      expect(formObject.title).toBe('Title');
      expect(formObject.description).toBe('Description');
      expect(component.keyResultForm.invalid).toBeFalsy();
    }));

    it('should display error message of too short input', waitForAsync(async () => {
      component.keyResultForm.setValue({
        owner: testUser,
        actionList: [],
        title: 'T',
        baseline: null,
        stretchZone: null,
        targetZone: null,
        commitZone: null,
        unit: null,
        description: '',
        stretchGoal: null,
        keyResultType: null,
      });
      fixture.detectChanges();

      expect(component.keyResultForm.invalid).toBeTruthy();
      expect(component.keyResultForm.get('title')!.errors?.['minlength']).toBeTruthy();
    }));

    it('should display error message of required', waitForAsync(async () => {
      component.keyResultForm.setValue({
        owner: testUser,
        actionList: [],
        title: null,
        baseline: null,
        stretchZone: null,
        targetZone: null,
        commitZone: null,
        unit: null,
        description: '',
        stretchGoal: null,
        keyResultType: null,
      });
      fixture.detectChanges();

      expect(component.keyResultForm.invalid).toBeTruthy();
      expect(component.keyResultForm.get('title')!.errors?.['required']).toBeTruthy();
    }));

    it('should call service save method', waitForAsync(() => {
      const spy = jest.spyOn(keyResultService, 'saveKeyResult');
      spy.mockImplementation(() => of({ id: 2 } as KeyResult));

      component.saveKeyResult();

      expect(spy).toBeCalledTimes(1);
      expect(spy).toHaveBeenCalledWith(receivedKeyResultMetric);
    }));

    it('should not display logged in user when editing', waitForAsync(() => {
      jest.resetAllMocks();
      const userServiceSpy = jest.spyOn(userService, 'getUsers');
      fixture.detectChanges();
      expect(userServiceSpy).toHaveBeenCalledTimes(0);
      expect(component.keyResultForm.controls.owner.value).toBe(testUser);
    }));
  });

  describe('Edit KeyResult Ordinal', () => {
    beforeEach(() => {
      mockUserService.getUsers.mockReturnValue(users);
      TestBed.configureTestingModule({
        imports: [
          MatDialogModule,
          NoopAnimationsModule,
          MatInputModule,
          ReactiveFormsModule,
          MatIconModule,
          MatAutocompleteModule,
          DragDropModule,
          TranslateModule.forRoot(),
          MatDividerModule,
        ],
        providers: [
          provideRouter([]),
          provideHttpClient(),
          provideHttpClientTesting(),
          KeyresultService,
          {
            provide: MatDialogRef,
            useValue: matDialogRefMock,
          },
          {
            provide: OAuthService,
            useValue: oauthMockService,
          },
          {
            provide: MAT_DIALOG_DATA,
            useValue: { keyResult: fullKeyResultOrdinal, objective: keyResultObjective },
          },
        ],
        declarations: [
          KeyresultDialogComponent,
          KeyResultFormComponent,
          DialogTemplateCoreComponent,
          ActionPlanComponent,
          KeyresultTypeComponent,
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyresultDialogComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      keyResultService = TestBed.inject(KeyresultService);
    });

    afterEach(() => {
      mockUserService.getUsers.mockReset();
    });

    it('should use KeyResult value from data input', waitForAsync(() => {
      const formObject = fixture.componentInstance.keyResultForm.value;
      expect(formObject.title).toBe('Der Titel ist hier');
      expect(formObject.description).toBe('Die Beschreibung');
      expect(formObject.owner).toBe(testUser);
    }));

    it('should be able to set title and description', waitForAsync(async () => {
      expect(component.keyResultForm.value.title).toEqual('Der Titel ist hier');
      expect(component.keyResultForm.value.description).toEqual('Die Beschreibung');

      component.keyResultForm.setValue({
        owner: testUser,
        actionList: [],
        title: 'Title',
        baseline: 0,
        stretchZone: 'stretchZone',
        targetZone: 'targetZone',
        commitZone: 'commitZone',
        unit: 'FTE',
        description: 'Description',
        stretchGoal: 0,
        keyResultType: 'ordinal',
      });
      fixture.detectChanges();
      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(await submitButton.nativeElement.getAttribute('disabled')).toBeFalsy();

      const formObject = fixture.componentInstance.keyResultForm.value;
      expect(formObject.title).toBe('Title');
      expect(formObject.description).toBe('Description');
      expect(component.keyResultForm.invalid).toBeFalsy();
    }));

    it('should display error message of too short input', waitForAsync(async () => {
      component.keyResultForm.setValue({
        owner: testUser,
        actionList: [],
        title: 'T',
        baseline: 0,
        stretchZone: '',
        targetZone: '',
        commitZone: '',
        unit: 'FTE',
        description: 'Description',
        stretchGoal: 0,
        keyResultType: 'metric',
      });
      fixture.detectChanges();

      expect(component.keyResultForm.invalid).toBeTruthy();
      expect(component.keyResultForm.get('title')!.errors?.['minlength']).toBeTruthy();
    }));

    it('should display error message of required', waitForAsync(async () => {
      component.keyResultForm.setValue({
        owner: testUser,
        actionList: [],
        title: null,
        baseline: null,
        stretchZone: null,
        targetZone: null,
        commitZone: null,
        unit: null,
        description: '',
        stretchGoal: null,
        keyResultType: null,
      });
      fixture.detectChanges();

      expect(component.keyResultForm.invalid).toBeTruthy();
      expect(component.keyResultForm.get('title')!.errors?.['required']).toBeTruthy();
    }));

    it('should call service save method', waitForAsync(() => {
      const spy = jest.spyOn(keyResultService, 'saveKeyResult');
      spy.mockImplementation(() => of({ id: 2 } as KeyResult));

      component.saveKeyResult();

      expect(spy).toBeCalledTimes(1);
      expect(spy).toHaveBeenCalledWith(receivedKeyResultOrdinal);
    }));
  });
});
