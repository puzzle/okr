import { Pipe, PipeTransform } from '@angular/core';
import { Unit } from '../../types/enums/Unit';

@Pipe({
  name: 'unitLabelTransformation',
})
export class UnitLabelTransformationPipe implements PipeTransform {
  transform(unitLabel: string): string {
    switch (unitLabel) {
      case Unit.PERCENT:
        return '';
      case Unit.FTE:
        return '';
      case Unit.CHF:
        return '';
      case Unit.EUR:
        return '';
      case Unit.NUMBER:
        return '';
      default:
        return unitLabel;
    }
  }
}
