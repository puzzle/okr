import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardComponent } from './dashboard.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TeamService } from './team.service';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';

export interface OkrCycle {
  cycle: string;
}

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let mockToolBarService;
  let cycleList: OkrCycle[] = [
    { cycle: '22-4' },
    { cycle: '22-3' },
    { cycle: '22-2' },
    { cycle: '22-1' },
    { cycle: '21-4' },
    { cycle: '23-1' },
  ];

  beforeEach(async () => {
    mockToolBarService = jasmine.createSpyObj(['getTeams', 'getQuarter']);
    console.log(cycleList[0]);
    mockToolBarService.getQuarter.and.returnValue(
      of([
        { cycle: '22-4' },
        { cycle: '22-3' },
        { cycle: '22-2' },
        { cycle: '22-1' },
        { cycle: '21-4' },
        { cycle: '23-1' },
      ])
    );
    mockToolBarService.getTeams.and.returnValue(
      of([
        { id: 1, name: 'Team1' },
        { id: 2, name: 'Team2' },
      ])
    );

    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [{ provide: TeamService, useValue: mockToolBarService }],
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

  it('should display 5 items in cycle dropdown', () => {
    const dropdownItems = fixture.debugElement.queryAll(
      By.css('.cycle-dropdown')
    );
    expect(dropdownItems.length).toEqual(6);
  });

  it('should display 2 teams in team dropdown', () => {
    const dropdownItems = fixture.debugElement.queryAll(
      By.css('.team-dropdown')
    );
    expect(dropdownItems.length).toEqual(2);
  });

  // Ziele und Resultate soll stehen
  // Menu
  // TeamService mock
  // 4 Teams als return value
  // Team Filter soll 4 Einträge haben
  // Man kann mehrere auswählen, diese werden angezeigt
  // Zyklus soll 6 Einträge haben
  // Man soll einen auswählen können und Name stimmt
});
