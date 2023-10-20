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
        return Unit.FTE;
      case Unit.CHF:
        return Unit.CHF;
      case Unit.NUMBER:
        return '';
      default:
        return unitLabel;
    }
  }
}
