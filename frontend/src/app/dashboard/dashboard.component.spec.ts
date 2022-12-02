import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardComponent } from './dashboard.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TeamService } from '../services/team.service';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { AppModule } from '../app.module';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  const teamServiceMock = {
    getQuarter: jest.fn(),
    getTeams: jest.fn(),
  };

  beforeEach(async () => {
    teamServiceMock.getQuarter.mockReturnValue([
      { quarter: '22-4' },
      { quarter: '22-3' },
      { quarter: '22-2' },
      { quarter: '22-1' },
      { quarter: '21-4' },
      { quarter: '23-1' },
    ]);
    teamServiceMock.getTeams.mockReturnValue(
      of([
        { id: 1, name: 'Team1' },
        { id: 2, name: 'Team2' },
      ])
    );

    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        NoopAnimationsModule,
        MatFormFieldModule,
        MatSelectModule,
        ReactiveFormsModule,
        AppModule,
      ],
      providers: [{ provide: TeamService, useValue: teamServiceMock }],
      declarations: [DashboardComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    teamServiceMock.getQuarter.mockReset();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display Objectives und Keyresults headline', () => {
    expect(fixture.nativeElement.querySelector('p').textContent).toEqual(
      'Objectives und Keyresults'
    );
  });

  it('should display 2 dropdowns links', () => {
    expect(
      fixture.nativeElement.querySelectorAll('mat-form-field').length
    ).toEqual(2);
  });

  xit('should display 6 items in quarter dropdown', () => {
    const dropdownItems = fixture.debugElement.queryAll(
      By.css('.quarter-dropdown')
    );
    expect(dropdownItems.length).toEqual(6);
  });

  xit('should display 2 teams in team dropdown', () => {
    const dropdownItems = fixture.debugElement.queryAll(
      By.css('.team-dropdown')
    );
    expect(dropdownItems.length).toEqual(2);
  });

  it('should display 2 team detail components when having 2 teams', () => {
    expect(
      fixture.nativeElement.querySelectorAll('app-team-detail').length
    ).toEqual(2);
  });
});
