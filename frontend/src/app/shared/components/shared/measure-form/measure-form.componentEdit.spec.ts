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
import * as measureData from '../../../testing/mock-data/measure.json';
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
import { MatDialog } from '@angular/material/dialog';
import { Goal, GoalService } from '../../../services/goal.service';
import * as goalsData from '../../../testing/mock-data/goals.json';
import {
  QuarterService,
  StartEndDateDTO,
} from '../../../services/quarter.service';
import { UnitValueValidator } from '../../../validators';
import { TranslateTestingModule } from 'ngx-translate-testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatDatepickerInputHarness } from '@angular/material/datepicker/testing';
import { HttpErrorResponse } from '@angular/common/http';

describe('MeasureFormComponent Edit', () => {
  let component: MeasureFormComponent;
  let fixture: ComponentFixture<MeasureFormComponent>;

  let keyResult: Observable<KeyResultMeasure> = of(keyresultData.keyresults[0]);

  let measure1 = of(loadMeasure('measure'));
  let receivedEditedMeasure = loadMeasure('receivedEditedMeasure');
  let goal: Observable<Goal> = of(goalsData.goals[0]);
  let measures: Observable<any[]> = of(measureData.measures);

  let startAndEndDate: StartEndDateDTO = {
    startDate: new Date(Date.UTC(2020, 9, 1)),
    endDate: new Date(Date.UTC(2028, 11, 31)),
  };

  const mockGetNumerOrNull = {
    getNumberOrNull: jest.fn(),
  };

  const mockMeasureService = {
    getMeasureById: jest.fn(),
    saveMeasure: jest.fn(),
  };

  const mockGoalService = {
    getGoalByKeyResultId: jest.fn(),
  };

  const mockKeyResultService = {
    getKeyResultById: jest.fn(),
    getMeasuresOfKeyResult: jest.fn(),
  };

  const mockToastrService = {
    success: jest.fn(),
    error: jest.fn(),
  };

  const mockQuarterService = {
    getStartAndEndDateOfKeyresult: jest.fn(),
  };

  describe('Edit Measure', () => {
    beforeEach(() => {
      mockKeyResultService.getKeyResultById.mockReturnValue(keyResult);
      mockGetNumerOrNull.getNumberOrNull.mockReturnValue(1);
      mockMeasureService.getMeasureById.mockReturnValue(measure1);
      mockGoalService.getGoalByKeyResultId.mockReturnValue(goal);
      mockKeyResultService.getMeasuresOfKeyResult.mockReturnValue(measures);
      mockQuarterService.getStartAndEndDateOfKeyresult.mockReturnValue(
        of(startAndEndDate)
      );

      TestBed.configureTestingModule({
        declarations: [
          MeasureFormComponent,
          KeyResultDescriptionComponent,
          MeasureRowComponent,
          UnitValueValidator,
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
          ToastrModule.forRoot(),
          TranslateTestingModule.withTranslations({
            de: require('../../../../../assets/i18n/de.json'),
          }),
        ],
        providers: [
          DatePipe,
          { provide: KeyResultService, useValue: mockKeyResultService },
          { provide: MeasureService, useValue: mockMeasureService },
          { provide: ToastrService, useValue: mockToastrService },
          { provide: GoalService, useValue: mockGoalService },
          { provide: QuarterService, useValue: mockQuarterService },
          { provide: MatDialog, useValue: {} },
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
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      mockMeasureService.getMeasureById.mockReset();
      mockKeyResultService.getKeyResultById.mockReset();
      mockGetNumerOrNull.getNumberOrNull.mockReset();
      mockGoalService.getGoalByKeyResultId.mockReset();
      mockKeyResultService.getMeasuresOfKeyResult.mockReset();
      mockQuarterService.getStartAndEndDateOfKeyresult.mockReset();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should set goal of component', () => {
      component.goal$.subscribe((componentGoal) => {
        goal.subscribe((testGoal) => {
          expect(componentGoal).toEqual(testGoal);
        });
      });
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

    test('should have one mat accordion for measure row', () => {
      const matAccordions = fixture.debugElement.queryAll(
        By.css('mat-accordion')
      );
      expect(matAccordions.length).toEqual(1);
    });

    test('should have three mat dividers', () => {
      const dividers = fixture.debugElement.queryAll(By.css('mat-divider'));
      expect(dividers.length).toEqual(3);
    });

    test('should have one measure row tag with right panel title', () => {
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

    test('should set Key Result', () => {
      component.keyresult$.subscribe((keyresult) => {
        expect(keyresult.title).toContain('Key Result 1');
        expect(keyresult.id).toEqual(1);
      });
      expect(mockMeasureService.getMeasureById).toHaveBeenCalledWith(1);
      expect(mockKeyResultService.getKeyResultById).toHaveBeenCalledWith(1);
    });

    test('should set Key Result unit right', () => {
      expect(component.keyResultUnit).toContain('PERCENT');
    });

    test('should set create to false and set title right', () => {
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

    test('should set measure and measureform', () => {
      component.measure$.subscribe((measure) => {
        measure1.subscribe((testMeasure) => {
          expect(measure).toEqual(testMeasure);
        });
      });
      expect(component.measureForm.get('value')?.value).toEqual(42);
      expect(component.measureForm.get('measureDate')?.value).toEqual(
        new Date('2023-01-10T22:00:00.000Z')
      );
      expect(component.measureForm.get('changeInfo')?.value).toEqual(
        'Changeinfo 1'
      );
      expect(component.measureForm.get('initiatives')?.value).toEqual(
        'Initiatives 1'
      );
    });

    test('should have 4 titles', () => {
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

    test('should set measure value', () => {
      const value = fixture.debugElement.query(
        By.css('input[formControlName="value"]')
      );
      expect(value.nativeElement.value).toEqual('42');
    });

    test('should have Key Result unit', () => {
      const unit = fixture.debugElement.query(By.css('.unit-label'));
      expect(unit.nativeElement.textContent).toEqual('Prozent');
    });

    test('should be invalid when not matching pattern of Key Result unit', () => {
      const unit = fixture.debugElement.query(By.css('.unit-label'));
      expect(unit.nativeElement.textContent).toEqual('Prozent');
      component.measureForm.get('value')?.setValue(333);
      expect(component.measureForm.get('value')?.valid).toEqual(false);

      component.keyresult$ = of(keyresultData.keyresults[5]);
      fixture.detectChanges();

      expect(component.measureForm.get('value')?.valid).toEqual(false);
      component.measureForm.get('value')?.setValue(0);
      expect(component.measureForm.get('value')?.valid).toEqual(true);
    });

    test('should be invalid when value has more than one number behind comma', () => {
      component.measureForm.get('value')?.setValue(33.456);
      expect(component.measureForm.get('value')?.valid).toEqual(false);
    });

    test('should be invalid when value is greater than 100 and has numbers behind comma', () => {
      component.measureForm.get('value')?.setValue(133.456);
      expect(component.measureForm.get('value')?.valid).toEqual(false);
    });

    test('should have datepicker value', async () => {
      const datePickerInputHarness =
        await TestbedHarnessEnvironment.documentRootLoader(fixture).getHarness(
          MatDatepickerInputHarness.with({
            selector: 'input[formControlName="measureDate"]',
          })
        );

      expect(await datePickerInputHarness.getValue()).toEqual('1/10/2023');
    });

    xtest('should update datepicker with right value from measureForm wintertime', async () => {
      // Problem: Github Action server is not in the same timezone as we are. Because of that, he receives another date, but our implementation is right.
      const datePickerInputHarness =
        await TestbedHarnessEnvironment.documentRootLoader(fixture).getHarness(
          MatDatepickerInputHarness.with({
            selector: 'input[formControlName="measureDate"]',
          })
        );

      component.measureForm
        .get('measureDate')
        ?.setValue(new Date('2023-02-23T23:00:00Z'));
      fixture.detectChanges();

      expect(await datePickerInputHarness.getValue()).toEqual('2/24/2023');
    });

    xtest('should update datepicker with right value from measureForm summertime', async () => {
      // Problem: Github Action server is not in the same timezone as we are. Because of that, he receives another date, but our implementation is right.
      const datePickerInputHarness =
        await TestbedHarnessEnvironment.documentRootLoader(fixture).getHarness(
          MatDatepickerInputHarness.with({
            selector: 'input[formControlName="measureDate"]',
          })
        );

      component.measureForm
        .get('measureDate')
        ?.setValue(new Date('2023-07-01T22:00:00Z'));
      fixture.detectChanges();

      expect(await datePickerInputHarness.getValue()).toEqual('7/2/2023');
    });

    xtest('should update measureDate with datepicker', async () => {
      // Problem: Github Action server is not in the same timezone as we are. Because of that, he receives another date, but our implementation is right.
      const datePickerInputHarness =
        await TestbedHarnessEnvironment.documentRootLoader(fixture).getHarness(
          MatDatepickerInputHarness.with({
            selector: 'input[formControlName="measureDate"]',
          })
        );

      await datePickerInputHarness.setValue('22/12/2022');

      expect(component.measureForm.get('measureDate')?.value).toEqual(
        new Date('2023-01-10T22:00:00.000Z')
      );
    });

    test('should have changeinfo', () => {
      const textareas = fixture.debugElement.queryAll(
        By.css('.description-textarea')
      );
      expect(textareas.length).toEqual(2);
      expect(textareas[0].nativeElement.value).toContain('Changeinfo');
    });

    test('should have initiatives', () => {
      const textareas = fixture.debugElement.queryAll(
        By.css('.description-textarea')
      );
      expect(textareas.length).toEqual(2);
      expect(textareas[1].nativeElement.value).toContain('Initiatives');
    });

    test('should have 3 buttons for edit', () => {
      const buttons = fixture.debugElement.queryAll(By.css('button'));
      expect(buttons.length).toEqual(3);
      expect(buttons[1].nativeElement.textContent).toContain('Abbrechen');
      expect(buttons[2].nativeElement.textContent).toContain('Aktualisieren');
    });

    test('should set form valid when no changes and set to invalid if empty input', () => {
      let button = fixture.debugElement.query(By.css('.create-button'));
      expect(button.nativeElement.disabled).toEqual(false);
      expect(component.measureForm.valid).toEqual(true);

      component.measureForm.get('changeInfo')?.setValue('');
      fixture.detectChanges();

      button = fixture.debugElement.query(By.css('.create-button'));
      expect(component.measureForm.valid).toEqual(false);
      expect(button.nativeElement.disabled).toEqual(true);
    });

    xtest('should save edited measure', () => {
      // Problem: Github Action server is not in the same timezone as we are. Because of that, he receives another date, but our implementation is right.
      component.measureForm.get('changeInfo')?.setValue('New Changeinfo');
      component.measureForm.get('value')?.setValue(30);
      fixture.detectChanges();
      component.save();

      expect(mockMeasureService.saveMeasure).toHaveBeenCalledTimes(1);
      expect(mockMeasureService.saveMeasure).toHaveBeenCalledWith(
        receivedEditedMeasure,
        false
      );
    });

    xtest('should set measureDate time to midnight when save edited measure', () => {
      component.measureForm.get('changeInfo')?.setValue('New Changeinfo');
      component.measureForm.get('value')?.setValue(30);
      component.measureForm
        .get('measureDate')
        ?.setValue(new Date('2023-01-10T14:30:48Z'));
      fixture.detectChanges();
      component.save();

      expect(mockMeasureService.saveMeasure).toHaveBeenCalled();
      expect(mockMeasureService.saveMeasure).toHaveBeenCalledWith(
        receivedEditedMeasure,
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
