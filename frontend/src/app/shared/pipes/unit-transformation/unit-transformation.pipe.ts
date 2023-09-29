import { Pipe, PipeTransform } from '@angular/core';
import { formatCurrency } from '@angular/common';
import { Unit } from '../../types/enums/Unit';

@Pipe({
  name: 'unitTransformation',
})
export class UnitTransformationPipe implements PipeTransform {
  transform(value: number, unit: String): string {
    if (Number.isNaN(value)) {
      value = 0;
    }
    switch (unit) {
      case Unit.CHF:
        return value % 1 != 0 ? formatCurrency(value, 'en', '') : value + '.-';
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
}
