import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveDetailComponent } from './objective-detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Objective, ObjectiveService } from '../../services/objective.service';
import { Observable, of } from 'rxjs';
import {
  ExpectedEvolution,
  KeyResultMeasure,
  KeyResultService,
  Measure,
  Unit,
} from '../../services/key-result.service';

describe('ObjectiveDetailComponent', () => {
  let component: ObjectiveDetailComponent;
  let fixture: ComponentFixture<ObjectiveDetailComponent>;

  const mockKeyResultService = {
    getKeyResultsOfObjective: jest.fn(),
  };

  let measure: Measure = {
    id: 1,
    keyResultId: 2,
    value: 15,
    changeInfo: 'Changeinfo',
    initiatives: 'Initiatives',
    createdBy: 1,
    createdOn: Date.prototype,
  };

  let keyResultList: Observable<KeyResultMeasure[]> = of([
    {
      id: 1,
      objectiveId: 1,
      title: 'KeyResult 1',
      description: 'Description of KeyResult',
      ownerId: 1,
      ownerFirstname: 'Rudi',
      ownerLastname: 'Voller',
      quarterId: 1,
      quarterNumber: 3,
      quarterYear: 2022,
      expectedEvolution: ExpectedEvolution.CONSTANT,
      unit: Unit.NUMBER,
      basicValue: 10,
      targetValue: 50,
      measure: measure,
      progress: 50,
    },
  ]);

  beforeEach(async () => {
    mockKeyResultService.getKeyResultsOfObjective.mockReturnValue(
        keyResultList
    );

    await TestBed.configureTestingModule({
      declarations: [ObjectiveDetailComponent],
      imports: [HttpClientTestingModule],
      providers: [
        { provide: KeyResultService, useValue: mockKeyResultService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ObjectiveDetailComponent);
    component = fixture.componentInstance;

    //Test objective which fills our component with values
    let testObjective: Objective = {
      id: 1,
      title: 'title',
      ownerLastname: 'lastname',
      ownerFirstname: 'firstname',
      description: 'description',
      quarterYear: 2022,
      quarterNumber: 4,
      quarterId: 1,
      progress: 10,
      ownerId: 1,
      created: '01.01.2022',
    };

    component.objective = testObjective;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should contain Zyklus', () => {
    expect(fixture.nativeElement.querySelector('p').textContent).toEqual(
        'Zyklus GJ 2022'
    );
  });

  it('should contain Owner', () => {
    expect(fixture.nativeElement.querySelectorAll('p')[1].textContent).toEqual(
        'Ziel Besitzer firstname lastname '
    );
  });

  it('should create keyResults for this objective', () => {
    expect(fixture.nativeElement.querySelectorAll('div').length).toEqual(3);
  });
});