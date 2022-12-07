import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatMenuModule } from '@angular/material/menu';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { RouterTestingModule } from '@angular/router/testing';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { trigger } from '@angular/animations';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { KeyResultRowComponent } from './key-result-row.component';
import { MenuEntry } from 'src/app/shared/types/menu-entry';
import {
  ExpectedEvolution,
  KeyResultMeasure,
  Unit,
} from '../../shared/services/key-result.service';
import { DatePipe } from '@angular/common';
import { By } from '@angular/platform-browser';

describe('KeyResultKeyResultRowComponent', () => {
  let component: KeyResultRowComponent;
  let fixture: ComponentFixture<KeyResultRowComponent>;
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
      createdOn: new Date('2022-12-07T00:00:00'),
    },
  };

  describe('KeyResultRow with set measure', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [
          BrowserDynamicTestingModule,
          NoopAnimationsModule,
          MatMenuModule,
          MatExpansionModule,
          MatIconModule,
          RouterTestingModule,
          MatProgressBarModule,
        ],
        providers: [DatePipe],
        declarations: [KeyResultRowComponent],
      })
        .overrideComponent(KeyResultRowComponent, {
          set: {
            animations: [trigger('entryModeTransition', [])],
          },
        })
        .compileComponents();

      fixture = TestBed.createComponent(KeyResultRowComponent);
      component = fixture.componentInstance;
      component.keyResult = keyResult;
      fixture.detectChanges();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should have objetive title as class objectiveTitle', () => {
      expect(
        fixture.nativeElement.querySelector('mat-panel-title').textContent
      ).toEqual('Keyresult 1');
    });

    test('should have right owner', () => {
      const ownerTag = fixture.debugElement.query(By.css('.keyResultOwner'));
      expect(ownerTag.nativeElement.textContent).toContain('Alice Wunderland');
    });

    test('should have right last measure when measure is set', () => {
      const ownerTag = fixture.debugElement.query(By.css('.measure-date'));
      expect(ownerTag.nativeElement.textContent).toContain('07.12.2022');
    });

    test('should have progress label with right calculated progress', () => {
      let progressLabel = fixture.debugElement.query(By.css('.h6'));
      expect(progressLabel.nativeElement.textContent).toEqual('20%');
    });

    test('should have progress bar with progress from objective', () => {
      let progressBar = fixture.nativeElement
        .querySelector('#progressContainer')
        .querySelector('mat-progress-bar');
      expect(progressBar.getAttribute('ng-reflect-value')).toEqual('20');
    });

    test('should have menu button with icon', () => {
      expect(fixture.nativeElement.querySelector('button').textContent).toEqual(
        'more_vert'
      );
    });

    // @ts-ignore
    test.each([
      [
        [
          { displayName: 'Resultat hinzufügen', routeLine: 'result/add' },
          { displayName: 'Ziel bearbeiten', routeLine: 'objective/edit' },
          { displayName: 'Ziel duplizieren', routeLine: 'objective/duplicate' },
          { displayName: 'Ziel löschen', routeLine: 'objective/delete' },
        ] as MenuEntry[],
      ],
      [
        [
          { displayName: 'Resultat bearbeiten', routeLine: 'result/add' },
          { displayName: 'Resultat duplizieren', routeLine: 'objective/edit' },
          { displayName: 'Details einsehen', routeLine: 'result/add' },
          { displayName: 'Resultat löschen', routeLine: 'result/add' },
          { displayName: 'Messung hinzufügen', routeLine: 'result/add' },
        ] as MenuEntry[],
      ],
    ])('should have menu items', (menuEntries: MenuEntry[]) => {
      fixture = TestBed.createComponent(KeyResultRowComponent);
      component = fixture.componentInstance;
      component.keyResult = keyResult;
      component.menuEntries = menuEntries;
      fixture.detectChanges();

      let button = fixture.debugElement.nativeElement.querySelector(
        'button[mat-icon-button]'
      );
      button.click();
      let matMenu: HTMLElement = document.querySelector('.mat-menu-content')!;
      let children = Array.from(matMenu.children)
        .map((e) => e.querySelector('span')!)
        .map((e) => e.textContent);
      let itemTexts = menuEntries.map((e) => e.displayName);
      expect(children).toEqual(itemTexts);
    });
  });

  describe('KeyResultRow with empty measure', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [
          BrowserDynamicTestingModule,
          NoopAnimationsModule,
          MatMenuModule,
          MatExpansionModule,
          MatIconModule,
          RouterTestingModule,
          MatProgressBarModule,
        ],
        providers: [DatePipe],
        declarations: [KeyResultRowComponent],
      })
        .overrideComponent(KeyResultRowComponent, {
          set: {
            animations: [trigger('entryModeTransition', [])],
          },
        })
        .compileComponents();

      fixture = TestBed.createComponent(KeyResultRowComponent);
      component = fixture.componentInstance;

      let emptyMeasureKeyResult = keyResult;
      emptyMeasureKeyResult.measure = null!;
      component.keyResult = emptyMeasureKeyResult;

      fixture.detectChanges();
    });

    test('should have right last measure when measure is set', () => {
      const ownerTag = fixture.debugElement.query(By.css('.measure-null-date'));
      expect(ownerTag.nativeElement.textContent).toEqual(' - ');
    });
  });
});
