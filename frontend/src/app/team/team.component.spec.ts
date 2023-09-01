import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamComponent } from './team.component';
import { OverviewEntity } from '../model/OverviewEntity';
import { TeamMin } from '../model/TeamMin';
import { QuarterMin } from '../model/QuarterMin';
import { CheckInMinimal } from '../model/CheckInMin';
import { KeyResultMetricMin } from '../model/KeyResultMetricMin';
import { KeyResultOrdinalMin } from '../model/KeyResultOrdinalMin';
import { KeyresultMin } from '../model/KeyresultMin';
import { ObjectiveMin } from '../model/ObjectiveMin';
import { MatIcon } from '@angular/material/icon';
import { By } from '@angular/platform-browser';

const BS_WIDTH_XS = 400;
const BS_WIDTH_SM = 600;
const BS_WIDTH_MD = 800;
const BS_WIDTH_LG = 1000;
const BS_WIDTH_XL = 1200;
const BS_WIDTH_XXL = 1400;

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

  test.each([
    [BS_WIDTH_XS, 100],
    [BS_WIDTH_SM, 41],
    [BS_WIDTH_MD, 33],
    [BS_WIDTH_LG, 33],
    [BS_WIDTH_XL, 25],
    [BS_WIDTH_XXL, 16],
  ])(
    'Check width of objective in parent in percentage',
    async (browserWidth: number, objectiveWidthPctExpected: number) => {
      //Use the following line in browser to prove calc logic
      //Math.floor(temp0.getBoundingClientRect().width / temp0.parentElement.getBoundingClientRect().width * 100)
      Object.defineProperty(window, 'innerWidth', {
        configurable: true,
        value: browserWidth,
        writable: true,
      });
      fixture.detectChanges();
      await fixture.whenStable();
      await fixture.whenRenderingDone();
      await new Promise((r) => setTimeout(r, 4000));
      const objectives = fixture.nativeElement.querySelectorAll('[data-testid="objective"]');
      objectives.forEach((objective: any) => {
        const parentWidth = objective.parentElement.getBoundingClientRect().width;
        const objectiveWidth = objective.getBoundingClientRect().width;
        const objectiveWidthPct: number = Math.floor((parentWidth / objectiveWidth) * 100);
        expect(objectiveWidthPct).toBe(objectiveWidthPctExpected);
      });
    }
  );

  test.each([
    [BS_WIDTH_XS, false],
    [BS_WIDTH_SM, false],
    [BS_WIDTH_MD, true],
    [BS_WIDTH_LG, true],
    [BS_WIDTH_XL, true],
    [BS_WIDTH_XXL, true],
  ])('Check if objective container has scrollbar', async (browserWidth: number, hasScrollbar: boolean) => {
    Object.defineProperty(window, 'innerWidth', {
      configurable: true,
      value: browserWidth,
      writable: true,
    });
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      const objectiveContainer = fixture.debugElement.query(By.css('[data-testid="objective-container"]'));

      objectiveContainer.nativeElement.getAttribute('width').then((width: number) => {
        objectiveContainer.nativeElement.getAttribute('scrollWidth').then((scrollWidth: number) => {
          expect(scrollWidth > width).toBe(hasScrollbar);
        });
      });
    });
  });
});
