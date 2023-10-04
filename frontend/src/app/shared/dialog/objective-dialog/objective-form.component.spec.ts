import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';

import { ObjectiveFormComponent } from './objective-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ObjectiveService } from '../../services/objective.service';
import { objective, quarter, team1 } from '../../testData';
import { Observable, of, throwError } from 'rxjs';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { HarnessLoader } from '@angular/cdk/testing';
import { MatInputHarness } from '@angular/material/input/testing';
import { MatSelectHarness } from '@angular/material/select/testing';
import { MatCheckboxHarness } from '@angular/material/checkbox/testing';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { Quarter } from '../../types/model/Quarter';
import { QuarterService } from '../../services/quarter.service';
import { Team } from '../../types/model/Team';
import { TeamService } from '../../services/team.service';
import { State } from '../../types/enums/State';

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

const quarterService = {
  getAllQuarters(): Observable<Quarter[]> {
    return of([{ id: 1, startDate: quarter.startDate, endDate: quarter.endDate, label: quarter.label }]);
  },
};

const teamService = {
  getAllTeams(): Observable<Team[]> {
    return of([{ id: 1, name: team1.name, activeObjectives: 10 }]);
  },
};

const dialogMock = {
  close: jest.fn(),
};

const matDataMock: { objectiveId: number | undefined; teamId: number | undefined } = {
  objectiveId: undefined,
  teamId: 1,
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
        { provide: QuarterService, useValue: quarterService },
        { provide: TeamService, useValue: teamService },
      ],
    });
    fixture = TestBed.createComponent(ObjectiveFormComponent);
    component = fixture.componentInstance;
    // component.objectiveForm.reset({createKeyResults: false, description: '', title: '', quarter: 0, team: 0});
    objectiveService.createObjective.mockClear();
    fixture.detectChanges();
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it.each([['DRAFT'], ['ONGOING']])('onSubmit create', async (state: string) => {
    let title: string = '';
    let description: string = '';
    let createKeyresults: boolean = true;
    let quarter: number = 0;
    let team: number = 0;

    const inputs = loader.getAllHarnesses(MatInputHarness);
    const selects = loader.getAllHarnesses(MatSelectHarness);
    const checkbox = loader.getHarness(MatCheckboxHarness);

    await Promise.all([inputs, selects, checkbox]).then(
      fakeAsync(([inputs, selects, checkbox]: [MatInputHarness[], MatSelectHarness[], MatCheckboxHarness]) => {
        inputs[0].setValue('title');
        advance();
        inputs[0].getValue().then((value) => {
          title = value;
        });
        advance();
        inputs[1].setValue('description');
        advance();
        inputs[1].getValue().then((value) => {
          description = value;
        });
        advance();
        checkbox.uncheck();
        advance();
        checkbox.isChecked().then((isChecked) => {
          createKeyresults = isChecked;
        });
        advance();
        const quarterSelect = selects[0];
        advance();
        quarterSelect.open().then(() => {
          quarterSelect.getOptions().then((selectOptions) => {
            selectOptions[0].click();
            advance();
            selectOptions[0].getText().then((value) => {
              quarterService.getAllQuarters().subscribe((options) => {
                quarter = options[0].id;
              });
            });
          });
        });
        teamService.getAllTeams().subscribe((teams) => {
          team = teams[0].id;
        });
        advance();
      }),
    );

    const rawFormValue = component.objectiveForm.getRawValue();
    expect(rawFormValue.description).toBe(description);
    expect(rawFormValue.quarter).toBe(quarter);
    expect(rawFormValue.team).toBe(team);
    expect(rawFormValue.title).toBe(title);
    expect(rawFormValue.createKeyResults).toBe(createKeyresults);

    submitEvent.submitter.status = state;
    objectiveService.createObjective.mockReturnValue(of({ ...objective, state: state }));
    component.onSubmit(submitEvent);

    expect(dialogMock.close).toHaveBeenCalledWith({
      addKeyResult: createKeyresults,
      delete: false,
      objective: {
        description: description,
        id: 5,
        quarterId: 2,
        state: State[state as keyof typeof State],
        teamId: 2,
        title: title,
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
      teamId: 1,
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
  ])('create or update', (id: number | undefined, funcName: string) => {
    matDataMock.objectiveId = id;
    objectiveService[funcName as keyof typeof objectiveService].mockReturnValue(of({ ...objective, state: 'DRAFT' }));
    component.onSubmit(submitEvent);

    fixture.detectChanges();

    expect(objectiveService[funcName as keyof typeof objectiveService]).toHaveBeenCalledWith({
      description: '',
      id: id,
      quarterId: 1,
      state: 'DRAFT',
      teamId: 1,
      title: '',
    });
  });

  // it('should create Objective', () => {
  //   matDataMock.objectiveId = 1;
  //   objectiveService.updateObjective.mockReturnValue(of({...objective, state: 'DRAFT'}));
  //   component.onSubmit(submitEvent);
  //
  //   fixture.detectChanges();
  //
  //   expect(objectiveService.updateObjective).toHaveBeenCalledWith({
  //     description: '',
  //     id: 1,
  //     quarterId: 1,
  //     state: 'DRAFT',
  //     teamId: 1,
  //     title: '',
  //   });
  // });

  function advance(duration = 100) {
    tick(duration);
    fixture.detectChanges();
    tick(duration);
  }
});
