import { Pipe, PipeTransform } from '@angular/core';
import { formatCurrency } from '@angular/common';

@Pipe({
  name: 'unitTransformation',
})
export class UnitTransformationPipe implements PipeTransform {
  transform(value: number, unit: String): string {
    if (Number.isNaN(value)) {
      value = 0;
    }
    switch (unit) {
      case 'CHF':
        return value % 1 != 0 ? formatCurrency(value, 'en', '') : value + '.-';
      case 'PERCENT':
        return value + '%';
      case 'FTE':
        return value.toString();
      default:
        return value.toString();
    }
  }
}
