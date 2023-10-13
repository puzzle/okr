import { Pipe, PipeTransform } from '@angular/core';
import { Unit } from '../../types/enums/Unit';
import { THOUSAND_SEPERATOR } from '../../regexLibrary';

@Pipe({
  name: 'unitValueTransformation',
})
export class UnitValueTransformationPipe implements PipeTransform {
  transform(value: number, unit: String): string {
    if (Number.isNaN(value)) {
      value = 0;
    }
    switch (unit) {
      case Unit.CHF:
        return this.addCHFSign(value.toFixed(2).toString().replace(THOUSAND_SEPERATOR, "'"));
      case Unit.PERCENT:
        return value + '%';
      case Unit.FTE:
        return value.toString();
      case Unit.NUMBER:
        return value.toString();
      default:
        return value.toString();
    }
  }

  addCHFSign(value: string): string {
    return !+value.split('.')[1] ? value.split('.')[0] + '.-' : value;
  }
}
