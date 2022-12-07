import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultDetailComponent } from './key-result-detail.component';
import {
  ExpectedEvolution,
  KeyResultMeasure,
  Unit,
} from '../../shared/services/key-result.service';
import { By } from '@angular/platform-browser';

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
    expectedEvolution: ExpectedEvolution.INCREASE,
    unit: Unit.PERCENT,
    basicValue: 0,
    targetValue: 100,
    measure: {
      id: 1,
      keyResultId: 1,
      value: 20,
      changeInfo: 'Change Infos',
      initiatives: 'Initatives',
      createdBy: 2,
      createdOn: new Date(),
    },
  };

  describe('KeyResultDetail with measures', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [KeyResultDetailComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyResultDetailComponent);
      component = fixture.componentInstance;

      component.keyResult = keyResult;

      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should have right strong titles', () => {
      const strongs = fixture.debugElement.queryAll(By.css('strong'));

      expect(strongs.length).toEqual(3);

      expect(strongs[0].nativeElement.textContent).toEqual('Details');

      expect(strongs[1].nativeElement.textContent).toEqual('Letzte Messung');
    });

    it('should have keyresult description', () => {
      const paragraphs = fixture.debugElement.queryAll(By.css('p'));
      expect(paragraphs[1].nativeElement.textContent).toEqual(
        'This is a description'
      );
    });

    it('should have last measure date', () => {
      const lastMeasureDate = fixture.debugElement.query(
        By.css('.lastMeasureDate')
      );
      expect(lastMeasureDate.nativeElement.textContent).toEqual('Dec 7, 2022');
    });

    it('should have last measure date', () => {
      const lastMeasureDate = fixture.debugElement.query(
        By.css('.lastMeasurechangeInfo')
      );
      expect(lastMeasureDate.nativeElement.textContent).toEqual('Change Infos');
    });
  });

  describe('KeyResultDetail with no measures', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [KeyResultDetailComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyResultDetailComponent);
      component = fixture.componentInstance;

      let emptyMeasureKeyResult = keyResult;
      emptyMeasureKeyResult.measure = null!;
      component.keyResult = emptyMeasureKeyResult;

      fixture.detectChanges();
    });

    it('should display - when measure is null', () => {
      const lastMeasureDate = fixture.debugElement.query(
        By.css('.emptyLastMeasure')
      );
      expect(lastMeasureDate.nativeElement.textContent).toEqual('-');
    });
  });
});
