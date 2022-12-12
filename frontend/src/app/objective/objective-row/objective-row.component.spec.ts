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

describe('ObjectiveComponent', () => {
  let component: ObjectiveRowComponent;
  let fixture: ComponentFixture<ObjectiveRowComponent>;

  let objective: Objective = {
    id: 1,
    teamName: 'TeamName',
    teamId: 1,
    title: 'Objective 1',
    ownerId: 2,
    ownerFirstname: 'Alice',
    ownerLastname: 'Wunderland',
    description: 'This is a description',
    progress: 5,
    quarterId: 1,
    quarterNumber: 3,
    quarterYear: 2022,
    created: '01.01.2022',
  };

  let keyResultList: Observable<KeyResultMeasure[]> = of([
    {
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
    },
    {
      id: 2,
      objectiveId: 1,
      title: 'Keyresult 2',
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
        id: 2,
        keyResultId: 2,
        value: 40,
        changeInfo: 'Change Infos',
        initiatives: 'Initatives',
        createdBy: 2,
        createdOn: new Date('2022-12-07T00:00:00'),
      },
    },
  ]);

  const mockKeyResultService = {
    getKeyResultsOfObjective: jest.fn(),
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
    ).toEqual('5%');
  });

  test('should have progress bar with progress from objective', () => {
    let progressBar = fixture.nativeElement
      .querySelector('#progressContainer')
      .querySelector('mat-progress-bar');
    expect(progressBar.getAttribute('ng-reflect-value')).toEqual(
      objective.progress.toString()
    );
  });

  test('should have menu button with icon', () => {
    expect(fixture.nativeElement.querySelector('button').textContent).toEqual(
      'more_vert'
    );
  });

  test.each([
    [
      [
        { displayName: 'Resultat hinzufügen', routeLine: 'result/add' },
        { displayName: 'Ziel bearbeiten', routeLine: 'objective/edit' },
        { displayName: 'Ziel duplizieren', routeLine: 'objective/duplicate' },
        { displayName: 'Ziel löschen', routeLine: 'objective/delete' },
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
