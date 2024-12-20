import { HttpType } from './types/enums/HttpType';
import { ToasterType } from './types/enums/ToasterType';
import { HttpStatusCode } from '@angular/common/http';

interface MessageKeyMap {
  [key: string]: {
    key: string;
    methods: {
      method: HttpType;
      keysForCode?: {
        code: number;
        toaster?: ToasterType;
        key: string;
      }[];
    }[];
  };
}

export const PUZZLE_TOP_BAR_HEIGHT: number = 48;
export const DEFAULT_HEADER_HEIGHT_PX = 140;

export const SUCCESS_MESSAGE_KEY_PREFIX = 'SUCCESS.';
export const ERROR_MESSAGE_KEY_PREFIX = 'ERROR.';

export const DATE_FORMAT = 'dd.MM.yyyy';
export const DRAWER_ROUTES = ['objective', 'keyresult'];

export const GJ_REGEX_PATTERN = /^GJ \d{2}\/\d{2}-Q\d$/;

export const SUCCESS_MESSAGE_MAP: MessageKeyMap = {
  teams: {
    key: 'TEAM',
    methods: [{ method: HttpType.POST }, { method: HttpType.PUT }, { method: HttpType.DELETE }],
  },
  objectives: {
    key: 'OBJECTIVE',
    methods: [
      { method: HttpType.POST },
      { method: HttpType.DELETE },
      {
        method: HttpType.PUT,
        keysForCode: [
          {
            key: 'IM_USED',
            toaster: ToasterType.WARN,
            code: HttpStatusCode.ImUsed,
          },
        ],
      },
    ],
  },
  keyresults: {
    key: 'KEY_RESULT',
    methods: [
      { method: HttpType.POST },
      { method: HttpType.DELETE },
      {
        method: HttpType.PUT,
        keysForCode: [
          {
            key: 'IM_USED',
            toaster: ToasterType.WARN,
            code: HttpStatusCode.ImUsed,
          },
        ],
      },
    ],
  },
  checkins: {
    key: 'CHECK_IN',
    methods: [{ method: HttpType.POST }, { method: HttpType.PUT }],
  },
  user: {
    key: 'USERS',
    methods: [{ method: HttpType.PUT }, { method: HttpType.POST }, { method: HttpType.DELETE }],
  },
};
