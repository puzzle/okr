import { AddEditTeamDialog } from './add-edit-team-dialog.component';
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
import { DialogHeaderComponent } from '../../shared/custom/dialog-header/dialog-header.component';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { TeamService } from '../../services/team.service';
import { of } from 'rxjs';
import { teamFormObject, teamMin1 } from '../../shared/testData';
import { Team } from '../../shared/types/model/Team';
import { TranslateService } from '@ngx-translate/core';

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

describe('TeamManagementComponent', () => {
  let component: AddEditTeamDialog;
  let fixture: ComponentFixture<AddEditTeamDialog>;
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
      declarations: [AddEditTeamDialog, DialogHeaderComponent],
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
          provide: MAT_DIALOG_DATA,
          useValue: null,
        },
        { provide: TranslateService, useValue: {} },
      ],
    });
    fixture = TestBed.createComponent(AddEditTeamDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
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
    component.data = { team: teamMin1 };
    component.ngOnInit();
    expect(component.teamForm.controls.name.value).toBe(teamMin1.name);
    expect(component.hasInActiveOrganisations).toBeFalsy();
  });
});
