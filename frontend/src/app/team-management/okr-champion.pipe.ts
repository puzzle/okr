import { Pipe, PipeTransform } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Pipe({
  name: 'okrChampion',
  standalone: true,
})
export class OkrChampionPipe implements PipeTransform {
  constructor(private readonly translate: TranslateService) {}

  transform(isOkrChampion: boolean): string {
    return isOkrChampion ? this.translate.instant('SHARED.JA') : '-';
  }
}
