import {
  AbstractControl,
  NG_VALIDATORS,
  ValidationErrors,
  Validator,
} from '@angular/forms';
import { Directive, Input } from '@angular/core';
import { BINARY_REGEX, NUMBER_REGEX, PERCENT_REGEX } from './regexLibrary';

@Directive({
  selector: '[unitValueValidator]',
  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: ValueValidatorDirective,
      multi: true,
    },
  ],
})
export class ValueValidatorDirective implements Validator {
  @Input('unitValueValidator') unit: string | null = '';

  validate(control: AbstractControl): ValidationErrors | null {
    let value: string = control.value;
    switch (this.unit) {
      case 'BINARY': {
        return this.proceedRegex(value, BINARY_REGEX);
      }
      case 'PERCENT': {
        return this.proceedRegex(value, PERCENT_REGEX);
      }
      case 'CHF': {
        return this.proceedRegex(value, NUMBER_REGEX);
      }
      case 'NUMBER': {
        return this.proceedRegex(value, NUMBER_REGEX);
      }
      default: {
        return null;
      }
    }
  }

  proceedRegex(value: string, regex: string) {
    return String(value).match(regex) ? null : { valid: false };
  }
}
