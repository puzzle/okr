import { State } from './types/enums/State';
import { CheckInMin, CheckInMinMetric, CheckInMinOrdinal } from './types/model/CheckInMin';
import { KeyResultMetricMin } from './types/model/KeyResultMetricMin';
import { KeyResultOrdinalMin } from './types/model/KeyResultOrdinalMin';
import { KeyresultMin } from './types/model/KeyresultMin';
import { ObjectiveMin } from './types/model/ObjectiveMin';
import { OverviewEntity } from './types/model/OverviewEntity';
import { KeyResultObjective } from './types/model/KeyResultObjective';
import { Quarter } from './types/model/Quarter';
import { KeyResultOrdinal } from './types/model/KeyResultOrdinal';
import { CheckIn, CheckInMetric, CheckInOrdinal } from './types/model/CheckIn';
import { Objective } from './types/model/Objective';
import { User } from './types/model/User';
import { KeyResultMetric } from './types/model/KeyResultMetric';
import { Unit } from './types/enums/Unit';
import { Team } from './types/model/Team';
import { Action } from './types/model/Action';

export const teamFormObject = {
  name: 'newTeamName',
};

export const marketingTeamWriteable: Team = {
  id: 1,
  version: 2,
  name: 'Marketing Team',
  writeable: true,
};

export const marketingTeamNotWriteable: Team = {
  id: 1,
  version: 3,
  name: 'Marketing Team',
  writeable: false,
};

export const team1: Team = {
  id: 1,
  version: 2,
  name: 'Team1',
  writeable: false,
};

export const team2: Team = {
  id: 2,
  version: 3,
  name: 'Team2',
  writeable: false,
};

export const team3: Team = {
  id: 3,
  version: 4,
  name: 'Team3',
  writeable: false,
};

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

export const quarterMin: Quarter = new Quarter(1, 'GJ 23/24-Q1', null, null);

export const quarter1: Quarter = new Quarter(1, 'GJ 22/23-Q4', new Date('2023-04-01'), new Date('2023-07-30'));

export const quarter2: Quarter = new Quarter(2, 'GJ 22/23-Q3', new Date('2023-01-01'), new Date('2023-03-31'));

export const quarterBacklog: Quarter = new Quarter(999, 'GJ 23/24-Q1', null, null);

export const quarterList: Quarter[] = [quarter1, quarter2, quarterBacklog];

export const checkInMetric: CheckInMinMetric = {
  id: 815,
  version: 1,
  value: 15,
  confidence: 5,
  createdOn: '2023-07-20T12:34:56Z' as unknown as Date,
  initiatives: 'Initiatives metric',
  changeInfo: 'Changeinfo metric',
  writeable: true,
} as CheckInMinMetric;

export const checkInMetricWriteableFalse: CheckInMinMetric = {
  id: 815,
  version: 1,
  value: 15,
  confidence: 6,
  createdOn: '2023-07-20T12:34:56Z' as unknown as Date,
  initiatives: 'Initiatives metric writeable false',
  changeInfo: 'Changeinfo metric writeable false',
  writeable: false,
} as CheckInMinMetric;

export const checkInOrdinal: CheckInMinOrdinal = {
  id: 816,
  version: 2,
  zone: 'COMMIT',
  confidence: 7,
  createdOn: '2023-07-22T08:45:21Z' as unknown as Date,
  initiatives: 'Initiatives ordinal',
  changeInfo: 'Changeinfo ordinal',
  writeable: true,
} as CheckInMinOrdinal;

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
    zone: 'COMMIT',
    confidence: 8,
    createdOn: '2023-07-22T08:45:21Z' as unknown as Date,
    initiatives: 'Initiatives of ordinal',
    changeInfo: 'Changeinfo ordinal',
  } as CheckInMinOrdinal,
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

export const objectiveResponse1: any = {
  id: 101,
  version: 1,
  title: 'Increase Environment Engagement',
  state: 'ONGOING',
  quarter: quarterMin,
  keyResults: [keyResultMetricMin, keyResultOrdinalMin] as KeyresultMin[],
};

export const objectiveResponse2: any = {
  id: 102,
  version: 1,
  title: 'Increase Social Engagement',
  state: 'DRAFT',
  quarter: quarterMin,
  keyResults: [keyResultMetricMin, keyResultOrdinalMin] as KeyresultMin[],
};

export const objectiveResponse3: any = {
  id: 103,
  version: 1,
  title: 'Increase Member Engagement',
  state: 'NOTSUCCESSFUL',
  quarter: quarterMin,
  keyResults: [keyResultMetricMin, keyResultOrdinalMin] as KeyresultMin[],
};

export const objectiveResponse4: any = {
  id: 104,
  version: 1,
  title: 'Increase Company Engagement',
  state: 'SUCCESSFUL',
  quarter: quarterMin,
  keyResults: [keyResultMetricMin, keyResultOrdinalMin] as KeyresultMin[],
};

