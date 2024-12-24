import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';

import { ObjectiveFormComponent } from './objective-form.component';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ObjectiveService } from '../../../services/objective.service';
import { keyResult, objective, quarter, quarterList } from '../../testData';
import { Observable, of } from 'rxjs';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { HarnessLoader } from '@angular/cdk/testing';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { Quarter } from '../../types/model/Quarter';
import { QuarterService } from '../../../services/quarter.service';
import { TeamService } from '../../../services/team.service';
import { State } from '../../types/enums/State';
import { By } from '@angular/platform-browser';
import { MatCheckboxHarness } from '@angular/material/checkbox/testing';
import { RouterTestingHarness } from '@angular/router/testing';
import { TranslateTestingModule } from 'ngx-translate-testing';
// @ts-ignore
import * as de from '../../../../assets/i18n/de.json';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { DialogTemplateCoreComponent } from '../../custom/dialog-template-core/dialog-template-core.component';
import { MatDividerModule } from '@angular/material/divider';
import { Team } from '../../types/model/Team';

const objectiveService = {
  getFullObjective: jest.fn(),
  createObjective: jest.fn(),
  updateObjective: jest.fn(),
  deleteObjective: jest.fn(),
  getAllKeyResultsByObjective: jest.fn()
};

interface MatDialogDataInterface {
  objective: { objectiveId: number | undefined;
    teamId: number | undefined; };
}

const quarterService = {
  getAllQuarters(): Observable<Quarter[]> {
    return of([new Quarter(
      1, quarter.label, quarter.startDate, quarter.endDate
    ),
    new Quarter(
      2, quarter.label, quarter.startDate, quarter.endDate
    ),
    new Quarter(
      999, 'Backlog', null, null
    )]);
  },
  getCurrentQuarter(): Observable<Quarter> {
    return of(new Quarter(
      2, quarter.label, quarter.startDate, quarter.endDate
    ));
  }
};

const teamService = {
  getAllTeams: jest.fn()
};

// Mock the return value of getAllTeams
teamService.getAllTeams.mockReturnValue(of([{ id: 1,
  version: 2,
  name: 'Marketing Team',
  writeable: true,
  organisations: [] },
{ id: 4,
  version: 5,
  name: 'Team 2',
  writeable: true,
  organisations: [] }]));

const dialogMock = {
  close: jest.fn()
};

const matDataMock: MatDialogDataInterface = {
  objective: {
    objectiveId: undefined,
    teamId: 1
  }
};

const mockActivatedRoute = {
  snapshot: {
    queryParams: {
      quarter: '999'
    }
  }
};

