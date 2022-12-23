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
import { KeyResultMeasure } from '../../shared/services/key-result.service';
import { DatePipe } from '@angular/common';
import { By } from '@angular/platform-browser';
import { KeyresultModule } from '../keyresult.module';
import * as keyresultData from '../../shared/testing/mock-data/keyresults.json';

describe('KeyResultKeyResultRowComponent', () => {
  let component: KeyResultRowComponent;
  let fixture: ComponentFixture<KeyResultRowComponent>;
  let keyResult: KeyResultMeasure = keyresultData.keyresults[0];

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
          KeyresultModule,
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

    test.each([
      [
        [
          {
            displayName: 'KeyResult bearbeiten',
            routeLine: 'objective/objectiveId/keyresult/edit/keyresultId',
          },
          { displayName: 'KeyResult duplizieren', routeLine: 'objective/edit' },
          { displayName: 'Details einsehen', routeLine: 'result/add' },
          { displayName: 'KeyResult löschen', routeLine: 'result/add' },
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
          KeyresultModule,
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
