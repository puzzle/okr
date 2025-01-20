import { HttpType } from './types/enums/http-type';
import { ToasterType } from './types/enums/toaster-type';
import { HttpStatusCode } from '@angular/common/http';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Unit } from './types/enums/unit';
import { getFullNameOfUser, User } from './types/model/user';

type MessageKeyMap = Record<string, MessageEntry>;

export interface MessageEntry {
  KEY: string;
  methods: MessageMethod[];
}

export interface MessageMethod {
  method: HttpType;
  keysForCode?: MessageStatusCode[];
}

export interface MessageStatusCode {
  code: number;
  toaster?: ToasterType;
  key: string;
}
export const PUZZLE_TOP_BAR_HEIGHT = 48;
export const DEFAULT_HEADER_HEIGHT_PX = 140;

export const SUCCESS_MESSAGE_KEY_PREFIX = 'SUCCESS.';
export const ERROR_MESSAGE_KEY_PREFIX = 'ERROR.';

export const DATE_FORMAT = 'dd.MM.yyyy';
export const DRAWER_ROUTES = ['objective',
  'keyresult'];

export const GJ_REGEX_PATTERN = /^GJ \d{2}\/\d{2}-Q\d$/;

export const SUCCESS_MESSAGE_MAP: MessageKeyMap = {
  teams: {
    KEY: 'TEAM',
    methods: [{ method: HttpType.POST },
      { method: HttpType.PUT },
      { method: HttpType.DELETE }]
  },
  objectives: {
    KEY: 'OBJECTIVE',
    methods: [{ method: HttpType.POST },
      { method: HttpType.DELETE },
      {
        method: HttpType.PUT,
        keysForCode: [{
          key: 'IM_USED',
          toaster: ToasterType.WARN,
          code: HttpStatusCode.ImUsed
        }]
      }]
  },
  keyresults: {
    KEY: 'KEY_RESULT',
    methods: [{ method: HttpType.POST },
      { method: HttpType.DELETE },
      {
        method: HttpType.PUT,
        keysForCode: [{
          key: 'IM_USED',
          toaster: ToasterType.WARN,
          code: HttpStatusCode.ImUsed
        }]
      }]
  },
  checkins: {
    KEY: 'CHECK_IN',
    methods: [{ method: HttpType.POST },
      { method: HttpType.PUT }]
  },
  user: {
    KEY: 'USERS',
    methods: [{ method: HttpType.PUT },
      { method: HttpType.POST },
      { method: HttpType.DELETE }]
  }
};

export function getKeyResultForm(): FormGroup {
  return new FormGroup({
    title: new FormControl('', [Validators.required,
      Validators.minLength(2),
      Validators.maxLength(250)]),
    description: new FormControl('', [Validators.maxLength(4096)]),
    owner: new FormControl<User>({} as User, [Validators.required,
      Validators.nullValidator,
      ownerValidator()]),
    actionList: new FormControl([]),
    keyResultType: new FormControl('metric'),
    metric: new FormGroup({
      unit: new FormControl<Unit>(Unit.NUMBER, [Validators.required]),
      baseline: new FormControl(0, [Validators.required,
        numberValidator()]),
      targetGoal: new FormControl(0, [Validators.required,
        numberValidator()]),
      stretchGoal: new FormControl(0, [Validators.required,
        numberValidator()])
    }),
    ordinal: new FormGroup({
      commitZone: new FormControl('', [Validators.required,
        Validators.maxLength(400),
        Validators.minLength(2)]),
      targetZone: new FormControl('', [Validators.required,
        Validators.maxLength(400),
        Validators.minLength(2)]),
      stretchZone: new FormControl('', [Validators.required,
        Validators.maxLength(400),
        Validators.minLength(2)])
    })
  });
}

function ownerValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const user = control.value as User;
    if (user?.id > 0 && getFullNameOfUser(user).length > 3) {
      return null;
    }
    return { invalid_user: { value: control.value } };
  };
}

function numberValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const isAllowed = (/^-?\d+\.?\d*$/).test(control.value);
    return isAllowed ? null : { number: { value: control.value } };
  };
}
