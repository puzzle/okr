import { KeyResultDialogComponent } from './key-result-dialog.component';
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { KeyResultService } from '../../services/key-result.service';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { By } from '@angular/platform-browser';
import { testUser, users } from '../../shared/test-data';
import { State } from '../../shared/types/enums/state';
import { KeyResult } from '../../shared/types/model/key-result';
import { of } from 'rxjs';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { KeyResultObjective } from '../../shared/types/model/key-result-objective';
import { OAuthService } from 'angular-oauth2-oidc';
import { KeyResultTypeComponent } from '../key-result-type/key-result-type.component';
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
import { Quarter } from '../../shared/types/model/quarter';
import { TranslateTestingModule } from 'ngx-translate-testing';
// @ts-ignore
import * as de from '../../../assets/i18n/de.json';
import { getValueOfForm } from '../../shared/common';
import { ErrorComponent } from '../../shared/custom/error/error.component';


describe('KeyResultDialogComponent', () => {
  let component: KeyResultDialogComponent;
  let fixture: ComponentFixture<KeyResultDialogComponent>;
  let keyResultService: KeyResultService;

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

  const fullObjective = {
    id: 1,
    title: 'Das ist ein Objective',
    description: 'Das ist die Beschreibung',
    state: State.ONGOING,
    team: {
      id: 1,
      name: 'Das Puzzle Team'
    },
    quarter: {
      id: 1,
      label: 'GJ 22/23-Q2'
    }
  };

  const keyResultObjective: KeyResultObjective = {
    id: 2,
    state: State.ONGOING,
    quarter: new Quarter(
      1, 'GJ 22/23-Q2', new Date(), new Date(), false
    )
  };

  const fullKeyResultMetric = {
    id: 3,
    version: 2,
    actionList: [{
      id: 1,
      action: 'Test',
      isChecked: false,
      keyResultId: 3,
      priority: 0
    },
    {
      id: 2,
      action: 'Katze',
      isChecked: false,
      keyResultId: 3,
      priority: 1
    },
    {
      id: 3,
      action: 'Hund',
      isChecked: true,
      keyResultId: 3,
      priority: 2
    }],
    title: 'Der Titel ist hier',
    description: 'Die Beschreibung',
    owner: testUser,
    objective: keyResultObjective,
    baseline: 3,
    keyResultType: 'metric',
    stretchGoal: 25,
    unit: 'CHF'
  };

  const fullKeyResultOrdinal = {
    id: 6,
    version: 2,
    actionList: [{
      id: 1,
      action: 'Test',
      isChecked: false,
      keyResultId: 3,
      priority: 0
    },
    {
      id: 2,
      action: 'Katze',
      isChecked: false,
      keyResultId: 3,
      priority: 1
    },
    {
      id: 3,
      action: 'Hund',
      isChecked: true,
      keyResultId: 3,
      priority: 2
    }],
    title: 'Der Titel ist hier',
    description: 'Die Beschreibung',
    owner: testUser,
    objective: keyResultObjective,
    keyResultType: 'ordinal',
    commitZone: 'Commit zone',
    targetZone: 'Target zone',
    stretchZone: 'Stretch goal'
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
    actionList: []
  };

  const matDialogRefMock = {
    close: jest.fn()
  };

  const mockUserService = {
    getUsers: jest.fn(),
    getCurrentUser: jest.fn()
  };

  describe('New KeyResult', () => {
    beforeEach(() => {
      mockUserService.getCurrentUser.mockReturnValue(testUser);
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
          TranslateTestingModule.withTranslations({
            de: de
          })
        ],
        providers: [
          provideRouter([]),
          provideHttpClient(),
          provideHttpClientTesting(),
          KeyResultService,
          TranslateService,
          {
            provide: UserService,
            useValue: userService
          },
          {
            provide: MatDialogRef,
            useValue: matDialogRefMock
          },
          {
            provide: MAT_DIALOG_DATA,
            useValue: {
              objective: fullObjective,
              keyResult: undefined
            }
          },
          {
            provide: OAuthService,
            useValue: oAuthMockService
          }
        ],
        declarations: [
          KeyResultDialogComponent,
          KeyResultFormComponent,
          KeyResultTypeComponent,
          ActionPlanComponent,
          DialogTemplateCoreComponent,
          ErrorComponent
        ]
      })
        .compileComponents();

      fixture = TestBed.createComponent(KeyResultDialogComponent);
      component = fixture.componentInstance;

      userService.getCurrentUser.mockReturnValue(testUser);

      fixture.detectChanges();
      keyResultService = TestBed.inject(KeyResultService);
    });

    it('should create', () => {
      expect(component)
        .toBeTruthy();
    });

    it('should be able to set title', waitForAsync(async() => {
      component.keyResultForm.patchValue({
        owner: null,
        title: 'Title',
        description: null,
        keyResultType: 'metric',
        metric: {
          baseline: 0,
          targetGoal: 0,
          stretchGoal: 0,
          unit: 'FTE'
        },
        ordinal: {
          stretchZone: null,
          targetZone: null,
          commitZone: null
        }
      });
      fixture.detectChanges();
      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(await submitButton.nativeElement.getAttribute('disabled'))
        .toBeFalsy();

      const formObject = component.keyResultForm.value;
      expect(formObject.title)
        .toBe('Title');
      expect(formObject.description)
        .toBe(null);
    }));

    it('should display error message of too short input', waitForAsync(async() => {
      component.keyResultForm.patchValue({
        owner: testUser,
        title: 'T',
        description: 'f',
        keyResultType: 'metric',
        metric: {
          baseline: 0,
          targetGoal: 0,
          stretchGoal: 0,
          unit: 'FTE'
        },
        ordinal: {
          stretchZone: null,
          targetZone: null,
          commitZone: null
        }
      });
      fixture.detectChanges();

      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(await submitButton.nativeElement.getAttribute('disabled'))
        .toEqual('');
      expect(component.keyResultForm.invalid)
        .toBeTruthy();
      expect(component.keyResultForm.get('title')!.errors?.['minlength'])
        .toBeTruthy();
    }));

    it('should display error message of required', waitForAsync(async() => {
      component.keyResultForm.setValue({
        owner: testUser,
        actionList: [],
        title: null,
        description: 'f',
        keyResultType: 'metric',
        metric: {
          baseline: 0,
          targetGoal: 0,
          stretchGoal: 0,
          unit: 'FTE'
        },
        ordinal: {
          stretchZone: null,
          targetZone: null,
          commitZone: null
        }
      });
      fixture.detectChanges();

      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(await submitButton.nativeElement.getAttribute('disabled'))
        .toEqual('');
      expect(component.keyResultForm.invalid)
        .toBeTruthy();
      expect(component.keyResultForm.get('title')!.errors?.['required'])
        .toBeTruthy();
    }));

    it('should call service save method', waitForAsync(() => {
      const spy = jest.spyOn(keyResultService, 'saveKeyResult');
      spy.mockImplementation(() => of({ id: 2 } as KeyResult));

      component.keyResultForm.setValue({
        owner: testUser,
        actionList: [],
        title: 'Neuer Titel',
        description: 'Description',
        keyResultType: 'metric',
        metric: {
          baseline: 0,
          targetGoal: 0,
          stretchGoal: 0,
          unit: 'FTE'
        },
        ordinal: {
          stretchZone: null,
          targetZone: null,
          commitZone: null
        }
      });

      initKeyResult.title = 'Neuer Titel';
      initKeyResult.description = 'Description';

      component.saveKeyResult();

      expect(spy)
        .toHaveBeenCalledTimes(1);
    }));

    it('should have logged in user as owner', waitForAsync(() => {
      fixture.detectChanges();

      expect(component.keyResultForm.get('owner')?.value)
        .toBe(testUser);
    }));
  });

  describe('Edit KeyResult Metric', () => {
    beforeEach(() => {
      mockUserService.getUsers.mockReturnValue(users);
      mockUserService.getCurrentUser.mockReturnValue(testUser);
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
          MatDividerModule
        ],
        providers: [
          provideRouter([]),
          provideHttpClient(),
          provideHttpClientTesting(),
          KeyResultService,
          {
            provide: MatDialogRef,
            useValue: matDialogRefMock
          },
          {
            provide: OAuthService,
            useValue: oAuthMockService
          },
          {
            provide: MAT_DIALOG_DATA,
            useValue: {
              keyResult: fullKeyResultMetric,
              objective: keyResultObjective
            }
          },

          {
            provide: UserService,
            useValue: userService
          }

        ],
        declarations: [
          KeyResultDialogComponent,
          KeyResultFormComponent,
          DialogTemplateCoreComponent,
          ActionPlanComponent,
          KeyResultTypeComponent,
          ErrorComponent
        ]
      })
        .compileComponents();

      fixture = TestBed.createComponent(KeyResultDialogComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      keyResultService = TestBed.inject(KeyResultService);
    });

    afterEach(() => {
      mockUserService.getUsers.mockReset();
    });

    it('should use key-result value from data input', waitForAsync(() => {
      const formObject = component.keyResultForm.value;
      expect(formObject.title)
        .toBe('Der Titel ist hier');
      expect(formObject.description)
        .toBe('Die Beschreibung');
      expect(formObject.owner)
        .toBe(testUser);
    }));

    it('should display error message of too short input', waitForAsync(async() => {
      component.keyResultForm.patchValue({
        title: 'T'
      });
      fixture.detectChanges();

      expect(component.keyResultForm.invalid)
        .toBeTruthy();
      expect(component.keyResultForm.get('title')!.errors?.['minlength'])
        .toBeTruthy();
    }));

    it('should display error message of required', waitForAsync(async() => {
      component.keyResultForm.patchValue({
        title: null
      });
      fixture.detectChanges();

      expect(component.keyResultForm.invalid)
        .toBeTruthy();
      expect(component.keyResultForm.get('title')!.errors?.['required'])
        .toBeTruthy();
    }));

    it('should call service save method', waitForAsync(() => {
      const spy = jest.spyOn(keyResultService, 'saveKeyResult');
      spy.mockImplementation(() => of({ id: 2 } as KeyResult));

      component.saveKeyResult();

      expect(spy)
        .toHaveBeenCalledTimes(1);
    }));

    it('should not display logged in user when editing', waitForAsync(() => {
      jest.resetAllMocks();
      const userServiceSpy = jest.spyOn(userService, 'getUsers');
      fixture.detectChanges();
      expect(userServiceSpy)
        .toHaveBeenCalledTimes(0);
      expect(component.keyResultForm.get('owner')!.value)
        .toBe(testUser);
    }));

    it('should use values from input', () => {
      expect(getValueOfForm(component.keyResultForm, ['metric',
        'unit']))
        .toEqual('CHF');
      expect(getValueOfForm(component.keyResultForm, ['metric',
        'baseline']))
        .toEqual(3);
      expect(getValueOfForm(component.keyResultForm, ['metric',
        'stretchGoal']))
        .toEqual(25);

      expect(getValueOfForm(component.keyResultForm, ['ordinal',
        'commitZone']))
        .toEqual('');
      expect(getValueOfForm(component.keyResultForm, ['ordinal',
        'targetZone']))
        .toEqual('');

      expect(getValueOfForm(component.keyResultForm, ['ordinal',
        'stretchZone']))
        .toEqual('');
    });

    it('should set validators for metric', () => {
      component.setValidators('metric');
      expect(component.keyResultForm.get('metric')!.enabled)
        .toBe(true);
      expect(component.keyResultForm.get('ordinal')!.enabled)
        .toBe(false);
    });

    it('should set validators for ordinal', () => {
      component.setValidators('ordinal');
      expect(component.keyResultForm.get('ordinal')!.enabled)
        .toBe(true);
      expect(component.keyResultForm.get('metric')!.enabled)
        .toBe(false);
    });

    it('should set right owner, validators and type on init', () => {
      expect(component.keyResultForm.get('metric')!.enabled)
        .toBe(true);
      expect(component.keyResultForm.get('keyResultType')!.value)
        .toEqual('metric');
      expect(component.keyResultForm.get('owner')!.value)
        .toEqual(testUser);
    });
  });

  describe('Edit KeyResult Ordinal', () => {
    beforeEach(() => {
      mockUserService.getUsers.mockReturnValue(users);
      mockUserService.getCurrentUser.mockReturnValue(testUser);

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
          MatDividerModule
        ],
        providers: [
          provideRouter([]),
          provideHttpClient(),
          provideHttpClientTesting(),
          KeyResultService,
          {
            provide: MatDialogRef,
            useValue: matDialogRefMock
          },
          {
            provide: OAuthService,
            useValue: oAuthMockService
          },
          {
            provide: MAT_DIALOG_DATA,
            useValue: {
              keyResult: fullKeyResultOrdinal,
              objective: keyResultObjective
            }
          },
          {
            provide: UserService,
            useValue: userService
          }
        ],
        declarations: [
          KeyResultDialogComponent,
          KeyResultFormComponent,
          DialogTemplateCoreComponent,
          ActionPlanComponent,
          KeyResultTypeComponent
        ]
      })
        .compileComponents();

      fixture = TestBed.createComponent(KeyResultDialogComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      keyResultService = TestBed.inject(KeyResultService);
    });

    afterEach(() => {
      mockUserService.getUsers.mockReset();
    });

    it('should use values from input', () => {
      // Default value
      expect(getValueOfForm(component.keyResultForm, ['metric',
        'unit']))
        .toEqual('NUMBER');

      expect(getValueOfForm(component.keyResultForm, ['metric',
        'baseline']))
        .toEqual(0);
      expect(getValueOfForm(component.keyResultForm, ['metric',
        'stretchGoal']))
        .toEqual(0);

      expect(getValueOfForm(component.keyResultForm, ['ordinal',
        'commitZone']))
        .toEqual('Commit zone');
      expect(getValueOfForm(component.keyResultForm, ['ordinal',
        'targetZone']))
        .toEqual('Target zone');
      expect(getValueOfForm(component.keyResultForm, ['ordinal',
        'stretchZone']))
        .toEqual('Stretch goal');
    });

    it('should use key-result value from data input', waitForAsync(() => {
      const formObject = fixture.componentInstance.keyResultForm.value;
      expect(formObject.title)
        .toBe('Der Titel ist hier');
      expect(formObject.description)
        .toBe('Die Beschreibung');
      expect(formObject.owner)
        .toBe(testUser);
    }));

    it('should be able to set title and description', waitForAsync(async() => {
      expect(component.keyResultForm.value.title)
        .toEqual('Der Titel ist hier');
      expect(component.keyResultForm.value.description)
        .toEqual('Die Beschreibung');

      component.keyResultForm.patchValue({
        title: 'Title',
        description: 'Description'
      });
      fixture.detectChanges();
      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(await submitButton.nativeElement.getAttribute('disabled'))
        .toBeFalsy();

      const formObject = fixture.componentInstance.keyResultForm.value;
      expect(formObject.title)
        .toBe('Title');
      expect(formObject.description)
        .toBe('Description');
      expect(component.keyResultForm.invalid)
        .toBeFalsy();
    }));

    it('should display error message of too short input', waitForAsync(async() => {
      component.keyResultForm.patchValue({
        title: 'T'
      });
      fixture.detectChanges();

      expect(component.keyResultForm.invalid)
        .toBeTruthy();
      expect(component.keyResultForm.get('title')!.errors?.['minlength'])
        .toBeTruthy();
    }));

    it('should display error message of required', waitForAsync(async() => {
      component.keyResultForm.patchValue({
        title: null
      });
      fixture.detectChanges();

      expect(component.keyResultForm.invalid)
        .toBeTruthy();
      expect(component.keyResultForm.get('title')!.errors?.['required'])
        .toBeTruthy();
    }));

    it('should call service save method', waitForAsync(() => {
      const spy = jest.spyOn(keyResultService, 'saveKeyResult');
      spy.mockImplementation(() => of({ id: 2 } as KeyResult));

      component.saveKeyResult();

      expect(spy)
        .toHaveBeenCalledTimes(1);
    }));
  });
});
