import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MeasureFormComponent } from './measure-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { MatIconModule } from '@angular/material/icon';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  KeyResultMeasure,
  KeyResultService,
} from '../../../services/key-result.service';
import * as keyresultData from '../../../testing/mock-data/keyresults.json';
import { Observable, of, throwError } from 'rxjs';
import { Measure, MeasureService } from '../../../services/measure.service';
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
import { HttpErrorResponse } from '@angular/common/http';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MeasureRowComponent } from '../measure-row/measure-row.component';
import { DatePipe } from '@angular/common';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { KeyResultDescriptionComponent } from '../key-result-description/key-result-description.component';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatDatepickerInputHarness } from '@angular/material/datepicker/testing';
import { Goal, GoalService } from '../../../services/goal.service';
import * as goalsData from '../../../testing/mock-data/goals.json';
import { MatDialog } from '@angular/material/dialog';
import { NUMBER_REGEX } from '../../../regexLibrary';
import { MeasureValueValidator } from '../../../validators';
import { TranslateTestingModule } from 'ngx-translate-testing';
import {
  QuarterService,
  StartEndDateDTO,
} from '../../../services/quarter.service';

describe('MeasureFormComponent Create', () => {
  let component: MeasureFormComponent;
  let fixture: ComponentFixture<MeasureFormComponent>;

  let goal: Observable<Goal> = of(goalsData.goals[0]);

  let keyResult: Observable<KeyResultMeasure> = of(keyresultData.keyresults[0]);

  let initMeasure = loadMeasure('initMeasure');
  let measure1 = of(loadMeasure('measure'));
  let measures: Observable<Measure[]> = of([loadMeasure('measure')]);
  let receivedCreatedMeasure = loadMeasure('receivedCreatedMeasure');

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
    getInitMeasure: jest.fn(),
    saveMeasure: jest.fn(),
  };

  const mockToastrService = {
    success: jest.fn(),
    error: jest.fn(),
  };

  let startAndEndDate: StartEndDateDTO = {
    startDate: new Date(Date.UTC(2022, 9, 1)),
    endDate: new Date(Date.UTC(2023, 11, 31)),
  };
  const mockQuarterService = {
    getStartAndEndDateOfKeyresult: jest.fn(),
  };

  describe('Create new Measure', () => {
    let createMeasureForm = new FormGroup({
      value: new FormControl<number | boolean>(33, [
        Validators.required,
        Validators.pattern(NUMBER_REGEX),
      ]),
      measureDate: new FormControl<Date>(new Date('2022-12-01T00:00:00Z'), [
        Validators.required,
      ]),
      changeInfo: new FormControl<string>('Changeinfo 1', [
        Validators.required,
      ]),
      initiatives: new FormControl<string>('Initiatives 1'),
    });

    beforeEach(() => {
      mockGoalService.getGoalByKeyResultId.mockReturnValue(goal);
      mockKeyResultService.getMeasuresOfKeyResult.mockReturnValue(measures);
      mockKeyResultService.getKeyResultById.mockReturnValue(keyResult);
      mockMeasureService.getInitMeasure.mockReturnValue(initMeasure);
      mockGetNumerOrNull.getNumberOrNull.mockReturnValue(1);
      mockQuarterService.getStartAndEndDateOfKeyresult.mockReturnValue(
        of(startAndEndDate)
      );

      TestBed.configureTestingModule({
        declarations: [
          MeasureFormComponent,
          MeasureValueValidator,
          KeyResultDescriptionComponent,
          MeasureRowComponent,
          MeasureValueValidator,
        ],
        imports: [
          TranslateTestingModule.withTranslations({
            de: require('../../../../../assets/i18n/de.json'),
          }),
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
          ToastrModule.forRoot(),
        ],
        providers: [
          DatePipe,
          { provide: GoalService, useValue: mockGoalService },
          { provide: KeyResultService, useValue: mockKeyResultService },
          { provide: MeasureService, useValue: mockMeasureService },
          { provide: ToastrService, useValue: mockToastrService },
          { provide: QuarterService, useValue: mockQuarterService },
          { provide: MatDialog, useValue: {} },
          {
            provide: ActivatedRoute,
            useValue: {
              snapshot: {
                paramMap: convertToParamMap({
                  keyresultId: '1',
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
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      mockGoalService.getGoalByKeyResultId.mockReset();
      mockMeasureService.getInitMeasure.mockReset();
      mockKeyResultService.getMeasuresOfKeyResult.mockReset();
      mockMeasureService.saveMeasure.mockReset();
      mockKeyResultService.getKeyResultById.mockReset();
      mockGetNumerOrNull.getNumberOrNull.mockReset();

      mockToastrService.success.mockReset();
      mockToastrService.error.mockReset();

      mockQuarterService.getStartAndEndDateOfKeyresult.mockReset();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should have one key result description tag', () => {
      const keyResultDescription = fixture.debugElement.queryAll(
        By.css('app-key-result-description')
      );
      expect(keyResultDescription.length).toEqual(1);
    });

    test('should set create to true and set title right two times', () => {
      expect(component.create).toEqual(true);

      const title1 = fixture.debugElement.query(By.css('.headline-large'));
      expect(title1.nativeElement.textContent).toContain('Messung hinzufügen');
      const headingLabels = fixture.debugElement.queryAll(
        By.css('.heading-label')
      );
      expect(headingLabels[1].nativeElement.textContent).toContain(
        'Messung hinzufügen'
      );
    });

    test('should have one key result description tag with right panel title', () => {
      const keyResultDescription = fixture.debugElement.queryAll(
        By.css('app-key-result-description')
      );
      expect(keyResultDescription.length).toEqual(1);

      const panelTitle = fixture.debugElement.query(By.css('.panel-title'));
      expect(panelTitle.nativeElement.textContent).toContain(
        'Key Result Beschreibung'
      );
    });

    test('should have two mat accordion for Key Result description and measure row', () => {
      const matAccordions = fixture.debugElement.queryAll(
        By.css('mat-accordion')
      );
      expect(matAccordions.length).toEqual(2);
    });

    test('should have three mat dividers', () => {
      const dividers = fixture.debugElement.queryAll(By.css('mat-divider'));
      expect(dividers.length).toEqual(3);
    });

    test('should have one measure row tag with right title', () => {
      const measureRow = fixture.debugElement.queryAll(
        By.css('app-measure-row')
      );
      expect(measureRow.length).toEqual(1);

      const title = fixture.debugElement.query(By.css('.heading-label'));
      expect(title.nativeElement.textContent).toContain('Vergangene Messungen');
    });

    test('should set all input fields empty except datepicker and have invalid form', async () => {
      let button = fixture.debugElement.query(By.css('.create-button'));
      expect(button.nativeElement.disabled).toEqual(true);
      expect(component.measureForm.valid).toEqual(false);

      const valueInput = fixture.debugElement.query(By.css('.value-input'));
      expect(valueInput.nativeElement.value).toEqual('0');
      fixture.detectChanges();

      const datePickerInputHarness =
        await TestbedHarnessEnvironment.documentRootLoader(fixture).getHarness(
          MatDatepickerInputHarness.with({
            selector: 'input[formControlName="measureDate"]',
          })
        );

      expect(await datePickerInputHarness.getValue()).toEqual('12/23/2022');
      expect(await datePickerInputHarness.getMin()).toEqual('2022-10-01');
      expect(await datePickerInputHarness.getMax()).toEqual('2023-12-31');

      const textareas = fixture.debugElement.queryAll(
        By.css('.description-textarea')
      );
      expect(textareas.length).toEqual(2);
      expect(textareas[0].nativeElement.value).toContain('');
      expect(textareas[1].nativeElement.value).toContain('');
    });

    test('should have Key Result unit', () => {
      const unit = fixture.debugElement.query(By.css('.unit-label'));
      expect(unit.nativeElement.textContent).toEqual('Prozent');
    });

    test('should update measureDate with datepicker', async () => {
      const datePickerInputHarness =
        await TestbedHarnessEnvironment.documentRootLoader(fixture).getHarness(
          MatDatepickerInputHarness.with({
            selector: 'input[formControlName="measureDate"]',
          })
        );

      datePickerInputHarness.setValue('22/12/2022');

      expect(component.measureForm.get('measureDate')?.value).toEqual(
        new Date('2022-12-23T00:00:00.000Z')
      );
    });

    test('should have 3 buttons for create', () => {
      const buttons = fixture.debugElement.queryAll(By.css('button'));
      expect(buttons.length).toEqual(3);
      expect(buttons[1].nativeElement.textContent).toContain('Abbrechen');
      expect(buttons[2].nativeElement.textContent).toContain('Erstellen');
    });

    xtest('should save new measure', () => {
      // Problem: Github Action server is not in the same timezone as we are. Because of that, he receives another date, but our implementation is right.
      component.measureForm = createMeasureForm;
      fixture.detectChanges();
      expect(component.measureForm.disabled).toEqual(false);

      component.save();

      expect(mockMeasureService.saveMeasure).toHaveBeenCalledTimes(1);
      expect(mockMeasureService.saveMeasure).toHaveBeenCalledWith(
        receivedCreatedMeasure,
        true
      );
    });

    xtest('should set measureDate time to midnight when save new measure', () => {
      component.measureForm = createMeasureForm;
      component.measureForm
        .get('measureDate')
        ?.setValue(new Date('2022-12-01T11:24:45Z'));
      fixture.detectChanges();

      expect(component.measureForm.disabled).toEqual(false);

      component.save();

      expect(mockMeasureService.saveMeasure).toHaveBeenCalledTimes(1);
      expect(mockMeasureService.saveMeasure).toHaveBeenCalledWith(
        receivedCreatedMeasure,
        true
      );
    });

    test('should trigger success notification', () => {
      mockMeasureService.saveMeasure.mockReturnValue(measure1);
      component.measureForm = createMeasureForm;
      fixture.detectChanges();

      const createbutton = fixture.debugElement.query(By.css('.create-button'));
      createbutton.nativeElement.click();
      fixture.detectChanges();
      expect(mockToastrService.success).toHaveBeenCalledTimes(1);
      expect(mockToastrService.error).not.toHaveBeenCalled();
      expect(mockToastrService.success).toHaveBeenCalledWith(
        '',
        'Messung gespeichert!',
        { timeOut: 5000 }
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
      component.measureForm = createMeasureForm;
      fixture.detectChanges();

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

    test('should trigger error notification', () => {
      mockMeasureService.saveMeasure.mockReturnValue(
        throwError(
          () =>
            new HttpErrorResponse({
              status: 500,
              error: { message: 'Something went wrong' },
            })
        )
      );
      component.measureForm = createMeasureForm;
      fixture.detectChanges();

      const createbutton = fixture.debugElement.query(By.css('.create-button'));
      createbutton.nativeElement.click();
      fixture.detectChanges();
      expect(mockToastrService.error).toHaveBeenCalledTimes(1);
      expect(mockToastrService.success).not.toHaveBeenCalled();
      expect(mockToastrService.error).toHaveBeenCalledWith(
        'Something went wrong',
        'Fehlerstatus: 500',
        { timeOut: 5000 }
      );
    });
  });
});
