import { HttpType } from './types/enums/HttpType';
import { ToasterType } from './types/enums/ToasterType';

interface MessageKeyMap {
  [key: string]: {
    KEY: string;
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

export const SUCCESS_MESSAGE_KEY_PREFIX = 'SUCCESS.';
export const ERROR_MESSAGE_KEY_PREFIX = 'ERROR.';

export const DATE_FORMAT = 'dd.MM.yyyy';
export const CONFIRM_DIALOG_WIDTH: string = '450px';
export const DRAWER_ROUTES = ['objective', 'keyresult'];
export const BLACKLIST_TOASTER_ROUTES_ERROR = ['/token'];
export const SUCCESS_MESSAGE_MAP: MessageKeyMap = {
  teams: {
    KEY: 'TEAM',
    methods: [{ method: HttpType.POST }, { method: HttpType.PUT }, { method: HttpType.DELETE }],
  },
  objectives: {
    KEY: 'OBJECTIVE',
    methods: [
      { method: HttpType.POST },
      { method: HttpType.DELETE },
      {
        method: HttpType.PUT,
        keysForCode: [
          {
            key: 'IM_USED',
            toaster: ToasterType.WARN,
            code: 226,
          },
        ],
      },
    ],
  },
  keyresults: {
    KEY: 'KEY_RESULT',
    methods: [
      { method: HttpType.POST },
      { method: HttpType.DELETE },
      {
        method: HttpType.PUT,
        keysForCode: [
          {
            key: 'IM_USED',
            toaster: ToasterType.WARN,
            code: 226,
          },
        ],
      },
    ],
  },
  checkins: {
    KEY: 'CHECK_IN',
    methods: [{ method: HttpType.POST }, { method: HttpType.PUT }, { method: HttpType.DELETE }],
  },
};
