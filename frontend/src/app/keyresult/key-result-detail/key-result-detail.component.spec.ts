import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultDetailComponent } from './key-result-detail.component';
import {
  KeyResultMeasure,
} from '../../shared/services/key-result.service';
import { By } from '@angular/platform-browser';
import { MatCardModule } from '@angular/material/card';

describe('KeyResultDetailComponent', () => {
  let component: KeyResultDetailComponent;
  let fixture: ComponentFixture<KeyResultDetailComponent>;
  let keyResult: KeyResultMeasure = {
    id: 1,
    objectiveId: 1,
    title: 'Keyresult 1',
    description: 'This is a description',
    ownerId: 2,
    ownerFirstname: 'Alice',
    ownerLastname: 'Wunderland',
    quarterId: 1,
    quarterNumber: 3,
    quarterYear: 2022,
    expectedEvolution: 'INCREASE',
    unit: 'PERCENT',
    basicValue: 0,
    targetValue: 100,
    measure: {
      id: 1,
      keyResultId: 1,
      value: 20,
      changeInfo: 'Change Infos',
      initiatives: 'Initatives',
      createdBy: 2,
      createdOn: new Date('2022-12-07T00:00:00'),
    },
  };

  describe('KeyResultDetail with measures', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MatCardModule],
        declarations: [KeyResultDetailComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyResultDetailComponent);
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

      expect(strongs[1].nativeElement.textContent).toEqual('Letzte Messung');
    });

    test('should have keyresult description', () => {
      const paragraphs = fixture.debugElement.queryAll(By.css('p'));
      expect(paragraphs[1].nativeElement.textContent).toEqual(
        'This is a description'
      );
    });

    test('should have last measure date', () => {
      const lastMeasureDate = fixture.debugElement.query(
        By.css('.lastMeasureDate')
      );
      expect(lastMeasureDate.nativeElement.textContent).toEqual('Dec 7, 2022');
    });

    test('should have last measure date', () => {
      const lastMeasureDate = fixture.debugElement.query(
        By.css('.lastMeasurechangeInfo')
      );
      expect(lastMeasureDate.nativeElement.textContent).toEqual('Change Infos');
    });
  });

  describe('KeyResultDetail with no measures', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MatCardModule],
        declarations: [KeyResultDetailComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyResultDetailComponent);
      component = fixture.componentInstance;

      let emptyMeasureKeyResult = keyResult;
      emptyMeasureKeyResult.measure = null!;
      component.keyResult = emptyMeasureKeyResult;

      fixture.detectChanges();
    });

    test('should display - when measure is null', () => {
      const lastMeasureDate = fixture.debugElement.query(
        By.css('.emptyLastMeasure')
      );
      expect(lastMeasureDate.nativeElement.textContent).toContain('-');
    });
  });
});
