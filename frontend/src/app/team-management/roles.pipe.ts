import { Pipe, PipeTransform } from '@angular/core';
import { UserRole } from '../shared/types/enums/user-role';
import { TranslateService } from '@ngx-translate/core';

@Pipe({
  name: 'roles',
  standalone: false
})
export class RolesPipe implements PipeTransform {
  constructor(private translate: TranslateService) {}

  transform(roles: UserRole[]): string {
    if (!roles?.length) {
      return '';
    }
    return roles.map((r) => this.translate.instant('USER_ROLE.' + r))
      .join(', ');
  }
}
