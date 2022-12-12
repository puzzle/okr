import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveDetailComponent } from './objective-detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Objective } from '../../shared/services/objective.service';
import { Observable, of } from 'rxjs';
import { KeyResultMeasure } from '../../shared/services/key-result.service';
import { By } from '@angular/platform-browser';
import { ObjectiveModule } from '../objective.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';

describe('ObjectiveDetailComponent', () => {
  let component: ObjectiveDetailComponent;
  let fixture: ComponentFixture<ObjectiveDetailComponent>;

  let keyResultList: Observable<KeyResultMeasure[]> = of([
    {
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
    },
    {
      id: 2,
      objectiveId: 1,
      title: 'KeyResult 2',
      description: 'Description of KeyResult 2',
      ownerId: 1,
      ownerFirstname: 'Rudi',
      ownerLastname: 'Voller',
      quarterId: 1,
      quarterNumber: 2,
      quarterYear: 2022,
      expectedEvolution: 'CONSTANT',
      unit: 'NUMBER',
      basicValue: 20,
      targetValue: 120,
      measure: {
        id: 2,
        keyResultId: 2,
        value: 45,
        changeInfo: 'Changeinfo 2',
        initiatives: 'Initiatives 2',
        createdBy: 1,
        createdOn: new Date('2022-12-07T00:00:00'),
      },
    },
  ]);

  let objective: Objective = {
    id: 1,
    teamId: 1,
    teamName: 'Team Name',
    title: 'title',
    ownerLastname: 'Alice',
    ownerFirstname: 'Wunderland',
    teamId: 1,
    teamName: 'Puzzle ITC',
    description: 'description',
    quarterYear: 2022,
    quarterNumber: 4,
    quarterId: 1,
    progress: 10,
    ownerId: 1,
    created: '01.01.2022',
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        ObjectiveModule,
        NoopAnimationsModule,
        RouterTestingModule,
      ],
      declarations: [ObjectiveDetailComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ObjectiveDetailComponent);
    component = fixture.componentInstance;

    component.keyResultList = keyResultList;
    component.objective = objective;

    fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should have cycle with right quarteryear', () => {
    const cycleText = fixture.debugElement.query(By.css('.objective-cycle'));
    expect(cycleText.nativeElement.textContent).toEqual('Zyklus GJ 2022 ');
  });

  test('should have owner with right first- and lastname', () => {
    const ownerText = fixture.debugElement.query(By.css('.objective-owner'));
    expect(ownerText.nativeElement.textContent).toEqual(
      'Ziel Besitzer Wunderland Alice '
    );
  });

  test('should have right table titles', () => {
    const spanTextes = fixture.debugElement.queryAll(By.css('span'));
    expect(spanTextes[0].nativeElement.textContent).toEqual('Resultate');
    expect(spanTextes[1].nativeElement.textContent).toEqual('Besitzer');
    expect(spanTextes[2].nativeElement.textContent).toEqual('Letzte Messung');
    expect(spanTextes[3].nativeElement.textContent).toEqual('Fortschritt');
  });

  test('should have 2 key result rows', () => {
    const keyResultRows = fixture.debugElement.queryAll(
      By.css('app-keyresult-row')
    );
    expect(keyResultRows.length).toEqual(2);
  });
});
