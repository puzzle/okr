import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveFormComponent } from './objective-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { By } from '@angular/platform-browser';
import { Observable, of, throwError } from 'rxjs';
import {
  Objective,
  ObjectiveService,
} from '../../shared/services/objective.service';
import { HttpErrorResponse } from '@angular/common/http';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { HarnessLoader } from '@angular/cdk/testing';
import { ObjectiveModule } from '../objective.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { User, UserService } from '../../shared/services/user.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import * as objectivesData from '../../shared/testing/mock-data/objectives.json';
import * as usersData from '../../shared/testing/mock-data/users.json';
import * as quartersData from '../../shared/testing/mock-data/quarters.json';
import * as teamsData from '../../shared/testing/mock-data/teams.json';
import { Team, TeamService } from '../../shared/services/team.service';
import { Quarter, QuarterService } from '../../shared/services/quarter.service';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { MatSelectHarness } from '@angular/material/select/testing';

let component: ObjectiveFormComponent;
let fixture: ComponentFixture<ObjectiveFormComponent>;

let userList: Observable<User[]> = of(usersData.users);
let quarterList: Observable<Quarter[]> = of(quartersData.quarters);
let teamList: Observable<Team[]> = of(teamsData.teams);
let objective: Observable<Objective> = of(objectivesData.objectives[0]);
let initObjective: Objective = objectivesData.initObjective;
let createObjective: Objective = objectivesData.createObjectiveObject;

const mockUserService = {
  getUsers: jest.fn(),
};
const mockQuarterService = {
  getQuarters: jest.fn(),
};
const mockTeamService = {
  getTeams: jest.fn(),
};
const mockToastrService = {
  success: jest.fn(),
  error: jest.fn(),
};
const mockGetNumerOrNull = {
  getNumberOrNull: jest.fn(),
};
const mockObjectiveService = {
  saveObjective: jest.fn(),
  getObjectiveById: jest.fn(),
  getInitObjective: jest.fn(),
};

let loader: HarnessLoader;

