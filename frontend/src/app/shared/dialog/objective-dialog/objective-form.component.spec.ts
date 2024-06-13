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
import {
    alignmentObject2, alignmentObject3,
    marketingTeamWriteable,
    objective,
    objectiveWithAlignment,
    quarter,
    quarterList
} from '../../testData';
import { Observable, of } from 'rxjs';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { HarnessLoader } from '@angular/cdk/testing';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { Quarter } from '../../types/model/Quarter';
import { QuarterService } from '../../../services/quarter.service';
import { Team } from '../../types/model/Team';
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
import { ActivatedRoute } from '@angular/router';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { AlignmentPossibility } from '../../types/model/AlignmentPossibility';
import { ElementRef } from '@angular/core';

let objectiveService = {
  getFullObjective: jest.fn(),
  createObjective: jest.fn(),
  updateObjective: jest.fn(),
  deleteObjective: jest.fn(),
  getAlignmentPossibilities: jest.fn(),
};

interface MatDialogDataInterface {
  objective: { objectiveId: number | undefined; teamId: number | undefined };
}

const quarterService = {
  getAllQuarters(): Observable<Quarter[]> {
    return of([
      new Quarter(1, quarter.label, quarter.startDate, quarter.endDate),
      new Quarter(2, quarter.label, quarter.startDate, quarter.endDate),
      new Quarter(999, 'Backlog', null, null),
    ]);
  },
  getCurrentQuarter(): Observable<Quarter> {
    return of(new Quarter(2, quarter.label, quarter.startDate, quarter.endDate));
  },
};

const teamService = {
  getAllTeams(): Observable<Team[]> {
    return of([
      { id: 1, version: 2, name: marketingTeamWriteable.name, writeable: true, organisations: [] },
      { id: 4, version: 5, name: 'team2', writeable: true, organisations: [] },
    ]);
  },
};

const dialogMock = {
  close: jest.fn(),
};

let matDataMock: MatDialogDataInterface = {
  objective: {
    objectiveId: undefined,
    teamId: 1,
  },
};

