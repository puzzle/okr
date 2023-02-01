import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveRowComponent } from './objective-row.component';
import { Objective } from '../../shared/services/objective.service';
import { MatMenuModule } from '@angular/material/menu';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CommonModule } from '@angular/common';
import { Observable, of } from 'rxjs';
import {
  KeyResultMeasure,
  KeyResultService,
} from '../../shared/services/key-result.service';
import { MenuEntry } from '../../shared/types/menu-entry';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { RouterTestingModule } from '@angular/router/testing';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { trigger } from '@angular/animations';
import { By } from '@angular/platform-browser';
import { ObjectiveModule } from '../objective.module';
import * as objectivesData from '../../shared/testing/mock-data/objectives.json';
import * as keyresultData from '../../shared/testing/mock-data/keyresults.json';
import { ToastrService } from 'ngx-toastr';

describe('ObjectiveRowComponent', () => {
  let component: ObjectiveRowComponent;
  let fixture: ComponentFixture<ObjectiveRowComponent>;

  let objective: Objective = objectivesData.objectives[0];

  let keyResultList: Observable<KeyResultMeasure[]> = of(
    keyresultData.keyresults
  );

  const mockKeyResultService = {
    getKeyResultsOfObjective: jest.fn(),
  };

  const mockToastrService = {
    success: jest.fn(),
    error: jest.fn(),
  };

  beforeEach(() => {
    mockKeyResultService.getKeyResultsOfObjective.mockReturnValue(
      keyResultList
    );

    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        CommonModule,
        BrowserDynamicTestingModule,
        NoopAnimationsModule,
        MatMenuModule,
        MatExpansionModule,
        MatIconModule,
        RouterTestingModule,
        MatProgressBarModule,
        ObjectiveModule,
      ],
      declarations: [ObjectiveRowComponent],
      providers: [
        { provide: KeyResultService, useValue: mockKeyResultService },
        { provide: ToastrService, useValue: mockToastrService },
      ],
    })
      .overrideComponent(ObjectiveRowComponent, {
        set: {
          animations: [trigger('entryModeTransition', [])],
        },
      })
      .compileComponents();

    fixture = TestBed.createComponent(ObjectiveRowComponent);
    component = fixture.componentInstance;

    component.objective = objective;

    fixture.detectChanges();
  });

  afterEach(() => {
    mockKeyResultService.getKeyResultsOfObjective.mockReset();
    mockToastrService.success.mockReset();
    mockToastrService.error.mockReset();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should have objective title', () => {
    expect(
      fixture.nativeElement.querySelector('.objective-title').textContent
    ).toEqual('Objective 1');
  });

  test('should have progress label with progress from objective', () => {
    expect(
      fixture.nativeElement.querySelector('#progressSpan').textContent
    ).toEqual('20%');
  });

  test('should have progress bar with progress from objective', () => {
    let progressBar = fixture.nativeElement
      .querySelector('#progressContainer')
      .querySelector('app-progress-bar');
    expect(progressBar.getAttribute('ng-reflect-value')).toEqual(
      objective.progress!.toString()
    );
  });

  test('should have add keyresult button', () => {
    const button = fixture.debugElement.query(By.css('#add-keyresult-button'));

    expect(button.nativeElement.textContent).toEqual(' Key Result hinzufügen ');
    expect(button.attributes['ng-reflect-router-link']).toEqual(
      'objective/1/keyresult/new'
    );
  });

  test.each([
    [
      [
        { displayName: 'Objective löschen', routeLine: 'objective/delete' },
      ] as MenuEntry[],
    ],
  ])('should have menu items', (menuEntries: MenuEntry[]) => {
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

  test('should have 1 objective detail rows', () => {
    const keyResultRows = fixture.debugElement.queryAll(
      By.css('app-objective-detail')
    );
    expect(keyResultRows.length).toEqual(1);
  });
});
