import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TeamComponent } from './team.component';
import { MatIcon } from '@angular/material/icon';
import {
  organisationActive,
  organisationInActive,
  overViewEntity1,
  overViewEntity2,
  team1,
  teamMin1,
} from '../shared/testData';
import { ObjectiveComponent } from '../objective/objective.component';
import { RouterTestingModule } from '@angular/router/testing';
import { MatMenuModule } from '@angular/material/menu';
import { KeyresultComponent } from '../keyresult/keyresult.component';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';
import { OrganisationService } from '../shared/services/organisation.service';
import { of, ReplaySubject } from 'rxjs';
import { RefreshDataService } from '../shared/services/refresh-data.service';

const organisationServiceMock = {
  getOrganisationsByTeamId: jest.fn(),
};

const dialogMock = {
  open: jest.fn(),
};

const refreshDataServiceMock = {
  markDataRefresh: jest.fn(),
};

describe('TeamComponent', () => {
  let component: TeamComponent;
  let fixture: ComponentFixture<TeamComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, MatMenuModule, MatDialogModule, HttpClientTestingModule],
      declarations: [TeamComponent, MatIcon, ObjectiveComponent, KeyresultComponent],
      providers: [
        {
          provide: OrganisationService,
          useValue: organisationServiceMock,
        },
        {
          provide: MatDialog,
          useValue: dialogMock,
        },
        {
          provide: RefreshDataService,
          useValue: refreshDataServiceMock,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(TeamComponent);
    component = fixture.componentInstance;
    component.hasAdminAccess = new ReplaySubject<boolean>(1);
    component.hasAdminAccess.next(true);
    component.overviewEntity = overViewEntity1;
    fixture.detectChanges();
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should display add objective button if writeable is true', async () => {
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('[data-testId="add-objective"]'));
    expect(button).toBeTruthy();
  });

  it('should not display add objective button if writeable is false', async () => {
    component.overviewEntity = overViewEntity2;
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('[data-testId="add-objective"]'));
    expect(button).toBeFalsy();
  });

  it('should set has inactive teams to false', async () => {
    jest
      .spyOn(organisationServiceMock, 'getOrganisationsByTeamId')
      .mockReturnValue(of([organisationActive, organisationActive]));
    component.checkIfTeamHasInActiveOrganisations();
    expect(component.hasInActiveOrganisation.value).toBeFalsy();
  });

  it('should open Teamdialog and make call to dialog object', async () => {
    jest.spyOn(dialogMock, 'open').mockReturnValue({ afterClosed: () => of(team1) });
    component.openEditTeamDialog(teamMin1);
    expect(dialogMock.open).toHaveBeenCalled();
    expect(refreshDataServiceMock.markDataRefresh).toHaveBeenCalled();
  });

  it('should set has inactive teams to true', async () => {
    jest
      .spyOn(organisationServiceMock, 'getOrganisationsByTeamId')
      .mockReturnValue(of([organisationInActive, organisationInActive]));
    component.checkIfTeamHasInActiveOrganisations();
    expect(component.hasInActiveOrganisation.value).toBeTruthy();
  });
});
