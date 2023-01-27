import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamDetailComponent } from './team-detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { DashboardComponent } from '../../dashboard/dashboard.component';
import { CommonModule } from '@angular/common';
import { MatExpansionModule } from '@angular/material/expansion';
import { ObjectiveModule } from '../../objective/objective.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import * as overviewData from '../../shared/testing/mock-data/overview.json';
import { Overview } from '../../shared/services/overview.service';
import { ToastrService } from 'ngx-toastr';

const mockToastrService = {
  success: jest.fn(),
  error: jest.fn(),
};

describe('TeamDetailComponent', () => {
  let componentTeamDetails: TeamDetailComponent;
  let fixtureTeamDetails: ComponentFixture<TeamDetailComponent>;

  let overview: Overview = overviewData.overview[0];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        CommonModule,
        MatExpansionModule,
        ObjectiveModule,
        NoopAnimationsModule,
      ],
      declarations: [TeamDetailComponent, DashboardComponent],
      providers: [{ provide: ToastrService, useValue: mockToastrService }],
    }).compileComponents();

    fixtureTeamDetails = TestBed.createComponent(TeamDetailComponent);
    componentTeamDetails = fixtureTeamDetails.componentInstance;
    componentTeamDetails.overview = overview;
    fixtureTeamDetails.detectChanges();
  });

  afterEach(() => {
    mockToastrService.success.mockReset();
    mockToastrService.error.mockReset();
  });

  test('should create', () => {
    expect(componentTeamDetails).toBeTruthy();
  });

  test('should have 1 title from team', () => {
    expect(
      fixtureTeamDetails.nativeElement.querySelectorAll('h1').length
    ).toEqual(1);
  });

  test('should have title Puzzle ITC Objectives', () => {
    expect(
      fixtureTeamDetails.nativeElement.querySelector('h1').textContent
    ).toEqual('Team 1');
  });

  test('should create 3 hr when having 1 team with 1 objectives', () => {
    expect(
      fixtureTeamDetails.nativeElement.querySelectorAll('hr').length
    ).toEqual(1);
  });
});
