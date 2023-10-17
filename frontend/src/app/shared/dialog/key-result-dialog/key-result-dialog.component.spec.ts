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
import { KeyResultEmitMetricDTO } from '../../types/DTOs/KeyResultEmitMetricDTO';
import { KeyResultEmitOrdinalDTO } from '../../types/DTOs/KeyResultEmitOrdinalDTO';
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

  let initKeyResult = {
    id: null,
    title: '',
    description: '',
    owner: testUser,
    objective: fullObjective,
    baseline: 3,
    keyResultType: 'metric',
    stretchGoal: 25,
    unit: 'CHF',
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

    it('should be able to set title', waitForAsync(async () => {
      const titleInput = fixture.debugElement.query(By.css('[data-testId="titleInput"]'));
      titleInput.nativeElement.textContent = 'Title';
      expect(component.keyResultForm.value.title).toEqual('Title');

      component.keyResultForm.setValue({
        owner: testUser,
        title: 'Title',
        baseline: null,
        stretchZone: null,
        targetZone: null,
        commitZone: null,
        unit: null,
        description: '',
        stretchGoal: null,
      });
      fixture.detectChanges();
      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(await submitButton.nativeElement.disabled).toBeFalsy();

      const formObject = fixture.componentInstance.keyResultForm.value;
      expect(formObject.title).toBe('Title');
      expect(component.keyResultForm.invalid).toBeFalsy();
    }));

    it('should be able to set title with description', waitForAsync(async () => {
      const titleInput = fixture.debugElement.query(By.css('[data-testId="titleInput"]'));
      const descriptionInput = fixture.debugElement.query(By.css('[data-testId="descriptionInput"]'));
      titleInput.nativeElement.textContent = 'Title';
      descriptionInput.nativeElement.textContent = 'Description';

      component.keyResultForm.patchValue({ owner: testUser });

      let submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(submitButton.nativeElement.disabled).toBeFalsy();
      let saveAndNewButton = fixture.debugElement.query(By.css('[data-testId="saveAndNew"]'));
      expect(saveAndNewButton.nativeElement.disabled).toBeFalsy();

      const formObject = fixture.componentInstance.keyResultForm.value;
      expect(formObject.title).toBe('Title');
      expect(formObject.description).toBe('Description');
      expect(component.keyResultForm.invalid).toBeFalsy();
    }));

    it('should display error message of too short input', waitForAsync(async () => {
      const titleInput = fixture.debugElement.query(By.css('[data-testId="titleInput"]'));
      titleInput.nativeElement.textContent = 'T';

      const errorMessage = fixture.debugElement.query(By.css('mat-error'));
      expect(errorMessage.nativeElement.textContent).toContain(errors.MINLENGTH);

      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(submitButton.nativeElement.disabled).toBeTruthy();
      const saveAndNewButton = fixture.debugElement.query(By.css('[data-testId="saveAndNew"]'));
      expect(saveAndNewButton.nativeElement.disabled).toBeTruthy();
      expect(component.keyResultForm.invalid).toBeTruthy();
      expect(component.keyResultForm.errors).not.toBeNull();
    }));

    it('should display error message of required', waitForAsync(async () => {
      const titleInput = fixture.debugElement.query(By.css('[data-testId="titleInput"]'));
      const descriptionInput = fixture.debugElement.query(By.css('[data-testId="descriptionInput"]'));
      titleInput.nativeElement.textContent = '';
      descriptionInput.nativeElement.textContent = 'Description';

      const errorMessage = fixture.debugElement.query(By.css('mat-error'));
      expect(errorMessage.nativeElement.textContent).toContain(errors.REQUIRED);

      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(submitButton.nativeElement.disabled).toBeTruthy();
      const saveAndNewButton = fixture.debugElement.query(By.css('[data-testId="saveAndNew"]'));
      expect(saveAndNewButton.nativeElement.disabled).toBeTruthy();
      expect(component.keyResultForm.invalid).toBeTruthy();
    }));

    it('should call service save method', waitForAsync(() => {
      const spy = jest.spyOn(keyResultService, 'saveKeyResult');
      spy.mockImplementation(() => of({ id: 2 } as KeyResult));

      component.keyResultForm.patchValue({
        owner: testUser,
        title: 'Neuer Titel',
        description: 'Description',
      });
      initKeyResult.title = 'Neuer Titel';
      initKeyResult.description = 'Description';

      let submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      submitButton.nativeElement.click();
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

    it('should be able to set title', () => {
      const titleInput = fixture.debugElement.query(By.css('[data-testId="titleInput"]'));

      expect(titleInput.nativeElement.value).toEqual('Der Titel ist hier');
      titleInput.nativeElement.textContent = 'New title';

      component.keyResultForm.patchValue({
        owner: testUser,
      });

      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(submitButton.nativeElement.disabled).toBeFalsy();
      const formObject = fixture.componentInstance.keyResultForm.value;
      expect(formObject.title).toBe('New title');
    });

    it('should display error message of too short input', waitForAsync(async () => {
      const titleInput = fixture.debugElement.query(By.css('[data-testId="titleInput"]'));
      titleInput.nativeElement.textContent = 'T';

      const errorMessage = fixture.debugElement.query(By.css('mat-error'));
      expect(errorMessage.nativeElement.textContent).toContain(errors.MINLENGTH);

      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(submitButton.nativeElement.disabled).toBeTruthy();
      expect(component.keyResultForm.invalid).toBeTruthy();
      expect(component.keyResultForm.errors).not.toBeNull();
    }));

    it('should display error message of required', waitForAsync(async () => {
      const titleInput = fixture.debugElement.query(By.css('[data-testId="titleInput"]'));
      const descriptionInput = fixture.debugElement.query(By.css('[data-testId="descriptionInput"]'));
      titleInput.nativeElement.textContent = '';
      descriptionInput.nativeElement.textContent = 'Description';

      const errorMessage = fixture.debugElement.query(By.css('mat-error'));
      expect(errorMessage.nativeElement.textContent).toContain(errors.REQUIRED);

      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(submitButton.nativeElement.disabled).toBeTruthy();
      const saveAndNewButton = fixture.debugElement.query(By.css('[data-testId="saveAndNew"]'));
      expect(saveAndNewButton.nativeElement.disabled).toBeTruthy();
      expect(component.keyResultForm.invalid).toBeTruthy();
    }));

    it('should call service save method', waitForAsync(() => {
      const spy = jest.spyOn(keyResultService, 'saveKeyResult');
      spy.mockImplementation(() => of({ id: 2 } as KeyResult));

      component.keyResultForm.patchValue({
        owner: testUser,
        title: 'Neuer Titel',
        description: 'Description',
      });
      fullKeyResultMetric.title = 'Neuer Titel';
      fullKeyResultMetric.description = 'Description';

      let submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      submitButton.nativeElement.click();
      expect(spy).toBeCalledTimes(1);
      expect(spy).toHaveBeenCalledWith(fullKeyResultMetric);
    }));

    it('should call service delete method', waitForAsync(() => {
      const spy = jest.spyOn(keyResultService, 'deleteKeyResult');
      spy.mockImplementation(() => of({ id: 2 } as KeyResult));

      component.keyResultForm.patchValue({
        owner: testUser,
        title: 'Neuer Titel',
        description: 'Description',
      });
      fullKeyResultOrdinal.title = 'Neuer Titel';
      fullKeyResultOrdinal.description = 'Description';

      let deleteButton = fixture.debugElement.query(By.css('[data-testId="delete"]'));
      deleteButton.nativeElement.click();
      expect(spy).toBeCalledTimes(1);
      expect(spy).toHaveBeenCalledWith(fullKeyResultOrdinal);
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
            useValue: { keyResult: fullKeyResultOrdinal },
          },
        ],
        declarations: [KeyResultDialogComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyResultDialogComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      keyResultService = TestBed.inject(KeyresultService);
      fullKeyResultOrdinal.id = 3;
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

    it('should be able to set title', () => {
      const titleInput = fixture.debugElement.query(By.css('[data-testId="titleInput"]'));

      expect(titleInput.nativeElement.value).toEqual('Der Titel ist hier');
      titleInput.nativeElement.textContent = 'New title';

      component.keyResultForm.patchValue({
        owner: testUser,
      });

      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(submitButton.nativeElement.disabled).toBeFalsy();
      const formObject = fixture.componentInstance.keyResultForm.value;
      expect(formObject.title).toBe('New title');
    });

    it('should display error message of too short input', waitForAsync(async () => {
      const titleInput = fixture.debugElement.query(By.css('[data-testId="titleInput"]'));
      titleInput.nativeElement.textContent = 'T';

      const errorMessage = fixture.debugElement.query(By.css('mat-error'));
      expect(errorMessage.nativeElement.textContent).toContain(errors.MINLENGTH);

      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(submitButton.nativeElement.disabled).toBeTruthy();
      expect(component.keyResultForm.invalid).toBeTruthy();
      expect(component.keyResultForm.errors).not.toBeNull();
    }));

    it('should display error message of required', waitForAsync(async () => {
      const titleInput = fixture.debugElement.query(By.css('[data-testId="titleInput"]'));
      const descriptionInput = fixture.debugElement.query(By.css('[data-testId="descriptionInput"]'));
      titleInput.nativeElement.textContent = '';
      descriptionInput.nativeElement.textContent = 'Description';

      const errorMessage = fixture.debugElement.query(By.css('mat-error'));
      expect(errorMessage.nativeElement.textContent).toContain(errors.REQUIRED);

      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(submitButton.nativeElement.disabled).toBeTruthy();
      const saveAndNewButton = fixture.debugElement.query(By.css('[data-testId="saveAndNew"]'));
      expect(saveAndNewButton.nativeElement.disabled).toBeTruthy();
      expect(component.keyResultForm.invalid).toBeTruthy();
    }));

    it('should call service save method', waitForAsync(() => {
      const spy = jest.spyOn(keyResultService, 'saveKeyResult');
      spy.mockImplementation(() => of({ id: 2 } as KeyResult));

      component.keyResultForm.patchValue({
        owner: testUser,
        title: 'Neuer Titel',
        description: 'Description',
      });
      fullKeyResultOrdinal.title = 'Neuer Titel';
      fullKeyResultOrdinal.description = 'Description';

      let submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      submitButton.nativeElement.click();
      expect(spy).toBeCalledTimes(1);
      expect(spy).toHaveBeenCalledWith(fullKeyResultOrdinal);
    }));

    it('should call service delete method', waitForAsync(() => {
      const spy = jest.spyOn(keyResultService, 'deleteKeyResult');
      spy.mockImplementation(() => of({ id: 2 } as KeyResult));

      component.keyResultForm.patchValue({
        owner: testUser,
        title: 'Neuer Titel',
        description: 'Description',
      });
      fullKeyResultOrdinal.title = 'Neuer Titel';
      fullKeyResultOrdinal.description = 'Description';

      let deleteButton = fixture.debugElement.query(By.css('[data-testId="delete"]'));
      deleteButton.nativeElement.click();
      expect(spy).toBeCalledTimes(1);
      expect(spy).toHaveBeenCalledWith(fullKeyResultOrdinal);
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

    it('should update form from emitted data metric', () => {
      let keyResultMetric: KeyResultEmitMetricDTO = {
        keyresultType: 'metric',
        unit: 'CHF',
        baseline: 30,
        stretchGoal: 100,
      };

      component.saveEmittedData(keyResultMetric);
      expect(component.isMetricKeyResult).toBeTruthy();
      expect(component.keyResultForm.value.unit).toEqual('CHF');
      expect(component.keyResultForm.value.baseline).toEqual(30);
      expect(component.keyResultForm.value.stretchGoal).toEqual(100);
    });

    it('should update form from emitted data ordinal', () => {
      let keyResultOrdinal: KeyResultEmitOrdinalDTO = {
        keyresultType: 'ordinal',
        commitZone: 'Commit zone',
        targetZone: 'Target zone',
        stretchZone: 'Stretch zone',
      };

      component.saveEmittedData(keyResultOrdinal);
      expect(component.isMetricKeyResult).toBeFalsy();
      expect(component.keyResultForm.value.commitZone).toEqual('Commit zone');
      expect(component.keyResultForm.value.targetZone).toEqual('Target zone');
      expect(component.keyResultForm.value.stretchZone).toEqual('Stretch zone');
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
