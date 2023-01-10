import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveFormComponent } from './objective-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { By } from '@angular/platform-browser';
import { Observable, of, throwError } from 'rxjs';
import {
  Objective,
  ObjectiveService,
} from '../../shared/services/objective.service';
import * as objectivesData from '../../shared/testing/mock-data/objectives.json';
import * as teamsData from '../../shared/testing/mock-data/teams.json';
import * as quarterData from '../../shared/testing/mock-data/quarters.json';
import { HttpErrorResponse } from '@angular/common/http';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { HarnessLoader } from '@angular/cdk/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { User, UserService } from '../../shared/services/user.service';
import * as usersData from '../../shared/testing/mock-data/users.json';
import { Team, TeamService } from '../../shared/services/team.service';
import { ObjectiveModule } from '../objective.module';
import { Quarter, QuarterService } from '../../shared/services/quarter.service';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

let loader: HarnessLoader;

let component: ObjectiveFormComponent;
let fixture: ComponentFixture<ObjectiveFormComponent>;

let objective: Observable<Objective> = of(objectivesData.objectives[0]);
let userList: Observable<User[]> = of(usersData.users);
let teamList: Observable<Team[]> = of(teamsData.teams);
let quarterList: Observable<Quarter[]> = of(quarterData.quarters);

const mockObjectiveService = {
  saveObjective: jest.fn(),
  getObjectiveById: jest.fn(),
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

const mockTeamService = {
  getTeams: jest.fn(),
};

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
          ReactiveFormsModule,
          NoopAnimationsModule,
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
});
