interface MessageKeyMap {
  [key: string]: {
    KEY: string;
    methods: HTTP_TYPES[];
  };
}

export type HTTP_TYPES = 'GET' | 'POST' | 'PUT' | 'DELETE';

export const DATE_FORMAT = 'dd.MM.yyyy';
export const CONFIRM_DIALOG_WIDTH: string = '450px';

export const DRAWER_ROUTES = ['objective', 'keyresult'];
export const BLACKLIST_TOASTER_ROUTES_ERROR = ['/token'];
export const BLACKLIST_TOASTER_ROUTES_SUCCESS = ['/action', '/completed'];
export const WHITELIST_TOASTER_ROUTES_SUCCESS = ['/objectives', '/keyresults', '/checkins'];
export const WHITELIST_TOASTER_HTTP_METHODS_SUCCESS = ['PUT', 'POST', 'DELETE'];
export const SUCCESS_MESSAGE_KEYS: MessageKeyMap = {
  teams: {
    KEY: 'TEAM',
    methods: ['POST', 'PUT', 'DELETE'],
  },
  objectives: {
    KEY: 'OBJECTIVE',
    methods: ['POST', 'PUT', 'DELETE'],
  },
  keyresults: {
    KEY: 'KEY_RESULT',
    methods: ['POST', 'PUT', 'DELETE'],
  },
  checkins: {
    KEY: 'CHECK_IN',
    methods: ['POST', 'PUT', 'DELETE'],
  },
};
