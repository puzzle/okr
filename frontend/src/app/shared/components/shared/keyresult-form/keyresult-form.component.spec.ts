import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyresultFormComponent } from './keyresult-form.component';
import { User, UserService } from '../../../services/user.service';
import {
  KeyResultMeasure,
  KeyResultService,
} from '../../../services/key-result.service';
import { Observable, of, throwError } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import {
  Objective,
  ObjectiveService,
} from '../../../services/objective.service';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { By } from '@angular/platform-browser';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSelectHarness } from '@angular/material/select/testing';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import * as keyresultData from '../../../testing/mock-data/keyresults.json';
import * as usersData from '../../../testing/mock-data/users.json';
import * as objectivesData from '../../../testing/mock-data/objectives.json';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { SharedModule } from '../shared.module';
import { TranslateTestingModule } from 'ngx-translate-testing';

describe('KeyresultFormComponent', () => {
  let component: KeyresultFormComponent;
  let fixture: ComponentFixture<KeyresultFormComponent>;

  let keyResult: Observable<KeyResultMeasure> = of(keyresultData.keyresults[0]);

  let objective: Observable<Objective> = of(objectivesData.objectives[0]);

  let userList: Observable<User[]> = of(usersData.users);

  let initKeyResult: KeyResultMeasure = keyresultData.initKeyResult;

  let createKeyResultObject: KeyResultMeasure =
    keyresultData.createKeyResultObject;

  let createKeyResultForm = new FormGroup({
    title: new FormControl<string>('Key Result 1', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(250),
    ]),
    unit: new FormControl<string>('PERCENT', [Validators.required]),
    expectedEvolution: new FormControl<string>('INCREASE'),
    basicValue: new FormControl<number>(0, Validators.required),
    targetValue: new FormControl<number>(100, Validators.required),
    description: new FormControl<string>('This is a description', [
      Validators.maxLength(4096),
    ]),
    ownerId: new FormControl<number | null>(2, [
      Validators.required,
      Validators.nullValidator,
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

  const mockToastrService = {
    success: jest.fn(),
    error: jest.fn(),
  };

  let loader: HarnessLoader;

  describe('KeyresultFormComponent Edit Key Result', () => {
    beforeEach(() => {
      mockUserService.getUsers.mockReturnValue(userList);
      mockKeyResultService.getKeyResultById.mockReturnValue(keyResult);
      mockKeyResultService.getInitKeyResult.mockReturnValue(initKeyResult);
      mockObjectiveService.getObjectiveById.mockReturnValue(objective);
      mockGetNumerOrNull.getNumberOrNull.mockReturnValue(1);

      TestBed.configureTestingModule({
        declarations: [KeyresultFormComponent],
        imports: [
          ToastrModule.forRoot(),
          TranslateTestingModule.withTranslations({
            de: require('../../../../../assets/i18n/de.json'),
          }),
          RouterTestingModule,
          SharedModule,
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
      loader = TestbedHarnessEnvironment.loader(fixture);
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
        'Key Result bearbeiten'
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
        ' Objective 1 '
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
      expect(objectiveTeamName.nativeElement.textContent).toContain(' Team 1 ');
    });

    test('should have objective description', () => {
      const teamNameTitle = fixture.debugElement.query(
        By.css('.description-title')
      );
      expect(teamNameTitle.nativeElement.textContent).toEqual('Beschreibung');
      const objectiveTeamName = fixture.debugElement.query(
        By.css('.objective-description')
      );
      expect(objectiveTeamName.nativeElement.textContent).toContain(
        ' This is the description of Objective 1 '
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
        'GJ 22/23-Q1'
      );
    });

    test('should set Key Result title in input field and set input invalid when empty value', () => {
      const titleinputfield = fixture.debugElement.query(
        By.css('.title-input')
      );
      expect(titleinputfield.nativeElement.value).toContain('Key Result 1');
      expect(component.keyResultForm.get('title')?.valid).toBeTruthy();

      component.keyResultForm.get('title')?.setValue('');
      titleinputfield.nativeElement.value = '';
      fixture.detectChanges();

      expect(component.keyResultForm.valid).toBeFalsy();
    });

    test('should set ExpectedEvolution to NONE', async () => {
      const select = await loader.getHarness(
        MatSelectHarness.with({
          selector: 'mat-select[formControlName="expectedEvolution"]',
        })
      );
      await select.open();
      const bugOption = await select.getOptions({ text: 'NONE' });
      await bugOption[0].click();

      expect(await select.getValueText()).toEqual('');
      expect(
        component.keyResultForm.controls['expectedEvolution'].value
      ).toBeNull();
      expect(component.keyResultForm.valid).toBeTruthy();
    });

    test('should not have Key Result unit drop down', () => {
      const unitMatSelect = fixture.debugElement.query(
        By.css('mat-select[formControlName="unit"]')
      );
      expect(unitMatSelect).toBeNull();
    });

    test('should set keyresult evolution in mat select and set it new on item change', async () => {
      const select = await loader.getHarness(
        MatSelectHarness.with({
          selector: 'mat-select[formControlName="expectedEvolution"]',
        })
      );
      expect(await select.getValueText()).toEqual('ERHÖHT');

      await select.open();
      const bugOption = await select.getOptions({ text: 'VERMINDERT' });
      await bugOption[0].click();

      expect(await select.getValueText()).toEqual('VERMINDERT');
      expect(await select.isDisabled()).toBeFalsy();
      expect(await select.isOpen()).toBeFalsy();
    });

    test('should set Key Result basicvalue in input field and set input invalid when empty value', () => {
      const basicValueIputfield = fixture.debugElement.query(
        By.css('.basicValue-input')
      );
      expect(basicValueIputfield.nativeElement.value).toContain('0');
      expect(basicValueIputfield.nativeElement.placeholder).toContain('0');
      expect(component.keyResultForm.valid).toBeTruthy();

      component.keyResultForm.get('basicValue')?.setValue(null);
      basicValueIputfield.nativeElement.value = '';
      fixture.detectChanges();

      expect(component.keyResultForm.valid).toBeFalsy();
    });

    test('should set Key Result targetValue in input field and set input invalid when empty value', () => {
      const targetValueIputfield = fixture.debugElement.query(
        By.css('.targetValue-input')
      );
      expect(targetValueIputfield.nativeElement.value).toContain('100');
      expect(targetValueIputfield.nativeElement.placeholder).toContain('0');
      expect(component.keyResultForm.valid).toBeTruthy();

      component.keyResultForm.get('targetValue')?.setValue(null);
      targetValueIputfield.nativeElement.value = '';
      fixture.detectChanges();

      expect(component.keyResultForm.valid).toBeFalsy();
    });

    test('should set Key Result description in text area and dont set input invalid when empty value', () => {
      const descriptionTextArea = fixture.debugElement.query(
        By.css('.description-textarea')
      );
      expect(descriptionTextArea.nativeElement.value).toContain(
        'This is the description'
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

    test('should set Key Result owner in mat select and set it new on item change', async () => {
      const select = await loader.getHarness(
        MatSelectHarness.with({
          selector: 'mat-select[formControlName="ownerId"]',
        })
      );
      expect(await select.getValueText()).toEqual('Alice Wunderland');

      await select.open();
      const bugOption = await select.getOptions({ text: 'Paco Egiman' });
      await bugOption[0].click();

      expect(await select.getValueText()).toEqual('Paco Egiman');
      expect(await select.isDisabled()).toBeFalsy();
      expect(await select.isOpen()).toBeFalsy();
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

    test('should save Key Result', () => {
      const createbutton = fixture.debugElement.query(By.css('.create-button'));

      expect(component.keyResultForm.valid).toBeTruthy();
      expect(createbutton.nativeElement.disabled).toEqual(false);

      createbutton.nativeElement.click();
      fixture.detectChanges();

      keyResult.subscribe((keyresult) => {
        expect(mockKeyResultService.saveKeyresult).toHaveBeenCalledWith(
          keyresult,
          false
        );
        expect(mockKeyResultService.saveKeyresult).toHaveBeenCalledTimes(1);
      });
    });

    test('should have label for Key Result unit', () => {
      const unitLabel = fixture.debugElement.query(By.css('.keyresult-unit'));
      expect(unitLabel.nativeElement.textContent).toContain('PROZENT');
    });
  });

  describe('KeyresultFormComponent Create Key Result', () => {
    beforeEach(() => {
      mockUserService.getUsers.mockReturnValue(userList);
      mockObjectiveService.getObjectiveById.mockReturnValue(objective);
      mockKeyResultService.getInitKeyResult.mockReturnValue(initKeyResult);
      mockKeyResultService.saveKeyresult.mockReturnValue(keyResult);
      mockGetNumerOrNull.getNumberOrNull.mockReturnValue(1);

      TestBed.configureTestingModule({
        declarations: [KeyresultFormComponent],
        imports: [
          ToastrModule.forRoot(),
          TranslateTestingModule.withTranslations({
            de: require('../../../../../assets/i18n/de.json'),
          }),
          RouterTestingModule,
          SharedModule,
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
      loader = TestbedHarnessEnvironment.loader(fixture);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      mockUserService.getUsers.mockReset();
      mockObjectiveService.getObjectiveById.mockReset();
      mockKeyResultService.getInitKeyResult.mockReset();
      mockKeyResultService.saveKeyresult.mockReset();
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

    test('should set Key Result unit in mat select and set it new on item change', async () => {
      const select = await loader.getHarness(
        MatSelectHarness.with({
          selector: 'mat-select[formControlName="unit"]',
        })
      );
      expect(await select.getValueText()).toEqual('');

      await select.open();
      const bugOption = await select.getOptions({ text: 'PROZENT' });
      await bugOption[0].click();
      expect(component.keyResultForm.controls['unit'].value).toContain(
        'PERCENT'
      );
    });

    test('should set init Key Result', () => {
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
        'Key Result hinzufügen'
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
        ' Objective 1 '
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
      expect(objectiveTeamName.nativeElement.textContent).toContain(' Team 1 ');
    });

    test('should have objective description', () => {
      const teamNameTitle = fixture.debugElement.query(
        By.css('.description-title')
      );
      expect(teamNameTitle.nativeElement.textContent).toEqual('Beschreibung');
      const objectiveTeamName = fixture.debugElement.query(
        By.css('.objective-description')
      );
      expect(objectiveTeamName.nativeElement.textContent).toContain(
        'This is the description of Objective 1'
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
        'GJ 22/23-Q1'
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
        'GJ 22/23-Q1 '
      );
    });

    test('should be possible to set Key Result unit in mat select when creating', async () => {
      const unitMatSelect = fixture.debugElement.query(
        By.css('mat-select[formControlName="unit"]')
      );
      expect(unitMatSelect).not.toBeNull();

      const select = await loader.getHarness(
        MatSelectHarness.with({
          selector: 'mat-select[formControlName="unit"]',
        })
      );

      await select.open();
      const bugOption = await select.getOptions({ text: 'ZAHL' });
      await bugOption[0].click();

      expect(await select.getValueText()).toEqual('ZAHL');
      expect(await select.isDisabled()).toBeFalsy();
      expect(await select.isOpen()).toBeFalsy();
    });

    test('should be possible to set expected evolution in mat select', async () => {
      const select = await loader.getHarness(
        MatSelectHarness.with({
          selector: 'mat-select[formControlName="expectedEvolution"]',
        })
      );
      expect(await select.getValueText()).toEqual('');

      await select.open();
      const bugOption = await select.getOptions({ text: 'VERMINDERT' });
      await bugOption[0].click();

      expect(await select.getValueText()).toEqual('VERMINDERT');
      expect(await select.isDisabled()).toBeFalsy();
      expect(await select.isOpen()).toBeFalsy();
    });

    test('should be possible to set Key Result owner in mat select', async () => {
      const select = await loader.getHarness(
        MatSelectHarness.with({
          selector: 'mat-select[formControlName="ownerId"]',
        })
      );
      expect(await select.getValueText()).toEqual('');

      await select.open();
      const bugOption = await select.getOptions({ text: 'Paco Egiman' });
      await bugOption[0].click();

      expect(await select.getValueText()).toEqual('Paco Egiman');
      expect(await select.isDisabled()).toBeFalsy();
      expect(await select.isOpen()).toBeFalsy();
    });

    test('should save new Key Result', () => {
      component.keyResultForm = createKeyResultForm;
      fixture.detectChanges();

      const createbutton = fixture.debugElement.query(By.css('.create-button'));
      createbutton.nativeElement.click();
      fixture.detectChanges();

      expect(mockKeyResultService.saveKeyresult).toHaveBeenCalledTimes(1);
      expect(mockKeyResultService.saveKeyresult).toHaveBeenCalledWith(
        createKeyResultObject,
        true
      );
    });

    test('should be invalid form if unit is changed and values do not match regex', async () => {
      //Set Values
      component.keyResultForm.controls['ownerId'].setValue(1);
      component.keyResultForm.controls['title'].setValue('Title');
      component.keyResultForm.controls['expectedEvolution'].setValue(
        'INCREASE'
      );
      component.keyResultForm.controls['description'].setValue('Description');
      component.keyResultForm.controls['targetValue'].setValue(1000);
      component.keyResultForm.controls['basicValue'].setValue(50);

      //Chose PERCENT as unit and check if form is now invalid
      const select = await loader.getHarness(
        MatSelectHarness.with({
          selector: 'mat-select[formControlName="unit"]',
        })
      );
      await select.open();
      let bugOption = await select.getOptions({ text: 'PROZENT' });
      await bugOption[0].click();
      expect(component.keyResultForm.valid).toBeFalsy();

      //Change unit to binary which accepts every number
      await select.open();
      bugOption = await select.getOptions({ text: 'BINÄR' });
      await bugOption[0].click();
      expect(component.keyResultForm.valid).toBeTruthy();
    });

    test('should be valid form if targetValue or basicValue is a double', async () => {
      //Set Values
      component.keyResultForm.controls['ownerId'].setValue(3);
      component.keyResultForm.controls['title'].setValue('Title');
      component.keyResultForm.controls['expectedEvolution'].setValue(
        'INCREASE'
      );
      component.keyResultForm.controls['description'].setValue('Description');
      component.keyResultForm.controls['targetValue'].setValue(10.5);
      component.keyResultForm.controls['basicValue'].setValue(57.8);

      //Chose unit and check validation of form
      const select = await loader.getHarness(
        MatSelectHarness.with({
          selector: 'mat-select[formControlName="unit"]',
        })
      );
      await select.open();
      let bugOption = await select.getOptions({ text: 'ZAHL' });
      await bugOption[0].click();
      expect(component.keyResultForm.valid).toBeTruthy();
    });

    test('should be valid form if targetValue or basicValue is a double and unit is percent', async () => {
      //Set Values
      component.keyResultForm.controls['title'].setValue('KeyResult');
      component.keyResultForm.controls['targetValue'].setValue(50.6);
      component.keyResultForm.controls['basicValue'].setValue(23.5);

      //Chose unit and check validation of form
      const select = await loader.getHarness(
        MatSelectHarness.with({
          selector: 'mat-select[formControlName="unit"]',
        })
      );
      await select.open();
      let bugOption = await select.getOptions({ text: 'PROZENT' });
      await bugOption[0].click();
      expect(component.keyResultForm.valid).toBeTruthy();
    });

    test('should be invalid form if targetValue or basicValue have more numbers behind the comma than 1', async () => {
      //Set Values
      component.keyResultForm.controls['title'].setValue('KeyResult');
      component.keyResultForm.controls['targetValue'].setValue(50.667);
      component.keyResultForm.controls['basicValue'].setValue(23.53);

      //Chose unit and check validation of form
      const select = await loader.getHarness(
        MatSelectHarness.with({
          selector: 'mat-select[formControlName="unit"]',
        })
      );
      await select.open();
      let bugOption = await select.getOptions({ text: 'ZAHL' });
      await bugOption[0].click();
      expect(component.keyResultForm.valid).toBeFalsy();
    });
  });

  describe('KeyresultFormComponent with no id in url', () => {
    beforeEach(() => {
      mockObjectiveService.getObjectiveById.mockReturnValue(null);

      TestBed.configureTestingModule({
        declarations: [KeyresultFormComponent],
        imports: [
          ToastrModule.forRoot(),
          RouterTestingModule,
          SharedModule,
          HttpClientTestingModule,
          NoopAnimationsModule,
        ],
        providers: [
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

    test('should not create', () => {
      expect(mockObjectiveService.getObjectiveById).toHaveBeenCalledTimes(0);
    });
  });

  describe('Check if component makes call to toastrservice', () => {
    beforeEach(() => {
      //Standard mocks to create keyresult-form
      mockUserService.getUsers.mockReturnValue(userList);
      mockKeyResultService.getKeyResultById.mockReturnValue(keyResult);
      mockKeyResultService.getInitKeyResult.mockReturnValue(initKeyResult);

      TestBed.configureTestingModule({
        declarations: [KeyresultFormComponent],
        imports: [
          ToastrModule.forRoot(),
          TranslateTestingModule.withTranslations({
            de: require('../../../../../assets/i18n/de.json'),
          }),
          RouterTestingModule,
          SharedModule,
          HttpClientTestingModule,
          NoopAnimationsModule,
        ],
        providers: [
          { provide: ToastrService, useValue: mockToastrService },
          { provide: UserService, useValue: mockUserService },
          { provide: KeyResultService, useValue: mockKeyResultService },
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
      loader = TestbedHarnessEnvironment.loader(fixture);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      //ToastrService Reset
      mockToastrService.success.mockReset();
      mockToastrService.error.mockReset();

      //Standard Services Reset
      mockUserService.getUsers.mockReset();
      mockKeyResultService.getKeyResultById.mockReset();
      mockKeyResultService.getInitKeyResult.mockReset();
    });

    test('should display success notification', () => {
      //Return Keyresult to trigger success notification of ToastrService
      mockKeyResultService.saveKeyresult.mockReturnValue(keyResult);

      const createbutton = fixture.debugElement.query(By.css('.create-button'));
      createbutton.nativeElement.click();
      fixture.detectChanges();
      expect(mockToastrService.success).toHaveBeenCalledTimes(1);
      expect(mockToastrService.error).not.toHaveBeenCalled();
      expect(mockToastrService.success).toHaveBeenCalledWith(
        '',
        'Key Result gespeichert!',
        { timeOut: 5000 }
      );
    });

    test('should display error notification', () => {
      //Return Error to trigger error notification of ToastrService
      mockKeyResultService.saveKeyresult.mockReturnValue(
        throwError(
          () =>
            new HttpErrorResponse({
              status: 500,
              error: { message: 'Something went wrong' },
            })
        )
      );

      const createbutton = fixture.debugElement.query(By.css('.create-button'));
      createbutton.nativeElement.click();
      fixture.detectChanges();
      expect(mockToastrService.error).toHaveBeenCalledTimes(1);
      expect(mockToastrService.success).not.toHaveBeenCalled();
      expect(mockToastrService.error).toHaveBeenCalledWith(
        'Key Result konnte nicht gespeichert werden!',
        'Fehlerstatus: 500',
        { timeOut: 5000 }
      );
    });
  });
});
