import { TeamMin } from './types/model/TeamMin';
import { State } from './types/enums/State';
import { QuarterMin } from './types/model/QuarterMin';
import { CheckInMin } from './types/model/CheckInMin';
import { KeyResultMetricMin } from './types/model/KeyResultMetricMin';
import { KeyResultOrdinalMin } from './types/model/KeyResultOrdinalMin';
import { KeyresultMin } from './types/model/KeyresultMin';
import { ObjectiveMin } from './types/model/ObjectiveMin';
import { OverviewEntity } from './types/model/OverviewEntity';
import { Objective } from './types/model/Objective';
import { Quarter } from './types/model/Quarter';

export const team1: TeamMin = {
  id: 1,
  name: 'Marketing Team',
} as TeamMin;

export const quarterMin: QuarterMin = {
  id: 1,
  label: 'GJ 23/24-Q1',
} as QuarterMin;

export const checkInMetric: CheckInMin = {
  id: 815,
  valueMetric: 15,
  confidence: 5,
  createdOn: '2023-07-20T12:34:56Z' as unknown as Date,
  zone: '',
} as CheckInMin;
export const checkInOrdinal: CheckInMin = {
  id: 816,
  zone: 'COMMIT',
  confidence: 7,
  createdOn: '2023-07-22T08:45:21Z' as unknown as Date,
  valueMetric: undefined,
} as CheckInMin;
export const keyResultMetric: KeyResultMetricMin = {
  id: 201,
  title: 'Achieve 20% Increase in Daily Active Users',
  keyResultType: 'metric',
  unit: '%',
  baseline: 10.0,
  stretchGoal: 25.0,
  lastCheckIn: checkInMetric,
  type: 'keyResult',
} as KeyResultMetricMin;

export const keyResultOrdinal: KeyResultOrdinalMin = {
  id: 202,
  title: 'Reduce Bounce Rate',
  keyResultType: 'ordinal',
  commitZone: '3 Birnen',
  targetZone: '2 Birnen und 2 Äpfel',
  stretchGoal: 'Alle Früchte',
  lastCheckIn: checkInOrdinal,
} as KeyResultOrdinalMin;

export const objectiveMin: ObjectiveMin = {
  id: 101,
  title: 'Increase User Engagement',
  state: State.ONGOING,
  quarter: quarterMin,
  keyResults: [keyResultMetric, keyResultOrdinal] as KeyresultMin[],
} as ObjectiveMin;
export const overViewEntity1: OverviewEntity = {
  team: team1,
  objectives: [objectiveMin, objectiveMin, objectiveMin] as ObjectiveMin[],
};

export const quarter: Quarter = {
  id: 1,
  label: '23.02.2025',
  endDate: new Date(),
  startDate: new Date(),
};

export const objective: Objective = {
  team: team1,
  createdOn: new Date(),
  title: 'title',
  state: State.NOTSUCCESSFUL,
  id: 1,
  quarter: quarter,
  description: 'description',
  modifiedOn: new Date(),
};

export const keyResultMetricWithIdEight: KeyResultMetricMin = {
  id: 8,
  title: 'KeyResult Title',
  unit: 'CHF',
  baseLine: 5.0,
  stretchGoal: 15.0,
  lastCheckIn: checkInMetric,
  type: 'keyResult',
} as KeyResultMetricMin;

export const firstCheckIn: CheckInMin = {
  id: 1,
  valueMetric: 77,
  confidence: 5,
  zone: undefined,
  changeInfo: '',
  initiatives: '',
  createdOn: new Date(),
};

export const secondCheckIn: CheckInMin = {
  id: 2,
  valueMetric: 89,
  confidence: 5,
  zone: undefined,
  changeInfo: '',
  initiatives: '',
  createdOn: new Date(),
};
