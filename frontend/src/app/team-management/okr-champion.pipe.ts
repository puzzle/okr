import { Pipe, PipeTransform, inject } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Pipe({
  name: 'okrChampion',
  standalone: true
})
export class OkrChampionPipe implements PipeTransform {
  private readonly translate = inject(TranslateService);


  transform(isOkrChampion: boolean): string {
    return isOkrChampion ? this.translate.instant('SHARED.JA') : '-';
  }
}
