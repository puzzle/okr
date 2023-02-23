import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultOverviewComponent } from './key-result-overview.component';
import { KeyResultMeasure } from '../../shared/services/key-result.service';
import * as keyresultData from '../../shared/testing/mock-data/keyresults.json';
import { MatCardModule } from '@angular/material/card';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import {
  QuarterService,
  StartEndDateDTO,
} from '../../shared/services/quarter.service';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { DatePipe } from '@angular/common';
import { RouteService } from '../../shared/services/route.service';

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
      mockQuarterService.getStartAndEndDateOfKeyresult.mockReturnValue(
        of(startAndEndDate)
      );
      TestBed.configureTestingModule({
        imports: [
          MatCardModule,
          RouterTestingModule,
          BrowserDynamicTestingModule,
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

    test('should have 4 strong titles', () => {
      const strongs = fixture.debugElement.queryAll(By.css('strong'));

      expect(strongs.length).toEqual(4);
    });

    test('should have right mat-card-titles', () => {
      const matcardtitles = fixture.debugElement.queryAll(
        By.css('mat-card-title')
      );

      expect(matcardtitles.length).toEqual(2);

      expect(matcardtitles[0].nativeElement.textContent).toEqual(
        'Beschreibung'
      );
      expect(matcardtitles[1].nativeElement.textContent).toEqual(
        'Letzte Messung'
      );
    });

    test('should have right mat-card-content', () => {
      const matcardcontens = fixture.debugElement.queryAll(
        By.css('mat-card-content')
      );
      expect(matcardcontens[1].nativeElement.textContent).toEqual(
        ' This is the description '
      );
    });

    test('should have title and last measure date', () => {
      const lastMeasureDateTitle = fixture.debugElement.query(
        By.css('.lastMeasureDateTitle')
      );
      expect(lastMeasureDateTitle.nativeElement.textContent).toContain(
        'Datum der Messung:'
      );
      const lastMeasureDate = fixture.debugElement.query(
        By.css('.lastMeasureDate')
      );
      expect(lastMeasureDate.nativeElement.textContent).toContain('23.12.2022');
    });

    test('should have title and change info', () => {
      const lastMeasureChangeInfoTitle = fixture.debugElement.query(
        By.css('.lastMeasureChangeInfoTitle')
      );
      expect(lastMeasureChangeInfoTitle.nativeElement.textContent).toContain(
        'Änderungen:'
      );
      const lastMeasureChangeInfo = fixture.debugElement.query(
        By.css('.lastMeasureChangeInfo')
      );
      expect(lastMeasureChangeInfo.nativeElement.textContent).toContain(
        ' Lorem Ipsum is simply dummy text of the printing and typesetting industry. '
      );
    });

    test('should have title and start date of last measure', () => {
      const lastMeasureStartDateTitle = fixture.debugElement.query(
        By.css('.lastMeasureStartDateTitle')
      );
      expect(lastMeasureStartDateTitle.nativeElement.textContent).toContain(
        'Startdatum:'
      );
      const lastMeasureStartDate = fixture.debugElement.query(
        By.css('.lastMeasureStartDate')
      );
      expect(lastMeasureStartDate.nativeElement.textContent).toContain(
        '01.10.2022'
      );
    });

    test('should have title and end date of last measure', () => {
      const lastMeasureStartDateTitle = fixture.debugElement.query(
        By.css('.lastMeasureEndDateTitle')
      );
      expect(lastMeasureStartDateTitle.nativeElement.textContent).toContain(
        'Enddatum:'
      );
      const lastMeasureStartDate = fixture.debugElement.query(
        By.css('.lastMeasureEndDate')
      );
      expect(lastMeasureStartDate.nativeElement.textContent).toContain(
        '31.12.2023'
      );
    });

    test('should have add measure button', () => {
      const button = fixture.debugElement.query(By.css('#add-measure-button'));

      expect(button.nativeElement.textContent).toEqual(' Messung hinzufügen ');
      button.nativeElement.click();
      expect(mockRouteService.navigate).toHaveBeenCalledWith(
        'keyresults/1/measure/new'
      );
    });

    test('should have show detail button', () => {
      const button = fixture.debugElement.query(By.css('#show-details-button'));

      expect(button.nativeElement.textContent).toEqual(' Details anzeigen ');
      button.nativeElement.click();
      expect(mockRouteService.navigate).toHaveBeenCalledWith('keyresults/1');
    });

    test('should edit keyresult button', () => {
      const button = fixture.debugElement.query(
        By.css('#edit-keyresult-button')
      );

      expect(button.nativeElement.textContent).toEqual(
        ' Key Result bearbeiten '
      );
      button.nativeElement.click();
      expect(mockRouteService.navigate).toHaveBeenCalledWith(
        'objective/1/keyresult/edit/1'
      );
    });
  });

  describe('KeyResultDetail with no measures', () => {
    beforeEach(() => {
      mockQuarterService.getStartAndEndDateOfKeyresult.mockReturnValue(
        of(startAndEndDate)
      );
      TestBed.configureTestingModule({
        imports: [
          MatCardModule,
          RouterTestingModule,
          BrowserDynamicTestingModule,
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
      expect(lastMeasureDate.nativeElement.textContent).toContain('-');
    });

    test('should have add measure button', () => {
      const button = fixture.debugElement.query(By.css('#add-measure-button'));

      expect(button.nativeElement.textContent).toEqual(' Messung hinzufügen ');
      button.nativeElement.click();
      expect(mockRouteService.navigate).toHaveBeenCalledWith(
        'keyresults/1/measure/new'
      );
    });
  });
});
