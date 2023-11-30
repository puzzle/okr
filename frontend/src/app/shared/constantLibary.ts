interface MessageKeyMap {
  [key: string]: {
    KEY: string;
    methods: HTTP_TYPE[];
    exceptions: {
      method: HTTP_TYPE;
      statusCode: number;
      key: string;
    }[];
  };
}

export type HTTP_TYPE = 'GET' | 'POST' | 'PUT' | 'DELETE';
export type TOASTER_TYPE = 'ERROR' | 'WARN' | 'SUCCESS';

export const SUCCESS_MESSAGE_KEY_PREFIX = 'SUCCESS.';
export const ERROR_MESSAGE_KEY_PREFIX = 'ERROR.';

export const DATE_FORMAT = 'dd.MM.yyyy';
export const CONFIRM_DIALOG_WIDTH: string = '450px';

export const DRAWER_ROUTES = ['objective', 'keyresult'];
export const BLACKLIST_TOASTER_ROUTES_ERROR = ['/token'];
export const WHITELIST_TOASTER_ROUTES_SUCCESS = ['/objectives', '/keyresults', '/checkins'];
export const WHITELIST_TOASTER_HTTP_METHODS_SUCCESS = ['PUT', 'POST', 'DELETE'];
export const SUCCESS_MESSAGE_MAP: MessageKeyMap = {
  teams: {
    KEY: 'TEAM',
    methods: ['POST', 'PUT', 'DELETE'],
    exceptions: [],
  },
  objectives: {
    KEY: 'OBJECTIVE',
    methods: ['POST', 'PUT', 'DELETE'],
    exceptions: [
      {
        key: 'IM_USED',
        statusCode: 226,
        method: 'PUT',
      },
    ],
  },
  keyresults: {
    KEY: 'KEY_RESULT',
    methods: ['POST', 'PUT', 'DELETE'],
    exceptions: [
      {
        key: 'IM_USED',
        statusCode: 226,
        method: 'PUT',
      },
    ],
  },
  checkins: {
    KEY: 'CHECK_IN',
    methods: ['POST', 'PUT', 'DELETE'],
    exceptions: [],
  },
};
