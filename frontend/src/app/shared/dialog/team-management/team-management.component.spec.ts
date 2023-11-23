import { TeamManagementComponent } from './team-management.component';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HarnessLoader } from '@angular/cdk/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MAT_DIALOG_DATA, MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { RouterTestingModule } from '@angular/router/testing';
import { DialogHeaderComponent } from '../../custom/dialog-header/dialog-header.component';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { TeamService } from '../../services/team.service';
import { of } from 'rxjs';
import { organisationActive, organisationInActive, teamFormObject, teamMin1 } from '../../testData';
import { Team } from '../../types/model/Team';
import { OrganisationService } from '../../services/organisation.service';

const dialogRefMock = {
  close: jest.fn(),
};

const dialogMock = {
  open: jest.fn(),
};

const teamServiceMock = {
  createTeam: jest.fn(),
  updateTeam: jest.fn(),
  deleteTeam: jest.fn(),
};

const organisationServiceMock = {
  getOrganisationsByTeamId: jest.fn(),
  getOrganisations: jest.fn(),
};

describe('TeamManagementComponent', () => {
  let component: TeamManagementComponent;
  let fixture: ComponentFixture<TeamManagementComponent>;
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
        RouterTestingModule,
      ],
      declarations: [TeamManagementComponent, DialogHeaderComponent],
      providers: [
        {
          provide: MatDialog,
          useValue: dialogMock,
        },
        {
          provide: MatDialogRef,
          useValue: dialogRefMock,
        },
        {
          provide: TeamService,
          useValue: teamServiceMock,
        },
        {
          provide: OrganisationService,
          useValue: organisationServiceMock,
        },
        {
          provide: MAT_DIALOG_DATA,
          useValue: null,
        },
      ],
    });
    fixture = TestBed.createComponent(TeamManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should style inactive teams with line through decoration', async () => {
    expect(component.getMatOptionStyle(organisationInActive).toString()).toBe(
      { 'text-decoration': 'line-through' }.toString(),
    );
  });

  it('should style active teams with no text decoration', async () => {
    expect(component.getMatOptionStyle(organisationActive).toString()).toBe({ 'text-decoration': 'none' }.toString());
  });

  it('should return true if team has inactive organisations', async () => {
    component.teamForm.controls.organisations.setValue([organisationActive, organisationInActive]);
    component.checkIfInActiveAdded();
    expect(component.hasInActiveOrganisations).toBeTruthy();
  });

  it('should return false if team does not have inactive organisations', async () => {
    component.teamForm.controls.organisations.setValue([organisationActive, organisationActive]);
    component.checkIfInActiveAdded();
    expect(component.hasInActiveOrganisations).toBeFalsy();
  });

  it('should return true if org-name is the same', async () => {
    expect(component.compareWithFunc(organisationActive, organisationActive)).toBeTruthy();
  });

  it('should return false if org-name is not the same', async () => {
    expect(component.compareWithFunc(organisationInActive, organisationActive)).toBeFalsy();
  });

  it('should call service method to save team', async () => {
    component.teamForm.setValue(teamFormObject);
    jest.spyOn(teamServiceMock, 'createTeam').mockReturnValue(of(teamFormObject));
    fixture.detectChanges();
    component.saveTeam();
    expect(teamServiceMock.createTeam).toHaveBeenCalled();
    expect(teamServiceMock.createTeam).toHaveBeenCalledWith(teamFormObject as Team);
  });

  it('should call service method to update team if data input is not null', async () => {
    component.data = { team: teamMin1 };
    component.teamForm.setValue(teamFormObject);
    jest.spyOn(teamServiceMock, 'updateTeam').mockReturnValue(of(teamFormObject));
    fixture.detectChanges();
    component.saveTeam();
    expect(teamServiceMock.updateTeam).toHaveBeenCalled();
    expect(teamServiceMock.updateTeam).toHaveBeenCalledWith({
      ...teamFormObject,
      id: teamMin1.id,
      version: teamMin1.version,
    } as Team);
  });

  it('should call service method to delete team', async () => {
    component.data = { team: teamMin1 };
    jest.spyOn(dialogMock, 'open').mockReturnValue({ afterClosed: () => of(true) });
    jest.spyOn(teamServiceMock, 'deleteTeam').mockReturnValue(null);
    component.deleteTeam();
    expect(teamServiceMock.deleteTeam).toHaveBeenCalledWith(teamMin1.id);
  });

  it('should set team values in from on init if data is not null', async () => {
    jest
      .spyOn(organisationServiceMock, 'getOrganisations')
      .mockReturnValue(of([organisationActive, organisationInActive]));
    jest.spyOn(organisationServiceMock, 'getOrganisationsByTeamId').mockReturnValue(of([organisationActive]));
    component.data = { team: teamMin1 };
    component.ngOnInit();
    expect(component.teamForm.controls.name.value).toBe(teamMin1.name);
    expect(component.teamForm.controls.organisations.value!.toString()).toBe([organisationActive].toString());
    expect(component.hasInActiveOrganisations).toBeFalsy();
  });

  it('should merge Organisations together', (done) => {
    let organisationsArray = [organisationActive, organisationInActive];
    component.organisations$ = of([organisationActive]);
    component.mergeOrganisations(organisationsArray);
    component.organisations$.subscribe((result) => {
      expect(result).toStrictEqual([organisationActive, organisationInActive]);
      done();
    });
  }, 1500);
});
