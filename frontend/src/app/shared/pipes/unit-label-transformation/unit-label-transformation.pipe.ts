import { Unit } from '../../types/enums/Unit';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'unitLabelTransformation',
})
export class UnitLabelTransformationPipe implements PipeTransform {
  transform(unitLabel: string): string {
    switch (unitLabel) {
      case Unit.PERCENT:
        return '%';
      case Unit.FTE:
        return Unit.FTE;
      case Unit.CHF:
        return Unit.CHF;
      case Unit.EUR:
        return Unit.EUR;
      case Unit.NUMBER:
        return '';
      default:
        return unitLabel;
    }
  }
}
