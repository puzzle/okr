import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TeamComponent } from './team.component';
import { MatIcon } from '@angular/material/icon';
import { organisationActive, organisationInActive, overViewEntity1, overViewEntity2 } from '../shared/testData';
import { ObjectiveComponent } from '../objective/objective.component';
import { RouterTestingModule } from '@angular/router/testing';
import { MatMenuModule } from '@angular/material/menu';
import { KeyresultComponent } from '../keyresult/keyresult.component';
import { MatDialogModule } from '@angular/material/dialog';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';
import { OrganisationService } from '../shared/services/organisation.service';
import { of } from 'rxjs';

const organisationServiceMock = {
  getOrganisationsByTeamId: jest.fn(),
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
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(TeamComponent);
    component = fixture.componentInstance;
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

  it('should set has inactive teams to true', async () => {
    jest
      .spyOn(organisationServiceMock, 'getOrganisationsByTeamId')
      .mockReturnValue(of([organisationInActive, organisationInActive]));
    component.hasWriteAllAccess = true;
    component.checkIfTeamHasInActiveOrganisations();
    expect(component.hasInActiveOrganisation.value).toBeTruthy();
  });

  it('should set has inactive teams to false', async () => {
    jest
      .spyOn(organisationServiceMock, 'getOrganisationsByTeamId')
      .mockReturnValue(of([organisationActive, organisationActive]));
    component.hasWriteAllAccess = true;
    component.checkIfTeamHasInActiveOrganisations();
    expect(component.hasInActiveOrganisation.value).toBeFalsy();
  });
});