describe('ObjectiveFormComponent', () => {
  describe('Check if component makes call to toastrservice', () => {
    beforeEach(() => {
      mockObjectiveService.getObjectiveById.mockReturnValue(objective);
      mockUserService.getUsers.mockReturnValue(userList);
      mockQuarterService.getQuarters.mockReturnValue(quarterList);
      mockTeamService.getTeams.mockReturnValue(teamList);

      TestBed.configureTestingModule({
        imports: [
          ToastrModule.forRoot(),
          HttpClientTestingModule,
          RouterTestingModule,
          ObjectiveModule,
          FormsModule,
          ReactiveFormsModule,
          NoopAnimationsModule,
          MatFormFieldModule,
          MatIconModule,
        ],
        declarations: [ObjectiveFormComponent],
        providers: [
          { provide: ObjectiveService, useValue: mockObjectiveService },
          { provide: ToastrService, useValue: mockToastrService },
          { provide: UserService, useValue: mockUserService },
          { provide: QuarterService, useValue: mockQuarterService },
          { provide: TeamService, useValue: mockTeamService },
          {
            provide: ActivatedRoute,
            useValue: {
              paramMap: of(convertToParamMap({ objectiveId: '1' })),
            },
          },
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(ObjectiveFormComponent);
      loader = TestbedHarnessEnvironment.loader(fixture);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      mockObjectiveService.saveObjective.mockReset();
      mockObjectiveService.getObjectiveById.mockReset();

      mockUserService.getUsers.mockReset();
      mockQuarterService.getQuarters.mockReset();
      mockTeamService.getTeams.mockReset();
      mockToastrService.success.mockReset();
      mockToastrService.error.mockReset();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should trigger success notification', () => {
      mockObjectiveService.saveObjective.mockReturnValue(objective);
      const createbutton = fixture.debugElement.query(
        By.css('button[type="submit"]')
      );
      createbutton.nativeElement.click();
      fixture.detectChanges();
      expect(mockToastrService.success).toHaveBeenCalledTimes(1);
      expect(mockToastrService.error).not.toHaveBeenCalled();
      expect(mockToastrService.success).toHaveBeenCalledWith(
        '',
        'Objective gespeichert!',
        { timeOut: 5000 }
      );
    });

    test('should trigger error notification', () => {
      mockObjectiveService.saveObjective.mockReturnValue(
        throwError(
          () =>
            new HttpErrorResponse({
              status: 500,
              error: { message: 'Something went wrong' },
            })
        )
      );
      const createbutton = fixture.debugElement.query(
        By.css('button[type="submit"]')
      );
      createbutton.nativeElement.click();
      fixture.detectChanges();
      expect(mockToastrService.error).toHaveBeenCalledTimes(1);
      expect(mockToastrService.success).not.toHaveBeenCalled();
      expect(mockToastrService.error).toHaveBeenCalledWith(
        'Objective konnte nicht gespeichert werden!',
        'Fehlerstatus: 500',
        { timeOut: 5000 }
      );
    });
  });
  describe('Edit Objective', () => {
    beforeEach(() => {
      mockUserService.getUsers.mockReturnValue(userList);
      mockQuarterService.getQuarters.mockReturnValue(quarterList);
      mockTeamService.getTeams.mockReturnValue(teamList);
      mockGetNumerOrNull.getNumberOrNull.mockReturnValue(1);
      mockObjectiveService.getObjectiveById.mockReturnValue(objective);

      TestBed.configureTestingModule({
        imports: [
          ToastrModule.forRoot(),
          HttpClientTestingModule,
          RouterTestingModule,
          FormsModule,
          ReactiveFormsModule,
          ObjectiveModule,
          NoopAnimationsModule,
          MatFormFieldModule,
          MatIconModule,
        ],
        providers: [
          { provide: UserService, useValue: mockUserService },
          { provide: QuarterService, useValue: mockQuarterService },
          { provide: TeamService, useValue: mockTeamService },
          { provide: ObjectiveService, useValue: mockObjectiveService },
          {
            provide: ActivatedRoute,
            useValue: {
              paramMap: of(convertToParamMap({ objectiveId: '1' })),
            },
          },
        ],
        declarations: [ObjectiveFormComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(ObjectiveFormComponent);
      loader = TestbedHarnessEnvironment.loader(fixture);

      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      mockUserService.getUsers.mockReset();
      mockQuarterService.getQuarters.mockReset();
      mockTeamService.getTeams.mockReset();
      mockObjectiveService.getObjectiveById.mockReset();
      mockGetNumerOrNull.getNumberOrNull.mockReset();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should set create boolean to false and dont disable create button', () => {
      expect(component.create).toBeFalsy();

      const createButton = fixture.debugElement.query(
        By.css('button[type="submit"]')
      );
      expect(createButton.nativeElement.disabled).toBeFalsy();
      expect(component.objectiveForm.valid).toBeTruthy();
    });

    test('should set objective observable', () => {
      expect(mockObjectiveService.getObjectiveById).toHaveBeenCalled();
      expect(mockObjectiveService.getObjectiveById).toHaveBeenCalledWith(1);
      component.objectives$.subscribe((componentObjective) => {
        objective.subscribe((testObjective) => {
          expect(componentObjective).toEqual(testObjective);
        });
      });
    });

    test('should set objective form and validate it', () => {
      expect(component.objectiveForm.valid).toEqual(true);
    });

    test('should have 2 buttons', () => {
      const buttons = fixture.debugElement.queryAll(By.css('button'));
      expect(buttons.length).toEqual(2);

      expect(buttons[0].nativeElement.textContent).toContain('Abbrechen');
      expect(buttons[1].nativeElement.textContent).toContain('Aktualisieren');
    });

    test('should have right title objective bearbeiten', () => {
      const objectiveTitle = fixture.debugElement.query(
        By.css('.heading-label')
      );
      expect(objectiveTitle.nativeElement.textContent).toContain(
        'Objective bearbeiten'
      );
    });

    test('should have right titles infront of input fields', () => {
      const titles = fixture.debugElement.queryAll(By.css('.fw-bold'));
      expect(titles.length).toEqual(5);
      expect(titles[0].nativeElement.textContent).toContain('Team');
      expect(titles[1].nativeElement.textContent).toContain('Titel');
      expect(titles[2].nativeElement.textContent).toContain('Beschreibung');
      expect(titles[3].nativeElement.textContent).toContain('Besitzer');
      expect(titles[4].nativeElement.textContent).toContain('Zyklus');
    });

    test('should set team in mat select and set it new on item change', async () => {
      const teamSelect = await loader.getHarness(
        MatSelectHarness.with({
          selector: 'mat-select[formControlName="teamId"]',
        })
      );
      expect(await teamSelect.getValueText()).toEqual('Team 1');

      await teamSelect.open();
      const bugOption = await teamSelect.getOptions({ text: 'Team 2' });
      await bugOption[0].click();

      expect(await teamSelect.getValueText()).toEqual('Team 2');
      expect(await teamSelect.isDisabled()).toBeFalsy();
      expect(await teamSelect.isOpen()).toBeFalsy();
    });

    test('should set title in input field and should validate it', () => {
      const titleInput = fixture.debugElement.query(
        By.css('input[formControlName="title"]')
      )!.nativeElement;

      expect(titleInput.value).toEqual('Objective 1');
      expect(titleInput.placeholder).toEqual('Titel');
      expect(component.objectiveForm.get('title')?.valid).toBeTruthy();

      component.objectiveForm.get('title')?.setValue('');
      fixture.detectChanges();

      expect(component.objectiveForm.get('title')?.valid).toBeFalsy();
      const createButton = fixture.debugElement.query(
        By.css('button[type="submit"]')
      );
      expect(createButton.nativeElement.disabled).toBeTruthy();
    });

    test('should set description in input field and should validate it', () => {
      const descriptionInput = fixture.debugElement.query(
        By.css('textarea[formControlName="description"]')
      )!.nativeElement;

      expect(descriptionInput.value).toEqual(
        'This is the description of Objective 1'
      );
      expect(descriptionInput.placeholder).toEqual('Beschreibung');
      expect(component.objectiveForm.get('title')?.valid).toBeTruthy();

      component.objectiveForm.get('description')?.setValue('');
      fixture.detectChanges();

      expect(component.objectiveForm.get('description')?.valid).toBeFalsy();
      const createButton = fixture.debugElement.query(
        By.css('button[type="submit"]')
      );
      expect(createButton.nativeElement.disabled).toBeTruthy();
    });

    test('should set owner in mat select and set it new on item change', async () => {
      const ownerSelect = await loader.getHarness(
        MatSelectHarness.with({
          selector: 'mat-select[formControlName="ownerId"]',
        })
      );
      expect(await ownerSelect.getValueText()).toEqual('Alice Wunderland');

      await ownerSelect.open();
      const bugOption = await ownerSelect.getOptions({ text: 'Robin Papier' });
      await bugOption[0].click();

      expect(await ownerSelect.getValueText()).toEqual('Robin Papier');
      expect(await ownerSelect.isDisabled()).toBeFalsy();
      expect(await ownerSelect.isOpen()).toBeFalsy();
    });

    test('should not have quarter mat select on edit', async () => {
      const quarterMatSelect = fixture.debugElement.query(
        By.css('mat-select[formControlName="quarterId"]')
      );
      expect(quarterMatSelect).toBeNull();
    });

    test('should disable create button if form is invalid', () => {
      const submit = fixture.debugElement.query(
        By.css('button[type="submit"]')
      );

      component.objectiveForm.controls['teamId'].setValue(1);
      component.objectiveForm.controls['title'].setValue('Title');
      component.objectiveForm.controls['description'].setValue('Description');
      component.objectiveForm.controls['ownerId'].setValue(null);
      component.objectiveForm.controls['quarterId'].setValue(1);
      fixture.detectChanges();

      expect(component.objectiveForm.valid).toBeFalsy();
      expect(submit.nativeElement.disabled).toEqual(true);
    });

    test('should call save method in objectiveService when save button is clicked', async () => {
      const submit = fixture.debugElement.query(
        By.css('button[type="submit"]')
      );
      submit.nativeElement.click();
      expect(mockObjectiveService.saveObjective).toHaveBeenCalledWith(
        objectivesData.objectives[0],
        false
      );
    });
  });

  describe('Create Objective', () => {
    beforeEach(() => {
      mockUserService.getUsers.mockReturnValue(userList);
      mockQuarterService.getQuarters.mockReturnValue(quarterList);
      mockTeamService.getTeams.mockReturnValue(teamList);
      mockGetNumerOrNull.getNumberOrNull.mockReturnValue(1);
      mockObjectiveService.getInitObjective.mockReturnValue(initObjective);

      TestBed.configureTestingModule({
        imports: [
          ToastrModule.forRoot(),
          HttpClientTestingModule,
          RouterTestingModule,
          FormsModule,
          ReactiveFormsModule,
          ObjectiveModule,
          NoopAnimationsModule,
          MatFormFieldModule,
          MatIconModule,
        ],
        providers: [
          { provide: UserService, useValue: mockUserService },
          { provide: QuarterService, useValue: mockQuarterService },
          { provide: TeamService, useValue: mockTeamService },
          { provide: ObjectiveService, useValue: mockObjectiveService },
          {
            provide: ActivatedRoute,
            useValue: {
              paramMap: of(convertToParamMap({})),
            },
          },
        ],
        declarations: [ObjectiveFormComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(ObjectiveFormComponent);
      loader = TestbedHarnessEnvironment.loader(fixture);

      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      mockUserService.getUsers.mockReset();
      mockQuarterService.getQuarters.mockReset();
      mockTeamService.getTeams.mockReset();
      mockObjectiveService.getObjectiveById.mockReset();
      mockObjectiveService.getInitObjective.mockReset();
      mockGetNumerOrNull.getNumberOrNull.mockReset();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should set create boolean to true', () => {
      expect(component.create).toBeTruthy();
    });

    test('should set init objective', () => {
      expect(mockObjectiveService.getInitObjective).toHaveBeenCalled();
      component.objectives$.subscribe((componentObjective) => {
        expect(componentObjective).toEqual(initObjective);
      });
    });

    test('should have 2 buttons', () => {
      const buttons = fixture.debugElement.queryAll(By.css('button'));
      expect(buttons.length).toEqual(2);

      expect(buttons[0].nativeElement.textContent).toContain('Abbrechen');
      expect(buttons[1].nativeElement.textContent).toContain('Erstellen');
    });

    test('should have title objective hinzufügen', () => {
      const objectiveTitle = fixture.debugElement.query(
        By.css('.heading-label')
      );
      expect(objectiveTitle.nativeElement.textContent).toContain(
        'Objective hinzufügen'
      );
    });

    test('should disable create button with init objective', () => {
      const submit = fixture.debugElement.query(
        By.css('button[type="submit"]')
      );

      expect(component.objectiveForm.valid).toBeFalsy();
      expect(submit.nativeElement.disabled).toEqual(true);
    });

    test('should enable button if form is valid', () => {
      const submit = fixture.debugElement.query(
        By.css('button[type="submit"]')
      );

      component.objectiveForm.controls['teamId'].setValue(1);
      component.objectiveForm.controls['title'].setValue('Title');
      component.objectiveForm.controls['description'].setValue('Description');
      component.objectiveForm.controls['ownerId'].setValue(1);
      component.objectiveForm.controls['quarterId'].setValue(1);
      fixture.detectChanges();

      expect(component.objectiveForm.valid).toBeTruthy();
      expect(submit.nativeElement.disabled).toEqual(false);
    });

    test('should have right titles infront of input fields', () => {
      const titles = fixture.debugElement.queryAll(By.css('.fw-bold'));
      expect(titles.length).toEqual(5);
      expect(titles[0].nativeElement.textContent).toContain('Team');
      expect(titles[1].nativeElement.textContent).toContain('Titel');
      expect(titles[2].nativeElement.textContent).toContain('Beschreibung');
      expect(titles[3].nativeElement.textContent).toContain('Besitzer');
      expect(titles[4].nativeElement.textContent).toContain('Zyklus');
    });

    test('should have mat select for team and set it on item change', async () => {
      const teamSelect = await loader.getHarness(
        MatSelectHarness.with({
          selector: 'mat-select[formControlName="teamId"]',
        })
      );
      expect(await teamSelect.getValueText()).toEqual('');

      await teamSelect.open();
      const bugOption = await teamSelect.getOptions({ text: 'Team 2' });
      await bugOption[0].click();

      expect(await teamSelect.getValueText()).toEqual('Team 2');
      expect(await teamSelect.isDisabled()).toBeFalsy();
      expect(await teamSelect.isOpen()).toBeFalsy();
    });

    test('should have input field named Title and should validate it', () => {
      const titleInput = fixture.debugElement.query(
        By.css('input[formControlName="title"]')
      )!.nativeElement;

      expect(titleInput.value).toEqual('');
      expect(titleInput.placeholder).toEqual('Titel');
      expect(component.objectiveForm.get('title')?.valid).toBeFalsy();

      component.objectiveForm.get('title')?.setValue('Titel 1');
      fixture.detectChanges();

      expect(component.objectiveForm.get('title')?.valid).toBeTruthy();
    });

    test('should have input field named description and should validate it', () => {
      const descriptionInput = fixture.debugElement.query(
        By.css('textarea[formControlName="description"]')
      )!.nativeElement;

      expect(descriptionInput.value).toEqual('');
      expect(descriptionInput.placeholder).toEqual('Beschreibung');
      expect(component.objectiveForm.get('title')?.valid).toBeFalsy();

      component.objectiveForm.get('description')?.setValue('Description 1');
      fixture.detectChanges();

      expect(component.objectiveForm.get('description')?.valid).toBeTruthy();
    });

    test('should have mat select for owner and set it on item change', async () => {
      const ownerSelect = await loader.getHarness(
        MatSelectHarness.with({
          selector: 'mat-select[formControlName="ownerId"]',
        })
      );
      expect(await ownerSelect.getValueText()).toEqual('');

      await ownerSelect.open();
      const bugOption = await ownerSelect.getOptions({ text: 'Robin Papier' });
      await bugOption[0].click();

      expect(await ownerSelect.getValueText()).toEqual('Robin Papier');
      expect(await ownerSelect.isDisabled()).toBeFalsy();
      expect(await ownerSelect.isOpen()).toBeFalsy();
    });

    test('should have mat select for quarter and set it on item change', async () => {
      const quarterSelect = await loader.getHarness(
        MatSelectHarness.with({
          selector: 'mat-select[formControlName="quarterId"]',
        })
      );
      expect(await quarterSelect.getValueText()).toEqual('');

      await quarterSelect.open();
      const bugOption = await quarterSelect.getOptions({ text: 'GJ 22/23-Q3' });
      await bugOption[0].click();

      expect(await quarterSelect.getValueText()).toEqual('GJ 22/23-Q3');
      expect(await quarterSelect.isDisabled()).toBeFalsy();
      expect(await quarterSelect.isOpen()).toBeFalsy();
    });

    test('should call save method in objectiveService when save button is clicked and form is valid', async () => {
      const submit = fixture.debugElement.query(
        By.css('button[type="submit"]')
      );
      component.objectiveForm.controls['teamId'].setValue(1);
      component.objectiveForm.controls['title'].setValue('Objective 1');
      component.objectiveForm.controls['description'].setValue(
        'Objective 1 description'
      );
      component.objectiveForm.controls['ownerId'].setValue(1);
      component.objectiveForm.controls['quarterId'].setValue(1);
      fixture.detectChanges();
      submit.nativeElement.click();
      expect(mockObjectiveService.saveObjective).toHaveBeenCalledWith(
        createObjective,
        true
      );
    });
  });
});
