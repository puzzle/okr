import { ComponentFixture, fakeAsync, TestBed, waitForAsync } from '@angular/core/testing';

import { KeyResultDialogComponent } from './key-result-dialog.component';
import { HarnessLoader } from '@angular/cdk/testing';
import * as errorData from '../../assets/errors/error-messages.json';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatIconModule } from '@angular/material/icon';
import { MatInputHarness } from '@angular/material/input/testing';
import { By } from '@angular/platform-browser';
import { testUser } from '../shared/testData';
import { State } from '../shared/types/enums/State';
import { KeyresultService } from '../shared/services/keyresult.service';
import { KeyResult } from '../shared/types/model/KeyResult';
import { of } from 'rxjs';

describe('KeyResultDialogComponent', () => {
  let component: KeyResultDialogComponent;
  let fixture: ComponentFixture<KeyResultDialogComponent>;
  let loader: HarnessLoader;
  let errors = errorData;
  let keyResultService: KeyresultService;

  let fullObjective = {
    id: 1,
    title: 'Das ist ein Objective',
    description: 'Das ist die Beschreibung',
    state: State.ONGOING,
    team: { id: 1, name: 'Das Puzzle Team' },
    quarter: { id: 1, label: 'GJ 22/23-Q2' },
  };
  let fullKeyResult = {
    id: 3,
    title: 'Der Titel ist hier',
    description: 'Die Beschreibung',
    owner: testUser,
    objective: fullObjective,
    baseline: 3,
    keyResultType: 'metric',
    stretchGoal: 25,
    unit: 'CHF',
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

  const dialogMock = {
    close: jest.fn(),
  };

  describe('New KeyResult', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [
          MatDialogModule,
          NoopAnimationsModule,
          MatInputModule,
          ReactiveFormsModule,
          HttpClientTestingModule,
          MatIconModule,
        ],
        providers: [
          KeyresultService,
          {
            provide: MatDialogRef,
            useValue: {},
          },
          {
            provide: MAT_DIALOG_DATA,
            useValue: { objective: fullObjective, keyResult: undefined },
          },
        ],
        declarations: [KeyResultDialogComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyResultDialogComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      loader = TestbedHarnessEnvironment.loader(fixture);
      keyResultService = TestBed.inject(KeyresultService);
    });

    it('should create', fakeAsync(() => {
      expect(component).toBeTruthy();
    }));

    it('should have all items in dialog', waitForAsync(async () => {
      const inputs = await loader.getAllHarnesses(MatInputHarness);
      const buttons = document.querySelectorAll('button');
      expect(inputs.length).toEqual(2);
      expect(buttons.length).toEqual(4);
    }));

    it('should be able to set title', waitForAsync(async () => {
      let submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));

      const inputs = await loader.getAllHarnesses(MatInputHarness);
      const titleInput = inputs[0];
      titleInput.setValue('Title').then(async () => {
        submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
        expect(await submitButton.nativeElement.disabled).toBeFalsy();

        const formObject = fixture.componentInstance.keyResultForm.value;
        expect(formObject.title).toBe('Title');
        expect(await submitButton.nativeElement.disabled).toBeTruthy();
        expect(component.keyResultForm.invalid).toBeFalsy();
      });
    }));

    it('should be able to set title with description', waitForAsync(async () => {
      let submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      let saveAndNewButton = fixture.debugElement.query(By.css('[data-testId="saveAndNew"]'));

      const inputs = await loader.getAllHarnesses(MatInputHarness);
      const titleInput = inputs[0];
      const descriptionInput = inputs[1];
      titleInput.setValue('Title').then(async () => {
        descriptionInput.setValue('New Description').then(async () => {
          submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
          expect(await submitButton.nativeElement.disabled).toBeFalsy();
          saveAndNewButton = fixture.debugElement.query(By.css('[data-testId="saveAndNew"]'));
          expect(saveAndNewButton.nativeElement.disabled).toBeFalsy();

          const formObject = fixture.componentInstance.keyResultForm.value;
          expect(formObject.title).toBe('Title');
          expect(formObject.description).toBe('New Description');
          expect(component.keyResultForm.invalid).toBeFalsy();
        });
      });
    }));

    it('should display error message of too short input', waitForAsync(async () => {
      const inputs = await loader.getAllHarnesses(MatInputHarness);
      const titleInput = inputs[0];
      await titleInput.setValue('T');

      const errorMessage = fixture.debugElement.query(By.css('mat-error'));
      expect(errorMessage.nativeElement.textContent).toContain(errors.MINLENGTH);

      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(submitButton.nativeElement.disabled).toBeTruthy();
      let saveAndNewButton = fixture.debugElement.query(By.css('[data-testId="saveAndNew"]'));
      expect(saveAndNewButton.nativeElement.disabled).toBeTruthy();
      expect(component.keyResultForm.invalid).toBeTruthy();
    }));

    it('should display error message of required', waitForAsync(async () => {
      const inputs = await loader.getAllHarnesses(MatInputHarness);
      const titleInput = inputs[0];
      const descriptionInput = inputs[1];
      await titleInput.setValue('');
      await descriptionInput.setValue('Description');

      const errorMessage = fixture.debugElement.query(By.css('mat-error'));
      expect(errorMessage.nativeElement.textContent).toContain(errors.REQUIRED);

      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(submitButton.nativeElement.disabled).toBeTruthy();
      let saveAndNewButton = fixture.debugElement.query(By.css('[data-testId="saveAndNew"]'));
      expect(saveAndNewButton.nativeElement.disabled).toBeTruthy();
      expect(component.keyResultForm.invalid).toBeTruthy();
    }));

    it('should call service save method', waitForAsync(async () => {
      const spy = jest.spyOn(keyResultService, 'saveKeyResult');
      spy.mockImplementation(() => of({ id: 2 } as KeyResult));

      loader.getAllHarnesses(MatInputHarness).then(async (inputs) => {
        const titleInput = inputs[0];
        const descriptionInput = inputs[1];
        await titleInput.setValue('Neuer Titel').then(async () => {
          descriptionInput.setValue('Description').then(async () => {
            initKeyResult.title = 'Neuer Titel';
            initKeyResult.description = 'Description';

            let saveAndNewButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
            saveAndNewButton.nativeElement.click();
            expect(spy).toBeCalledTimes(1);
            expect(spy).toHaveBeenCalledWith(initKeyResult);
          });
        });
      });
    }));

    it('should call service save method when creating new', waitForAsync(async () => {
      const spy = jest.spyOn(keyResultService, 'saveKeyResult');
      spy.mockImplementation(() => of({ id: 2 } as KeyResult));

      loader.getAllHarnesses(MatInputHarness).then(async (inputs) => {
        const titleInput = inputs[0];
        const descriptionInput = inputs[1];
        await titleInput.setValue('Neuer Titel').then(async () => {
          descriptionInput.setValue('Description').then(async () => {
            initKeyResult.title = 'Neuer Titel';
            initKeyResult.description = 'Description';

            let saveAndNewButton = fixture.debugElement.query(By.css('[data-testId="saveAndNew"]'));
            saveAndNewButton.nativeElement.click();
            expect(spy).toBeCalledTimes(1);
            expect(spy).toHaveBeenCalledWith(initKeyResult);
          });
        });
      });
    }));
  });

  describe('Edit KeyResult', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [
          MatDialogModule,
          NoopAnimationsModule,
          MatInputModule,
          ReactiveFormsModule,
          HttpClientTestingModule,
          MatIconModule,
        ],
        providers: [
          KeyresultService,
          { provide: MatDialogRef, useValue: dialogMock },
          {
            provide: MatDialogRef,
            useValue: {},
          },
          {
            provide: MAT_DIALOG_DATA,
            useValue: { keyResult: fullKeyResult },
          },
        ],
        declarations: [KeyResultDialogComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyResultDialogComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      loader = TestbedHarnessEnvironment.loader(fixture);
      keyResultService = TestBed.inject(KeyresultService);
      fullKeyResult.id = 3;
    });

    it('should create', fakeAsync(() => {
      expect(component).toBeTruthy();
    }));

    it('should have all items in dialog', waitForAsync(async () => {
      const inputs = await loader.getAllHarnesses(MatInputHarness);
      const buttons = document.querySelectorAll('button');
      expect(inputs.length).toEqual(2);
      expect(buttons.length).toEqual(4);
    }));

    it('should use KeyResult value from data input', waitForAsync(async () => {
      const formObject = fixture.componentInstance.keyResultForm.value;
      expect(formObject.title).toBe('Der Titel ist hier');
      expect(formObject.description).toBe('Die Beschreibung');
      expect(formObject.owner).toBe(testUser);
    }));

    it('should be able to set title', waitForAsync(async () => {
      let submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));

      const inputs = await loader.getAllHarnesses(MatInputHarness);
      const titleInput = inputs[0];
      titleInput.setValue('Title').then(async () => {
        submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
        expect(await submitButton.nativeElement.disabled).toBeFalsy();

        const formObject = fixture.componentInstance.keyResultForm.value;
        expect(formObject.title).toBe('Title');
        expect(await submitButton.nativeElement.disabled).toBeTruthy();
      });
    }));

    it('should display error message of too short input', waitForAsync(async () => {
      const inputs = await loader.getAllHarnesses(MatInputHarness);
      const titleInput = inputs[0];
      await titleInput.setValue('T');

      const errorMessage = fixture.debugElement.query(By.css('mat-error'));
      expect(errorMessage.nativeElement.textContent).toContain(errors.MINLENGTH);

      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(submitButton.nativeElement.disabled).toBeTruthy();
      expect(component.keyResultForm.invalid).toBeTruthy();
    }));

    it('should display error message of required', waitForAsync(async () => {
      const inputs = await loader.getAllHarnesses(MatInputHarness);
      const titleInput = inputs[0];
      const descriptionInput = inputs[1];
      await titleInput.setValue('');
      await descriptionInput.setValue('Description');

      const errorMessage = fixture.debugElement.query(By.css('mat-error'));
      expect(errorMessage.nativeElement.textContent).toContain(errors.REQUIRED);

      const submitButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
      expect(submitButton.nativeElement.disabled).toBeTruthy();
      expect(component.keyResultForm.invalid).toBeTruthy();
    }));

    it('should call service save method', waitForAsync(async () => {
      const spy = jest.spyOn(keyResultService, 'saveKeyResult');
      spy.mockImplementation(() => of({ id: 2 } as KeyResult));

      loader.getAllHarnesses(MatInputHarness).then(async (inputs) => {
        const titleInput = inputs[0];
        const descriptionInput = inputs[1];
        await titleInput.setValue('Neuer Titel').then(async () => {
          descriptionInput.setValue('Description').then(async () => {
            fullKeyResult.title = 'Neuer Titel';
            fullKeyResult.description = 'Description';

            let saveAndNewButton = fixture.debugElement.query(By.css('[data-testId="submit"]'));
            saveAndNewButton.nativeElement.click();
            expect(spy).toBeCalledTimes(1);
            expect(spy).toHaveBeenCalledWith(fullKeyResult);
          });
        });
      });
    }));

    it('should call service delete method', waitForAsync(async () => {
      const spy = jest.spyOn(keyResultService, 'deleteKeyResult');
      jest
        .spyOn(component.matDialog, 'open')
        .mockReturnValue({ afterClosed: () => of('deleteKeyResult') } as MatDialogRef<typeof component>);
      jest.spyOn(component.dialogRef, 'close');

      const deleteButton = fixture.debugElement.query(By.css('[data-testId="delete"]'));
      deleteButton.nativeElement.click();
      expect(spy).toHaveBeenCalledTimes(1);
      expect(spy).toHaveBeenCalledWith(fullKeyResult.id);
    }));
  });
});
