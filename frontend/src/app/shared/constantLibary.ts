interface MessageKeyMap {
  [key: string]: {
    KEY: string;
    methods: HttpMethod[];
  };
}

export type HttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE';

export const DATE_FORMAT = 'dd.MM.yyyy';
export const CONFIRM_DIALOG_WIDTH: string = '450px';

export const drawerRoutes = ['objective', 'keyresult'];

export const NO_TOASTER_ROUTES_ERROR = ['/token'];
export const NO_TOASTER_ROUTES_SUCCESS = ['/action'];
export const ALLOWED_TOASTER_METHODS_SUCCESS = ['PUT', 'POST', 'DELETE'];
export const messageKeys: MessageKeyMap = {
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
  action: {
    KEY: 'CHECK_IN',
    methods: ['PUT', 'DELETE'],
  },
  completed: {
    KEY: 'COMPLETED',
    methods: ['POST', 'DELETE'],
  },
};
