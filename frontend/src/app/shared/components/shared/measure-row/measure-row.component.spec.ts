import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MeasureRowComponent } from './measure-row.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { DatePipe } from '@angular/common';
import * as measureData from '../../../testing/mock-data/measure.json';
import {
  KeyResultService,
  Measure,
} from '../../../services/key-result.service';
import { Observable, of } from 'rxjs';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { By } from '@angular/platform-browser';
import {
  BrowserAnimationsModule,
  NoopAnimationsModule,
} from '@angular/platform-browser/animations';
import { MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';

describe('MeasureRowComponent', () => {
  let component: MeasureRowComponent;
  let fixture: ComponentFixture<MeasureRowComponent>;

  let measures: Observable<Measure[]> = of(measureData.measures);

  const mockKeyResultService = {
    getMeasuresOfKeyResult: jest.fn(),
  };

  const mockToastrService = {
    success: jest.fn(),
    error: jest.fn(),
  };

  describe('Get Measures from existing keyresult', () => {
    beforeEach(() => {
      mockKeyResultService.getMeasuresOfKeyResult.mockReturnValue(measures);

      TestBed.configureTestingModule({
        imports: [
          HttpClientTestingModule,
          BrowserDynamicTestingModule,
          BrowserAnimationsModule,
          NoopAnimationsModule,
          RouterTestingModule,
          MatCardModule,
          MatExpansionModule,
        ],
        providers: [
          DatePipe,
          { provide: KeyResultService, useValue: mockKeyResultService },
          { provide: ToastrService, useValue: mockToastrService },
          { provide: MatDialog, useValue: {} },
          {
            provide: ActivatedRoute,
            useValue: {
              paramMap: of(convertToParamMap({ keyresultId: '1' })),
            },
          },
        ],
        declarations: [MeasureRowComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(MeasureRowComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      mockKeyResultService.getMeasuresOfKeyResult.mockReset();
      mockToastrService.success.mockReset();
      mockToastrService.error.mockReset();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should set measures from keyresultService', () => {
      expect(mockKeyResultService.getMeasuresOfKeyResult).toHaveBeenCalledWith(
        1
      );

      component.measures$.subscribe((componentMeasures) => {
        measures.subscribe((testMeasures) => {
          expect(componentMeasures).toEqual(testMeasures);
        });
      });
    });

    it('should set right heading title', () => {
      const headingLabel = fixture.debugElement.query(By.css('.heading-label'));

      expect(headingLabel.nativeElement.textContent).toContain(
        'Vergangene Messungen'
      );
    });

    it('should have right titles for past measures', () => {
      const spans = fixture.debugElement.queryAll(By.css('span'));

      expect(spans.length).toEqual(6);
      expect(spans[0].nativeElement.textContent).toContain(
        'Vergangene Messung'
      );
      expect(spans[2].nativeElement.textContent).toContain('Datum');
      expect(spans[3].nativeElement.textContent).toContain('Aktueller Wert');
      expect(spans[4].nativeElement.textContent).toContain('Veränderungen');
      expect(spans[5].nativeElement.textContent).toContain('Massnahmen');
    });

    it('should create two mat cards for 2 measures', () => {
      const matCards = fixture.debugElement.queryAll(By.css('mat-card'));

      expect(matCards.length).toEqual(2);
    });

    it('should have two measure dates for 2 measures', () => {
      const measureDates = fixture.debugElement.queryAll(
        By.css('.measureDate')
      );

      expect(measureDates.length).toEqual(2);
      expect(measureDates[0].nativeElement.textContent).toContain('05.01.2023');
      expect(measureDates[1].nativeElement.textContent).toContain('23.01.2023');
    });

    it('should have two measure values for 2 measures', () => {
      const measureValues = fixture.debugElement.queryAll(
        By.css('.measureValue')
      );

      expect(measureValues.length).toEqual(2);
      expect(measureValues[0].nativeElement.textContent).toContain('42');
      expect(measureValues[1].nativeElement.textContent).toContain('50');
    });

    it('should have two measure changeInfos for 2 measures', () => {
      const measureChangeInfos = fixture.debugElement.queryAll(
        By.css('.measureChangeinfo')
      );

      expect(measureChangeInfos.length).toEqual(2);
      expect(measureChangeInfos[0].nativeElement.textContent).toContain(
        'Changeinfo 1'
      );
      expect(measureChangeInfos[1].nativeElement.textContent).toContain(
        'Changeinfo 2'
      );
    });

    it('should have two measure initiatives for 2 measures', () => {
      const measureInitiatives = fixture.debugElement.queryAll(
        By.css('.measureInitiatives')
      );

      expect(measureInitiatives.length).toEqual(2);
      expect(measureInitiatives[0].nativeElement.textContent).toContain(
        'Initiatives 1'
      );
      expect(measureInitiatives[1].nativeElement.textContent).toContain(
        'Initiatives 2'
      );
    });

    it('should have two edit icons for 2 measures', () => {
      const editIcons = fixture.debugElement.queryAll(By.css('.edit-icon'));

      expect(editIcons.length).toEqual(2);
    });

    it('should format date in right format dd.MM.yyyy hh:mm:ss', () => {
      let formattedDate = component.formatDate('2022-09-01T00:00:00');
      expect(formattedDate).toEqual('01.09.2022');
      formattedDate = component.formatDate('2023-02-20T10:00:00');
      expect(formattedDate).toEqual('20.02.2023');
    });
  });

  describe('Empty keyresult id', () => {
    beforeEach(() => {
      mockKeyResultService.getMeasuresOfKeyResult.mockReturnValue(measures);

      TestBed.configureTestingModule({
        imports: [
          HttpClientTestingModule,
          BrowserDynamicTestingModule,
          BrowserAnimationsModule,
          NoopAnimationsModule,
          RouterTestingModule,
          MatCardModule,
          MatExpansionModule,
        ],
        providers: [
          DatePipe,
          { provide: KeyResultService, useValue: mockKeyResultService },
          { provide: MatDialog, useValue: {} },
          { provide: ToastrService, useValue: mockToastrService },
          {
            provide: ActivatedRoute,
            useValue: {
              paramMap: of(convertToParamMap({})),
            },
          },
        ],
        declarations: [MeasureRowComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(MeasureRowComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      mockKeyResultService.getMeasuresOfKeyResult.mockReset();
      mockToastrService.success.mockReset();
      mockToastrService.error.mockReset();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should not get measures from keyresult', () => {
      expect(
        mockKeyResultService.getMeasuresOfKeyResult
      ).not.toHaveBeenCalled();
      component.measures$.subscribe((measures) => {
        expect(measures).toBeNull();
      });
    });
  });
});
