import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ObjectiveModule } from '../../../../objective/objective.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { Observable, of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { Goal, GoalService } from '../../../services/goal.service';
import * as goalsData from '../../../testing/mock-data/goals.json';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { KeyResultDescriptionComponent } from './key-result-description.component';

describe('KeyResultDescriptionComponent', () => {
  let component: KeyResultDescriptionComponent;
  let fixture: ComponentFixture<KeyResultDescriptionComponent>;

  let goal: Observable<Goal> = of(goalsData.goals[0]);

  const mockGoalService = {
    getGoalByKeyResultId: jest.fn(),
  };

  const mockGetNumerOrNull = {
    getNumberOrNull: jest.fn(),
  };

  describe('Key Result Overview', () => {
    beforeEach(() => {
      mockGoalService.getGoalByKeyResultId.mockReturnValue(goal);
      mockGetNumerOrNull.getNumberOrNull.mockReturnValue(1);

      TestBed.configureTestingModule({
        imports: [
          HttpClientTestingModule,
          ObjectiveModule,
          NoopAnimationsModule,
          RouterTestingModule,
        ],
        providers: [
          { provide: GoalService, useValue: mockGoalService },
          {
            provide: ActivatedRoute,
            useValue: {
              paramMap: of(convertToParamMap({ keyresultId: '1' })),
            },
          },
        ],
        declarations: [KeyResultDescriptionComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyResultDescriptionComponent);
      component = fixture.componentInstance;

      fixture.detectChanges();
    });

    afterEach(() => {
      mockGoalService.getGoalByKeyResultId.mockReset();
      mockGetNumerOrNull.getNumberOrNull.mockReset();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should set goal of component', () => {
      component.goal$.subscribe((componentGoal) => {
        goal.subscribe((testGoal) => {
          expect(componentGoal).toEqual(testGoal);
        });
      });
    });

    test('should have 4 strong titles', () => {
      const titles = fixture.debugElement.queryAll(By.css('strong'));
      expect(titles.length).toEqual(4);
    });

    test('should set title key result title', () => {
      const title = fixture.debugElement.query(By.css('.key-result-title'));
      expect(title.nativeElement.textContent).toContain(
        'Key Result KeyResult title'
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
});
