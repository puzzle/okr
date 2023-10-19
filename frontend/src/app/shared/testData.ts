import { TeamMin } from './types/model/TeamMin';
import { State } from './types/enums/State';
import { QuarterMin } from './types/model/QuarterMin';
import { CheckInMin } from './types/model/CheckInMin';
import { KeyResultMetricMin } from './types/model/KeyResultMetricMin';
import { KeyResultOrdinalMin } from './types/model/KeyResultOrdinalMin';
import { KeyresultMin } from './types/model/KeyresultMin';
import { ObjectiveMin } from './types/model/ObjectiveMin';
import { OverviewEntity } from './types/model/OverviewEntity';
import { KeyResultObjective } from './types/model/KeyResultObjective';
import { Quarter } from './types/model/Quarter';
import { KeyResultOrdinal } from './types/model/KeyResultOrdinal';
import { CheckIn } from './types/model/CheckIn';
import { Objective } from './types/model/Objective';
import { User } from './types/model/User';
import { KeyResultMetric } from './types/model/KeyResultMetric';
import { Unit } from './types/enums/Unit';
import { Team } from './types/model/Team';

export const teamMin1: TeamMin = {
  id: 1,
  name: 'Marketing Team',
} as TeamMin;

export const team1: Team = {
  id: 1,
  name: 'Team2',
  activeObjectives: 1,
} as Team;

export const team2: Team = {
  id: 2,
  name: 'Team2',
  activeObjectives: 2,
} as Team;

export const team3: Team = {
  id: 3,
  name: 'Team3',
  activeObjectives: 3,
} as Team;

export const teamList = [team1, team2, team3];

export const quarterMin: QuarterMin = {
  id: 1,
  label: 'GJ 23/24-Q1',
} as QuarterMin;

export const checkInMetric: CheckInMin = {
  id: 815,
  value: 15,
  confidence: 5,
  createdOn: '2023-07-20T12:34:56Z' as unknown as Date,
  initiatives: 'Initiatives metric',
  changeInfo: 'Changeinfo metric',
} as CheckInMin;
export const checkInOrdinal: CheckInMin = {
  id: 816,
  value: 'COMMIT',
  confidence: 7,
  createdOn: '2023-07-22T08:45:21Z' as unknown as Date,
  initiatives: 'Initiatives ordinal',
  changeInfo: 'Changeinfo ordinal',
} as CheckInMin;
export const keyResultMetricMin: KeyResultMetricMin = {
  id: 201,
  title: 'Achieve 20% Increase in Daily Active Users',
  keyResultType: 'metric',
  unit: '%',
  baseline: 10.0,
  stretchGoal: 25.0,
  lastCheckIn: checkInMetric,
  type: 'keyResult',
} as KeyResultMetricMin;

export const keyResultMetricMinScoring: KeyResultMetricMin = {
  id: 201,
  title: 'Achieve 20% Increase in Daily Active Users',
  keyResultType: 'metric',
  unit: '%',
  baseline: 25.0,
  stretchGoal: 75.0,
  lastCheckIn: {
    id: 800,
    value: 50,
    confidence: 4,
    createdOn: new Date(),
    modifiedOn: new Date(),
    changeInfo: 'Half way through',
    initiatives: 'Quality before quantity',
  },
  type: 'keyResult',
} as KeyResultMetricMin;

export const keyResultMetricMinScoringInversion: KeyResultMetricMin = {
  id: 306,
  title: 'Achieve 20% Increase in Daily Active Users',
  keyResultType: 'metric',
  unit: '%',
  baseline: 50.0,
  stretchGoal: 0.0,
  lastCheckIn: {
    id: 800,
    value: 12.5,
    confidence: 4,
    createdOn: new Date(),
    modifiedOn: new Date(),
    changeInfo: 'More Changes',
    initiatives: 'Some initatives',
  },
  type: 'keyResult',
} as KeyResultMetricMin;

export const keyResultOrdinalMinScoring: KeyResultOrdinalMin = {
  id: 202,
  title: 'We want to bake 10 cakes',
  keyResultType: 'ordinal',
  commitZone: '5 cakes',
  targetZone: '10 cakes',
  stretchGoal: '13 cakes',
  lastCheckIn: {
    id: 830,
    value: 'COMMIT',
    confidence: 8,
    createdOn: '2023-07-22T08:45:21Z' as unknown as Date,
    initiatives: 'Initiatives of ordinal',
    changeInfo: 'Changeinfo ordinal',
  } as CheckInMin,
} as KeyResultOrdinalMin;

export const keyResultOrdinalMin: KeyResultOrdinalMin = {
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
  keyResults: [keyResultMetricMin, keyResultOrdinalMin] as KeyresultMin[],
} as ObjectiveMin;
export const overViewEntity1: OverviewEntity = {
  team: teamMin1,
  objectives: [objectiveMin, objectiveMin, objectiveMin] as ObjectiveMin[],
};

