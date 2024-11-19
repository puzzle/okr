import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'unitValueTransformation',
})
export class UnitValueTransformationPipe implements PipeTransform {
  transform(value: number): string {
    /* If user tries to input String, set value to 0 */
    return Number.isNaN(value) ? '0' : this.roundAndAddThousandSplitSign(value);
  }

  roundAndAddThousandSplitSign(value: number): string {
    return (+value.toFixed(2)).toLocaleString('en-US').replace(/,/g, "'");
  }
}
