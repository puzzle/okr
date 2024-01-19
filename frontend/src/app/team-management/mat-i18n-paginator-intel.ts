import { MatPaginatorIntl } from '@angular/material/paginator';
import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Subject } from 'rxjs';

@Injectable()
export class MatI18nPaginatorIntl implements MatPaginatorIntl {
  private readonly vonLabel: string;

  changes = new Subject<void>();
  itemsPerPageLabel: string;
  nextPageLabel: string;
  previousPageLabel: string;
  firstPageLabel: string;
  lastPageLabel: string;

  constructor(private readonly translate: TranslateService) {
    this.itemsPerPageLabel = this.translate.instant('PAGINATOR.MEMBERS_PRO_SEITE');
    this.nextPageLabel = this.translate.instant('PAGINATOR.NAECHSTE_SEITE');
    this.previousPageLabel = this.translate.instant('PAGINATOR.VORHERIGE_SEITE');
    this.firstPageLabel = this.translate.instant('PAGINATOR.ERSTE_SEITE');
    this.lastPageLabel = this.translate.instant('PAGINATOR.LETZTE_SEITE');
    this.vonLabel = this.translate.instant('PAGINATOR.VON');
  }

  getRangeLabel = (page: number, pageSize: number, length: number) => {
    if (length === 0 || pageSize === 0) {
      return `0 ${this.vonLabel} ${length}`;
    }

    length = Math.max(length, 0);
    const startIndex = page * pageSize;
    const endIndex = startIndex < length ? Math.min(startIndex + pageSize, length) : startIndex + pageSize;
    return startIndex + 1 + ' - ' + endIndex + ` ${this.vonLabel} ` + length;
  };
}
