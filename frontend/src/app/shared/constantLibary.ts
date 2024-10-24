import { HttpType } from './types/enums/HttpType';
import { ToasterType } from './types/enums/ToasterType';
import { HttpStatusCode } from '@angular/common/http';
import { MatDialogConfig } from '@angular/material/dialog';
import { isMobileDevice } from './common';

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

export const PUZZLE_TOP_BAR_HEIGHT: number = 48;
export const DEFAULT_HEADER_HEIGHT_PX = 140;

export const SUCCESS_MESSAGE_KEY_PREFIX = 'SUCCESS.';
export const ERROR_MESSAGE_KEY_PREFIX = 'ERROR.';

export const DATE_FORMAT = 'dd.MM.yyyy';
export const CONFIRM_DIALOG_WIDTH: string = '450px';
export const DRAWER_ROUTES = ['objective', 'keyresult'];

export const GJ_REGEX_PATTERN = /^GJ \d{2}\/\d{2}-Q\d$/;

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
            code: HttpStatusCode.ImUsed,
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
            code: HttpStatusCode.ImUsed,
          },
        ],
      },
    ],
  },
  checkIns: {
    KEY: 'CHECK_IN',
    methods: [{ method: HttpType.POST }, { method: HttpType.PUT }],
  },
  user: {
    KEY: 'USERS',
    methods: [{ method: HttpType.PUT }, { method: HttpType.POST }, { method: HttpType.DELETE }],
  },
};

export const OKR_DIALOG_CONFIG: MatDialogConfig = isMobileDevice()
  ? {
      maxWidth: '100vw',
      maxHeight: '100vh',
      height: '100vh',
      width: '100vw',
    }
  : {
      width: '45em',
      height: 'auto',
    };
