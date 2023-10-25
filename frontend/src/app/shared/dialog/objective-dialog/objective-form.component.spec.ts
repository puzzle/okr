import { ComponentFixture, TestBed, tick } from '@angular/core/testing';

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
import { objective, quarter, teamMin1 } from '../../testData';
import { Observable, of } from 'rxjs';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { HarnessLoader } from '@angular/cdk/testing';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { Quarter } from '../../types/model/Quarter';
import { QuarterService } from '../../services/quarter.service';
import { Team } from '../../types/model/Team';
import { TeamService } from '../../services/team.service';
import { State } from '../../types/enums/State';
import { By } from '@angular/platform-browser';

const submitEvent = {
  submitter: {
    status: 'DRAFT',
    getAttribute() {
      return this.status;
    },
  },
};

let objectiveService = {
  getFullObjective: jest.fn(),
  createObjective: jest.fn(),
  updateObjective: jest.fn(),
  deleteObjective: jest.fn(),
};

const quarterService = {
  getAllQuarters(): Observable<Quarter[]> {
    return of([
      { id: 1, startDate: quarter.startDate, endDate: quarter.endDate, label: quarter.label },
      { id: 2, startDate: quarter.startDate, endDate: quarter.endDate, label: quarter.label },
    ]);
  },
};

const teamService = {
  getAllTeams(): Observable<Team[]> {
    return of([
      { id: 1, name: teamMin1.name, activeObjectives: 10 },
      { id: 4, name: 'team2', activeObjectives: 4 },
    ]);
  },
};

const dialogMock = {
  close: jest.fn(),
};

let matDataMock: { objective: { objectiveId: number | undefined; teamId: number | undefined } } = {
  objective: {
    objectiveId: undefined,
    teamId: 1,
  },
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
    fixture.detectChanges();
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it.each([['DRAFT'], ['ONGOING']])('onSubmit create', async (state: string) => {
    //Prepare data
    let title: string = 'title';
    let description: string = 'description';
    let createKeyresults: boolean = true;
    let quarter: number = 0;
    let team: number = 0;
    teamService.getAllTeams().subscribe((teams) => {
      team = teams[0].id;
    });
    quarterService.getAllQuarters().subscribe((quarters) => {
      quarter = quarters[0].id;
    });

    // Get input elements and set values
    const titleInput: HTMLInputElement = fixture.debugElement.query(By.css('[data-testId="title"]')).nativeElement;
    titleInput.value = title;
    const descriptionInput: HTMLInputElement = fixture.debugElement.query(
      By.css('[data-testId="description"]'),
    ).nativeElement;
    descriptionInput.value = description;
    const checkBox = fixture.debugElement.query(By.css('[data-testId="checkbox"]')).nativeElement;
    checkBox.click();
    const quarterSelect: HTMLSelectElement = fixture.debugElement.query(By.css('#quarter')).nativeElement;
    quarterSelect.value = quarter.toString();

    // Trigger update of form
    fixture.detectChanges();
    titleInput.dispatchEvent(new Event('input'));
    descriptionInput.dispatchEvent(new Event('input'));
    quarterSelect.dispatchEvent(new Event('input'));
    checkBox.dispatchEvent(new Event('input'));

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

  it('should create objective', () => {
    matDataMock.objective.objectiveId = undefined;
    component.objectiveForm.setValue({
      title: 'Test title',
      description: 'Test description',
      quarter: 0,
      team: 0,
      relation: 0,
      createKeyResults: false,
    });

    submitEvent.submitter.status = 'DRAFT';
    objectiveService.createObjective.mockReturnValue(of({ ...objective, state: 'DRAFT' }));
    component.onSubmit(submitEvent);

    fixture.detectChanges();

    expect(objectiveService.createObjective).toHaveBeenCalledWith({
      description: 'Test description',
      id: undefined,
      state: 'DRAFT',
      title: 'Test title',
      quarterId: 0,
      teamId: 0,
    });
  });

  it('should update objective', () => {
    matDataMock.objective.objectiveId = 1;
    component.objectiveForm.setValue({
      title: 'Test title',
      description: 'Test description',
      quarter: 1,
      team: 1,
      relation: 0,
      createKeyResults: false,
    });

    submitEvent.submitter.status = 'DRAFT';
    objectiveService.updateObjective.mockReturnValue(of({ ...objective, state: 'ONGOING' }));
    component.onSubmit(submitEvent);

    fixture.detectChanges();

    expect(objectiveService.updateObjective).toHaveBeenCalledWith({
      description: 'Test description',
      id: 1,
      state: 'DRAFT',
      title: 'Test title',
      quarterId: 1,
      teamId: 1,
    });
  });

  it('should load default values into form onInit with undefined objectiveId', () => {
    matDataMock.objective.objectiveId = undefined;
    matDataMock.objective.teamId = 1;
    component.ngOnInit();
    const rawFormValue = component.objectiveForm.getRawValue();
    const defaultComponent = component.getDefaultObjective();
    expect(rawFormValue.title).toBe(defaultComponent.title);
    expect(rawFormValue.description).toBe(defaultComponent.description);
    expect(rawFormValue.team).toBe(matDataMock.objective.teamId);
    quarterService.getAllQuarters().subscribe((quarters) => {
      expect(rawFormValue.quarter).toBe([quarters[0].id]);
    });
  });

  it('should load default values into form onInit with defined objectiveId', () => {
    matDataMock.objective.objectiveId = 1;
    objectiveService.getFullObjective.mockReturnValue(of(objective));
    component.ngOnInit();
    const rawFormValue = component.objectiveForm.getRawValue();
    expect(rawFormValue.title).toBe(objective.title);
    expect(rawFormValue.description).toBe(objective.description);
    expect(rawFormValue.team).toBe(objective.teamId);
    expect(rawFormValue.quarter).toBe(objective.quarterId);
  });
});
