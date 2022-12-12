import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyresultFormComponent } from './keyresult-form.component';
import { User, UserService } from '../../shared/services/user.service';
import {
  KeyResultMeasure,
  KeyResultService,
} from '../../shared/services/key-result.service';
import { Observable, of } from 'rxjs';

describe('KeyresultFormComponent', () => {
  let component: KeyresultFormComponent;
  let fixture: ComponentFixture<KeyresultFormComponent>;

  let keyResult: Observable<KeyResultMeasure> = of({
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
  });

  let userList: Observable<User[]> = of([
    {
      id: 1,
      username: 'alice',
      firstname: 'Alice',
      lastname: 'Wunderland',
      email: 'alice@wunerland.ch',
    },
    {
      id: 1,
      username: 'pago',
      firstname: 'Paco',
      lastname: 'Egiiman',
      email: 'paco@egiiman.ch',
    },
  ]);

  const mockUserService = {
    getUsers: jest.fn(),
  };

  const mockKeyResultService = {
    getKeyResultById: jest.fn(),
  };

  beforeEach(() => {
    mockUserService.getUsers.mockReturnValue(userList);
    mockKeyResultService.getKeyResultById.mockReturnValue(keyResult);

    TestBed.configureTestingModule({
      declarations: [KeyresultFormComponent],
      providers: [
        { provide: UserService, useValue: mockUserService },
        { provide: KeyResultService, useValue: mockKeyResultService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(KeyresultFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    mockUserService.getUsers.mockReset();
    mockKeyResultService.getKeyResultById.mockReset();
  });

  xtest('should create', () => {
    expect(component).toBeTruthy();
  });
});