export const quarter: Quarter = {
  id: 1,
  label: '23.02.2025',
  endDate: new Date(),
  startDate: new Date(),
};

export const keyResultObjective: KeyResultObjective = {
  id: 1,
  state: State.NOTSUCCESSFUL,
  quarter: quarter,
};

export const keyResultMetricWithIdEight: KeyResultMetricMin = {
  id: 8,
  title: 'KeyResult Title',
  unit: 'CHF',
  baseline: 5.0,
  stretchGoal: 15.0,
  lastCheckIn: checkInMetric,
  keyResultType: 'keyResult',
} as KeyResultMetricMin;

export const objective: Objective = {
  id: 5,
  title: 'title',
  description: 'description',
  teamId: 2,
  quarterId: 2,
  state: State.SUCCESSFUL,
};

export const firstCheckIn: CheckInMin = {
  id: 1,
  value: 77,
  confidence: 5,
  changeInfo: '',
  initiatives: '',
  createdOn: new Date(),
};

export const secondCheckIn: CheckInMin = {
  id: 2,
  value: 89,
  confidence: 5,
  changeInfo: '',
  initiatives: '',
  createdOn: new Date(),
};

export const testUser: User = {
  id: 1,
  firstname: 'Bob',
  lastname: 'Baumeister',
  username: 'username',
};

export const users: User[] = [
  testUser,
  {
    id: 2,
    username: 'pacoegiman',
    firstname: 'Paco',
    lastname: 'Egiman',
  },
  {
    id: 3,
    username: 'robinpapier',
    firstname: 'Robin',
    lastname: 'Papier',
  },
];

export const keyResult: KeyResultOrdinal = {
  id: 101,
  title: 'Ausbauen des Früchtesortiments',
  description: 'Dient zur Gesunderhaltung der Members',
  commitZone: 'Äpfel',
  targetZone: 'Äpfel und Birnen',
  stretchZone: 'Äpfel, Birnen, Bananen und Erdberen',
  owner: { id: 1, firstname: 'firstname', lastname: 'lastname', username: 'username' },
  keyResultType: 'ordinal',
  objective: {
    id: 301,
    state: State.DRAFT,
    quarter: {
      id: 1,
      label: 'GJ 23/24-Q1',
      startDate: new Date(),
      endDate: new Date(),
    } as Quarter,
  } as KeyResultObjective,
  lastCheckIn: {
    id: 745,
    value: 'FAIL',
    confidence: 8,
    createdOn: new Date(),
    modifiedOn: new Date(),
    changeInfo: 'info',
    initiatives: 'some',
  } as CheckIn,
  createdOn: new Date(),
  modifiedOn: new Date(),
};

export const keyResultOrdinal: KeyResultOrdinal = {
  id: 101,
  title: 'Bauen eines Hauses',
  description: 'Ein neues Haus für die Puzzle Members',
  commitZone: 'Grundriss steht',
  targetZone: 'Gebäude gebaut',
  stretchZone: 'Inneneinrichtung gestaltet',
  owner: { id: 1, firstname: 'firstname', lastname: 'lastname', username: 'username' },
  keyResultType: 'ordinal',
  objective: {
    id: 301,
    state: State.DRAFT,
    quarter: {
      id: 1,
      label: 'GJ 23/24-Q1',
      startDate: new Date(),
      endDate: new Date(),
    } as Quarter,
  } as KeyResultObjective,
  lastCheckIn: {
    id: 746,
    value: 'FAIL',
    confidence: 3,
    createdOn: new Date(),
    modifiedOn: new Date(),
    changeInfo: 'Does not look good',
    initiatives: 'We have to be faster',
  } as CheckIn,
  createdOn: new Date(),
  modifiedOn: new Date(),
};

export const keyResultMetric: KeyResultMetric = {
  id: 102,
  title: '100% aller Schweizer Kunden betreuen',
  description: 'Puzzle ITC erledigt die IT-Aufträge für 100% aller Unternehmen.',
  baseline: 30,
  stretchGoal: 100,
  unit: Unit.PERCENT,
  owner: { id: 1, firstname: 'firstname', lastname: 'lastname', username: 'username' },
  keyResultType: 'metric',
  objective: {
    id: 302,
    state: State.DRAFT,
    quarter: {
      id: 1,
      label: 'GJ 23/24-Q1',
      startDate: new Date(),
      endDate: new Date(),
    } as Quarter,
  } as KeyResultObjective,
  lastCheckIn: {
    id: 746,
    value: 45,
    confidence: 7,
    createdOn: new Date(),
    modifiedOn: new Date(),
    changeInfo: 'So far so good',
    initiatives: 'Work a bit harder',
  } as CheckIn,
  createdOn: new Date(),
  modifiedOn: new Date(),
};
