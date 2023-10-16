import { Pipe, PipeTransform } from '@angular/core';
import { CHAR_REGEX } from '../../regexLibrary';

@Pipe({
  name: 'parseUnitValue',
})
export class ParseUnitValuePipe implements PipeTransform {
  transform(value: string): number {
    if (value.toString().at(0) == '-') {
      return +('-' + value.toString().replace(CHAR_REGEX, ''));
    }
    return +value.toString().replace(CHAR_REGEX, '');
  }
}
