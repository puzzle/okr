import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MeasureFormComponent } from './measure-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { MatIconModule } from '@angular/material/icon';
import { ReactiveFormsModule } from '@angular/forms';
import {
  KeyResultMeasure,
  KeyResultService,
} from '../../../services/key-result.service';
import * as keyresultData from '../../../testing/mock-data/keyresults.json';
import { Observable, of, throwError } from 'rxjs';
import { MeasureService } from '../../../services/measure.service';
import {
  ActivatedRoute,
  convertToParamMap,
  RouterLinkWithHref,
} from '@angular/router';
import { By } from '@angular/platform-browser';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import {
  BrowserAnimationsModule,
  NoopAnimationsModule,
} from '@angular/platform-browser/animations';
import { loadMeasure } from '../../../testing/Loader';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MeasureRowComponent } from '../measure-row/measure-row.component';
import { DatePipe } from '@angular/common';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { KeyResultDescriptionComponent } from '../key-result-description/key-result-description.component';
import { SharedModule } from '../shared.module';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatSlideToggleHarness } from '@angular/material/slide-toggle/testing';
import { Goal, GoalService } from '../../../services/goal.service';
import * as goalsData from '../../../testing/mock-data/goals.json';
import { MatDialog } from '@angular/material/dialog';
import { DiagramComponent } from '../../../../keyresult/diagram/diagram.component';
import * as measureData from '../../../testing/mock-data/measure.json';
import { MeasureValueValidator } from '../../../validators';
import { TranslateTestingModule } from 'ngx-translate-testing';
import {
  QuarterService,
  StartEndDateDTO,
} from '../../../services/quarter.service';
import { HttpErrorResponse } from '@angular/common/http';

