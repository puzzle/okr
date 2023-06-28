import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultDetailComponent } from './key-result-detail.component';
import { By } from '@angular/platform-browser';
import { MatIconModule } from '@angular/material/icon';
import { KeyResultDescriptionComponent } from '../key-result-description/key-result-description.component';
import { MatDividerModule } from '@angular/material/divider';
import { MeasureRowComponent } from '../measure-row/measure-row.component';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatCardModule } from '@angular/material/card';
import { ActivatedRoute, convertToParamMap, RouterLinkWithHref } from '@angular/router';
import { DatePipe } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { Observable, of } from 'rxjs';
import { Goal, GoalService } from '../../../services/goal.service';
import * as goalsData from '../../../testing/mock-data/goals.json';
import { TranslateTestingModule } from 'ngx-translate-testing';

describe('KeyResultDetailComponent', () => {
  let component: KeyResultDetailComponent;
  let fixture: ComponentFixture<KeyResultDetailComponent>;

  let goal: Observable<Goal> = of(goalsData.goals[0]);

  const mockGoalService = {
    getGoalByKeyResultId: jest.fn(),
  };

  const mockGetNumerOrNull = {
    getNumberOrNull: jest.fn(),
  };

  const mockToastrService = {
    success: jest.fn(),
    error: jest.fn(),
  };

  beforeEach(() => {
    mockGoalService.getGoalByKeyResultId.mockReturnValue(goal);
    mockGetNumerOrNull.getNumberOrNull.mockReturnValue(1);

    TestBed.configureTestingModule({
      declarations: [KeyResultDetailComponent, KeyResultDescriptionComponent, MeasureRowComponent],
      providers: [
        DatePipe,
        { provide: MatDialog, useValue: {} },
        { provide: ToastrService, useValue: mockToastrService },
        { provide: GoalService, useValue: mockGoalService },
        {
          provide: ActivatedRoute,
          useValue: {
            paramMap: of(convertToParamMap({ keyresultId: '1' })),
          },
        },
      ],
      imports: [
        TranslateTestingModule.withTranslations({
          de: require('../../../../../../assets/i18n/de.json'),
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
      ],
    }).compileComponents();

    global.window = Object.create(window);
    const url = 'keyresults/2';
    Object.defineProperty(window, 'location', {
      value: {
        href: url,
      },
    });

    fixture = TestBed.createComponent(KeyResultDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    mockToastrService.success.mockReset();
    mockToastrService.error.mockReset();
    mockGoalService.getGoalByKeyResultId.mockReset();
    mockGetNumerOrNull.getNumberOrNull.mockReset();
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

  test('should have one key result description tag', () => {
    const keyResultDescription = fixture.debugElement.queryAll(By.css('app-key-result-description'));
    expect(keyResultDescription.length).toEqual(1);
  });

  test('should have two heading labels with right titles', () => {
    const headingLabel = fixture.debugElement.query(By.css('.heading-label'));
    expect(headingLabel.nativeElement.textContent).toContain('Keyresult Informationen');
  });

  test('should have one mat dividers', () => {
    const dividers = fixture.debugElement.queryAll(By.css('mat-divider'));
    expect(dividers.length).toBeTruthy();
  });

  test('should have one measure row tag', () => {
    const keyResultDescription = fixture.debugElement.queryAll(By.css('app-measure-row'));
    expect(keyResultDescription.length).toEqual(1);
  });

  test('should have a button to create measure', () => {
    const createButton = fixture.debugElement.query(By.css('#add-measure-button'));

    expect(createButton.nativeElement.textContent).toContain(' Messung hinzufügen ');
  });
});
