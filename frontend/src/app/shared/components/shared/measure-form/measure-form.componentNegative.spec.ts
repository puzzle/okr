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
import { Observable, of } from 'rxjs';
import { MeasureService } from '../../../services/measure.service';
import {
  ActivatedRoute,
  convertToParamMap,
  RouterLinkWithHref,
} from '@angular/router';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { DatePipe } from '@angular/common';
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
import { KeyResultDescriptionComponent } from '../key-result-description/key-result-description.component';
import { MeasureRowComponent } from '../measure-row/measure-row.component';
import { Goal, GoalService } from '../../../services/goal.service';
import * as goalsData from '../../../testing/mock-data/goals.json';
import { MatDialog } from '@angular/material/dialog';
import { MeasureValueValidator } from '../../../validators';
import { TranslateTestingModule } from 'ngx-translate-testing';

describe('MeasureFormComponent3', () => {
  let component: MeasureFormComponent;
  let fixture: ComponentFixture<MeasureFormComponent>;

  let goal: Observable<Goal> = of(goalsData.goals[0]);

  let keyResult: Observable<KeyResultMeasure> = of(keyresultData.keyresults[0]);

  let initMeasure = loadMeasure('initMeasure');

  const mockGetNumerOrNull = {
    getNumberOrNull: jest.fn(),
  };

  const mockGoalService = {
    getGoalByKeyResultId: jest.fn(),
  };

  const mockKeyResultService = {
    getKeyResultById: jest.fn(),
  };

  const mockMeasureService = {
    getMeasureById: jest.fn(),
    getInitMeasure: jest.fn(),
    saveMeasure: jest.fn(),
  };

  const mockToastrService = {
    success: jest.fn(),
    error: jest.fn(),
  };

  describe('Create throw error when Key Result id is null', () => {
    beforeEach(() => {
      mockGoalService.getGoalByKeyResultId.mockReturnValue(goal);
      mockKeyResultService.getKeyResultById.mockReturnValue(keyResult);
      mockMeasureService.getInitMeasure.mockReturnValue(initMeasure);
      mockGetNumerOrNull.getNumberOrNull.mockReturnValue(null);

      TestBed.configureTestingModule({
        declarations: [
          MeasureFormComponent,
          MeasureValueValidator,
          KeyResultDescriptionComponent,
          MeasureRowComponent,
        ],
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
          { provide: GoalService, useValue: mockGoalService },
          { provide: KeyResultService, useValue: mockKeyResultService },
          { provide: MeasureService, useValue: mockMeasureService },
          { provide: ToastrService, useValue: mockToastrService },
          { provide: MatDialog, useValue: {} },
          {
            provide: ActivatedRoute,
            useValue: {
              snapshot: {
                paramMap: convertToParamMap({}),
              },
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
      mockGoalService.getGoalByKeyResultId.mockReset();
      mockKeyResultService.getKeyResultById.mockReset();
      mockMeasureService.getInitMeasure.mockReset();
      mockGetNumerOrNull.getNumberOrNull.mockReset();
      mockMeasureService.saveMeasure.mockReset();
    });

    test('should create', () => {
      expect(component).toBeTruthy();

      expect(mockMeasureService.getMeasureById).toHaveBeenCalledTimes(0);
      expect(mockKeyResultService.getKeyResultById).toHaveBeenCalledTimes(0);
    });
  });
});
