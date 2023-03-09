import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { By } from '@angular/platform-browser';
import { Goal } from '../../../services/goal.service';
import * as goalsData from '../../../testing/mock-data/goals.json';
import { RouterLinkWithHref } from '@angular/router';
import { KeyResultDescriptionComponent } from './key-result-description.component';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { MatIconModule } from '@angular/material/icon';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatCardModule } from '@angular/material/card';
import { TranslateTestingModule } from 'ngx-translate-testing';

describe('KeyResultDescriptionComponent', () => {
  let component: KeyResultDescriptionComponent;
  let fixture: ComponentFixture<KeyResultDescriptionComponent>;

  let goal: Goal = goalsData.goals[0];

  describe('Key Result Description', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
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
        ],
        declarations: [KeyResultDescriptionComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyResultDescriptionComponent);
      component = fixture.componentInstance;
      component.goal = goal;
      fixture.detectChanges();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should have 5 fw-normal titles', () => {
      const titles = fixture.debugElement.queryAll(By.css('.key'));
      expect(titles.length).toEqual(6);
    });

    test('should have 4 texts', () => {
      const titles = fixture.debugElement.queryAll(By.css('.key'));
      var titelsText = titles.map((e) => e.nativeElement.innerHTML);

      var expectedTexts = [
        'Key Result: ',
        'Beschreibung:',
        'Team 1 Objective: ',
        'Zyklus: ',
        ' Startwert: ',
        ' Zielwert: ',
      ];
      expect(titelsText).toEqual(expectedTexts);
    });
  });
});
