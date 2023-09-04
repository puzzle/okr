import { TeamMin } from './types/model/TeamMin';
import { State } from './types/enums/State';
import { QuarterMin } from './types/model/QuarterMin';
import { CheckInMin } from './types/model/CheckInMin';
import { KeyResultMetricMin } from './types/model/KeyResultMetricMin';
import { KeyResultOrdinalMin } from './types/model/KeyResultOrdinalMin';
import { KeyresultMin } from './types/model/KeyresultMin';
import { ObjectiveMin } from './types/model/ObjectiveMin';
import { OverviewEntity } from './types/model/OverviewEntity';

export const team1: TeamMin = {
  id: 1,
  name: 'Marketing Team',
} as TeamMin;

export const quarter: QuarterMin = {
  id: 1,
  label: 'GJ 23/24-Q1',
} as QuarterMin;

export const checkInMetric: CheckInMin = {
  id: 815,
  value: 15,
  confidence: 5,
  createdOn: '2023-07-20T12:34:56Z' as unknown as Date,
} as CheckInMin;
export const checkInOrdinal: CheckInMin = {
  id: 816,
  value: 'COMMIT',
  confidence: 7,
  createdOn: '2023-07-22T08:45:21Z' as unknown as Date,
} as CheckInMin;
export const keyResultMetric: KeyResultMetricMin = {
  id: 201,
  title: 'Achieve 20% Increase in Daily Active Users',
  unit: '%',
  baseLine: 10.0,
  stretchGoal: 25.0,
  lastCheckIn: checkInMetric,
} as KeyResultMetricMin;

export const keyResultOrdinal: KeyResultOrdinalMin = {
  id: 202,
  title: 'Reduce Bounce Rate',
  commitZone: '3 Birnen',
  targetZone: '2 Birnen und 2 Äpfel',
  stretchGoal: 'Alle Früchte',
  lastCheckIn: checkInOrdinal,
} as KeyResultOrdinalMin;

export const objective: ObjectiveMin = {
  id: 101,
  title: 'Increase User Engagement',
  state: State.ONGOING,
  quarter: quarter,
  keyresults: [keyResultMetric, keyResultOrdinal] as KeyresultMin[],
} as ObjectiveMin;
export const overViewEntity1: OverviewEntity = {
  team: team1,
  objectives: [objective, objective, objective] as ObjectiveMin[],
};
