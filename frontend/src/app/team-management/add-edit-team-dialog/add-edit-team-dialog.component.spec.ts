import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { TeamService } from '../../services/team.service';
import { of } from 'rxjs';
import { marketingTeamWriteable, teamFormObject } from '../../shared/test-data';
import { Team } from '../../shared/types/model/team';
import { TranslateService } from '@ngx-translate/core';
import { DialogService } from '../../services/dialog.service';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { DialogTemplateCoreComponent } from '../../shared/custom/dialog-template-core/dialog-template-core.component';
import { MatDividerModule } from '@angular/material/divider';
import { AddEditTeamDialogComponent } from './add-edit-team-dialog.component';

const dialogRefMock = {
  close: jest.fn()
};

const dialogServiceMock = {
  open: jest.fn()
};

const teamServiceMock = {
  createTeam: jest.fn(),
  updateTeam: jest.fn(),
  deleteTeam: jest.fn()
};

describe('TeamManagementComponent', () => {
  let component: AddEditTeamDialogComponent;
  let fixture: ComponentFixture<AddEditTeamDialogComponent>;

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
        MatDividerModule
      ],
      declarations: [AddEditTeamDialogComponent,
        DialogTemplateCoreComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: DialogService,
          useValue: dialogServiceMock
        },
        {
          provide: MatDialogRef,
          useValue: dialogRefMock
        },
        {
          provide: TeamService,
          useValue: teamServiceMock
        },
        {
          provide: MAT_DIALOG_DATA,
          useValue: null
        },
        { provide: TranslateService,
          useValue: {} }
      ]
    });
    fixture = TestBed.createComponent(AddEditTeamDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should call service method to save team', async() => {
    component.teamForm.setValue(teamFormObject);
    jest.spyOn(teamServiceMock, 'createTeam')
      .mockReturnValue(of(teamFormObject));
    fixture.detectChanges();
    component.saveTeam();
    expect(teamServiceMock.createTeam)
      .toHaveBeenCalled();
    expect(teamServiceMock.createTeam)
      .toHaveBeenCalledWith(teamFormObject as Team);
  });

  it('should call service method to update team if data input is not null', async() => {
    jest.spyOn(teamServiceMock, 'updateTeam')
      .mockReturnValue(of(teamFormObject));
    component.data = { team: marketingTeamWriteable };
    component.teamForm.setValue(teamFormObject);
    fixture.detectChanges();
    component.saveTeam();

    expect(teamServiceMock.updateTeam)
      .toHaveBeenCalled();
    expect(teamServiceMock.updateTeam)
      .toHaveBeenCalledWith(expect.objectContaining({
        id: marketingTeamWriteable.id,
        version: marketingTeamWriteable.version
      }));
  });

  it('should set team values in from on init if data is not null', async() => {
    component.data = { team: marketingTeamWriteable };
    component.ngOnInit();
    expect(component.teamForm.controls.name.value)
      .toBe(marketingTeamWriteable.name);
  });
});
