import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultOverviewComponent } from './key-result-overview.component';
import { KeyResultMeasure } from '../../shared/services/key-result.service';
import * as keyresultData from '../../shared/testing/mock-data/keyresults.json';
import { MatCardModule } from '@angular/material/card';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';

describe('KeyResultOverviewComponent', () => {
  let component: KeyResultOverviewComponent;
  let fixture: ComponentFixture<KeyResultOverviewComponent>;
  let keyResult: KeyResultMeasure = keyresultData.keyresults[0];

  describe('KeyResultDetail with measures', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MatCardModule, RouterTestingModule],
        declarations: [KeyResultOverviewComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyResultOverviewComponent);
      component = fixture.componentInstance;

      component.keyResult = keyResult;

      fixture.detectChanges();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should have right strong titles', () => {
      const strongs = fixture.debugElement.queryAll(By.css('strong'));

      expect(strongs.length).toEqual(1);

      expect(strongs[0].nativeElement.textContent).toEqual('Dec 23, 2022');
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
      expect(matcardcontens[2].nativeElement.textContent).toEqual(
        'Dec 23, 2022 Lorem Ipsum is simply dummy text of the printing and typesetting industry. Details anzeigen  Messung hinzufügen '
      );
    });

    test('should have last measure date', () => {
      const lastMeasureDate = fixture.debugElement.query(
        By.css('.lastMeasureDate')
      );
      expect(lastMeasureDate.nativeElement.textContent).toEqual('Dec 23, 2022');
    });

    test('should have change info title', () => {
      const lastMeasureDate = fixture.debugElement.query(
        By.css('.lastMeasurechangeInfo')
      );
      expect(lastMeasureDate.nativeElement.textContent).toEqual(
        ' Lorem Ipsum is simply dummy text of the printing and typesetting industry. '
      );
    });

    test('should hava correct link in add measure button', () => {
      const button = fixture.debugElement.query(By.css('#add-measure-button'));

      expect(button.attributes['ng-reflect-router-link']).toEqual(
        'keyresults/1/measure/new'
      );
    });

    test('should hava correct link in show detail button', () => {
      const button = fixture.debugElement.query(By.css('#show-details-button'));

      expect(button.attributes['ng-reflect-router-link']).toEqual(
        'keyresults/1'
      );
    });

    test('should hava correct link in edit keyresult button', () => {
      const button = fixture.debugElement.query(
        By.css('#edit-keyresult-button')
      );

      expect(button.attributes['ng-reflect-router-link']).toEqual(
        'objective/1/keyresult/edit/1'
      );
    });
  });

  describe('KeyResultDetail with no measures', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MatCardModule],
        declarations: [KeyResultOverviewComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyResultOverviewComponent);
      component = fixture.componentInstance;

      let emptyMeasureKeyResult = keyResult;
      emptyMeasureKeyResult.measure = null!;
      component.keyResult = emptyMeasureKeyResult;

      fixture.detectChanges();
    });

    test('should display - when measure is null', () => {
      const lastMeasureDate = fixture.debugElement.query(
        By.css('mat-card-content[data-test-marker="emptyLastMeasure"]')
      );
      expect(lastMeasureDate.nativeElement.textContent).toContain('-');
    });
  });
});
