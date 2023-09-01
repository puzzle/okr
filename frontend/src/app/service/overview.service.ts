import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { QuarterMin } from '../shared/types/model/QuarterMin';
import { CheckInMinimal } from '../shared/types/model/CheckInMin';
import { KeyResultMetricMin } from '../shared/types/model/KeyResultMetricMin';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { KeyresultMin } from '../shared/types/model/KeyresultMin';
import { KeyResultOrdinalMin } from '../shared/types/model/KeyResultOrdinalMin';
import { TeamMin } from '../shared/types/model/TeamMin';
import { State } from '../shared/types/enums/State';

@Injectable({
  providedIn: 'root',
})
export class OverviewService {
  constructor(private http: HttpClient) {}

  getOverview(): Observable<OverviewEntity[]> {
    return of([
      {
        team: {
          id: 1,
          name: 'Marketing Team',
        } as TeamMin,
        objectives: [
          {
            id: 101,
            title: 'Increase User Engagement',
            state: State.ONGOING,
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
            state: State.ONGOING,
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
            state: State.ONGOING,
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
      },
      {
        team: {
          id: 2,
          name: 'Development Team',
        },
        objectives: [],
      },
      {
        team: {
          id: 1,
          name: 'Marketing Team',
        } as TeamMin,
        objectives: [
          {
            id: 101,
            title: 'Increase User Engagement',
            state: State.ONGOING,
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
            state: State.ONGOING,
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
            state: State.ONGOING,
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
            state: State.ONGOING,
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
            state: State.ONGOING,
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
            state: State.ONGOING,
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
            state: State.ONGOING,
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
            state: State.ONGOING,
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
      },
      {
        team: {
          id: 2,
          name: 'Development Team',
        },
        objectives: [],
      },
      {
        team: {
          id: 1,
          name: 'Marketing Team',
        } as TeamMin,
        objectives: [
          {
            id: 101,
            title: 'Increase User Engagement',
            state: State.ONGOING,
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
            state: State.ONGOING,
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
            state: State.ONGOING,
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
            state: State.ONGOING,
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
            state: State.ONGOING,
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
            state: State.ONGOING,
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
            state: State.ONGOING,
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
            state: State.ONGOING,
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
      },
      {
        team: {
          id: 2,
          name: 'Development Team',
        },
        objectives: [],
      },
    ] as OverviewEntity[]);
  }
}
