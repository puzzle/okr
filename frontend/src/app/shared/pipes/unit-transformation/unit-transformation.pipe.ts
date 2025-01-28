import { Unit } from '../../types/enums/unit';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'unitTransformation',
  standalone: false
})
export class UnitTransformationPipe implements PipeTransform {
  transform(value: number, unit: Unit): string {
    const label = unit.unitName;
    return this.transformValue(value) + this.transformLabel(label);
  }

  transformValue(value: number): string {
    return Number.isNaN(value) ? '0' : this.roundAndAddThousandSplitSign(value);
  }

  roundAndAddThousandSplitSign(value: number): string {
    return (+value.toFixed(2)).toLocaleString('en-US')
      .replace(/,/g, '\'');
  }

  transformLabel(label: string): string {
    switch (label) {
      case 'PERCENT':
        return '%';
      case 'FTE':
        return ' ' + 'FTE';
      case 'CHF':
        return ' CHF';
      case 'EUR':
        return ' EUR';
      case 'NUMBER':
      default:
        return '';
    }
  }
}
