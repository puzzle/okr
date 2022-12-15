import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyresultFormComponent } from './keyresult-form.component';
import { User, UserService } from '../../shared/services/user.service';
import {
  KeyResultMeasure,
  KeyResultService,
} from '../../shared/services/key-result.service';
import { Observable, of } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';
import { KeyresultModule } from '../keyresult.module';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import {
  Objective,
  ObjectiveService,
} from '../../shared/services/objective.service';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { By } from '@angular/platform-browser';
import { FormControl, FormGroup, Validators } from '@angular/forms';

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

  let objective: Observable<Objective> = of({
    id: 1,
    teamName: 'Team Name',
    teamId: 1,
    title: 'Wir wollen unseren Umsatz verdoppeln',
    ownerId: 1,
    ownerFirstname: 'Alice',
    ownerLastname: 'Wunderland',
    description: 'Description',
    progress: 5,
    quarterId: 1,
    quarterNumber: 3,
    quarterYear: 2022,
    created: '01.01.2022',
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

  let initKeyResult: KeyResultMeasure = {
    id: null,
    title: '',
    description: '',
    expectedEvolution: '',
    unit: '',
    ownerId: 0,
    ownerLastname: '',
    ownerFirstname: '',
    quarterId: 1,
    quarterNumber: 1,
    quarterYear: 2022,
    targetValue: 1,
    basicValue: 1,
    objectiveId: 3000,
  };

  let createKeyResultForm = new FormGroup({
    title: new FormControl<string>('Keyresult 1', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(20),
    ]),
    unit: new FormControl<string>('PERCENT', [Validators.required]),
    expectedEvolution: new FormControl<string>('INCREASE', [
      Validators.required,
    ]),
    basicValue: new FormControl<number>(0, Validators.required),
    targetValue: new FormControl<number>(100, Validators.required),
    description: new FormControl<string>('This is a description', [
      Validators.maxLength(4096),
    ]),
    ownerId: new FormControl<number>(2, [
      Validators.required,
      Validators.min(1),
    ]),
  });

  const mockUserService = {
    getUsers: jest.fn(),
  };
  const mockKeyResultService = {
    getKeyResultById: jest.fn(),
    getInitKeyResult: jest.fn(),
    saveKeyresult: jest.fn(),
  };
  const mockObjectiveService = {
    getObjectiveById: jest.fn(),
  };
  const mockGetNumerOrNull = {
    getNumberOrNull: jest.fn(),
  };

  describe('KeyresultFormComponent Edit KeyResult', () => {
    beforeEach(() => {
      mockUserService.getUsers.mockReturnValue(userList);
      mockKeyResultService.getKeyResultById.mockReturnValue(keyResult);
      mockKeyResultService.getInitKeyResult.mockReturnValue(initKeyResult);
      mockObjectiveService.getObjectiveById.mockReturnValue(objective);
      mockGetNumerOrNull.getNumberOrNull.mockReturnValue(1);

      TestBed.configureTestingModule({
        declarations: [KeyresultFormComponent],
        imports: [
          RouterTestingModule,
          KeyresultModule,
          HttpClientTestingModule,
          NoopAnimationsModule,
        ],
        providers: [
          { provide: UserService, useValue: mockUserService },
          { provide: KeyResultService, useValue: mockKeyResultService },
          { provide: ObjectiveService, useValue: mockObjectiveService },
          {
            provide: ActivatedRoute,
            useValue: {
              paramMap: of(
                convertToParamMap({ objectiveId: '1', keyresultId: '1' })
              ),
            },
          },
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyresultFormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      mockUserService.getUsers.mockReset();
      mockKeyResultService.getKeyResultById.mockReset();
      mockKeyResultService.getInitKeyResult.mockReset();
      mockObjectiveService.getObjectiveById.mockReset();
      mockGetNumerOrNull.getNumberOrNull.mockReset();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
      expect(mockObjectiveService.getObjectiveById).toHaveBeenCalledWith(1);
      expect(mockKeyResultService.getKeyResultById).toHaveBeenCalledWith(1);
    });

    test('should set is creating on false', () => {
      expect(component.create).toBeFalsy();
    });

    test('should have 3 right titles', () => {
      const titles = fixture.debugElement.queryAll(By.css('.title'));
      expect(titles.length).toEqual(3);
      expect(titles[0].nativeElement.textContent).toContain(
        'Keyresult bearbeiten'
      );
      expect(titles[1].nativeElement.textContent).toContain(
        'Objective Beschreibung'
      );
      expect(titles[2].nativeElement.textContent).toContain('Details');
    });

    test('should have objective title', () => {
      const objectiveTitle = fixture.debugElement.query(
        By.css('.objective-title')
      );
      expect(objectiveTitle.nativeElement.textContent).toContain(
        'Wir wollen unseren Umsatz verdoppeln'
      );
    });

    test('should have objective teamname', () => {
      const teamNameTitle = fixture.debugElement.query(
        By.css('.teamname-title')
      );
      expect(teamNameTitle.nativeElement.textContent).toEqual('Team');
      const objectiveTeamName = fixture.debugElement.query(
        By.css('.objective-teamname')
      );
      expect(objectiveTeamName.nativeElement.textContent).toContain(
        'Team Name'
      );
    });

    test('should have objective description', () => {
      const teamNameTitle = fixture.debugElement.query(
        By.css('.description-title')
      );
      expect(teamNameTitle.nativeElement.textContent).toEqual('Description');
      const objectiveTeamName = fixture.debugElement.query(
        By.css('.objective-description')
      );
      expect(objectiveTeamName.nativeElement.textContent).toContain(
        'Description'
      );
    });

    test('should have objective quarter', () => {
      const teamNameTitle = fixture.debugElement.query(
        By.css('.quarter-title')
      );
      expect(teamNameTitle.nativeElement.textContent).toEqual('Zyklus');
      const objectiveTeamName = fixture.debugElement.query(
        By.css('.objective-quarter')
      );
      expect(objectiveTeamName.nativeElement.textContent).toContain(
        'GJ 2022-3'
      );
    });

    test('should set keyresult title in input field and set input invalid when empty value', () => {
      const titleinputfield = fixture.debugElement.query(
        By.css('.title-input')
      );
      expect(titleinputfield.nativeElement.value).toContain('Keyresult 1');
      expect(component.keyResultForm.valid).toBeTruthy();

      component.keyResultForm.get('title')?.setValue('');
      titleinputfield.nativeElement.value = '';
      fixture.detectChanges();

      expect(component.keyResultForm.valid).toBeFalsy();
    });

    xtest('should set keyresult unit in mat select and set it new on item change', () => {
      const unitselect = fixture.debugElement.query(
        By.css('.unit-select')
      ).nativeElement;
      expect(unitselect.value).toEqual('PERCENT');

      unitselect.click();
      fixture.detectChanges();

      const selectOptions = fixture.debugElement.queryAll(By.css('mat-option'));
      expect(selectOptions.length).toEqual(4);

      selectOptions[2].nativeElement.click();
      fixture.detectChanges();

      expect(unitselect.value).toEqual('NUMBER');
    });

    xtest('should set keyresult evolution in mat select and set it new on item change', () => {
      const evolutionSelect = fixture.debugElement.query(
        By.css('.evolution-select')
      ).nativeElement;
      expect(evolutionSelect.value).toEqual('INCREASE');

      evolutionSelect.click();
      fixture.detectChanges();

      const selectOptions = fixture.debugElement.queryAll(By.css('mat-option'));
      expect(selectOptions.length).toEqual(3);

      selectOptions[2].nativeElement.click();
      fixture.detectChanges();

      expect(evolutionSelect.value).toEqual('CONSTANT');
    });

    test('should set keyresult basicvalue in input field and set input invalid when empty value', () => {
      const basicValueIputfield = fixture.debugElement.query(
        By.css('.basicValue-input')
      );
      expect(basicValueIputfield.nativeElement.value).toContain('0');
      expect(basicValueIputfield.nativeElement.placeholder).toContain('0.0');
      expect(component.keyResultForm.valid).toBeTruthy();

      component.keyResultForm.get('basicValue')?.setValue(null);
      basicValueIputfield.nativeElement.value = '';
      fixture.detectChanges();

      expect(component.keyResultForm.valid).toBeFalsy();
    });

    test('should set keyresult targetValue in input field and set input invalid when empty value', () => {
      const targetValueIputfield = fixture.debugElement.query(
        By.css('.targetValue-input')
      );
      expect(targetValueIputfield.nativeElement.value).toContain('100');
      expect(targetValueIputfield.nativeElement.placeholder).toContain('0.0');
      expect(component.keyResultForm.valid).toBeTruthy();

      component.keyResultForm.get('targetValue')?.setValue(null);
      targetValueIputfield.nativeElement.value = '';
      fixture.detectChanges();

      expect(component.keyResultForm.valid).toBeFalsy();
    });

    test('should set keyresult description in text area and dont set input invalid when empty value', () => {
      const descriptionTextArea = fixture.debugElement.query(
        By.css('.description-textarea')
      );
      expect(descriptionTextArea.nativeElement.value).toContain(
        'This is a description'
      );
      expect(descriptionTextArea.nativeElement.placeholder).toContain(
        'Beschreibung'
      );
      expect(component.keyResultForm.valid).toBeTruthy();

      component.keyResultForm.get('description')?.setValue(null);
      descriptionTextArea.nativeElement.value = '';
      fixture.detectChanges();

      expect(component.keyResultForm.valid).toBeTruthy();
    });

    xtest('should set keyresult owner in mat select and set it new on item change', () => {
      const ownerSelect = fixture.debugElement.query(
        By.css('.owner-select')
      ).nativeElement;
      expect(ownerSelect.value).toEqual('Alice Wunderland');

      ownerSelect.click();
      fixture.detectChanges();

      const selectOptions = fixture.debugElement.queryAll(By.css('mat-option'));
      expect(selectOptions.length).toEqual(2);

      selectOptions[1].nativeElement.click();
      fixture.detectChanges();

      expect(ownerSelect.value).toEqual('Paco Egiiman');
    });

    test('should disable button when form is invalid', () => {
      const createbutton = fixture.debugElement.query(By.css('.create-button'));
      const inputField = fixture.debugElement.query(By.css('.title-input'));

      expect(component.keyResultForm.valid).toBeTruthy();
      expect(createbutton.nativeElement.disabled).toEqual(false);

      component.keyResultForm.get('title')?.setValue('');
      inputField.nativeElement.value = '';
      fixture.detectChanges();

      expect(inputField.nativeElement.value).toEqual('');
      expect(component.keyResultForm.valid).toBeFalsy();
      expect(createbutton.nativeElement.disabled).toEqual(true);
    });

    test('should save keyresult', () => {
      const createbutton = fixture.debugElement.query(By.css('.create-button'));

      expect(component.keyResultForm.valid).toBeTruthy();
      expect(createbutton.nativeElement.disabled).toEqual(false);

      createbutton.nativeElement.click();
      fixture.detectChanges();

      keyResult.subscribe((keyresult) => {
        expect(mockKeyResultService.saveKeyresult).toHaveBeenCalledWith(
          keyresult
        );
      });
    });
  });

  describe('KeyresultFormComponent Create KeyResult', () => {
    beforeEach(() => {
      mockUserService.getUsers.mockReturnValue(userList);
      mockObjectiveService.getObjectiveById.mockReturnValue(objective);
      mockGetNumerOrNull.getNumberOrNull.mockReturnValue(1);

      TestBed.configureTestingModule({
        declarations: [KeyresultFormComponent],
        imports: [
          RouterTestingModule,
          KeyresultModule,
          HttpClientTestingModule,
          NoopAnimationsModule,
        ],
        providers: [
          { provide: UserService, useValue: mockUserService },
          { provide: KeyResultService, useValue: mockKeyResultService },
          { provide: ObjectiveService, useValue: mockObjectiveService },
          {
            provide: ActivatedRoute,
            useValue: {
              paramMap: of(
                convertToParamMap({ objectiveId: '1', keyresultId: null })
              ),
            },
          },
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyresultFormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      mockUserService.getUsers.mockReset();
      mockObjectiveService.getObjectiveById.mockReset();
      mockGetNumerOrNull.getNumberOrNull.mockReset();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should set is creating on false', () => {
      expect(component.create).toBeTruthy();
      expect(mockObjectiveService.getObjectiveById).toHaveBeenCalledWith(1);
      expect(mockKeyResultService.getKeyResultById).toHaveBeenCalledTimes(0);
      expect(mockKeyResultService.getInitKeyResult).toHaveBeenCalled();
    });

    test('should set init keyresult', () => {
      component.keyresult$.subscribe((keyresult) => {
        expect(keyresult.title).toEqual(initKeyResult.title);
        expect(keyresult.description).toEqual(initKeyResult.description);
        expect(keyresult.basicValue).toEqual(initKeyResult.basicValue);
        expect(keyresult.objectiveId).toEqual(1);
      });
    });

    test('should have 3 right titles', () => {
      const titles = fixture.debugElement.queryAll(By.css('.title'));
      expect(titles.length).toEqual(3);
      expect(titles[0].nativeElement.textContent).toContain(
        'Keyresult hinzufügen'
      );
      expect(titles[1].nativeElement.textContent).toContain(
        'Objective Beschreibung'
      );
      expect(titles[2].nativeElement.textContent).toContain('Details');
    });

    test('should have objective title', () => {
      const objectiveTitle = fixture.debugElement.query(
        By.css('.objective-title')
      );
      expect(objectiveTitle.nativeElement.textContent).toContain(
        'Wir wollen unseren Umsatz verdoppeln'
      );
    });

    test('should have objective teamname', () => {
      const teamNameTitle = fixture.debugElement.query(
        By.css('.teamname-title')
      );
      expect(teamNameTitle.nativeElement.textContent).toEqual('Team');
      const objectiveTeamName = fixture.debugElement.query(
        By.css('.objective-teamname')
      );
      expect(objectiveTeamName.nativeElement.textContent).toContain(
        'Team Name'
      );
    });

    test('should have objective description', () => {
      const teamNameTitle = fixture.debugElement.query(
        By.css('.description-title')
      );
      expect(teamNameTitle.nativeElement.textContent).toEqual('Description');
      const objectiveTeamName = fixture.debugElement.query(
        By.css('.objective-description')
      );
      expect(objectiveTeamName.nativeElement.textContent).toContain(
        'Description'
      );
    });

    test('should have objective quarter', () => {
      const teamNameTitle = fixture.debugElement.query(
        By.css('.quarter-title')
      );
      expect(teamNameTitle.nativeElement.textContent).toEqual('Zyklus');
      const objectiveTeamName = fixture.debugElement.query(
        By.css('.objective-quarter')
      );
      expect(objectiveTeamName.nativeElement.textContent).toContain(
        'GJ 2022-3'
      );
    });

    test('should have objective quarter', () => {
      const teamNameTitle = fixture.debugElement.query(
        By.css('.quarter-title')
      );
      expect(teamNameTitle.nativeElement.textContent).toEqual('Zyklus');
      const objectiveTeamName = fixture.debugElement.query(
        By.css('.objective-quarter')
      );
      expect(objectiveTeamName.nativeElement.textContent).toContain(
        'GJ 2022-3'
      );
    });

    test('should save new keyresult', () => {
      // Fill form group and submit it
    });
  });

  describe('KeyresultFormComponent with no id in url', () => {
    beforeEach(() => {
      mockUserService.getUsers.mockReturnValue(userList);
      mockObjectiveService.getObjectiveById.mockReturnValue(objective);
      mockGetNumerOrNull.getNumberOrNull.mockReturnValue(null);

      TestBed.configureTestingModule({
        declarations: [KeyresultFormComponent],
        imports: [
          RouterTestingModule,
          KeyresultModule,
          HttpClientTestingModule,
          NoopAnimationsModule,
        ],
        providers: [
          { provide: UserService, useValue: mockUserService },
          { provide: KeyResultService, useValue: mockKeyResultService },
          { provide: ObjectiveService, useValue: mockObjectiveService },
          {
            provide: ActivatedRoute,
            useValue: {
              paramMap: of(
                convertToParamMap({ objectiveId: null, keyresultId: null })
              ),
            },
          },
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(KeyresultFormComponent);
      component = fixture.componentInstance;
    });

    afterEach(() => {
      mockUserService.getUsers.mockReset();
      mockObjectiveService.getObjectiveById.mockReset();
      mockGetNumerOrNull.getNumberOrNull.mockReset();
    });

    xtest('should create', (done) => {
      expect(component.ngOnInit()).toThrowError(
        "Error: Objective with Idnulldoesn't exist"
      );

      component.objective$.subscribe((objective) => {
        expect(mockGetNumerOrNull).toHaveBeenCalledTimes(1);
        expect(mockGetNumerOrNull).toHaveBeenCalledWith(null);
        expect(mockObjectiveService.getObjectiveById).toHaveBeenCalledTimes(0);
        done();
      });
    });
  });
});
