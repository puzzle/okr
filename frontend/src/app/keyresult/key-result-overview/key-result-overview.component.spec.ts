import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultOverviewComponent } from './key-result-overview.component';
import { KeyResultMeasure } from '../../shared/services/key-result.service';
import * as keyresultData from '../../shared/testing/mock-data/keyresults.json';
import { MatCardModule } from '@angular/material/card';
import { By } from '@angular/platform-browser';

describe('KeyResultOverviewComponent', () => {
  let component: KeyResultOverviewComponent;
  let fixture: ComponentFixture<KeyResultOverviewComponent>;
  let keyResult: KeyResultMeasure = keyresultData.keyresults[0];

  describe('KeyResultDetail with measures', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MatCardModule],
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

      expect(strongs.length).toEqual(3);

      expect(strongs[0].nativeElement.textContent).toEqual('Details');

      expect(strongs[1].nativeElement.textContent).toEqual('Messungszeitpunkt');
    });

    test('should have keyresult description', () => {
      const paragraphs = fixture.debugElement.queryAll(By.css('p'));
      expect(paragraphs[1].nativeElement.textContent).toEqual(
        'This is the description'
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
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry.'
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
