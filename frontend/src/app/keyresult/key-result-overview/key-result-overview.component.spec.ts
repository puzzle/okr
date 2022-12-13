import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultOverviewComponent } from './key-result-overview.component';
import {KeyResultMeasure} from "../../shared/services/key-result.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {ObjectiveModule} from "../../objective/objective.module";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";
import {RouterTestingModule} from "@angular/router/testing";
import {Objective} from "../../shared/services/objective.service";
import {Observable, of} from "rxjs";

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
    }
  })

  let objective: Observable<Objective> = of({
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

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        ObjectiveModule,
        NoopAnimationsModule,
        RouterTestingModule,
      ],
      declarations: [KeyResultOverviewComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(KeyResultOverviewComponent);
    component = fixture.componentInstance;

    component.keyResult$ = keyresult1;
    component.objective$ = objective;

    fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });
});
