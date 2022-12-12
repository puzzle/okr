import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardComponent } from './dashboard.component';
import { TeamService } from '../shared/services/team.service';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { AppModule } from '../app.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  const teamServiceMock = {
    getQuarter: jest.fn(),
    getTeams: jest.fn(),
  };

  beforeEach(() => {
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

    TestBed.configureTestingModule({
      imports: [AppModule, NoopAnimationsModule, ReactiveFormsModule],
      providers: [{ provide: TeamService, useValue: teamServiceMock }],
      declarations: [DashboardComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    teamServiceMock.getQuarter.mockReset();
    teamServiceMock.getTeams.mockReset();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should display Objectives und Keyresults headline', () => {
    expect(fixture.nativeElement.querySelector('p').textContent).toEqual(
      'Objectives und Keyresults'
    );
  });

  test('should display 2 dropdowns links', () => {
    expect(
      fixture.nativeElement.querySelectorAll('mat-form-field').length
    ).toEqual(2);
  });

  test('should display 6 items in quarter dropdown', () => {
    fixture.debugElement
      .queryAll(By.css('mat-select'))[1]
      .nativeElement.click();
    fixture.detectChanges();
    const options = fixture.debugElement.queryAll(By.css('mat-option'));

    expect(options.length).toEqual(6);
  });

  test('should display 2 teams in team dropdown', () => {
    fixture.debugElement.query(By.css('mat-select')).nativeElement.click();
    fixture.detectChanges();
    const options = fixture.debugElement.queryAll(By.css('mat-option'));

    expect(options.length).toEqual(2);
  });

  test('should display 2 team detail components when having 2 teams', () => {
    expect(
      fixture.nativeElement.querySelectorAll('app-team-detail').length
    ).toEqual(2);
  });
});
