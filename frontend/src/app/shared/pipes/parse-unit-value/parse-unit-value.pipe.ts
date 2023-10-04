import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'parseUnitValue',
})
export class ParseUnitValuePipe implements PipeTransform {
  transform(value: string): number {
    return +value.replaceAll('%', '').replaceAll('.-', '')!;
  }
}
