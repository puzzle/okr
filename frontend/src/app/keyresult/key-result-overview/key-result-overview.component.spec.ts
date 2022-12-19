import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultOverviewComponent } from './key-result-overview.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ObjectiveModule } from '../../objective/objective.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { Observable, of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { Goal, GoalService } from '../../shared/services/goal.service';

describe('KeyresultOverviewComponent', () => {
  let component: KeyResultOverviewComponent;
  let fixture: ComponentFixture<KeyResultOverviewComponent>;

  let goal1: Observable<Goal> = of({
    objective: {
      id: 1,
      title: 'Objective title',
      description: 'Objective description',
    },
    keyresult: {
      id: 1,
      title: 'KeyResult title',
      description: 'KeyResult description',
    },
    teamId: 1,
    teamName: 'Team 1',
    progress: 30,
    quarterLabel: 'GJ 22/23-Q1',
    expectedEvolution: 'INCREASE',
    unit: 'PERCENT',
    basicValue: 1,
    targetValue: 100,
  });

  const mockGoalService = {
    getGoalByKeyResultId: jest.fn(),
  };

  beforeEach(() => {
    mockGoalService.getGoalByKeyResultId.mockReturnValue(goal1);

    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        ObjectiveModule,
        NoopAnimationsModule,
        RouterTestingModule,
      ],
      providers: [{ provide: GoalService, useValue: mockGoalService }],
      declarations: [KeyResultOverviewComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(KeyResultOverviewComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  afterEach(() => {
    mockGoalService.getGoalByKeyResultId.mockReset();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should set goal of component', () => {
    expect(component.goal$).toEqual(goal1);
  });

  test('should have 4 strong titles', () => {
    const titles = fixture.debugElement.queryAll(By.css('strong'));
    expect(titles.length).toEqual(4);
  });

  test('should set title from keyresult', () => {
    const title = fixture.debugElement.query(By.css('.key-result-title'));
    expect(title.nativeElement.textContent).toContain(
      'KeyResult KeyResult title'
    );
  });

  test('should set title from keyresult', () => {
    const description = fixture.debugElement.query(
      By.css('.key-result-description')
    );
    expect(description.nativeElement.textContent).toContain(
      'Beschreibung KeyResult description'
    );
  });

  test('should set teamname from objective', () => {
    const teamname = fixture.debugElement.query(By.css('.key-result-Ziel'));
    expect(teamname.nativeElement.textContent).toContain(
      'Team 1 Objective Objective title'
    );
  });

  test('should set quarter from keyresult', () => {
    const quarter = fixture.debugElement.query(By.css('.key-result-quarter'));
    expect(quarter.nativeElement.textContent).toContain('Zyklus GJ 22/23-Q1');
  });
});
