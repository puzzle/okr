import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultOverviewComponent } from './key-result-overview.component';
import { KeyResultMeasure } from '../../shared/services/key-result.service';
import * as keyresultData from '../../shared/testing/mock-data/keyresults.json';
import { MatCardModule } from '@angular/material/card';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { QuarterService, StartEndDateDTO } from '../../shared/services/quarter.service';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { DatePipe } from '@angular/common';
import { RouteService } from '../../shared/services/route.service';
import { TranslateTestingModule } from 'ngx-translate-testing';

describe('KeyResultOverviewComponent', () => {
  let component: KeyResultOverviewComponent;
  let fixture: ComponentFixture<KeyResultOverviewComponent>;
  let keyResult: KeyResultMeasure = keyresultData.keyresults[0];

  let startAndEndDate: StartEndDateDTO = {
    startDate: new Date(Date.UTC(2022, 9, 1)),
    endDate: new Date(Date.UTC(2023, 11, 31)),
  };

  const mockQuarterService = {
    getStartAndEndDateOfKeyresult: jest.fn(),
  };

  const mockRouteService = {
    navigate: jest.fn(),
  };

  describe('KeyResultDetail with measures', () => {
    beforeEach(() => {
      mockQuarterService.getStartAndEndDateOfKeyresult.mockReturnValue(of(startAndEndDate));
      TestBed.configureTestingModule({
        imports: [
          MatCardModule,
          RouterTestingModule,
          BrowserDynamicTestingModule,
          TranslateTestingModule.withTranslations({
            de: require('../../../assets/i18n/de.json'),
          }),
        ],
        declarations: [KeyResultOverviewComponent],
        providers: [
          DatePipe,
          { provide: QuarterService, useValue: mockQuarterService },
          { provide: RouteService, useValue: mockRouteService },
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyResultOverviewComponent);
      component = fixture.componentInstance;

      component.keyResult = keyResult;

      fixture.detectChanges();
    });

    afterEach(() => {
      mockQuarterService.getStartAndEndDateOfKeyresult.mockReset();
      mockRouteService.navigate.mockReset();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should have right mat-card-titles', () => {
      const matcardtitles = fixture.debugElement.queryAll(By.css('mat-card-title'));

      expect(matcardtitles.length).toBeTruthy();

      expect(matcardtitles[0].nativeElement.textContent).toEqual('Key Result 1');
      expect(matcardtitles[1].nativeElement.textContent).toEqual('Zielerreichung');
    });

    test('should have right mat-card-content', () => {
      const matcardcontent = fixture.debugElement.query(By.css('[data-testId="mat-card-content-keyresult"]'));
      expect(matcardcontent.nativeElement.textContent).toEqual(
        'Beschreibung:  This is the description Einheit:  Prozent Erwartete Entwicklung:  Erhöht Startdatum:  01.10.2022 Enddatum:  31.12.2023 '
      );
    });

    test('should have title and last measure date', () => {
      const matcardcontent = fixture.debugElement.query(By.css('[data-testId="mat-card-content-measure"]'));
      expect(matcardcontent.nativeElement.textContent).toContain('Datum der Messung:');
      expect(matcardcontent.nativeElement.textContent).toContain('23.12.2022');
    });

    test('should have title and change info', () => {
      const lastMeasure = fixture.debugElement.query(By.css('[data-testId="mat-card-content-measure"]'));
      expect(lastMeasure.nativeElement.textContent).toContain('Änderungen:');
      expect(lastMeasure.nativeElement.textContent).toContain(
        ' Lorem Ipsum is simply dummy text of the printing and typesetting industry. '
      );
    });

    test('should have title and start date of last measure', () => {
      const matcardcontent = fixture.debugElement.query(By.css('[data-testId="mat-card-content-keyresult"]'));
      expect(matcardcontent.nativeElement.textContent).toContain('Startdatum:');
      expect(matcardcontent.nativeElement.textContent).toContain('01.10.2022');
    });

    test('should have title and end date of last measure', () => {
      const matcardcontent = fixture.debugElement.query(By.css('[data-testId="mat-card-content-keyresult"]'));
      expect(matcardcontent.nativeElement.textContent).toContain('Enddatum:');
      expect(matcardcontent.nativeElement.textContent).toContain('31.12.2023');
    });

    test('should have targetvalue of Keyresult', () => {
      const matcardcontent = fixture.debugElement.query(By.css('[data-testId="mat-card-content-progress"]'));
      expect(matcardcontent.nativeElement.textContent).toContain('Zielwert:');
      expect(matcardcontent.nativeElement.textContent).toContain('100');
    });

    test('should have progress', () => {
      const matcardcontent = fixture.debugElement.query(By.css('[data-testId="mat-card-content-progress"]'));
      expect(matcardcontent.nativeElement.textContent).toContain('60 Prozent');
    });

    test('should have add measure button', () => {
      const button = fixture.debugElement.query(By.css('#add-measure-button'));

      expect(button.nativeElement.textContent).toEqual(' Messung hinzufügen ');
      button.nativeElement.click();
      expect(mockRouteService.navigate).toHaveBeenCalledWith('keyresults/1/measure/new');
    });

    test('should have show detail button', () => {
      const button = fixture.debugElement.query(By.css('#show-details-button'));

      expect(button.nativeElement.textContent).toEqual(' Details anzeigen ');
      button.nativeElement.click();
      expect(mockRouteService.navigate).toHaveBeenCalledWith('keyresults/1');
    });

    test('should edit keyresult button', () => {
      const button = fixture.debugElement.query(By.css('#edit-keyresult-button'));

      expect(button.nativeElement.textContent).toEqual(' Key Result bearbeiten ');
      button.nativeElement.click();
      expect(mockRouteService.navigate).toHaveBeenCalledWith('objective/1/keyresult/edit/1');
    });
  });

  describe('KeyResultDetail with no measures', () => {
    beforeEach(() => {
      mockQuarterService.getStartAndEndDateOfKeyresult.mockReturnValue(of(startAndEndDate));
      TestBed.configureTestingModule({
        imports: [
          MatCardModule,
          RouterTestingModule,
          BrowserDynamicTestingModule,
          TranslateTestingModule.withTranslations({
            de: require('../../../assets/i18n/de.json'),
          }),
        ],
        declarations: [KeyResultOverviewComponent],
        providers: [
          DatePipe,
          { provide: QuarterService, useValue: mockQuarterService },
          { provide: RouteService, useValue: mockRouteService },
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyResultOverviewComponent);
      component = fixture.componentInstance;

      let emptyMeasureKeyResult = keyResult;
      emptyMeasureKeyResult.measure = null!;
      component.keyResult = emptyMeasureKeyResult;

      fixture.detectChanges();
    });

    afterEach(() => {
      mockQuarterService.getStartAndEndDateOfKeyresult.mockReset();
      mockRouteService.navigate.mockReset();
    });

    test('should display - when measure is null', () => {
      const lastMeasureDate = fixture.debugElement.query(
        By.css('mat-card-content[data-test-marker="emptyLastMeasure"]')
      );
      expect(lastMeasureDate.nativeElement.textContent).toBeTruthy();
    });

    test('should have add measure button', () => {
      const button = fixture.debugElement.query(By.css('#add-measure-button'));

      expect(button.nativeElement.textContent).toEqual(' Messung hinzufügen ');
      button.nativeElement.click();
      expect(mockRouteService.navigate).toHaveBeenCalledWith('keyresults/1/measure/new');
    });
  });
});
