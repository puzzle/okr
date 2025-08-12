import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';

import { TeamManagementMobileFilterComponent } from './team-management-mobile-filter.component';
import { TeamService } from '../../services/team.service';
import { team1, teamList } from '../../shared/test-data';
import { BehaviorSubject, of } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { SearchTeamManagementComponent } from '../search-team-management/search-team-management.component';
import { HttpClientModule } from '@angular/common/http';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateTestingModule } from 'ngx-translate-testing';
import * as de from '../../../assets/i18n/de.json';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatIconModule } from '@angular/material/icon';
import { BrowserModule } from '@angular/platform-browser';
import { MatInputModule } from '@angular/material/input';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { getRouteToAllTeams, getRouteToTeam } from '../../shared/route-utils';

describe('TeamManagementMobileFilterComponent', () => {
  let component: TeamManagementMobileFilterComponent;
  let fixture: ComponentFixture<TeamManagementMobileFilterComponent>;

  const teamIdSubj = new BehaviorSubject({
    get: (): string | undefined => '1'
  });

  const teamServiceMock = {
    getAllTeams: jest.fn()
  };

  const activatedRouteMock = {
    paramMap: teamIdSubj.asObservable()
  };

  const routerMock = {
    navigateByUrl: jest.fn()
  };

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        MatFormFieldModule,
        MatSelectModule,
        FormsModule,
        ReactiveFormsModule,
        TranslateTestingModule.withTranslations({
          de: de
        }),
        MatAutocompleteModule,
        MatIconModule,
        BrowserModule,
        MatInputModule,
        NoopAnimationsModule
      ],
      declarations: [TeamManagementMobileFilterComponent,
        SearchTeamManagementComponent],
      providers: [{ provide: TeamService,
        useValue: teamServiceMock },
      { provide: ActivatedRoute,
        useValue: activatedRouteMock },
      { provide: Router,
        useValue: routerMock }]
    })
      .compileComponents();

    teamServiceMock.getAllTeams.mockReturnValue(of(teamList));

    fixture = TestBed.createComponent(TeamManagementMobileFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    routerMock.navigateByUrl.mockReset();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should set allTeams and selectedTeam correctly', fakeAsync(() => {
    tick();
    expect(component.selectedTeam)
      .toStrictEqual(team1);
    expect(component.teams)
      .toStrictEqual(teamList);
  }));

  it('should set allTeams and selectedTeam correctly if no teamId given', fakeAsync(() => {
    teamIdSubj.next({
      get: () => undefined
    });
    tick();
    expect(component.selectedTeam)
      .toBe(component.ALL_TEAMS);
    expect(component.teams)
      .toStrictEqual(teamList);
  }));

  it('should navigate to team ', () => {
    component.navigate(team1);
    expect(routerMock.navigateByUrl)
      .toHaveBeenCalledTimes(1);
    expect(routerMock.navigateByUrl)
      .toHaveBeenCalledWith(getRouteToTeam(team1.id));
  });

  it('should navigate to all teams ', () => {
    component.navigate(component.ALL_TEAMS);
    expect(routerMock.navigateByUrl)
      .toHaveBeenCalledTimes(1);
    expect(routerMock.navigateByUrl)
      .toHaveBeenCalledWith(getRouteToAllTeams());
  });
});