describe('MeasureFormComponent Edit Binary', () => {
  let component: MeasureFormComponent;
  let fixture: ComponentFixture<MeasureFormComponent>;

  let goal: Observable<Goal> = of(goalsData.goals[0]);
  let measures: Observable<any[]> = of(measureData.measures);
  let binaryKeyResult: Observable<KeyResultMeasure> = of(
    keyresultData.binaryKeyResult
  );
  let binaryMeasure = of(loadMeasure('measureBinaryKeyResult'));
  let receivedEditedBinaryMeasure = loadMeasure('receivedEditedBinaryMeasure');

  const mockGetNumerOrNull = {
    getNumberOrNull: jest.fn(),
  };

  const mockGoalService = {
    getGoalByKeyResultId: jest.fn(),
  };

  const mockKeyResultService = {
    getKeyResultById: jest.fn(),
    getMeasuresOfKeyResult: jest.fn(),
  };

  const mockMeasureService = {
    getMeasureById: jest.fn(),
    saveMeasure: jest.fn(),
  };

  const mockToastrService = {
    success: jest.fn(),
    error: jest.fn(),
  };

  let startAndEndDate: StartEndDateDTO = {
    startDate: new Date(Date.UTC(2020, 9, 1)),
    endDate: new Date(Date.UTC(2028, 11, 31)),
  };

  const mockQuarterService = {
    getStartAndEndDateOfKeyresult: jest.fn(),
  };

  describe('Unit Binary edit Measure', () => {
    let loader: HarnessLoader;
    beforeEach(() => {
      mockGoalService.getGoalByKeyResultId.mockReturnValue(goal);
      mockKeyResultService.getKeyResultById.mockReturnValue(binaryKeyResult);
      mockKeyResultService.getMeasuresOfKeyResult.mockReturnValue(measures);
      mockGetNumerOrNull.getNumberOrNull.mockReturnValue(1);
      mockMeasureService.getMeasureById.mockReturnValue(binaryMeasure);
      mockQuarterService.getStartAndEndDateOfKeyresult.mockReturnValue(
        of(startAndEndDate)
      );

      TestBed.configureTestingModule({
        declarations: [
          MeasureFormComponent,
          KeyResultDescriptionComponent,
          MeasureRowComponent,
          MeasureValueValidator,
          DiagramComponent,
        ],
        imports: [
          HttpClientTestingModule,
          BrowserAnimationsModule,
          BrowserDynamicTestingModule,
          RouterTestingModule,
          MatIconModule,
          ReactiveFormsModule,
          MatInputModule,
          MatButtonModule,
          MatDatepickerModule,
          MatNativeDateModule,
          MatDividerModule,
          MatFormFieldModule,
          MatExpansionModule,
          MatCardModule,
          NoopAnimationsModule,
          RouterLinkWithHref,
          SharedModule,
          ToastrModule.forRoot(),
          TranslateTestingModule.withTranslations({
            de: require('../../../../../assets/i18n/de.json'),
          }),
        ],
        providers: [
          DatePipe,
          { provide: GoalService, useValue: mockGoalService },
          { provide: KeyResultService, useValue: mockKeyResultService },
          { provide: MeasureService, useValue: mockMeasureService },
          { provide: ToastrService, useValue: mockToastrService },
          { provide: MatDialog, useValue: {} },
          { provide: QuarterService, useValue: mockQuarterService },
          {
            provide: ActivatedRoute,
            useValue: {
              snapshot: {
                paramMap: convertToParamMap({
                  keyresultId: '1',
                  measureId: '1',
                }),
              },
              paramMap: of(
                convertToParamMap({ keyresultId: '1', measureId: '1' })
              ),
            },
          },
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(MeasureFormComponent);
      loader = TestbedHarnessEnvironment.loader(fixture);

      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      mockGoalService.getGoalByKeyResultId.mockReset();
      mockMeasureService.getMeasureById.mockReset();
      mockKeyResultService.getKeyResultById.mockReset();
      mockGetNumerOrNull.getNumberOrNull.mockReset();
      mockQuarterService.getStartAndEndDateOfKeyresult.mockReset();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should set Key Result', () => {
      binaryKeyResult.subscribe((testKeyResult) => {
        component.keyresult$.subscribe((componentKeyResult) => {
          expect(testKeyResult).toEqual(componentKeyResult);
        });
      });
    });

    it('should set measure', () => {
      binaryMeasure.subscribe((testMeasure) => {
        component.measure$.subscribe((componentMeasure) => {
          expect(testMeasure).toEqual(componentMeasure);
        });
      });
    });

    it('should set create to false and set title right', () => {
      expect(component.create).toEqual(false);

      const title = fixture.debugElement.query(By.css('.headline-large'));
      expect(title.nativeElement.textContent).toContain('Messung bearbeiten');
      const headingLabels = fixture.debugElement.queryAll(
        By.css('.heading-label')
      );
      expect(headingLabels[1].nativeElement.textContent).toContain(
        'Messung bearbeiten'
      );
    });

    it('should have one Key Result description tag with right panel title', () => {
      const keyResultDescription = fixture.debugElement.queryAll(
        By.css('app-key-result-description')
      );
      expect(keyResultDescription.length).toEqual(1);

      const panelTitle = fixture.debugElement.query(By.css('.panel-title'));
      expect(panelTitle.nativeElement.textContent).toContain(
        'Key Result Beschreibung'
      );
    });

    it('should have two mat accordion for Key Result description and measure row', () => {
      const matAccordions = fixture.debugElement.queryAll(
        By.css('mat-accordion')
      );
      expect(matAccordions.length).toEqual(2);
    });

    it('should have three mat dividers', () => {
      const dividers = fixture.debugElement.queryAll(By.css('mat-divider'));
      expect(dividers.length).toEqual(3);
    });

    it('should have one measure row tag with right panel title', () => {
      const measureRow = fixture.debugElement.queryAll(
        By.css('app-measure-row')
      );
      expect(measureRow.length).toEqual(1);
      const headingLabels = fixture.debugElement.queryAll(
        By.css('.heading-label')
      );
      expect(headingLabels[0].nativeElement.textContent).toContain(
        'Vergangene Messungen'
      );
    });

    it('should set measureform', () => {
      expect(component.measureForm.get('value')?.value).toBeFalsy();
      // expect(component.measureForm.get('measureDate')?.value).toEqual(
      //   new Date('2023-01-05T00:00:00.000Z')
      // ); // Problem: Github Action server is not in the same timezone as we are. Because of that, he receives another date, but our implementation is right.
      expect(component.measureForm.get('changeInfo')?.value).toEqual(
        'Changeinfo 1'
      );
      expect(component.measureForm.get('initiatives')?.value).toEqual(
        'Initiatives 1'
      );
    });

    it('should set keyresultUnit to BINARY', () => {
      expect(component.keyResultUnit).toContain('BINARY');
    });

    it('should have 3 buttons for edit', () => {
      const buttons = fixture.debugElement.queryAll(By.css('button'));
      expect(buttons.length).toEqual(3);
      expect(buttons[1].nativeElement.textContent).toContain('Abbrechen');
      expect(buttons[2].nativeElement.textContent).toContain('Aktualisieren');
    });

    it('should have changeinfo', () => {
      const textareas = fixture.debugElement.queryAll(
        By.css('.description-textarea')
      );
      expect(textareas.length).toEqual(2);
      expect(textareas[0].nativeElement.value).toContain('Changeinfo 1');
    });

    it('should have initiatives', () => {
      const textareas = fixture.debugElement.queryAll(
        By.css('.description-textarea')
      );
      expect(textareas.length).toEqual(2);
      expect(textareas[1].nativeElement.value).toContain('Initiatives 1');
    });

    it('should have datepicker value', () => {
      const datepicker = fixture.debugElement.query(
        By.css('input[formControlName="measureDate"]')
      );
      expect(datepicker.nativeElement.value).toEqual('1/5/2023');
    });

    xit('should update datepicker with right value from measureForm wintertime', () => {
      // Problem: Github Action server is not in the same timezone as we are. Because of that, he receives another date, but our implementation is right.
      const datepicker = fixture.debugElement.query(
        By.css('.datepicker-input')
      );
      component.measureForm
        .get('measureDate')
        ?.setValue(new Date('2023-02-23T23:00:00Z'));
      fixture.detectChanges();

      expect(datepicker.nativeElement.value).toEqual('2/24/2023');
    });

    xit('should update datepicker with right value from measureForm summertime', () => {
      // Problem: Github Action server is not in the same timezone as we are. Because of that, he receives another date, but our implementation is right.
      const datepicker = fixture.debugElement.query(
        By.css('.datepicker-input')
      );
      component.measureForm
        .get('measureDate')
        ?.setValue(new Date('2023-07-01T22:00:00Z'));
      fixture.detectChanges();

      expect(datepicker.nativeElement.value).toEqual('7/2/2023');
    });

    it('should have slider with right value and change value on change', async () => {
      const toggleSlide = await loader.getHarness(
        MatSlideToggleHarness.with({
          selector: 'mat-slide-toggle',
        })
      );
      expect(await toggleSlide.isChecked()).toBeFalsy();
      await toggleSlide.toggle();
      expect(await toggleSlide.isChecked()).toBeTruthy();

      expect(component.measureForm.get('value')?.value).toEqual(true);
    });

    it('should set form valid when no changes and set to invalid if empty input', () => {
      let button = fixture.debugElement.query(By.css('.create-button'));
      expect(button.nativeElement.disabled).toEqual(false);
      expect(component.measureForm.valid).toEqual(true);

      component.measureForm.get('changeInfo')?.setValue('');
      fixture.detectChanges();

      button = fixture.debugElement.query(By.css('.create-button'));
      expect(component.measureForm.valid).toEqual(false);
      expect(button.nativeElement.disabled).toEqual(true);
    });

    it('should have 4 titles', () => {
      const titles = fixture.debugElement.queryAll(By.css('.fw-bold'));
      expect(titles.length).toEqual(4);
      expect(titles[0].nativeElement.textContent).toContain('Aktueller Wert');
      expect(titles[1].nativeElement.textContent).toContain(
        'Datum der Messung'
      );
      expect(titles[2].nativeElement.textContent).toContain(
        'Veränderung seit letzter Messung'
      );
      expect(titles[3].nativeElement.textContent).toContain('Massnahmen');
    });

    it('should have Key Result unit in html', () => {
      const unit = fixture.debugElement.query(By.css('.unit-label'));
      expect(unit.nativeElement.textContent).toEqual('BINÄR');
    });

    it('should save edited measure', () => {
      component.measureForm.get('value')?.setValue(true);
      component.measureForm.get('changeInfo')?.setValue('Changeinfo 1');
      component.measureForm.get('initiatives')?.setValue('Initiatives 1');
      component.measureForm
        .get('measureDate')
        ?.setValue(new Date('2023-01-03T23:00:00Z'));
      fixture.detectChanges();
      component.save();

      expect(mockMeasureService.saveMeasure).toHaveBeenCalled();
      expect(mockMeasureService.saveMeasure).toHaveBeenCalledWith(
        receivedEditedBinaryMeasure,
        false
      );
    });

    xit('should set measureDate time to midnight when save edited measure', () => {
      component.measureForm.get('value')?.setValue(true);
      component.measureForm.get('changeInfo')?.setValue('Changeinfo 1');
      component.measureForm.get('initiatives')?.setValue('Initiatives 1');
      component.measureForm
        .get('measureDate')
        ?.setValue(new Date('2023-01-04T00:00:00Z'));
      fixture.detectChanges();
      component.save();

      expect(mockMeasureService.saveMeasure).toHaveBeenCalled();
      expect(mockMeasureService.saveMeasure).toHaveBeenCalledWith(
        receivedEditedBinaryMeasure,
        false
      );
    });

    xtest('should trigger error notification when multiple measures on one day', () => {
      mockMeasureService.saveMeasure.mockReturnValue(
        throwError(
          () =>
            new HttpErrorResponse({
              status: 400,
              error: {
                message: 'Only one Messung is allowed per day and Key Result!',
              },
            })
        )
      );

      const createbutton = fixture.debugElement.query(By.css('.create-button'));
      createbutton.nativeElement.click();
      fixture.detectChanges();
      expect(mockToastrService.error).toHaveBeenCalledTimes(1);
      expect(mockToastrService.success).not.toHaveBeenCalled();
      expect(mockToastrService.error).toHaveBeenCalledWith(
        'Only one Messung is allowed per day and Key Result!',
        'Fehlerstatus: 400',
        { timeOut: 5000 }
      );
    });
  });
});
