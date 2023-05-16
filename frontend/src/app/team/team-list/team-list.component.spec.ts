import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';

import { TeamListComponent } from './team-list.component';
import { Observable, of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Team, TeamService } from '../../shared/services/team.service';
import { By } from '@angular/platform-browser';
import { MatDividerModule } from '@angular/material/divider';
import { RouterTestingModule } from '@angular/router/testing';
import * as teamsData from '../../shared/testing/mock-data/teams.json';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import spyOn = jest.spyOn;
import { ConfirmDialogComponent } from '../../shared/dialog/confirm-dialog/confirm-dialog.component';
import { MatDialogHarness } from '@angular/material/dialog/testing';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('TeamListComponent', () => {
  let component: TeamListComponent;
  let fixture: ComponentFixture<TeamListComponent>;
  let matDialog: MatDialog;
  let toastrService: ToastrService;
  let loader: HarnessLoader;

  let teamList: Observable<Team[]> = of(teamsData.teams);
  const teamServiceMock = {
    getTeams: jest.fn(),
  };

  beforeEach(() => {
    teamServiceMock.getTeams.mockReturnValue(teamList);

    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        MatDividerModule,
        RouterTestingModule,
        MatDialogModule,
        ToastrModule.forRoot(),
        NoopAnimationsModule,
      ],
      providers: [{ provide: TeamService, useValue: teamServiceMock, matDialog: matDialog }, ToastrService],
      declarations: [TeamListComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TeamListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    matDialog = TestBed.inject(MatDialog);
    toastrService = TestBed.inject(ToastrService);
    loader = TestbedHarnessEnvironment.documentRootLoader(fixture);
  });

  afterEach(() => {
    teamServiceMock.getTeams.mockReset();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should display a headline with right text', () => {
    expect(fixture.nativeElement.querySelector('.headline-large').textContent).toEqual('Teams');
  });

  test('should have create team button with right text', () => {
    expect(fixture.nativeElement.querySelector('.create-button').textContent).toEqual(' Team erstellen ');
  });

  test('should display three teams', () => {
    const teamRows = fixture.debugElement.queryAll(By.css('.team-row'));
    expect(teamRows.length).toEqual(3);
  });

  test('should have right name on teams', () => {
    const teamRows = fixture.debugElement.queryAll(By.css('.team-row'));
    expect(teamRows[0].nativeElement.querySelector('p').textContent).toEqual('Team 1');
    expect(teamRows[1].nativeElement.querySelector('p').textContent).toEqual('Team 2');
  });

  test('should have three edit buttons', () => {
    const editButtons = fixture.debugElement.queryAll(By.css('.edit-icon'));
    expect(editButtons.length).toEqual(3);
  });

  test('should have three delete buttons', () => {
    const editButtons = fixture.debugElement.queryAll(By.css('.delete-icon'));
    expect(editButtons.length).toEqual(3);
  });

  test('should have three lines under the teams', () => {
    const dividers = fixture.debugElement.queryAll(By.css('.divider'));
    expect(dividers.length).toEqual(3);
  });

  test('should delete team', fakeAsync(() => {
    const matDialogSpy = spyOn(matDialog, 'open');
    let deleteButton = fixture.nativeElement.querySelector('[data-testId="delete-Icon-0"]');
    deleteButton.click();
    tick();
    expect(matDialogSpy).toHaveBeenCalled();
    expect(matDialogSpy).toHaveBeenCalledWith(ConfirmDialogComponent, {
      data: {
        closeText: 'Abbrechen',
        confirmText: 'Bestätigen',
        title: 'Willst du dieses Team und die ' + 'zugehörigen Objectives, Keyresults und Messungen wirklich löschen?',
      },
      disableClose: true,
      width: '400px',
    });
  }));

  test('should delete team if confirmed', async () => {
    fixture.componentInstance.openDeleteDialog(1);
    const dialogs = await loader.getAllHarnesses(MatDialogHarness);
    expect(dialogs.length).toEqual(1);
  });
});
