import { Unit } from '../../types/enums/Unit';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'unitTransformation',
})
export class UnitTransformationPipe implements PipeTransform {
  transform(value: number, label: string): string {
    return this.transformValue(value) + this.transformLabel(label);
  }

  transformValue(value: number): string {
    return Number.isNaN(value) ? '0' : this.roundAndAddThousandSplitSign(value);
  }

  roundAndAddThousandSplitSign(value: number): string {
    return (+value.toFixed(2)).toLocaleString('en-US').replace(/,/g, "'");
  }

  transformLabel(label: string): string {
    switch (label) {
      case Unit.PERCENT:
        return '%';
      case Unit.FTE:
        return ' ' + Unit.FTE;
      case Unit.CHF:
        return ' ' + Unit.CHF;
      case Unit.EUR:
        return ' ' + Unit.EUR;
      case Unit.NUMBER:
      default:
        return '';
    }
  }
}
