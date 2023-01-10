import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MeasureFormComponent} from './measure-form.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {MatIconModule} from '@angular/material/icon';
import {FormControl, FormGroup, ReactiveFormsModule, Validators,} from '@angular/forms';
import {KeyResultMeasure, KeyResultService,} from '../../shared/services/key-result.service';
import * as keyresultData from '../../shared/testing/mock-data/keyresults.json';
import {Observable, of, throwError} from 'rxjs';
import {MeasureService} from '../../shared/services/measure.service';
import {ActivatedRoute, convertToParamMap} from '@angular/router';
import {By} from '@angular/platform-browser';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatNativeDateModule} from '@angular/material/core';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {loadMeasure} from '../../shared/testing/Loader';
import {ToastrModule, ToastrService} from 'ngx-toastr';
import {HttpErrorResponse} from '@angular/common/http';

describe('MeasureFormComponent', () => {
  let component: MeasureFormComponent;
  let fixture: ComponentFixture<MeasureFormComponent>;

  let keyResult: Observable<KeyResultMeasure> = of(keyresultData.keyresults[0]);

  let initMeasure = loadMeasure('initMeasure');
  let measure1 = of(loadMeasure('measure'));
  let receivedEditedMeasure = loadMeasure('receivedEditedMeasure');
  let receivedCreatedMeasure = loadMeasure('receivedCreatedMeasure');

  const mockGetNumerOrNull = {
    getNumberOrNull: jest.fn(),
  };

  const mockMeasureService = {
    getMeasureById: jest.fn(),
    getInitMeasure: jest.fn(),
    saveMeasure: jest.fn(),
  };
  const mockKeyResultService = {
    getKeyResultById: jest.fn(),
  };

  const mockToastrService = {
    success: jest.fn(),
    error: jest.fn(),
  };

  describe('Edit Measure', () => {
    beforeEach(() => {
      mockKeyResultService.getKeyResultById.mockReturnValue(keyResult);
      mockGetNumerOrNull.getNumberOrNull.mockReturnValue(1);
      mockMeasureService.getInitMeasure.mockReturnValue(initMeasure);
      mockMeasureService.getMeasureById.mockReturnValue(measure1);

      TestBed.configureTestingModule({
        declarations: [MeasureFormComponent],
        imports: [
          HttpClientTestingModule,
          BrowserAnimationsModule,
          RouterTestingModule,
          MatIconModule,
          ReactiveFormsModule,
          MatInputModule,
          MatButtonModule,
          MatDatepickerModule,
          MatNativeDateModule,
          ToastrModule.forRoot(),
        ],
        providers: [
          {provide: KeyResultService, useValue: mockKeyResultService},
          {provide: MeasureService, useValue: mockMeasureService},
          {provide: ToastrService, useValue: mockToastrService},
          {
            provide: ActivatedRoute,
            useValue: {
              paramMap: of(
                convertToParamMap({keyresultId: '1', measureId: '1'})
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
      mockMeasureService.getInitMeasure.mockReset();
      mockKeyResultService.getKeyResultById.mockReset();
      mockGetNumerOrNull.getNumberOrNull.mockReset();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should set keyresult', () => {
      component.keyresult$.subscribe((keyresult) => {
        expect(keyresult.title).toContain('Keyresult 1');
        expect(keyresult.id).toContain(1);
      });
      expect(mockMeasureService.getMeasureById).toHaveBeenCalledWith(1);
      expect(mockKeyResultService.getKeyResultById).toHaveBeenCalledWith(1);
    });

    it('should set create to false and set title right', () => {
      expect(component.create).toEqual(false);

      const title = fixture.debugElement.query(By.css('.heading-label'));
      expect(title.nativeElement.textContent).toContain('Messung bearbeiten');
    });

    it('should set measure and measureform', () => {
      component.measure$.subscribe((measure) => {
        expect(measure.id).toEqual(1);
        expect(measure.value).toEqual(42);
        expect(measure.measureDate).toEqual(
          new Date('2023-01-05T00:00:00.000Z')
        );

        expect(component.measureForm.get('value')?.value).toEqual(33);
        expect(component.measureForm.get('measureDate')?.value).toEqual(
          new Date('2023-01-04')
        );
        expect(component.measureForm.get('changeInfo')?.value).toEqual(
          'Changeinfo'
        );
        expect(component.measureForm.get('initiatives')?.value).toEqual(
          'Inititatives'
        );
      });
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

    it('should set measure value', () => {
      const value = fixture.debugElement.query(By.css('.value-input'));
      expect(value.nativeElement.value).toEqual('42');
    });

    it('should have keyresult unit', () => {
      const unit = fixture.debugElement.query(By.css('.unit-label'));
      expect(unit.nativeElement.textContent).toEqual('PERCENT');
    });

    it('should be invalid when not matching pattern of keyresult unit', () => {
      const unit = fixture.debugElement.query(By.css('.unit-label'));
      expect(unit.nativeElement.textContent).toEqual('PERCENT');
      component.measureForm.get('value')?.setValue(333);
      expect(component.measureForm.get('value')?.valid).toEqual(false);

      //Set Keyresult to BINARY Unit so that button has to be disabled in with value 333
      component.keyresult$ = of(keyresultData.keyresults[5]);
      fixture.detectChanges();

      /* Check if button is really disabled, then set value to 0 which fits BINARY regex and
       * check if button is not enabled */
      expect(component.measureForm.get('value')?.valid).toEqual(false);
      component.measureForm.get('value')?.setValue(0);
      expect(component.measureForm.get('value')?.valid).toEqual(true);
    });

    it('should have datepicker value', () => {
      const datepicker = fixture.debugElement.query(
        By.css('.datepicker-input')
      );
      expect(datepicker.nativeElement.value).toEqual('1/5/2023');
    });

    it('should have changeinfo', () => {
      const textareas = fixture.debugElement.queryAll(
        By.css('.description-textarea')
      );
      expect(textareas.length).toEqual(2);
      expect(textareas[0].nativeElement.value).toContain('Changeinfo');
    });

    it('should have initiatives', () => {
      const textareas = fixture.debugElement.queryAll(
        By.css('.description-textarea')
      );
      expect(textareas.length).toEqual(2);
      expect(textareas[1].nativeElement.value).toContain('Initiatives');
    });

    it('should have 3 buttons for edit', () => {
      const buttons = fixture.debugElement.queryAll(By.css('button'));
      expect(buttons.length).toEqual(3);
      expect(buttons[1].nativeElement.textContent).toContain('Abbrechen');
      expect(buttons[2].nativeElement.textContent).toContain('Aktualisieren');
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

    it('should save edited measure', () => {
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
  });

  describe('Create new Measure', () => {
    let createMeasureForm = new FormGroup({
      value: new FormControl<number>(33, [
        Validators.required,
        Validators.pattern('^[0-9]*$'),
      ]),
      measureDate: new FormControl<Date>(new Date('2022-12-01 00:00:00:'), [
        Validators.required,
      ]),
      changeInfo: new FormControl<string>('Changeinfo 1', [
        Validators.required,
      ]),
      initiatives: new FormControl<string>('Initiatives 1'),
    });

    beforeEach(() => {
      mockKeyResultService.getKeyResultById.mockReturnValue(keyResult);
      mockMeasureService.getInitMeasure.mockReturnValue(initMeasure);
      mockGetNumerOrNull.getNumberOrNull.mockReturnValue(1);

      TestBed.configureTestingModule({
        declarations: [MeasureFormComponent],
        imports: [
          HttpClientTestingModule,
          RouterTestingModule,
          MatIconModule,
          BrowserAnimationsModule,
          ReactiveFormsModule,
          MatInputModule,
          MatButtonModule,
          MatDatepickerModule,
          MatNativeDateModule,
          ToastrModule.forRoot(),
        ],
        providers: [
          {provide: KeyResultService, useValue: mockKeyResultService},
          {provide: MeasureService, useValue: mockMeasureService},
          {provide: ToastrService, useValue: mockToastrService},
          {
            provide: ActivatedRoute,
            useValue: {
              paramMap: of(convertToParamMap({keyresultId: '1'})),
            },
          },
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(MeasureFormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      mockMeasureService.getInitMeasure.mockReset();
      mockMeasureService.saveMeasure.mockReset();
      mockKeyResultService.getKeyResultById.mockReset();
      mockGetNumerOrNull.getNumberOrNull.mockReset();

      mockToastrService.success.mockReset();
      mockToastrService.error.mockReset();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should set create to true and set title right', () => {
      expect(component.create).toEqual(true);

      const title = fixture.debugElement.query(By.css('.heading-label'));
      expect(title.nativeElement.textContent).toContain('Messung hinzufügen');
    });

    it('should set all input fields empty except datepicker and have invalid form', () => {
      let button = fixture.debugElement.query(By.css('.create-button'));
      expect(button.nativeElement.disabled).toEqual(true);
      expect(component.measureForm.valid).toEqual(false);

      const valueInput = fixture.debugElement.query(By.css('.value-input'));
      expect(valueInput.nativeElement.value).toEqual('0');

      const datepicker = fixture.debugElement.query(
        By.css('.datepicker-input')
      );

      expect(datepicker.nativeElement.value).toEqual('12/23/2022');

      const textareas = fixture.debugElement.queryAll(
        By.css('.description-textarea')
      );
      expect(textareas.length).toEqual(2);
      expect(textareas[0].nativeElement.value).toContain('');
      expect(textareas[1].nativeElement.value).toContain('');
    });

    it('should have keyresult unit', () => {
      const unit = fixture.debugElement.query(By.css('.unit-label'));
      expect(unit.nativeElement.textContent).toEqual('PERCENT');
    });

    it('should have 3 buttons for create', () => {
      const buttons = fixture.debugElement.queryAll(By.css('button'));
      expect(buttons.length).toEqual(3);
      expect(buttons[1].nativeElement.textContent).toContain('Abbrechen');
      expect(buttons[2].nativeElement.textContent).toContain('Erstellen');
    });

    it('should save new measure', () => {
      component.measureForm = createMeasureForm;
      fixture.detectChanges();
      let button = fixture.debugElement.query(By.css('.create-button'));
      expect(button.nativeElement.disabled).toEqual(false);

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

    test('should trigger error notification', () => {
      mockMeasureService.saveMeasure.mockReturnValue(
        throwError(
          () =>
            new HttpErrorResponse({
              status: 500,
              error: {message: 'Something went wrong'},
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
        'Messung konnte nicht gespeichert werden!',
        'Fehlerstatus: 500',
        {timeOut: 5000}
      );
    });
  });

  describe('Create throw error when keyresult id is null', () => {
    beforeEach(() => {
      mockKeyResultService.getKeyResultById.mockReturnValue(keyResult);
      mockMeasureService.getInitMeasure.mockReturnValue(initMeasure);
      mockGetNumerOrNull.getNumberOrNull.mockReturnValue(null);

      TestBed.configureTestingModule({
        declarations: [MeasureFormComponent],
        imports: [
          HttpClientTestingModule,
          RouterTestingModule,
          MatIconModule,
          BrowserAnimationsModule,
          ReactiveFormsModule,
          MatInputModule,
          MatButtonModule,
          MatDatepickerModule,
          MatNativeDateModule,
        ],
        providers: [
          {provide: KeyResultService, useValue: mockKeyResultService},
          {provide: MeasureService, useValue: mockMeasureService},
          {provide: ToastrService, useValue: mockToastrService},
          {
            provide: ActivatedRoute,
            useValue: {
              paramMap: of(convertToParamMap({})),
            },
          },
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(MeasureFormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      mockMeasureService.getInitMeasure.mockReset();
      mockKeyResultService.getKeyResultById.mockReset();
      mockGetNumerOrNull.getNumberOrNull.mockReset();
      mockMeasureService.saveMeasure.mockReset();
    });

    it('should create', () => {
      expect(component).toBeTruthy();

      expect(mockMeasureService.getMeasureById).toHaveBeenCalledTimes(0);
      expect(mockKeyResultService.getKeyResultById).toHaveBeenCalledTimes(0);
    });
  });
});
