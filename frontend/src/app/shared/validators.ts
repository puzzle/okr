import {
  AbstractControl,
  NG_VALIDATORS,
  ValidationErrors,
  Validator,
  ValidatorFn,
} from '@angular/forms';
import { Directive, Input } from '@angular/core';
import { NUMBER_REGEX, PERCENT_REGEX } from './regexLibrary';

@Directive({
  selector: '[unitValueValidator]',
  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: UnitValueValidator,
      multi: true,
    },
  ],
})
export class UnitValueValidator implements Validator {
  @Input('unitValueValidator') unit: string | null = '';

  validate(control: AbstractControl): ValidationErrors | null {
    let value: string = control.value;
    switch (this.unit) {
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

export function comparisonValidator(
  secondControl: AbstractControl
): ValidatorFn {
  return (firstControl: AbstractControl): ValidationErrors | null => {
    return firstControl.value == secondControl.value ? { valid: false } : null;
  };
}
