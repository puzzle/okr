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
import { Action } from './types/model/Action';

export const teamMin1: TeamMin = {
  id: 1,
  name: 'Marketing Team',
  writable: true,
} as TeamMin;

export const teamMin2: TeamMin = {
  id: 1,
  name: 'Marketing Team',
  writable: false,
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

export const action1: Action = {
  id: 33,
  version: 1,
  action: 'Drucker kaufen',
  priority: 0,
  isChecked: false,
  keyResultId: 1,
};

export const action2: Action = {
  id: 44,
  version: 1,
  action: 'Blätter kaufen',
  priority: 1,
  isChecked: true,
  keyResultId: 2,
};

export const action3: Action = {
  id: null,
  version: 1,
  action: '',
  priority: 3,
  isChecked: false,
  keyResultId: null,
};

export const addedAction: Action = {
  action: '',
  priority: 0,
  keyResultId: 1,
} as Action;

export const quarterMin: QuarterMin = {
  id: 1,
  label: 'GJ 23/24-Q1',
} as QuarterMin;

export const checkInMetric: CheckInMin = {
  id: 815,
  version: 1,
  value: 15,
  confidence: 5,
  createdOn: '2023-07-20T12:34:56Z' as unknown as Date,
  initiatives: 'Initiatives metric',
  changeInfo: 'Changeinfo metric',
  writeable: true,
} as CheckInMin;

export const checkInMetricWriteableFalse: CheckInMin = {
  id: 815,
  version: 1,
  value: 15,
  confidence: 6,
  createdOn: '2023-07-20T12:34:56Z' as unknown as Date,
  initiatives: 'Initiatives metric writeable false',
  changeInfo: 'Changeinfo metric writeable false',
  writeable: false,
} as CheckInMin;

export const checkInOrdinal: CheckInMin = {
  id: 816,
  version: 2,
  value: 'COMMIT',
  confidence: 7,
  createdOn: '2023-07-22T08:45:21Z' as unknown as Date,
  initiatives: 'Initiatives ordinal',
  changeInfo: 'Changeinfo ordinal',
  writeable: true,
} as CheckInMin;

export const keyResultMetricMin: KeyResultMetricMin = {
  id: 201,
  version: 1,
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
  version: 1,
  title: 'Achieve 20% Increase in Daily Active Users',
  keyResultType: 'metric',
  unit: '%',
  baseline: 25.0,
  stretchGoal: 75.0,
  lastCheckIn: {
    id: 800,
    version: 1,
    value: 50,
    confidence: 4,
    createdOn: new Date(),
    modifiedOn: new Date(),
    changeInfo: 'Half way through',
    initiatives: 'Quality before quantity',
    writeable: true,
  },
  type: 'keyResult',
} as KeyResultMetricMin;

export const keyResultMetricMinScoringInversion: KeyResultMetricMin = {
  id: 306,
  version: 1,
  title: 'Achieve 20% Increase in Daily Active Users',
  keyResultType: 'metric',
  unit: '%',
  baseline: 50.0,
  stretchGoal: 0.0,
  lastCheckIn: {
    id: 800,
    version: 1,
    value: 12.5,
    confidence: 4,
    createdOn: new Date(),
    modifiedOn: new Date(),
    changeInfo: 'More Changes',
    initiatives: 'Some initatives',
    writeable: true,
  },
  type: 'keyResult',
} as KeyResultMetricMin;

export const keyResultOrdinalMinScoring: KeyResultOrdinalMin = {
  id: 202,
  version: 1,
  title: 'We want to bake 10 cakes',
  keyResultType: 'ordinal',
  commitZone: '5 cakes',
  targetZone: '10 cakes',
  stretchGoal: '13 cakes',
  lastCheckIn: {
    id: 830,
    version: 1,
    value: 'COMMIT',
    confidence: 8,
    createdOn: '2023-07-22T08:45:21Z' as unknown as Date,
    initiatives: 'Initiatives of ordinal',
    changeInfo: 'Changeinfo ordinal',
  } as CheckInMin,
} as KeyResultOrdinalMin;

export const keyResultOrdinalMin: KeyResultOrdinalMin = {
  id: 202,
  version: 1,
  title: 'Reduce Bounce Rate',
  keyResultType: 'ordinal',
  commitZone: '3 Birnen',
  targetZone: '2 Birnen und 2 Äpfel',
  stretchGoal: 'Alle Früchte',
  lastCheckIn: checkInOrdinal,
} as KeyResultOrdinalMin;

export const objectiveMin: ObjectiveMin = {
  id: 101,
  version: 1,
  title: 'Increase User Engagement',
  state: State.ONGOING,
  quarter: quarterMin,
  keyResults: [keyResultMetricMin, keyResultOrdinalMin] as KeyresultMin[],
} as ObjectiveMin;
export const overViewEntity1: OverviewEntity = {
  team: teamMin1,
  objectives: [objectiveMin, objectiveMin, objectiveMin] as ObjectiveMin[],
  writable: true,
};

export const overViewEntity2: OverviewEntity = {
  team: teamMin2,
  objectives: [objectiveMin, objectiveMin, objectiveMin] as ObjectiveMin[],
  writable: true,
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
  version: 1,
  title: 'KeyResult Title',
  unit: 'CHF',
  baseline: 5.0,
  stretchGoal: 15.0,
  lastCheckIn: checkInMetric,
  keyResultType: 'keyResult',
} as KeyResultMetricMin;

export const objective: Objective = {
  id: 5,
  version: 1,
  title: 'title',
  description: 'description',
  teamId: 2,
  quarterId: 2,
  state: State.SUCCESSFUL,
  writeable: true,
};

export const objectiveWriteableFalse: Objective = {
  id: 6,
  version: 1,
  title: 'titleWriteableFalse',
  description: 'descriptionWriteableFalse',
  teamId: 2,
  quarterId: 2,
  state: State.NOTSUCCESSFUL,
  writeable: false,
};

export const firstCheckIn: CheckInMin = {
  id: 1,
  version: 1,
  value: 77,
  confidence: 5,
  changeInfo: '',
  initiatives: '',
  createdOn: new Date(),
  writeable: true,
};

export const secondCheckIn: CheckInMin = {
  id: 2,
  version: 1,
  value: 89,
  confidence: 5,
  changeInfo: '',
  initiatives: '',
  createdOn: new Date(),
  writeable: true,
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
  version: 1,
  title: 'Ausbauen des Früchtesortiments',
  description: 'Dient zur Gesunderhaltung der Members',
  commitZone: 'Äpfel',
  targetZone: 'Äpfel und Birnen',
  stretchZone: 'Äpfel, Birnen, Bananen und Erdberen',
  owner: { id: 1, firstname: 'firstname', lastname: 'lastname', username: 'username' },
  keyResultType: 'ordinal',
  objective: {
    id: 301,
    version: 1,
    state: State.DRAFT,
    quarter: {
      id: 1,
      label: 'GJ 23/24-Q1',
      startDate: new Date(),
      endDate: new Date(),
    } as Quarter,
    writeable: true,
  } as KeyResultObjective,
  lastCheckIn: {
    id: 745,
    version: 1,
    value: 'FAIL',
    confidence: 8,
    createdOn: new Date(),
    modifiedOn: new Date(),
    changeInfo: 'info',
    initiatives: 'some',
    writeable: true,
  } as CheckIn,
  createdOn: new Date(),
  modifiedOn: new Date(),
  actionList: null,
  writeable: true,
};

export const keyResultOrdinal: KeyResultOrdinal = {
  id: 101,
  version: 1,
  title: 'Bauen eines Hauses',
  description: 'Ein neues Haus für die Puzzle Members',
  commitZone: 'Grundriss steht',
  targetZone: 'Gebäude gebaut',
  stretchZone: 'Inneneinrichtung gestaltet',
  owner: { id: 1, firstname: 'firstname', lastname: 'lastname', username: 'username' },
  keyResultType: 'ordinal',
  objective: {
    id: 301,
    version: 1,
    state: State.DRAFT,
    quarter: {
      id: 1,
      label: 'GJ 23/24-Q1',
      startDate: new Date(),
      endDate: new Date(),
    } as Quarter,
    writeable: true,
  } as KeyResultObjective,
  lastCheckIn: {
    id: 746,
    version: 1,
    value: 'FAIL',
    confidence: 3,
    createdOn: new Date(),
    modifiedOn: new Date(),
    changeInfo: 'Does not look good',
    initiatives: 'We have to be faster',
    writeable: true,
  } as CheckIn,
  createdOn: new Date(),
  modifiedOn: new Date(),
  actionList: [],
  writeable: true,
};

export const keyResultWriteableFalse: KeyResultOrdinal = {
  id: 101,
  version: 1,
  title: 'This is not writeable',
  description: 'Still not writeable',
  commitZone: 'Not writeable',
  targetZone: 'Not writeable',
  stretchZone: 'Not writeable',
  owner: { id: 1, firstname: 'firstname', lastname: 'lastname', username: 'username' },
  keyResultType: 'ordinal',
  objective: {
    id: 301,
    version: 1,
    state: State.DRAFT,
    quarter: {
      id: 1,
      label: 'GJ 23/24-Q1',
      startDate: new Date(),
      endDate: new Date(),
    } as Quarter,
    writeable: false,
  } as KeyResultObjective,
  lastCheckIn: {
    id: 746,
    version: 1,
    value: 'FAIL',
    confidence: 3,
    createdOn: new Date(),
    modifiedOn: new Date(),
    changeInfo: 'Also not writeable',
    initiatives: 'Perhaps make it writeable',
    writeable: false,
  } as CheckIn,
  createdOn: new Date(),
  modifiedOn: new Date(),
  actionList: [],
  writeable: false,
};

export const keyResultMetric: KeyResultMetric = {
  id: 102,
  version: 1,
  title: '100% aller Schweizer Kunden betreuen',
  description: 'Puzzle ITC erledigt die IT-Aufträge für 100% aller Unternehmen.',
  baseline: 30,
  stretchGoal: 100,
  unit: Unit.PERCENT,
  owner: { id: 1, firstname: 'firstname', lastname: 'lastname', username: 'username' },
  keyResultType: 'metric',
  objective: {
    id: 302,
    version: 1,
    state: State.DRAFT,
    quarter: {
      id: 1,
      label: 'GJ 23/24-Q1',
      startDate: new Date(),
      endDate: new Date(),
    } as Quarter,
    writeable: true,
  } as KeyResultObjective,
  lastCheckIn: {
    id: 746,
    version: 1,
    value: 45,
    confidence: 7,
    createdOn: new Date(),
    modifiedOn: new Date(),
    changeInfo: 'So far so good',
    initiatives: 'Work a bit harder',
    writeable: true,
  } as CheckIn,
  createdOn: new Date(),
  modifiedOn: new Date(),
  actionList: [action1, action2],
  writeable: true,
};

export const keyResultActions: KeyResultMetric = {
  id: 334,
  version: 1,
  title: 'Das Büro ist modern und vollständig',
  description: 'Puzzle ITC hat schöne Büros, wo es alles hat.',
  baseline: 10,
  stretchGoal: 30,
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
    writeable: true,
  } as KeyResultObjective,
  lastCheckIn: {
    id: 746,
    value: 45,
    confidence: 7,
    createdOn: new Date(),
    modifiedOn: new Date(),
    changeInfo: 'So far so good',
    initiatives: 'Work a bit harder',
    writeable: true,
  } as CheckIn,
  createdOn: new Date(),
  modifiedOn: new Date(),
  actionList: [action1, action2],
  writeable: true,
};