export const overViewEntity1: OverviewEntity = {
  team: marketingTeamWriteable,
  objectives: [objectiveMin, objectiveMin, objectiveMin] as ObjectiveMin[],
  writeable: true,
};

export const overViewEntity2: OverviewEntity = {
  team: marketingTeamNotWriteable,
  objectives: [objectiveMin, objectiveMin, objectiveMin] as ObjectiveMin[],
  writeable: true,
};

export const overViewEntityResponse1: any = {
  team: team1,
  objectives: [objectiveResponse1, objectiveResponse2],
  writable: true,
};

export const overViewEntityResponse2: any = {
  team: team2,
  objectives: [objectiveResponse3, objectiveResponse4],
  writable: false,
};

export const overviews: OverviewEntity[] = [overViewEntityResponse1, overViewEntityResponse2];

export const quarter: Quarter = new Quarter(1, '23.02.2025', new Date(), new Date());

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
  quarterLabel: 'GJ 22/23-Q2',
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
  quarterLabel: 'GJ 22/23-Q2',
  state: State.NOTSUCCESSFUL,
  writeable: false,
};

export const firstCheckIn: CheckInMinMetric = {
  id: 1,
  version: 1,
  value: 77,
  confidence: 5,
  changeInfo: '',
  initiatives: '',
  createdOn: new Date(),
  writeable: true,
};

export const secondCheckIn: CheckInMinMetric = {
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
  isOkrChampion: false,
  userTeamList: [
    {
      id: 1,
      team: team1,
      isTeamAdmin: false,
    },
  ],
  email: 'bob.baumeister@puzzle.ch',
};

export const users: User[] = [
  testUser,
  {
    id: 2,
    firstname: 'Paco',
    lastname: 'Egiman',
    isOkrChampion: true,
    userTeamList: [],
    email: 'peggimann@puzzle.ch',
  },
  {
    id: 3,
    firstname: 'Robin',
    lastname: 'Papier',
    isOkrChampion: false,
    userTeamList: [],
    email: 'robin.papier@puzzle.ch',
  },
  {
    id: 4,
    firstname: 'Key Result',
    lastname: 'Owner',
    isOkrChampion: false,
    userTeamList: [],
    email: 'keyresult.owner@puzzle.ch',
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
  owner: users[3],
  keyResultType: 'ordinal',
  objective: {
    id: 301,
    version: 1,
    state: State.DRAFT,
    quarter: new Quarter(1, 'GJ 23/24-Q1', new Date(), new Date()),
    writeable: true,
  } as KeyResultObjective,
  lastCheckIn: {
    id: 745,
    version: 1,
    zone: 'FAIL',
    confidence: 8,
    createdOn: new Date(),
    modifiedOn: new Date(),
    changeInfo: 'info',
    initiatives: 'some',
    writeable: true,
  } as CheckInOrdinal,
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
  owner: users[3],
  keyResultType: 'ordinal',
  objective: {
    id: 301,
    version: 1,
    state: State.DRAFT,
    quarter: new Quarter(1, 'GJ 23/24-Q1', new Date(), new Date()),
    writeable: true,
  } as KeyResultObjective,
  lastCheckIn: {
    id: 746,
    version: 1,
    zone: 'FAIL',
    confidence: 3,
    createdOn: new Date(),
    modifiedOn: new Date(),
    changeInfo: 'Does not look good',
    initiatives: 'We have to be faster',
    writeable: true,
  } as CheckInOrdinal,
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
  owner: users[3],
  keyResultType: 'ordinal',
  objective: {
    id: 301,
    version: 1,
    state: State.DRAFT,
    quarter: new Quarter(1, 'GJ 23/24-Q1', new Date(), new Date()),
    writeable: false,
  } as KeyResultObjective,
  lastCheckIn: {
    id: 746,
    version: 1,
    zone: 'FAIL',
    confidence: 3,
    createdOn: new Date(),
    modifiedOn: new Date(),
    changeInfo: 'Also not writeable',
    initiatives: 'Perhaps make it writeable',
    writeable: false,
  } as CheckInOrdinal,
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
  owner: users[3],
  keyResultType: 'metric',
  objective: {
    id: 302,
    version: 1,
    state: State.DRAFT,
    quarter: new Quarter(1, 'GJ 23/24-Q1', new Date(), new Date()),
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
  } as CheckInMetric,
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
  owner: users[3],
  keyResultType: 'metric',
  objective: {
    id: 302,
    state: State.DRAFT,
    quarter: new Quarter(1, 'GJ 23/24-Q1', new Date(), new Date()),
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
  } as CheckInMetric,
  createdOn: new Date(),
  modifiedOn: new Date(),
  actionList: [action1, action2],
  writeable: true,
};
