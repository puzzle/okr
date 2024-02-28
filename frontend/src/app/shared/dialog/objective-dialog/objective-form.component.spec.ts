import { ComponentFixture, TestBed } from '@angular/core/testing';

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
import { objective, objectiveWithAlignment, quarter, quarterList, teamMin1 } from '../../testData';
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
import { MatCheckboxHarness } from '@angular/material/checkbox/testing';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { DialogHeaderComponent } from '../../custom/dialog-header/dialog-header.component';
import { TranslateTestingModule } from 'ngx-translate-testing';
import * as de from '../../../../assets/i18n/de.json';
import { ActivatedRoute } from '@angular/router';

let objectiveService = {
  getFullObjective: jest.fn(),
  createObjective: jest.fn(),
  updateObjective: jest.fn(),
  deleteObjective: jest.fn(),
  getAlignmentPossibilities: jest.fn(),
};

const quarterService = {
  getAllQuarters(): Observable<Quarter[]> {
    return of([
      { id: 1, startDate: quarter.startDate, endDate: quarter.endDate, label: quarter.label },
      { id: 2, startDate: quarter.startDate, endDate: quarter.endDate, label: quarter.label },
      { id: 199, startDate: null, endDate: null, label: 'Backlog' },
    ]);
  },
};

const teamService = {
  getAllTeams(): Observable<Team[]> {
    return of([
      { id: 1, version: 2, name: teamMin1.name, writeable: true, organisations: [], filterIsActive: true },
      { id: 4, version: 5, name: 'team2', writeable: true, organisations: [], filterIsActive: true },
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

const mockActivatedRoute = {
  snapshot: {
    queryParams: {
      quarter: '199',
    },
  },
};

const alignmentPossibilities = [
  {
    objectiveId: 1003,
    objectiveTitle: 'O - Test Objective',
    keyResultAlignmentsDtos: [],
  },
  {
    objectiveId: 1005,
    objectiveTitle: 'O - Company will grow',
    keyResultAlignmentsDtos: [
      {
        keyResultId: 6,
        keyResultTitle: 'K - New structure',
      },
    ],
  },
];

describe('ObjectiveDialogComponent', () => {
  let component: ObjectiveFormComponent;
  let fixture: ComponentFixture<ObjectiveFormComponent>;
  let loader: HarnessLoader;

  describe('Normal Objective dialog', () => {
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
          RouterTestingModule,
          TranslateTestingModule.withTranslations({
            de: de,
          }),
        ],
        declarations: [ObjectiveFormComponent, DialogHeaderComponent],
        providers: [
          { provide: MatDialogRef, useValue: dialogMock },
          { provide: MAT_DIALOG_DATA, useValue: matDataMock },
          { provide: ObjectiveService, useValue: objectiveService },
          { provide: QuarterService, useValue: quarterService },
          { provide: TeamService, useValue: teamService },
        ],
      });
      jest.spyOn(objectiveService, 'getAlignmentPossibilities').mockReturnValue(of(alignmentPossibilities));
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
        quarter = quarters[1].id;
      });

      // Get input elements and set values
      const titleInput: HTMLInputElement = fixture.debugElement.query(By.css('[data-testId="title"]')).nativeElement;
      titleInput.value = title;
      const descriptionInput: HTMLInputElement = fixture.debugElement.query(
        By.css('[data-testId="description"]'),
      ).nativeElement;
      descriptionInput.value = description;
      const checkBox = await loader.getHarness(MatCheckboxHarness);
      await checkBox.check();
      const quarterSelect: HTMLSelectElement = fixture.debugElement.query(By.css('#quarter')).nativeElement;
      quarterSelect.value = quarter.toString();

      // Trigger update of form
      fixture.detectChanges();
      titleInput.dispatchEvent(new Event('input'));
      descriptionInput.dispatchEvent(new Event('input'));
      quarterSelect.dispatchEvent(new Event('input'));

      const rawFormValue = component.objectiveForm.getRawValue();
      expect(rawFormValue.description).toBe(description);
      expect(rawFormValue.quarter).toBe(quarter);
      expect(rawFormValue.team).toBe(team);
      expect(rawFormValue.title).toBe(title);
      expect(rawFormValue.createKeyResults).toBe(createKeyresults);

      objectiveService.createObjective.mockReturnValue(of({ ...objective, state: state }));
      component.onSubmit(state);

      expect(dialogMock.close).toHaveBeenCalledWith({
        addKeyResult: createKeyresults,
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
          writeable: true,
          alignedEntityId: null,
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
        alignment: '',
        createKeyResults: false,
      });

      objectiveService.createObjective.mockReturnValue(of({ ...objective, state: 'DRAFT' }));
      component.onSubmit('DRAFT');

      fixture.detectChanges();

      expect(objectiveService.createObjective).toHaveBeenCalledWith({
        description: 'Test description',
        id: undefined,
        state: 'DRAFT',
        title: 'Test title',
        quarterId: 0,
        teamId: 0,
        version: undefined,
        alignedEntityId: '',
      });
    });

    it('should create objective with alignment', () => {
      matDataMock.objective.objectiveId = undefined;
      component.objectiveForm.setValue({
        title: 'Test title with alignment',
        description: 'Test description',
        quarter: 0,
        team: 0,
        alignment: 'K37',
        createKeyResults: false,
      });

      objectiveService.createObjective.mockReturnValue(of({ ...objective, state: 'DRAFT' }));
      component.onSubmit('DRAFT');

      fixture.detectChanges();

      expect(objectiveService.createObjective).toHaveBeenCalledWith({
        description: 'Test description',
        id: undefined,
        state: 'DRAFT',
        title: 'Test title with alignment',
        quarterId: 0,
        teamId: 0,
        version: undefined,
        alignedEntityId: 'K37',
      });
    });

    it('should update objective', () => {
      matDataMock.objective.objectiveId = 1;
      component.objectiveForm.setValue({
        title: 'Test title',
        description: 'Test description',
        quarter: 1,
        team: 1,
        alignment: '',
        createKeyResults: false,
      });

      objectiveService.updateObjective.mockReturnValue(of({ ...objective, state: 'ONGOING' }));
      component.onSubmit('DRAFT');

      fixture.detectChanges();

      expect(objectiveService.updateObjective).toHaveBeenCalledWith({
        description: 'Test description',
        id: 1,
        state: 'DRAFT',
        title: 'Test title',
        quarterId: 1,
        teamId: 1,
        version: undefined,
        alignedEntityId: '',
      });
    });

    it('should update objective with alignment', () => {
      objectiveService.updateObjective.mockReset();
      matDataMock.objective.objectiveId = 1;
      component.state = 'DRAFT';
      component.objectiveForm.setValue({
        title: 'Test title with alignment',
        description: 'Test description',
        quarter: 1,
        team: 1,
        alignment: 'K37',
        createKeyResults: false,
      });

      objectiveService.updateObjective.mockReturnValue(of({ ...objective, state: 'ONGOING' }));
      fixture.detectChanges();

      component.onSubmit('DRAFT');

      expect(objectiveService.updateObjective).toHaveBeenCalledWith({
        description: 'Test description',
        id: 1,
        state: 'DRAFT',
        title: 'Test title with alignment',
        quarterId: 1,
        teamId: 1,
        version: undefined,
        alignedEntityId: 'K37',
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

    it('should load default values into form onInit with defined objectiveId', async () => {
      matDataMock.objective.objectiveId = 1;
      const routerHarness = await RouterTestingHarness.create();
      await routerHarness.navigateByUrl('/?quarter=2');
      objectiveService.getFullObjective.mockReturnValue(of(objective));
      component.ngOnInit();
      const rawFormValue = component.objectiveForm.getRawValue();
      expect(rawFormValue.title).toBe(objective.title);
      expect(rawFormValue.description).toBe(objective.description);
      expect(rawFormValue.team).toBe(objective.teamId);
      expect(rawFormValue.quarter).toBe(objective.quarterId);
    });

    it('should load default values into form onInit with defined objectiveId with an alignment', async () => {
      matDataMock.objective.objectiveId = 1;
      const routerHarness = await RouterTestingHarness.create();
      await routerHarness.navigateByUrl('/?quarter=2');
      objectiveService.getFullObjective.mockReturnValue(of(objectiveWithAlignment));
      component.ngOnInit();
      const rawFormValue = component.objectiveForm.getRawValue();
      expect(rawFormValue.title).toBe(objectiveWithAlignment.title);
      expect(rawFormValue.description).toBe(objectiveWithAlignment.description);
      expect(rawFormValue.team).toBe(objectiveWithAlignment.teamId);
      expect(rawFormValue.quarter).toBe(objectiveWithAlignment.quarterId);
      expect(rawFormValue.alignment).toBe(objectiveWithAlignment.alignedEntityId);
    });

    it('should return correct value if allowed to save to backlog', async () => {
      component.quarters = quarterList;
      const isBacklogQuarterSpy = jest.spyOn(component, 'isBacklogQuarter');
      isBacklogQuarterSpy.mockReturnValue(false);

      component.data.action = 'duplicate';
      fixture.detectChanges();
      expect(component.allowedToSaveBacklog()).toBeTruthy();

      component.objectiveForm.controls.quarter.setValue(999);
      component.data.action = '';
      component.data.objective.objectiveId = 5;
      component.state = 'DRAFT';
      fixture.detectChanges();
      expect(component.allowedToSaveBacklog()).toBeTruthy();

      component.state = 'ONGOING';
      fixture.detectChanges();
      expect(component.allowedToSaveBacklog()).toBeFalsy();

      component.objectiveForm.controls.quarter.setValue(2);
      isBacklogQuarterSpy.mockReturnValue(true);
      fixture.detectChanges();
      expect(component.allowedToSaveBacklog()).toBeTruthy();

      component.objectiveForm.controls.quarter.setValue(999);
      component.data.objective.objectiveId = undefined;
      isBacklogQuarterSpy.mockReturnValue(false);
      fixture.detectChanges();
      expect(component.allowedToSaveBacklog()).toBeFalsy();

      component.objectiveForm.controls.quarter.setValue(2);
      isBacklogQuarterSpy.mockReturnValue(true);
      fixture.detectChanges();
      expect(component.allowedToSaveBacklog()).toBeTruthy();
    });

    it('should return if option is allowed for quarter select', async () => {
      let quarter: Quarter = {
        id: 199,
        label: 'GJ 22/23-Q3',
        startDate: null,
        endDate: null,
      };
      let data = {
        action: 'duplicate',
        objective: {
          objectiveId: 22,
        },
      };
      component.data = data;
      component.state = 'DRAFT';
      fixture.detectChanges();

      expect(component.allowedOption(quarter)).toBeTruthy();

      quarter.label = 'Backlog';
      expect(component.allowedOption(quarter)).toBeTruthy();
      data.action = 'releaseBacklog';
      fixture.detectChanges();
      expect(component.allowedOption(quarter)).toBeFalsy();

      data.action = 'edit';
      fixture.detectChanges();

      expect(component.allowedOption(quarter)).toBeTruthy();

      component.state = 'ONGOING';
      fixture.detectChanges();
      expect(component.allowedOption(quarter)).toBeFalsy();

      component.data = {
        action: 'duplicate',
        objective: {},
      };

      fixture.detectChanges();
      expect(component.allowedOption(quarter)).toBeTruthy();
    });

    it('should load correct alignment possibilities', async () => {
      let generatedPossibilities = [
        {
          objectiveId: null,
          objectiveTitle: 'Bitte wählen',
          keyResultAlignmentsDtos: [],
        },
        {
          objectiveId: 1003,
          objectiveTitle: 'O - Test Objective',
          keyResultAlignmentsDtos: [],
        },
        {
          objectiveId: 1005,
          objectiveTitle: 'O - Company will grow',
          keyResultAlignmentsDtos: [
            {
              keyResultId: 6,
              keyResultTitle: 'K - New structure',
            },
          ],
        },
      ];

      let componentValue = null;
      component.alignmentPossibilities$.subscribe((value) => {
        componentValue = value;
      });
      expect(componentValue).toStrictEqual(generatedPossibilities);
    });

    it('should not load current Objective to alignment possibilities', async () => {
      matDataMock.objective.objectiveId = 1;
      component.objective = objectiveWithAlignment;
      const routerHarness = await RouterTestingHarness.create();
      await routerHarness.navigateByUrl('/?quarter=2');
      objectiveService.getFullObjective.mockReturnValue(of(objectiveWithAlignment));
      component.ngOnInit();

      let generatedPossibilities = [
        {
          objectiveId: null,
          objectiveTitle: 'Bitte wählen',
          keyResultAlignmentsDtos: [],
        },
        {
          objectiveId: 1003,
          objectiveTitle: 'O - Test Objective',
          keyResultAlignmentsDtos: [],
        },
        {
          objectiveId: 1005,
          objectiveTitle: 'O - Company will grow',
          keyResultAlignmentsDtos: [
            {
              keyResultId: 6,
              keyResultTitle: 'K - New structure',
            },
          ],
        },
      ];

      let componentValue = null;
      component.alignmentPossibilities$.subscribe((value) => {
        componentValue = value;
      });
      expect(componentValue).toStrictEqual(generatedPossibilities);
    });

    it('should call ObjectiveService when updating Alignments', async () => {
      component.updateAlignments();
      expect(objectiveService.getAlignmentPossibilities).toHaveBeenCalled();
    });
  });

  describe('Backlog quarter', () => {
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
          RouterTestingModule,
          TranslateTestingModule.withTranslations({
            de: de,
          }),
        ],
        declarations: [ObjectiveFormComponent, DialogHeaderComponent],
        providers: [
          { provide: MatDialogRef, useValue: dialogMock },
          { provide: MAT_DIALOG_DATA, useValue: matDataMock },
          { provide: ObjectiveService, useValue: objectiveService },
          { provide: QuarterService, useValue: quarterService },
          { provide: TeamService, useValue: teamService },
          { provide: ActivatedRoute, useValue: mockActivatedRoute },
        ],
      });
      jest.spyOn(objectiveService, 'getAlignmentPossibilities').mockReturnValue(of(alignmentPossibilities));
      fixture = TestBed.createComponent(ObjectiveFormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      loader = TestbedHarnessEnvironment.loader(fixture);
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should set correct default value if objective is released in backlog', async () => {
      component.data = {
        objective: {
          objectiveId: 1,
          teamId: 1,
        },
        action: 'releaseBacklog',
      };

      const isBacklogQuarterSpy = jest.spyOn(component, 'isBacklogQuarter');
      isBacklogQuarterSpy.mockReturnValue(false);

      const routerHarness = await RouterTestingHarness.create();
      await routerHarness.navigateByUrl('/?quarter=199');
      objectiveService.getFullObjective.mockReturnValue(of(objective));
      fixture.detectChanges();
      component.ngOnInit();

      const rawFormValue = component.objectiveForm.getRawValue();
      expect(rawFormValue.title).toBe(objective.title);
      expect(rawFormValue.description).toBe(objective.description);
      expect(rawFormValue.team).toBe(objective.teamId);
      expect(rawFormValue.quarter).not.toBe(199);
      expect(rawFormValue.quarter).toBe(2);
    });
  });
});
