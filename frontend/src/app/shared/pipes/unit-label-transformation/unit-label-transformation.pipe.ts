import { Pipe, PipeTransform } from '@angular/core';
import { Unit } from '../../types/enums/Unit';
import translation from '../../../../assets/i18n/de.json';

@Pipe({
  name: 'unitLabelTransformation',
})
export class UnitLabelTransformationPipe implements PipeTransform {
  transform(unitLabel: String): String {
    switch (unitLabel) {
      case Unit.PERCENT:
        return '';
      case Unit.FTE:
        return Unit.FTE;
      case Unit.CHF:
        return Unit.CHF;
      case Unit.NUMBER:
        return translation.UNIT.NUMBER;
      default:
        return unitLabel;
    }
  }
}
