import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveRowComponent } from './objective-row.component';
import { Objective } from '../../shared/services/objective.service';
import { MatMenuModule } from '@angular/material/menu';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CommonModule } from '@angular/common';
import { Observable, of } from 'rxjs';
import {
  ExpectedEvolution,
  KeyResultMeasure,
  KeyResultService,
  Unit,
} from '../../shared/services/key-result.service';

describe('ObjectiveComponent', () => {
  let component: ObjectiveRowComponent;
  let fixture: ComponentFixture<ObjectiveRowComponent>;

  let objective: Objective = {
    id: 1,
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
      expectedEvolution: ExpectedEvolution.INCREASE,
      unit: Unit.PERCENT,
      basicValue: 0,
      targetValue: 100,
      measure: {
        id: 2,
        keyResultId: 2,
        value: 40,
        changeInfo: 'Change Infos',
        initiatives: 'Initatives',
        createdBy: 2,
        createdOn: new Date(),
      },
    },
  ]);

  const mockKeyResultService = {
    getKeyResultsOfObjective: jest.fn(),
  };

  beforeEach(async () => {
    mockKeyResultService.getKeyResultsOfObjective.mockReturnValue(
      keyResultList
    );

    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, CommonModule, MatMenuModule],
      declarations: [ObjectiveRowComponent],
      providers: [
        { provide: KeyResultService, useValue: mockKeyResultService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ObjectiveRowComponent);
    component = fixture.componentInstance;
    component.objective = objective;
    fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should have objective title', () => {
    expect(
      fixture.nativeElement.querySelector('.objective-title').textContent
    ).toEqual(objective.title);
  });

  test('should have progress label with progress from objective', () => {
    expect(
      fixture.nativeElement.querySelector('#progressSpan').textContent
    ).toEqual(objective.progress + '%');
  });

  test('should have progress bar with progress from objective', () => {
    expect(
      fixture.nativeElement.querySelector('.objectiveProgress').value
    ).toEqual(objective.progress);
  });

  test('should have menu button with icon', () => {
    expect(fixture.nativeElement.querySelector('button').textContent).toEqual(
      'more_vert'
    );
  });

  test('should have button with icon', () => {
    expect(fixture.nativeElement.querySelector('button').textContent).toEqual(
      'more_vert'
    );
  });

  // it('should have 4 menu items', () => {
  //   let button = fixture.debugElement.nativeElement.querySelector('#triggerButton');
  //   button.click();
  //   expect(
  //     fixture.debugElement.nativeElement.querySelector('.matMenu')
  //   ).toEqual(" ");
  // });
});
