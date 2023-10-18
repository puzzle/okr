import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { KeyResultDialogComponent } from './key-result-dialog.component';
import * as errorData from '../../../../assets/errors/error-messages.json';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatIconModule } from '@angular/material/icon';
import { By } from '@angular/platform-browser';
import { testUser, users } from '../../testData';
import { State } from '../../types/enums/State';
import { KeyresultService } from '../../services/keyresult.service';
import { KeyResult } from '../../types/model/KeyResult';
import { Observable, of } from 'rxjs';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { KeyResultObjective } from '../../types/model/KeyResultObjective';
import { User } from '../../types/model/User';

class matDialogRefMock {
  close() {}
}

describe('KeyResultDialogComponent', () => {
  let component: KeyResultDialogComponent;
  let fixture: ComponentFixture<KeyResultDialogComponent>;
  let errors = errorData;
  let keyResultService: KeyresultService;
  let matDialogRef: MatDialogRef<KeyResultDialogComponent>;

  let fullObjective = {
    id: 1,
    title: 'Das ist ein Objective',
    description: 'Das ist die Beschreibung',
    state: State.ONGOING,
    team: { id: 1, name: 'Das Puzzle Team' },
    quarter: { id: 1, label: 'GJ 22/23-Q2' },
  };

  let keyResultObjective: KeyResultObjective = {
    id: 2,
    state: State.ONGOING,
    quarter: { id: 1, label: 'GJ 22/23-Q2', endDate: new Date(), startDate: new Date() },
  };

  let fullKeyResultMetric = {
    id: 3,
    title: 'Der Titel ist hier',
    description: 'Die Beschreibung',
    owner: testUser,
    objective: keyResultObjective,
    baseline: 3,
    keyResultType: 'metric',
    stretchGoal: 25,
    unit: 'CHF',
  };

  let receivedKeyResultMetric = {
    id: 3,
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

  let fullKeyResultOrdinal = {
    id: 6,
    title: 'Der Titel ist hier',
    description: 'Die Beschreibung',
    owner: testUser,
    objective: keyResultObjective,
    keyResultType: 'ordinal',
    commitZone: 'Commit zone',
    targetZone: 'Target zone',
    stretchZone: 'Stretch zone',
  };

  let receivedKeyResultOrdinal = {
    id: 6,
    title: 'Der Titel ist hier',
    description: 'Die Beschreibung',
    owner: testUser,
    objective: keyResultObjective,
    keyResultType: 'ordinal',
    commitZone: 'Commit zone',
    targetZone: 'Target zone',
    stretchZone: 'Stretch zone',
    baseline: null,
    stretchGoal: null,
    unit: null,
  };

  let initKeyResult = {
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
  };

  const mockUserService = {
    getUsers: jest.fn(),
  };

  describe('New KeyResult', () => {
    beforeEach(() => {
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
        ],
        providers: [
          KeyresultService,
          {
            provide: MatDialogRef,
            useValue: { close: (dialogResult: any) => {} },
          },
          {
            provide: MAT_DIALOG_DATA,
            useValue: { objective: fullObjective, keyResult: undefined },
          },
        ],
        declarations: [KeyResultDialogComponent],
      }).compileComponents();

      matDialogRef = TestBed.inject(MatDialogRef);
      fixture = TestBed.createComponent(KeyResultDialogComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      keyResultService = TestBed.inject(KeyresultService);
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should have all items in dialog', waitForAsync(async () => {
      const labels = document.querySelectorAll('label');
      const textareas = document.querySelectorAll('textarea');
      const inputs = document.querySelectorAll('input');
      const keyResultTypes = document.querySelectorAll('app-keyresult-type');
      const buttons = document.querySelectorAll('button');
      expect(labels.length).toEqual(3);
      expect(textareas.length).toEqual(2);
      expect(inputs.length).toEqual(1);
      expect(keyResultTypes.length).toEqual(1);
      expect(buttons.length).toEqual(4);
    }));

    it('should be able to set title and description', waitForAsync(async () => {
      component.keyResultForm.setValue({
        owner: testUser,
        title: 'Title',
        baseline: null,
        stretchZone: null,
        targetZone: null,
        commitZone: null,
        unit: null,
        description: 'Description',
        stretchGoal: null,
        keyResultType: null,
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
      expect(spy).toHaveBeenCalledWith(initKeyResult);
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
          HttpClientTestingModule,
          MatIconModule,
          MatAutocompleteModule,
        ],
        providers: [
          KeyresultService,
          {
            provide: MatDialogRef,
            useValue: { close: (dialogResult: any) => {} },
          },
          {
            provide: MAT_DIALOG_DATA,
            useValue: { keyResult: fullKeyResultMetric, objective: keyResultObjective },
          },
        ],
        declarations: [KeyResultDialogComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyResultDialogComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      keyResultService = TestBed.inject(KeyresultService);
      fullKeyResultMetric.id = 3;
    });

    afterEach(() => {
      mockUserService.getUsers.mockReset();
    });

    it('should have all items in dialog', waitForAsync(async () => {
      const labels = document.querySelectorAll('label');
      const textareas = document.querySelectorAll('textarea');
      const inputs = document.querySelectorAll('input');
      const keyResultTypes = document.querySelectorAll('app-keyresult-type');
      const buttons = document.querySelectorAll('button');
      expect(labels.length).toEqual(3);
      expect(textareas.length).toEqual(2);
      expect(inputs.length).toEqual(1);
      expect(keyResultTypes.length).toEqual(1);
      expect(buttons.length).toEqual(4);
    }));

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
        title: 'Title',
        baseline: null,
        stretchZone: null,
        targetZone: null,
        commitZone: null,
        unit: null,
        description: 'Description',
        stretchGoal: null,
        keyResultType: null,
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
          HttpClientTestingModule,
          MatIconModule,
          MatAutocompleteModule,
        ],
        providers: [
          KeyresultService,
          {
            provide: MatDialogRef,
            useValue: { close: (dialogResult: any) => {} },
          },
          {
            provide: MAT_DIALOG_DATA,
            useValue: { keyResult: fullKeyResultOrdinal, objective: keyResultObjective },
          },
        ],
        declarations: [KeyResultDialogComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyResultDialogComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      keyResultService = TestBed.inject(KeyresultService);
    });

    afterEach(() => {
      mockUserService.getUsers.mockReset();
    });

    it('should have all items in dialog', waitForAsync(async () => {
      const labels = document.querySelectorAll('label');
      const textareas = document.querySelectorAll('textarea');
      const inputs = document.querySelectorAll('input');
      const keyResultTypes = document.querySelectorAll('app-keyresult-type');
      const buttons = document.querySelectorAll('button');
      expect(labels.length).toEqual(3);
      expect(textareas.length).toEqual(2);
      expect(inputs.length).toEqual(1);
      expect(keyResultTypes.length).toEqual(1);
      expect(buttons.length).toEqual(4);
    }));

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
        title: 'Title',
        baseline: null,
        stretchZone: null,
        targetZone: null,
        commitZone: null,
        unit: null,
        description: 'Description',
        stretchGoal: null,
        keyResultType: null,
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

  describe('Method testing', () => {
    beforeEach(() => {
      mockUserService.getUsers.mockReturnValue(users);
      TestBed.configureTestingModule({
        imports: [
          MatDialogModule,
          NoopAnimationsModule,
          MatInputModule,
          ReactiveFormsModule,
          HttpClientTestingModule,
          MatIconModule,
          MatAutocompleteModule,
        ],
        providers: [
          KeyresultService,
          {
            provide: MatDialogRef,
            useValue: { close: (dialogResult: any) => {} },
          },
          {
            provide: MAT_DIALOG_DATA,
            useValue: { keyResult: fullKeyResultMetric },
          },
        ],
        declarations: [KeyResultDialogComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyResultDialogComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      keyResultService = TestBed.inject(KeyresultService);
      fullKeyResultMetric.id = 3;
    });

    afterEach(() => {
      mockUserService.getUsers.mockReset();
    });

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
  });
});
