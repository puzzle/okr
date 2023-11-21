import { Pipe, PipeTransform } from '@angular/core';
import { CHAR_REGEX } from '../../regexLibrary';

@Pipe({
  name: 'parseUnitValue',
})
export class ParseUnitValuePipe implements PipeTransform {
  transform(param: string | null): number {
    const value: string = param || '0';
    if (value.toString().at(0) == '-') {
      return +('-' + value.toString().replace(CHAR_REGEX, ''));
    }
    return Number(value.toString().replace(CHAR_REGEX, ''));
  }
}
