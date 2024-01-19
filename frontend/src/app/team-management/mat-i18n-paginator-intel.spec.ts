import { TestBed } from '@angular/core/testing';
import { MatI18nPaginatorIntl } from './mat-i18n-paginator-intel';
import { TranslateTestingModule } from 'ngx-translate-testing';
import * as de from '../../assets/i18n/de.json';

describe('MatI18nPaginatorIntl', () => {
  let service: MatI18nPaginatorIntl;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateTestingModule.withTranslations({ de: de })],
      providers: [MatI18nPaginatorIntl],
    });

    service = TestBed.inject(MatI18nPaginatorIntl);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getRangeLabel', () => {
    it('should return correct range label when length equals 0 or pageSize equals 0', () => {
      const result = service.getRangeLabel(0, 0, 0);
      expect(result).toEqual('0 von 0');
    });

    it('should return correct range label when startIndex is less than length', () => {
      const result = service.getRangeLabel(0, 10, 100);
      expect(result).toEqual('1 - 10 von 100');
    });

    it('should return correct range label when startIndex is greater than or equal to length', () => {
      const result = service.getRangeLabel(5, 20, 100);
      expect(result).toEqual('101 - 120 von 100');
    });
  });
});
