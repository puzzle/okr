import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ObjectiveFormComponent} from './objective-form.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import {By} from '@angular/platform-browser';
import {Observable, of, throwError} from 'rxjs';
import {Objective, ObjectiveService,} from '../../shared/services/objective.service';
import * as objectivesData from '../../shared/testing/mock-data/objectives.json';
import * as teamsData from '../../shared/testing/mock-data/teams.json';
import * as quarterData from '../../shared/testing/mock-data/quarters.json';
import {HttpErrorResponse} from '@angular/common/http';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {HarnessLoader} from '@angular/cdk/testing';
import {ObjectiveModule} from '../objective.module';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {User, UserService} from '../../shared/services/user.service';
import * as usersData from '../../shared/testing/mock-data/users.json';
import {Team, TeamService} from '../../shared/services/team.service';
import {Quarter, QuarterService} from '../../shared/services/quarter.service';
import {ActivatedRoute, convertToParamMap} from '@angular/router';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatSelectHarness} from '@angular/material/select/testing';

describe('ObjectiveFormComponent', () => {
  let component: ObjectiveFormComponent;
  let fixture: ComponentFixture<ObjectiveFormComponent>;

let objective: Observable<Objective> = of(objectivesData.objectives[0]);
let userList: Observable<User[]> = of(usersData.users);
let teamList: Observable<Team[]> = of(teamsData.teams);
let quarterList: Observable<Quarter[]> = of(quarterData.quarters);
  let initObjective: Objective = objectivesData.initObjective;
  let objectiveForm = new FormGroup({
    teamId: new FormControl<number | null>(1, [
      Validators.required,
      Validators.nullValidator,
    ]),
    title: new FormControl<string>('Title', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(250),
    ]),
    description: new FormControl<string>('Description', [
      Validators.required,
      Validators.maxLength(4096),
      Validators.minLength(2),
    ]),
    ownerId: new FormControl<number | null>(1, [
      Validators.required,
      Validators.nullValidator,
    ]),
    quarterId: new FormControl<number | null>(1, [
      Validators.required,
      Validators.nullValidator,
    ]),
  });

const mockObjectiveService = {
  saveObjective: jest.fn(),
  getObjectiveById: jest.fn(),
  getInitObjective: jest.fn(),
};
const mockUserService = {
  getUsers: jest.fn(),
};
const mockToastrService = {
  success: jest.fn(),
  error: jest.fn(),
};
const mockQuarterService = {
  getQuarters: jest.fn(),
};
  const mockGetNumerOrNull = {
    getNumberOrNull: jest.fn(),
  };
const teamServiceMock = {
  getTeams: jest.fn(),
};
  let loader: HarnessLoader;

describe('ObjectiveFormComponent', () => {
  describe('Check if component makes call to toastrservice', () => {
    beforeEach(() => {
      mockObjectiveService.getObjectiveById.mockReturnValue(objective);
      mockUserService.getUsers.mockReturnValue(userList);
      mockQuarterService.getQuarters.mockReturnValue(quarterList);
      teamServiceMock.getTeams.mockReturnValue(teamList);

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
          { provide: TeamService, useValue: teamServiceMock },
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
      teamServiceMock.getTeams.mockReset();
      mockToastrService.success.mockReset();
      mockToastrService.error.mockReset();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should trigger success notification', () => {
      mockObjectiveService.saveObjective.mockReturnValue(objective);
      const createbutton = fixture.debugElement.query(By.css('.create-button'));
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
      const createbutton = fixture.debugElement.query(By.css('.create-button'));
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
      teamServiceMock.getTeams.mockReturnValue(teamList);
      mockGetNumerOrNull.getNumberOrNull.mockReturnValue(1);
      mockObjectiveService.getObjectiveById.mockReturnValue(objective);

      TestBed.configureTestingModule({
        imports: [
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
          { provide: TeamService, useValue: teamServiceMock },
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
      teamServiceMock.getTeams.mockReset();
      mockObjectiveService.getObjectiveById.mockReset();
      mockGetNumerOrNull.getNumberOrNull.mockReset();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should set create boolean to false', () => {
      expect(component.create).toBeFalsy();

      const createButton = fixture.debugElement.query(By.css('.create-button'));
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
      const select = await loader.getHarness(
        MatSelectHarness.with({
          selector: 'mat-select[formControlName="teamId"]',
        })
      );
      expect(await select.getValueText()).toEqual('Team 1');

      await select.open();
      const bugOption = await select.getOptions({ text: 'Team 2' });
      await bugOption[0].click();

      expect(await select.getValueText()).toEqual('Team 2');
      expect(await select.isDisabled()).toBeFalsy();
      expect(await select.isOpen()).toBeFalsy();
    });

    test('should have input field named Title', () => {
      const input = fixture.debugElement.query(
        By.css('input[formControlName="title"]')
      )!.nativeElement;

      expect(input.value).toEqual('Objective 1');
      expect(input.placeholder).toEqual('Titel');
      expect(component.objectiveForm.get('title')?.valid).toBeTruthy();

      component.objectiveForm.get('title')?.setValue('');
      fixture.detectChanges();

      expect(component.objectiveForm.get('title')?.valid).toBeFalsy();
    });
  });

  describe('Create Objective', () => {
    beforeEach(() => {
      mockUserService.getUsers.mockReturnValue(userList);
      mockQuarterService.getQuarters.mockReturnValue(quarterList);
      teamServiceMock.getTeams.mockReturnValue(teamList);
      mockGetNumerOrNull.getNumberOrNull.mockReturnValue(1);
      mockObjectiveService.getInitObjective.mockReturnValue(initObjective);

      TestBed.configureTestingModule({
        imports: [
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
          { provide: TeamService, useValue: teamServiceMock },
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
      teamServiceMock.getTeams.mockReset();
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

    test('should have title objective hinzufügen', () => {
      const objectiveTitle = fixture.debugElement.query(
        By.css('.heading-label')
      );
      expect(objectiveTitle.nativeElement.textContent).toContain(
        'Objective hinzufügen'
      );
    });
  });
});
});
