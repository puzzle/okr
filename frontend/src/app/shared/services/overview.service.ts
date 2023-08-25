
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class OverviewService {
  constructor() {}

  getObjectiveWithKeyresults() {
    return {
      id: 101,
      title: 'Increase User Engagement',
      state: 'ONGOING',
      quarter: {
        id: 1,
        label: 'GJ 23/24-Q1',
      },
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
            createdOn: '2023-07-20T12:34:56Z',
          },
        },
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
            createdOn: '2023-07-22T08:45:21Z',
          },
        },
      ],
    };
  }
}