describe('ObjectiveDialogComponent', () => {
  let component: ObjectiveFormComponent;
  let fixture: ComponentFixture<ObjectiveFormComponent>;
  let loader: HarnessLoader;

  describe('Normal Objective dialog', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [
          MatDialogModule,
          MatIconModule,
          MatFormFieldModule,
          MatSelectModule,
          ReactiveFormsModule,
          MatInputModule,
          NoopAnimationsModule,
          MatCheckboxModule,
          TranslateTestingModule.withTranslations({
            de: de
          }),
          MatDividerModule
        ],
        declarations: [ObjectiveFormComponent,
          DialogTemplateCoreComponent],
        providers: [
          provideRouter([]),
          provideHttpClient(),
          provideHttpClientTesting(),
          { provide: MatDialogRef,
            useValue: dialogMock },
          { provide: MAT_DIALOG_DATA,
            useValue: matDataMock },
          { provide: ObjectiveService,
            useValue: objectiveService },
          { provide: QuarterService,
            useValue: quarterService },
          { provide: TeamService,
            useValue: teamService }
        ]
      });
      fixture = TestBed.createComponent(ObjectiveFormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      loader = TestbedHarnessEnvironment.loader(fixture);
    });

    it('should create', () => {
      expect(component)
        .toBeTruthy();
    });

    it.each([['DRAFT'],
      ['ONGOING']])('onSubmit create',
      fakeAsync((state: string) => {
        // Prepare data
        const title = 'title';
        const description = 'description';
        const createKeyResults = true;
        let quarter = 0;
        let team = 0;
        teamService.getAllTeams()
          .subscribe((teams: { id: number }[]) => {
            team = teams[0].id;
          });
        quarterService.getAllQuarters()
          .subscribe((quarters) => {
            quarter = quarters[1].id;
          });

        // Get input elements and set values
        const titleInput: HTMLInputElement = fixture.debugElement.query(By.css('[data-testId="title"]')).nativeElement;
        titleInput.value = title;
        const descriptionInput: HTMLInputElement = fixture.debugElement.query(By.css('[data-testId="description"]')).nativeElement;
        descriptionInput.value = description;
        loader.getHarness(MatCheckboxHarness)
          .then((checkBox) => checkBox.check());
        tick(200);
        const quarterSelect: HTMLSelectElement = fixture.debugElement.query(By.css('[data-testId="quarterSelect"]')).nativeElement;
        quarterSelect.value = quarter.toString();
        // Trigger update of form
        fixture.detectChanges();
        titleInput.dispatchEvent(new Event('input'));
        descriptionInput.dispatchEvent(new Event('input'));
        quarterSelect.dispatchEvent(new Event('change'));

        const rawFormValue = component.objectiveForm.getRawValue();
        expect(rawFormValue.description)
          .toBe(description);
        expect(rawFormValue.quarter)
          .toBe(quarter.toString());
        expect(rawFormValue.team)
          .toBe(team);
        expect(rawFormValue.title)
          .toBe(title);
        expect(rawFormValue.createKeyResults)
          .toBe(createKeyResults);

        objectiveService.createObjective.mockReturnValue(of({ ...objective,
          state: state }));
        component.onSubmit(state);

        expect(dialogMock.close)
          .toHaveBeenCalledWith({
            addKeyResult: createKeyResults,
            delete: false,
            objective: {
              description: description,
              id: 5,
              version: 1,
              quarterId: 2,
              quarterLabel: 'GJ 22/23-Q2',
              state: State[state as keyof typeof State],
              teamId: 2,
              title: title,
              isWriteable: true
            },
            teamId: 1
          });
      }));

    it('should create objective', () => {
      matDataMock.objective.objectiveId = undefined;
      component.objectiveForm.setValue({
        title: 'Test title',
        description: 'Test description',
        quarter: 0,
        team: 0,
        relation: 0,
        createKeyResults: false,
        keyResults: []
      });

      objectiveService.createObjective.mockReturnValue(of({ ...objective,
        state: 'DRAFT' }));
      component.onSubmit('DRAFT');

      fixture.detectChanges();

      expect(objectiveService.createObjective)
        .toHaveBeenCalledWith({
          description: 'Test description',
          id: undefined,
          state: 'DRAFT',
          title: 'Test title',
          quarterId: 0,
          teamId: 0
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
        keyResults: []
      });

      objectiveService.updateObjective.mockReturnValue(of({ ...objective,
        state: 'ONGOING' }));
      component.onSubmit('DRAFT');

      fixture.detectChanges();

      expect(objectiveService.updateObjective)
        .toHaveBeenCalledWith({
          description: 'Test description',
          id: 1,
          state: 'DRAFT',
          title: 'Test title',
          quarterId: 1,
          teamId: 1
        });
    });

    it('should load default values into form onInit with undefined objectiveId', () => {
      matDataMock.objective.objectiveId = undefined;
      matDataMock.objective.teamId = 1;
      component.ngOnInit();
      const rawFormValue = component.objectiveForm.getRawValue();
      const defaultComponent = component.getDefaultObjective();
      expect(rawFormValue.title)
        .toBe(defaultComponent.title);
      expect(rawFormValue.description)
        .toBe(defaultComponent.description);
      expect(rawFormValue.team)
        .toBe(matDataMock.objective.teamId);
      quarterService.getAllQuarters()
        .subscribe((quarters) => {
          expect(rawFormValue.quarter)
            .toBe([quarters[0].id]);
        });
    });

    it('should load default values into form onInit with defined objectiveId', async() => {
      matDataMock.objective.objectiveId = 1;
      const routerHarness = await RouterTestingHarness.create();
      await routerHarness.navigateByUrl('/?quarter=2');
      objectiveService.getFullObjective.mockReturnValue(of(objective));
      objectiveService.getAllKeyResultsByObjective.mockReturnValue(of(keyResult));
      component.ngOnInit();
      const rawFormValue = component.objectiveForm.getRawValue();
      expect(rawFormValue.title)
        .toBe(objective.title);
      expect(rawFormValue.description)
        .toBe(objective.description);
      expect(rawFormValue.team)
        .toBe(objective.teamId);
      expect(rawFormValue.quarter)
        .toBe(objective.quarterId);
    });

    it('should set teams$ observable correctly on ngOnInit', () => {
      const mockTeams = [{ id: 1,
        name: 'Team A' },
      { id: 2,
        name: 'Team B' }];
      teamService.getAllTeams.mockReturnValue(of(mockTeams));

      component.ngOnInit();

      component.teams$.subscribe((teams: Team[]) => {
        expect(teams)
          .toEqual(mockTeams);
      });
    });

    it('should set keyResults$ observable correctly when objectiveId is defined', () => {
      const mockKeyResults = [{ id: 1,
        name: 'Key Result A' }];
      matDataMock.objective.objectiveId = 1;
      objectiveService.getAllKeyResultsByObjective.mockReturnValue(of(mockKeyResults));

      component.ngOnInit();

      component.keyResults$.subscribe((keyResults) => {
        expect(keyResults)
          .toEqual(mockKeyResults);
      });
    });

    it('should return correct value if allowed to save to backlog', async() => {
      component.quarters = quarterList;
      const isBacklogQuarterSpy = jest.spyOn(component, 'isBacklogQuarter');
      isBacklogQuarterSpy.mockReturnValue(false);

      component.data.action = 'duplicate';
      fixture.detectChanges();
      expect(component.allowedToSaveBacklog())
        .toBeTruthy();

      component.objectiveForm.controls.quarter.setValue(999);
      component.data.action = '';
      component.data.objective.objectiveId = 5;
      component.state = 'DRAFT';
      fixture.detectChanges();
      expect(component.allowedToSaveBacklog())
        .toBeTruthy();

      component.state = 'ONGOING';
      fixture.detectChanges();
      expect(component.allowedToSaveBacklog())
        .toBeFalsy();

      component.objectiveForm.controls.quarter.setValue(2);
      isBacklogQuarterSpy.mockReturnValue(true);
      fixture.detectChanges();
      expect(component.allowedToSaveBacklog())
        .toBeTruthy();

      component.objectiveForm.controls.quarter.setValue(999);
      component.data.objective.objectiveId = undefined;
      isBacklogQuarterSpy.mockReturnValue(false);
      fixture.detectChanges();
      expect(component.allowedToSaveBacklog())
        .toBeFalsy();

      component.objectiveForm.controls.quarter.setValue(2);
      isBacklogQuarterSpy.mockReturnValue(true);
      fixture.detectChanges();
      expect(component.allowedToSaveBacklog())
        .toBeTruthy();
    });

    it('should return if option is allowed for quarter select', async() => {
      const quarter: Quarter = new Quarter(
        1, 'Backlog', null, null
      );

      const data = {
        action: 'duplicate',
        objective: {
          objectiveId: 22
        }
      };
      component.data = data;
      component.state = 'DRAFT';
      fixture.detectChanges();

      expect(component.allowedOption(quarter))
        .toBeTruthy();

      expect(component.allowedOption(quarter))
        .toBeTruthy();
      data.action = 'releaseBacklog';
      fixture.detectChanges();
      expect(component.allowedOption(quarter))
        .toBeFalsy();

      data.action = 'edit';
      fixture.detectChanges();

      expect(component.allowedOption(quarter))
        .toBeTruthy();

      component.state = 'ONGOING';
      fixture.detectChanges();
      expect(component.allowedOption(quarter))
        .toBeFalsy();

      component.data = {
        action: 'duplicate',
        objective: {}
      };

      fixture.detectChanges();
      expect(component.allowedOption(quarter))
        .toBeTruthy();
    });
  });

  describe('Backlog quarter', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [
          MatDialogModule,
          MatIconModule,
          MatFormFieldModule,
          MatSelectModule,
          ReactiveFormsModule,
          MatInputModule,
          NoopAnimationsModule,
          MatCheckboxModule,
          TranslateTestingModule.withTranslations({
            de: de
          }),
          MatDividerModule
        ],
        declarations: [ObjectiveFormComponent,
          DialogTemplateCoreComponent],
        providers: [
          provideRouter([]),
          provideHttpClient(),
          provideHttpClientTesting(),
          { provide: MatDialogRef,
            useValue: dialogMock },
          { provide: MAT_DIALOG_DATA,
            useValue: matDataMock },
          { provide: ObjectiveService,
            useValue: objectiveService },
          { provide: QuarterService,
            useValue: quarterService },
          { provide: TeamService,
            useValue: teamService },
          { provide: ActivatedRoute,
            useValue: mockActivatedRoute }
        ]
      });
      fixture = TestBed.createComponent(ObjectiveFormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      loader = TestbedHarnessEnvironment.loader(fixture);
    });

    it('should create', () => {
      expect(component)
        .toBeTruthy();
    });

    it('should set correct default value if objective is released in backlog', async() => {
      component.data = {
        objective: {
          objectiveId: 1,
          teamId: 1
        },
        action: 'releaseBacklog'
      };

      const isBacklogQuarterSpy = jest.spyOn(component, 'isBacklogQuarter');
      isBacklogQuarterSpy.mockReturnValue(false);

      const routerHarness = await RouterTestingHarness.create();
      await routerHarness.navigateByUrl('/?quarter=999');
      objectiveService.getFullObjective.mockReturnValue(of(objective));
      fixture.detectChanges();
      component.ngOnInit();

      const rawFormValue = component.objectiveForm.getRawValue();
      expect(rawFormValue.title)
        .toBe(objective.title);
      expect(rawFormValue.description)
        .toBe(objective.description);
      expect(rawFormValue.team)
        .toBe(objective.teamId);
      expect(rawFormValue.quarter).not.toBe(999);
      expect(rawFormValue.quarter)
        .toBe(2);
    });
  });
});
