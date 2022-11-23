import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardComponent } from './dashboard.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TeamService } from './team.service';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let teamServiceMock;

  beforeEach(async () => {
    teamServiceMock = jasmine.createSpyObj(['getTeams', 'getQuarter']);
    teamServiceMock.getQuarter.and.returnValue([
      { quarter: '22-4' },
      { quarter: '22-3' },
      { quarter: '22-2' },
      { quarter: '22-1' },
      { quarter: '21-4' },
      { quarter: '23-1' },
    ]);
    teamServiceMock.getTeams.and.returnValue(
      of([
        { id: 1, name: 'Team1' },
        { id: 2, name: 'Team2' },
      ])
    );

    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NoopAnimationsModule],
      providers: [{ provide: TeamService, useValue: teamServiceMock }],
      declarations: [DashboardComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display Ziele und Resultate headline', () => {
    expect(fixture.nativeElement.querySelector('p').textContent).toEqual(
      'Ziele und Resultate'
    );
  });

  it('should display 2 dropdowns links', () => {
    expect(
      fixture.nativeElement.querySelectorAll('mat-form-field').length
    ).toEqual(2);
  });

  it('should display 6 items in quarter dropdown', () => {
    const dropdownItems = fixture.debugElement.queryAll(
      By.css('.quarter-dropdown')
    );
    expect(dropdownItems.length).toEqual(6);
  });

  it('should display 2 teams in team dropdown', () => {
    const dropdownItems = fixture.debugElement.queryAll(
      By.css('.team-dropdown')
    );
    expect(dropdownItems.length).toEqual(2);
  });
});
