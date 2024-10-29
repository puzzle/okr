import { Pipe, PipeTransform } from '@angular/core';
import { Unit } from '../../types/enums/Unit';

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
        return 'CHF ' + this.roundAndAddThousandSplitSign(value);
      case Unit.EUR:
        return this.roundAndAddThousandSplitSign(value) + ' €';
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

  roundAndAddThousandSplitSign(value: number): string {
    return (+value.toFixed(2)).toLocaleString('en-US').replace(/,/g, "'");
  }
}
