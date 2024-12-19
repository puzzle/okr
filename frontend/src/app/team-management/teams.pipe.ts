import { Pipe, PipeTransform } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Pipe({
  name: 'teams'
})
export class TeamsPipe implements PipeTransform {
  private readonly SEPARATOR = ', ';

  constructor (private readonly translate: TranslateService) {}

  transform (teams: string[], maxEntries: number | undefined): string {
    if (!teams?.length) {
      return '';
    }
    if (!maxEntries) {
      return teams.join(this.SEPARATOR);
    }
    const overflow = teams.length - maxEntries;
    if (overflow > 0) {
      return (
        teams.slice(0, maxEntries)
          .join(this.SEPARATOR) +
          ', ' +
          this.translate.instant('TEAM_MANAGEMENT.WEITERE', { overflow })
      );
    }
    return teams.join(this.SEPARATOR);
  }
}
