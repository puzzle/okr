import { ComponentFixture, fakeAsync, TestBed, tick, waitForAsync } from '@angular/core/testing';

import { ObjectiveFormComponent } from './objective-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ObjectiveService } from '../../services/objective.service';
import { objective } from '../../testData';
import { of, throwError } from 'rxjs';
import { State } from '../../types/enums/State';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { HarnessLoader } from '@angular/cdk/testing';
import { MatInputHarness } from '@angular/material/input/testing';
import { MatSelectHarness } from '@angular/material/select/testing';
import { MatCheckboxHarness } from '@angular/material/checkbox/testing';
import { MatCheckboxModule } from '@angular/material/checkbox';

const submitEvent = {
  submitter: {
    status: 'DRAFT',
    getAttribute() {
      return this.status;
    },
  },
};

const objectiveService = {
  getFullObjective: jest.fn(),
  createObjective: jest.fn(),
  updateObjective: jest.fn(),
  deleteObjective: jest.fn(),
};

const dialogMock = {
  close: jest.fn(),
};

const matDataMock: { objectiveId: number | undefined; teamId: number | undefined } = {
  objectiveId: undefined,
  teamId: undefined,
};
describe('ObjectiveDialogComponent', () => {
  let component: ObjectiveFormComponent;
  let fixture: ComponentFixture<ObjectiveFormComponent>;
  let loader: HarnessLoader;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        MatDialogModule,
        MatIconModule,
        MatFormFieldModule,
        MatSelectModule,
        ReactiveFormsModule,
        MatInputModule,
        NoopAnimationsModule,
        MatCheckboxModule,
      ],
      declarations: [ObjectiveFormComponent],
      providers: [
        { provide: MatDialogRef, useValue: dialogMock },
        { provide: MAT_DIALOG_DATA, useValue: matDataMock },
        { provide: ObjectiveService, useValue: objectiveService },
      ],
    });
    fixture = TestBed.createComponent(ObjectiveFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it.each([['DRAFT'], ['ONGOING']])('onSubmit create', async (state: string) => {
    const inputs = loader.getAllHarnesses(MatInputHarness);
    const selects = loader.getAllHarnesses(MatSelectHarness);
    const checkbox = loader.getHarness(MatCheckboxHarness);

    await Promise.all([inputs, selects, checkbox]).then(
      fakeAsync(([inputs, selects, checkbox]: [MatInputHarness[], MatSelectHarness[], MatCheckboxHarness]) => {
        inputs[0].setValue('title');
        advance();
        inputs[1].setValue('description');
        advance();
        checkbox.check();
        advance();
        const quarterSelect = selects[0];
        const teamSelect = selects[2];
        advance();
        quarterSelect.open();
        advance();
        quarterSelect.getOptions().then((selectOptions) => {
          selectOptions[0].click();
        });
        advance();
        teamSelect.open();
        advance();
        teamSelect.getOptions().then((selectOptions) => {
          selectOptions[0].click();
        });
      }),
    );

    submitEvent.submitter.status = state;
    objectiveService.createObjective.mockReturnValue(of({ ...objective, state: state }));
    component.onSubmit(submitEvent);

    expect(dialogMock.close).toHaveBeenCalledWith({
      addKeyResult: false,
      delete: false,
      objective: {
        description: 'description',
        id: 5,
        quarterId: 2,
        state: State[state as keyof typeof State],
        teamId: 2,
        title: 'title',
      },
      teamId: 1,
    });
  });

  it('should delete', () => {
    matDataMock.objectiveId = 1;
    objectiveService.deleteObjective.mockReturnValue(of({}));
    component.deleteObjective();

    expect(dialogMock.close).toHaveBeenCalledWith({
      addKeyResult: false,
      delete: true,
      objective: {
        id: 1,
        state: undefined,
      },
      teamId: 0,
    });
  });

  it('delete fails because objective has checkin', () => {
    matDataMock.objectiveId = 1;
    objectiveService.deleteObjective.mockReturnValue(throwError(() => {}));
    component.deleteObjective();

    expect(dialogMock.close).toHaveBeenCalledWith();
  });

  it.each([
    [undefined, 'createObjective'],
    [1, 'updateObjective'],
  ])('delete or update', (id: number | undefined, funcName: string) => {
    matDataMock.objectiveId = id;
    objectiveService[funcName as keyof typeof objectiveService].mockReturnValue(of({ ...objective, state: 'DRAFT' }));
    component.onSubmit(submitEvent);

    expect(objectiveService[funcName as keyof typeof objectiveService]).toHaveBeenCalledWith({
      description: '',
      id: id,
      quarterId: 0,
      state: 'DRAFT',
      teamId: 0,
      title: '',
    });
  });

  function advance(duration = 100) {
    tick(duration);
    fixture.detectChanges();
    tick(duration);
  }
});
