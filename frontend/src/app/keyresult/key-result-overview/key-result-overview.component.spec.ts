import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultOverviewComponent } from './key-result-overview.component';
import {
  KeyResultMeasure,
  KeyResultService,
} from '../../shared/services/key-result.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ObjectiveModule } from '../../objective/objective.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import {
  Objective,
  ObjectiveService,
} from '../../shared/services/objective.service';
import { Observable, of } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('KeyresultOverviewComponent', () => {
  let component: KeyResultOverviewComponent;
  let fixture: ComponentFixture<KeyResultOverviewComponent>;

  let keyresult1: Observable<KeyResultMeasure> = of({
    id: 1,
    objectiveId: 1,
    title: 'KeyResult 1',
    description: 'Description of KeyResult 1',
    ownerId: 1,
    ownerFirstname: 'Rudi',
    ownerLastname: 'Voller',
    quarterId: 1,
    quarterNumber: 3,
    quarterYear: 2022,
    expectedEvolution: 'CONSTANT',
    unit: 'NUMBER',
    basicValue: 10,
    targetValue: 50,
    measure: {
      id: 1,
      keyResultId: 1,
      value: 15,
      changeInfo: 'Changeinfo 1',
      initiatives: 'Initiatives 2',
      createdBy: 1,
      createdOn: new Date('2022-12-07T00:00:00'),
    },
  });

  let objective1: Observable<Objective> = of({
    id: 1,
    teamId: 1,
    teamName: 'Team Name',
    title: 'title',
    ownerLastname: 'Alice',
    ownerFirstname: 'Wunderland',
    description: 'description',
    quarterYear: 2022,
    quarterNumber: 4,
    quarterId: 1,
    progress: 10,
    ownerId: 1,
    created: '01.01.2022',
  });

  const mockKeyResultService = {
    getKeyResultById: jest.fn(),
  };

  const mockObjectiveService = {
    getObjectiveById: jest.fn(),
  };

  beforeEach(() => {
    mockKeyResultService.getKeyResultById.mockReturnValue(keyresult1);

    mockObjectiveService.getObjectiveById.mockReturnValue(objective1);

    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        ObjectiveModule,
        NoopAnimationsModule,
        RouterTestingModule,
      ],
      providers: [
        { provide: KeyResultService, useValue: mockKeyResultService },
        { provide: ObjectiveService, useValue: mockObjectiveService },
      ],
      declarations: [KeyResultOverviewComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(KeyResultOverviewComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  afterEach(() => {
    mockKeyResultService.getKeyResultById.mockReset();
    mockObjectiveService.getObjectiveById.mockReset();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should set keyresult of component', () => {
    keyresult1.subscribe((keyresult1Value) => {
      let keyresult = keyresult1Value;
      expect(component.keyResult).toEqual(keyresult);
    });
  });

  test('should set objective of component', () => {
    objective1.subscribe((objective1Value) => {
      let objective = objective1Value;
      expect(component.objective).toEqual(objective);
    });
  });

  test('should have 4 strong titles', () => {
    const titles = fixture.debugElement.queryAll(By.css('strong'));
    expect(titles.length).toEqual(4);
  });

  test('should set title from keyresult', () => {
    const title = fixture.debugElement.query(By.css('.key-result-title'));
    expect(title.nativeElement.textContent).toContain('KeyResult KeyResult 1');
  });

  test('should set title from keyresult', () => {
    const description = fixture.debugElement.query(
      By.css('.key-result-description')
    );
    expect(description.nativeElement.textContent).toContain(
      'Beschreibung Description of KeyResult 1'
    );
  });

  test('should set teamname from objective', () => {
    const teamname = fixture.debugElement.query(By.css('.key-result-Ziel'));
    expect(teamname.nativeElement.textContent).toContain(
      'Team Name Objective title'
    );
  });

  test('should set quarter from keyresult', () => {
    const quarter = fixture.debugElement.query(By.css('.key-result-quarter'));
    expect(quarter.nativeElement.textContent).toContain('Zyklus GJ 2022');
  });
});
