import {
  AbstractControl,
  NG_VALIDATORS,
  ValidationErrors,
  Validator,
} from '@angular/forms';
import { Directive, Input } from '@angular/core';

@Directive({
  selector: '[measureValueValidator]',
  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: MeasureValueValidatorDirective,
      multi: true,
    },
  ],
})
export class MeasureValueValidatorDirective implements Validator {
  @Input('measureValueValidator') unit: string = '';

  validate(control: AbstractControl): ValidationErrors | null {
    let value: string = control.value;
    switch (this.unit) {
      case 'BINARY': {
        return this.proceedRegex(value, '^[0-1]{1}$');
      }
      case 'PERCENT': {
        return this.proceedRegex(value, '^[0-9][0-9]?$|^100$');
      }
      case 'CHF': {
        return this.proceedRegex(value, '^[0-9]*$');
      }
      case 'NUMBER': {
        return this.proceedRegex(value, '^[0-9]*$');
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
