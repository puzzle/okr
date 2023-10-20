import { Pipe, PipeTransform } from '@angular/core';
import { Unit } from '../../types/enums/Unit';
import { THOUSAND_SEPERATOR } from '../../regexLibrary';

@Pipe({
  name: 'unitValueTransformation',
})
export class UnitValueTransformationPipe implements PipeTransform {
  transform(value: number, unit: string): string {
    /* If user tries to input String, set value to 0 */
    if (Number.isNaN(value)) {
      value = 0;
    }
    switch (unit) {
      case Unit.CHF:
        return 'CHF ' + this.addCHFSign(this.roundAndAddThousandSplitSign(value));
      case Unit.PERCENT:
        return value + '%';
      case Unit.FTE:
        return this.roundAndAddThousandSplitSign(value) + ' FTE';
      case Unit.NUMBER:
        return this.roundAndAddThousandSplitSign(value);
      default:
        return value.toString();
    }
  }

  addCHFSign(value: string): string {
    return !+value.split('.')[1] ? value.split('.')[0] + '.-' : value;
  }

  roundAndAddThousandSplitSign(value: number): string {
    return (+value.toFixed(2)).toString().replace(THOUSAND_SEPERATOR, "'");
  }
}