const mockActivatedRoute = {
  snapshot: {
    queryParams: {
      quarter: '999',
    },
  },
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
          MatAutocompleteModule,
          NoopAnimationsModule,
          MatCheckboxModule,
          TranslateTestingModule.withTranslations({
            de: de,
          }),
          MatDividerModule,
        ],
        declarations: [ObjectiveFormComponent, DialogTemplateCoreComponent],
        providers: [
          provideRouter([]),
          provideHttpClient(),
          provideHttpClientTesting(),
          { provide: MatDialogRef, useValue: dialogMock },
          { provide: MAT_DIALOG_DATA, useValue: matDataMock },
          { provide: ObjectiveService, useValue: objectiveService },
          { provide: QuarterService, useValue: quarterService },
          { provide: TeamService, useValue: teamService },
        ],
      });
      jest.spyOn(objectiveService, 'getAlignmentPossibilities').mockReturnValue(of([]));
      fixture = TestBed.createComponent(ObjectiveFormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      loader = TestbedHarnessEnvironment.loader(fixture);
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it.each([['DRAFT'], ['ONGOING']])(
      'onSubmit create',
      fakeAsync((state: string) => {
        objectiveService.getAlignmentPossibilities.mockReturnValue(of([alignmentPossibility1, alignmentPossibility2]));

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
        quarter = quarters[2].id;
      });

        // Get input elements and set values
        const titleInput: HTMLInputElement = fixture.debugElement.query(By.css('[data-testId="title"]')).nativeElement;
        titleInput.value = title;
        const descriptionInput: HTMLInputElement = fixture.debugElement.query(
          By.css('[data-testId="description"]'),
        ).nativeElement;
        descriptionInput.value = description;
        loader.getHarness(MatCheckboxHarness).then((checkBox) => checkBox.check());
        tick(200);
        const quarterSelect: HTMLSelectElement = fixture.debugElement.query(
          By.css('[data-testId="quarterSelect"]'),
        ).nativeElement;
        quarterSelect.value = quarter.toString();
        // Trigger update of form
        fixture.detectChanges();
        titleInput.dispatchEvent(new Event('input'));
        descriptionInput.dispatchEvent(new Event('input'));
        quarterSelect.dispatchEvent(new Event('change'));

        const rawFormValue = component.objectiveForm.getRawValue();
        expect(rawFormValue.description).toBe(description);
        expect(rawFormValue.quarter).toBe(quarter.toString());
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
          alignedEntity: null,
        },
        teamId: 1,
      });
    }),
    );

    it('should create objective', () => {
      matDataMock.objective.objectiveId = undefined;
      component.objectiveForm.setValue({
        title: 'Test title',
        description: 'Test description',
        quarter: 0,
        team: 0,
        alignment: null,
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
        alignedEntity: null,
      });
    });

    it('should create objective with alignment objective', () => {
      matDataMock.objective.objectiveId = undefined;
      component.objectiveForm.setValue({
        title: 'Test title with alignment',
        description: 'Test description',
        quarter: 0,
        team: 0,
        alignment: alignmentObject2,
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
        alignedEntity: { id: 2, type: 'objective' },
      });
    });

    it('should create objective with alignment keyResult', () => {
      matDataMock.objective.objectiveId = undefined;
      component.objectiveForm.setValue({
        title: 'Test title with alignment',
        description: 'Test description',
        quarter: 0,
        team: 0,
        alignment: alignmentObject3,
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
        alignedEntity: { id: 1, type: 'keyResult' },
      });
    });

    it('should update objective', () => {
      matDataMock.objective.objectiveId = 1;
      component.objectiveForm.setValue({
        title: 'Test title',
        description: 'Test description',
        quarter: 1,
        team: 1,
        alignment: null,
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
        alignedEntity: null,
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
        alignment: alignmentObject3,
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
        alignedEntity: { id: 1, type: 'keyResult' },
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
      objectiveService.getAlignmentPossibilities.mockReturnValue(of([alignmentPossibility1, alignmentPossibility2]));
      objectiveService.getFullObjective.mockReturnValue(of(objectiveWithAlignment));
      component.ngOnInit();
      const rawFormValue = component.objectiveForm.getRawValue();
      expect(rawFormValue.title).toBe(objectiveWithAlignment.title);
      expect(rawFormValue.description).toBe(objectiveWithAlignment.description);
      expect(rawFormValue.team).toBe(objectiveWithAlignment.teamId);
      expect(rawFormValue.quarter).toBe(objectiveWithAlignment.quarterId);
      expect(rawFormValue.alignment).toBe(alignmentObject2);
    });

    it('should return correct value if allowed to save to backlog', async () => {
      component.quarters = quarterList;
      const isBacklogQuarterSpy = jest.spyOn(component, 'isNotBacklogQuarter');
      isBacklogQuarterSpy.mockReturnValue(true);

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
      isBacklogQuarterSpy.mockReturnValue(false);
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
      let quarter: Quarter = new Quarter(1, 'Backlog', null, null);

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
  });

  describe('AlignmentPossibilities', () => {
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
          MatAutocompleteModule,
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

      fixture = TestBed.createComponent(ObjectiveFormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      loader = TestbedHarnessEnvironment.loader(fixture);
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should load correct alignment possibilities', async () => {
      objectiveService.getAlignmentPossibilities.mockReturnValue(of([alignmentPossibility1, alignmentPossibility2]));
      component.generateAlignmentPossibilities(3, null, null);

      expect(component.alignmentPossibilities).toStrictEqual([alignmentPossibility1, alignmentPossibility2]);
      expect(component.filteredAlignmentOptions$.getValue()).toEqual([alignmentPossibility1, alignmentPossibility2]);
      expect(component.objectiveForm.getRawValue().alignment).toEqual(null);
    });

    it('should not include current team in alignment possibilities', async () => {
      objectiveService.getAlignmentPossibilities.mockReturnValue(of([alignmentPossibility1, alignmentPossibility2]));
      component.generateAlignmentPossibilities(3, null, 1);

      expect(component.alignmentPossibilities).toStrictEqual([alignmentPossibility2]);
      expect(component.filteredAlignmentOptions$.getValue()).toEqual([alignmentPossibility2]);
      expect(component.objectiveForm.getRawValue().alignment).toEqual(null);
    });

    it('should return team and objective with same text in alignment possibilities', async () => {
      component.alignmentInput.nativeElement.value = 'puzzle';
      component.alignmentPossibilities = [alignmentPossibility1, alignmentPossibility2];
      component.filter();
      expect(component.filteredAlignmentOptions$.getValue()).toEqual([alignmentPossibility1, alignmentPossibility2]);
    });

    it('should load existing objective alignment to objectiveForm', async () => {
      objectiveService.getAlignmentPossibilities.mockReturnValue(of([alignmentPossibility1, alignmentPossibility2]));
      component.generateAlignmentPossibilities(3, objectiveWithAlignment, null);

      expect(component.alignmentPossibilities).toStrictEqual([alignmentPossibility1, alignmentPossibility2]);
      expect(component.filteredAlignmentOptions$.getValue()).toEqual([alignmentPossibility1, alignmentPossibility2]);
      expect(component.objectiveForm.getRawValue().alignment).toEqual(alignmentObject2);
    });

    it('should load existing keyResult alignment to objectiveForm', async () => {
      objectiveWithAlignment.alignedEntity = { id: 1, type: 'keyResult' };
      objectiveService.getAlignmentPossibilities.mockReturnValue(of([alignmentPossibility1, alignmentPossibility2]));
      component.generateAlignmentPossibilities(3, objectiveWithAlignment, null);

      expect(component.alignmentPossibilities).toStrictEqual([alignmentPossibility1, alignmentPossibility2]);
      expect(component.filteredAlignmentOptions$.getValue()).toEqual([alignmentPossibility1, alignmentPossibility2]);
      expect(component.objectiveForm.getRawValue().alignment).toEqual(alignmentObject3);
    });

    it('should filter correct alignment possibilities', async () => {
      // Search for one title
      component.alignmentInput.nativeElement.value = 'palm';
      component.alignmentPossibilities = [alignmentPossibility1, alignmentPossibility2];
      component.filter();
      let modifiedAlignmentPossibility: AlignmentPossibility = {
        teamId: 1,
        teamName: 'Puzzle ITC',
        alignmentObjects: [alignmentObject3],
      };
      expect(component.filteredAlignmentOptions$.getValue()).toEqual([modifiedAlignmentPossibility]);

      // Search for team name
      component.alignmentInput.nativeElement.value = 'Puzzle IT';
      component.alignmentPossibilities = [alignmentPossibility1, alignmentPossibility2];
      component.filter();
      modifiedAlignmentPossibility = {
        teamId: 1,
        teamName: 'Puzzle ITC',
        alignmentObjects: [alignmentObject2, alignmentObject3],
      };
      expect(component.filteredAlignmentOptions$.getValue()).toEqual([modifiedAlignmentPossibility]);

      // Search for two objects
      component.alignmentInput.nativeElement.value = 'buy';
      component.alignmentPossibilities = [alignmentPossibility1, alignmentPossibility2];
      component.filter();
      let modifiedAlignmentPossibilities = [
        {
          teamId: 1,
          teamName: 'Puzzle ITC',
          alignmentObjects: [alignmentObject3],
        },
        {
          teamId: 2,
          teamName: 'We are cube',
          alignmentObjects: [alignmentObject1],
        },
      ];
      expect(component.filteredAlignmentOptions$.getValue()).toEqual(modifiedAlignmentPossibilities);

      // No match
      component.alignmentInput.nativeElement.value = 'findus';
      component.alignmentPossibilities = [alignmentPossibility1, alignmentPossibility2];
      component.filter();
      expect(component.filteredAlignmentOptions$.getValue()).toEqual([]);
    });

    it('should not include alignment object when already containing in team', async () => {
      component.alignmentInput.nativeElement.value = 'puzzle';
      component.alignmentPossibilities = [alignmentPossibility1];
      component.filter();
      expect(component.filteredAlignmentOptions$.getValue()).toEqual([alignmentPossibility1]);
    });

    it('should find correct alignment object', () => {
      // objective
      let alignmentObject = component.findAlignmentPossibilityObject(
        [alignmentPossibility1, alignmentPossibility2],
        1,
        'objective',
      );
      expect(alignmentObject!.objectId).toEqual(1);
      expect(alignmentObject!.objectTitle).toEqual('We want to increase the income puzzle buy');

      // keyResult
      alignmentObject = component.findAlignmentPossibilityObject(
        [alignmentPossibility1, alignmentPossibility2],
        1,
        'keyResult',
      );
      expect(alignmentObject!.objectId).toEqual(1);
      expect(alignmentObject!.objectTitle).toEqual('We buy 3 palms puzzle');

      // no match
      alignmentObject = component.findAlignmentPossibilityObject(
        [alignmentPossibility1, alignmentPossibility2],
        133,
        'keyResult',
      );
      expect(alignmentObject).toEqual(null);
    });

    it('should display kein alignment vorhanden when no alignment possibility', () => {
      component.filteredAlignmentOptions$.next([alignmentPossibility1, alignmentPossibility2]);
      fixture.detectChanges();
      expect(component.alignmentInput.nativeElement.getAttribute('placeholder')).toEqual('Bezug wählen');

      component.filteredAlignmentOptions$.next([]);
      fixture.detectChanges();
      expect(component.alignmentInput.nativeElement.getAttribute('placeholder')).toEqual('Kein Alignment vorhanden');
    });

    it('should update alignments on quarter change', () => {
      objectiveService.getAlignmentPossibilities.mockReturnValue(of([alignmentPossibility1, alignmentPossibility2]));
      component.updateAlignments();
      expect(component.alignmentInput.nativeElement.value).toEqual('');
      expect(component.objectiveForm.getRawValue().alignment).toEqual(null);
      expect(objectiveService.getAlignmentPossibilities).toHaveBeenCalled();
    });

    it('should return correct displayedValue', () => {
      component.alignmentInput.nativeElement.value = 'O - Objective 1';
      expect(component.displayedValue()).toEqual('O - Objective 1');

      component.alignmentInput = new ElementRef(document.createElement('input'));
      expect(component.displayedValue()).toEqual('');
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
          MatAutocompleteModule,
          NoopAnimationsModule,
          MatCheckboxModule,
          TranslateTestingModule.withTranslations({
            de: de,
          }),
          MatDividerModule,
        ],
        declarations: [ObjectiveFormComponent, DialogTemplateCoreComponent],
        providers: [
          provideRouter([]),
          provideHttpClient(),
          provideHttpClientTesting(),
          { provide: MatDialogRef, useValue: dialogMock },
          { provide: MAT_DIALOG_DATA, useValue: matDataMock },
          { provide: ObjectiveService, useValue: objectiveService },
          { provide: QuarterService, useValue: quarterService },
          { provide: TeamService, useValue: teamService },
          { provide: ActivatedRoute, useValue: mockActivatedRoute },
        ],
      });
      jest.spyOn(objectiveService, 'getAlignmentPossibilities').mockReturnValue(of([]));
      fixture = TestBed.createComponent(ObjectiveFormComponent);
      component = fixture.componentInstance;
      component.data = {
        objective: {
          objectiveId: 1,
          teamId: 1,
        },
        action: 'releaseBacklog',
      };
      fixture.detectChanges();
      loader = TestbedHarnessEnvironment.loader(fixture);
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should set correct default value if objective is released in backlog', async () => {
      const isBacklogQuarterSpy = jest.spyOn(component, 'isNotBacklogQuarter');
      isBacklogQuarterSpy.mockReturnValue(false);

      const routerHarness = await RouterTestingHarness.create();
      await routerHarness.navigateByUrl('/?quarter=999');
      objectiveService.getFullObjective.mockReturnValue(of(objective));
      fixture.detectChanges();
      component.ngOnInit();

      const rawFormValue = component.objectiveForm.getRawValue();
      expect(rawFormValue.title).toBe(objective.title);
      expect(rawFormValue.description).toBe(objective.description);
      expect(rawFormValue.team).toBe(objective.teamId);
      expect(rawFormValue.quarter).not.toBe(999);
      expect(rawFormValue.quarter).toBe(2);
    });
  });
});
