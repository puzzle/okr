interface MessageKeyMap {
  [key: string]: {
    KEY: string;
    methods: (MessageKey | HTTP_TYPE)[];
  };
}

export interface MessageKey {
  method: HTTP_TYPE;
  keysForCode?: {
    code: number;
    toaster?: TOASTER_TYPE;
    key: string;
  }[];
}

export type HTTP_TYPE = 'GET' | 'POST' | 'PUT' | 'DELETE';
export type TOASTER_TYPE = 'ERROR' | 'WARN' | 'SUCCESS';

export const SUCCESS_MESSAGE_KEY_PREFIX = 'SUCCESS.';
export const ERROR_MESSAGE_KEY_PREFIX = 'ERROR.';

export const DATE_FORMAT = 'dd.MM.yyyy';
export const CONFIRM_DIALOG_WIDTH: string = '450px';
export const DRAWER_ROUTES = ['objective', 'keyresult'];
export const BLACKLIST_TOASTER_ROUTES_ERROR = ['/token'];
export const SUCCESS_MESSAGE_MAP: MessageKeyMap = {
  teams: {
    KEY: 'TEAM',
    methods: ['POST', 'PUT', 'DELETE'],
  },
  objectives: {
    KEY: 'OBJECTIVE',
    methods: [
      'POST',
      'DELETE',
      {
        method: 'PUT',
        keysForCode: [
          {
            key: 'IM_USED',
            toaster: 'WARN',
            code: 226,
          },
        ],
      },
    ],
  },
  keyresults: {
    KEY: 'KEY_RESULT',
    methods: [
      'POST',
      'DELETE',
      {
        method: 'PUT',
        keysForCode: [
          {
            key: 'IM_USED',
            code: 226,
            toaster: 'WARN',
          },
        ],
      },
    ],
  },
  checkins: {
    KEY: 'CHECK_IN',
    methods: ['POST', 'PUT', 'DELETE'],
  },
  action: {
    KEY: 'CHECK_IN',
    methods: ['PUT', 'DELETE'],
  },
  completed: {
    KEY: 'COMPLETED',
    methods: ['POST', 'DELETE'],
  },
};
