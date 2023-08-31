import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamComponent } from './team.component';
import { By } from '@angular/platform-browser';
import { OverviewEntity } from '../model/OverviewEntity';
import { TeamMin } from '../model/TeamMin';
import { QuarterMin } from '../model/QuarterMin';
import { CheckInMinimal } from '../model/CheckInMin';
import { KeyResultMetricMin } from '../model/KeyResultMetricMin';
import { KeyResultOrdinalMin } from '../model/KeyResultOrdinalMin';
import { KeyresultMin } from '../model/KeyresultMin';
import { ObjectiveMin } from '../model/ObjectiveMin';
import { MatIcon } from '@angular/material/icon';

const BS_WIDTH_XS = 400;
const BS_WIDTH_SM = 600;
const BS_WIDTH_MD = 800;
const BS_WIDTH_XL = 1000;
const BS_WIDTH_XXL = 1200;

const overviewEntity: OverviewEntity = {
  team: {
    id: 1,
    name: 'Marketing Team',
  } as TeamMin,
  objectives: [
    {
      id: 101,
      title: 'Increase User Engagement',
      state: 'ONGOING',
      quarter: {
        id: 1,
        label: 'GJ 23/24-Q1',
      } as QuarterMin,
      keyresults: [
        {
          id: 201,
          title: 'Achieve 20% Increase in Daily Active Users',
          unit: '%',
          baseLine: 10.0,
          stretchGoal: 25.0,
          lastCheckIn: {
            id: 815,
            value: 15,
            confidence: 5,
            createdOn: '2023-07-20T12:34:56Z' as unknown as Date,
          } as CheckInMinimal,
        } as KeyResultMetricMin,
        {
          id: 202,
          title: 'Reduce Bounce Rate',
          commitZone: '3 Birnen',
          targetZone: '2 Birnen und 2 Äpfel',
          stretchGoal: 'Alle Früchte',
          lastCheckIn: {
            id: 816,
            value: 'COMMIT',
            confidence: 7,
            createdOn: '2023-07-22T08:45:21Z' as unknown as Date,
          } as CheckInMinimal,
        } as KeyResultOrdinalMin,
      ] as KeyresultMin[],
    } as ObjectiveMin,
    {
      id: 101,
      title: 'Increase User Engagement',
      state: 'ONGOING',
      quarter: {
        id: 1,
        label: 'GJ 23/24-Q1',
      } as QuarterMin,
      keyresults: [
        {
          id: 201,
          title: 'Achieve 20% Increase in Daily Active Users',
          unit: '%',
          baseLine: 10.0,
          stretchGoal: 25.0,
          lastCheckIn: {
            id: 815,
            value: 15,
            confidence: 5,
            createdOn: '2023-07-20T12:34:56Z' as unknown as Date,
          } as CheckInMinimal,
        } as KeyResultMetricMin,
        {
          id: 202,
          title: 'Reduce Bounce Rate',
          commitZone: '3 Birnen',
          targetZone: '2 Birnen und 2 Äpfel',
          stretchGoal: 'Alle Früchte',
          lastCheckIn: {
            id: 816,
            value: 'COMMIT',
            confidence: 7,
            createdOn: '2023-07-22T08:45:21Z' as unknown as Date,
          } as CheckInMinimal,
        } as KeyResultOrdinalMin,
      ] as KeyresultMin[],
    } as ObjectiveMin,
    {
      id: 101,
      title: 'Increase User Engagement',
      state: 'ONGOING',
      quarter: {
        id: 1,
        label: 'GJ 23/24-Q1',
      } as QuarterMin,
      keyresults: [
        {
          id: 201,
          title: 'Achieve 20% Increase in Daily Active Users',
          unit: '%',
          baseLine: 10.0,
          stretchGoal: 25.0,
          lastCheckIn: {
            id: 815,
            value: 15,
            confidence: 5,
            createdOn: '2023-07-20T12:34:56Z' as unknown as Date,
          } as CheckInMinimal,
        } as KeyResultMetricMin,
        {
          id: 202,
          title: 'Reduce Bounce Rate',
          commitZone: '3 Birnen',
          targetZone: '2 Birnen und 2 Äpfel',
          stretchGoal: 'Alle Früchte',
          lastCheckIn: {
            id: 816,
            value: 'COMMIT',
            confidence: 7,
            createdOn: '2023-07-22T08:45:21Z' as unknown as Date,
          } as CheckInMinimal,
        } as KeyResultOrdinalMin,
      ] as KeyresultMin[],
    } as ObjectiveMin,
    {
      id: 101,
      title: 'Increase User Engagement',
      state: 'ONGOING',
      quarter: {
        id: 1,
        label: 'GJ 23/24-Q1',
      } as QuarterMin,
      keyresults: [
        {
          id: 201,
          title: 'Achieve 20% Increase in Daily Active Users',
          unit: '%',
          baseLine: 10.0,
          stretchGoal: 25.0,
          lastCheckIn: {
            id: 815,
            value: 15,
            confidence: 5,
            createdOn: '2023-07-20T12:34:56Z' as unknown as Date,
          } as CheckInMinimal,
        } as KeyResultMetricMin,
        {
          id: 202,
          title: 'Reduce Bounce Rate',
          commitZone: '3 Birnen',
          targetZone: '2 Birnen und 2 Äpfel',
          stretchGoal: 'Alle Früchte',
          lastCheckIn: {
            id: 816,
            value: 'COMMIT',
            confidence: 7,
            createdOn: '2023-07-22T08:45:21Z' as unknown as Date,
          } as CheckInMinimal,
        } as KeyResultOrdinalMin,
      ] as KeyresultMin[],
    } as ObjectiveMin,
    {
      id: 101,
      title: 'Increase User Engagement',
      state: 'ONGOING',
      quarter: {
        id: 1,
        label: 'GJ 23/24-Q1',
      } as QuarterMin,
      keyresults: [
        {
          id: 201,
          title: 'Achieve 20% Increase in Daily Active Users',
          unit: '%',
          baseLine: 10.0,
          stretchGoal: 25.0,
          lastCheckIn: {
            id: 815,
            value: 15,
            confidence: 5,
            createdOn: '2023-07-20T12:34:56Z' as unknown as Date,
          } as CheckInMinimal,
        } as KeyResultMetricMin,
        {
          id: 202,
          title: 'Reduce Bounce Rate',
          commitZone: '3 Birnen',
          targetZone: '2 Birnen und 2 Äpfel',
          stretchGoal: 'Alle Früchte',
          lastCheckIn: {
            id: 816,
            value: 'COMMIT',
            confidence: 7,
            createdOn: '2023-07-22T08:45:21Z' as unknown as Date,
          } as CheckInMinimal,
        } as KeyResultOrdinalMin,
      ] as KeyresultMin[],
    } as ObjectiveMin,
    {
      id: 101,
      title: 'Increase User Engagement',
      state: 'ONGOING',
      quarter: {
        id: 1,
        label: 'GJ 23/24-Q1',
      } as QuarterMin,
      keyresults: [
        {
          id: 201,
          title: 'Achieve 20% Increase in Daily Active Users',
          unit: '%',
          baseLine: 10.0,
          stretchGoal: 25.0,
          lastCheckIn: {
            id: 815,
            value: 15,
            confidence: 5,
            createdOn: '2023-07-20T12:34:56Z' as unknown as Date,
          } as CheckInMinimal,
        } as KeyResultMetricMin,
        {
          id: 202,
          title: 'Reduce Bounce Rate',
          commitZone: '3 Birnen',
          targetZone: '2 Birnen und 2 Äpfel',
          stretchGoal: 'Alle Früchte',
          lastCheckIn: {
            id: 816,
            value: 'COMMIT',
            confidence: 7,
            createdOn: '2023-07-22T08:45:21Z' as unknown as Date,
          } as CheckInMinimal,
        } as KeyResultOrdinalMin,
      ] as KeyresultMin[],
    } as ObjectiveMin,
    {
      id: 101,
      title: 'Increase User Engagement',
      state: 'ONGOING',
      quarter: {
        id: 1,
        label: 'GJ 23/24-Q1',
      } as QuarterMin,
      keyresults: [
        {
          id: 201,
          title: 'Achieve 20% Increase in Daily Active Users',
          unit: '%',
          baseLine: 10.0,
          stretchGoal: 25.0,
          lastCheckIn: {
            id: 815,
            value: 15,
            confidence: 5,
            createdOn: '2023-07-20T12:34:56Z' as unknown as Date,
          } as CheckInMinimal,
        } as KeyResultMetricMin,
        {
          id: 202,
          title: 'Reduce Bounce Rate',
          commitZone: '3 Birnen',
          targetZone: '2 Birnen und 2 Äpfel',
          stretchGoal: 'Alle Früchte',
          lastCheckIn: {
            id: 816,
            value: 'COMMIT',
            confidence: 7,
            createdOn: '2023-07-22T08:45:21Z' as unknown as Date,
          } as CheckInMinimal,
        } as KeyResultOrdinalMin,
      ] as KeyresultMin[],
    } as ObjectiveMin,
    {
      id: 101,
      title: 'Increase User Engagement',
      state: 'ONGOING',
      quarter: {
        id: 1,
        label: 'GJ 23/24-Q1',
      } as QuarterMin,
      keyresults: [
        {
          id: 201,
          title: 'Achieve 20% Increase in Daily Active Users',
          unit: '%',
          baseLine: 10.0,
          stretchGoal: 25.0,
          lastCheckIn: {
            id: 815,
            value: 15,
            confidence: 5,
            createdOn: '2023-07-20T12:34:56Z' as unknown as Date,
          } as CheckInMinimal,
        } as KeyResultMetricMin,
        {
          id: 202,
          title: 'Reduce Bounce Rate',
          commitZone: '3 Birnen',
          targetZone: '2 Birnen und 2 Äpfel',
          stretchGoal: 'Alle Früchte',
          lastCheckIn: {
            id: 816,
            value: 'COMMIT',
            confidence: 7,
            createdOn: '2023-07-22T08:45:21Z' as unknown as Date,
          } as CheckInMinimal,
        } as KeyResultOrdinalMin,
      ] as KeyresultMin[],
    } as ObjectiveMin,
  ] as ObjectiveMin[],
} as OverviewEntity;

describe('TeamComponent', () => {
  let component: TeamComponent;
  let fixture: ComponentFixture<TeamComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TeamComponent, MatIcon],
    }).compileComponents();

    fixture = TestBed.createComponent(TeamComponent);
    component = fixture.componentInstance;
    component.overviewEntity = overviewEntity;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it.skip('One per row', () => {
    // Object.defineProperty(window, 'innerWidth', {
    //   configurable: true,
    //   value: BS_WIDTH_XS,
    //   writable: true,
    // });
    fixture.detectChanges();
    const objectives = fixture.debugElement.queryAll(By.css('[data-testid="objective"]'));
    console.log(objectives);
    objectives.forEach((objective) => {
      expect(objective.nativeElement.getBoundingClientRect()).toBe(400);
    });
  });
});
