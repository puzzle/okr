import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveFormComponent } from './objective-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatInputModule } from '@angular/material/input';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { MatSelectModule } from '@angular/material/select';
import { Team } from '../../shared/services/team.service';
import { MatIconModule } from '@angular/material/icon';
import { User } from '../../shared/services/user.service';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { Objective } from '../../shared/services/objective.service';
import { Quarter } from '../../shared/services/quarter.service';

describe('ObjectiveFormComponent', () => {
  let component: ObjectiveFormComponent;
  let fixture: ComponentFixture<ObjectiveFormComponent>;
  let objective: Objective = {
    id: 1,
    title: 'This is a title',
    description: 'This is the description',
    teamId: 2,
    quarterId: 3,
    ownerId: 4,
    ownerFirstname: '',
    ownerLastname: '',
    quarterLabel: '',
    teamName: 'teamName',
    progress: 0,
    created: '',
  };

  let teams: Team[] = [
    { id: 1, name: 'id1' },
    { id: 2, name: 'id2' },
    { id: 3, name: 'id3' },
  ];

  let users: User[] = [
    {
      id: 0,
      firstname: 'firstname0',
      lastname: 'lastname0',
      email: 'email0',
      username: 'username0',
    },
    {
      id: 0,
      firstname: 'firstname1',
      lastname: 'lastname1',
      email: 'email1',
      username: 'username01',
    },
    {
      id: 0,
      firstname: 'firstname2',
      lastname: 'lastname2',
      email: 'email2',
      username: 'username2',
    },
    {
      id: 0,
      firstname: 'firstname3',
      lastname: 'lastname3',
      email: 'email3',
      username: 'username3',
    },
  ];
  let quarters: Quarter[] = [
    { id: 1, label: 'quarter1' },
    { id: 2, label: 'quarter2' },
    { id: 3, label: 'quarter3' },
  ];

  describe('Create new Objective', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [
          NoopAnimationsModule,
          HttpClientTestingModule,
          RouterTestingModule,
          ReactiveFormsModule,
          MatInputModule,
          MatSelectModule,
          MatIconModule,
        ],
        declarations: [ObjectiveFormComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(ObjectiveFormComponent);
      component = fixture.componentInstance;
      component.teams$ = of(teams);
      fixture.detectChanges();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should have right title', () => {
      fixture.detectChanges();

      const heading = fixture.debugElement.query(By.css('.heading-label'))
        .nativeElement.textContent;

      expect(component.create).toEqual(true);
      expect(heading).toContain('Objective hinzufügen');
    });

    test('should have input team dropdown', () => {
      const select = fixture.debugElement.query(
        By.css('mat-select[formControlName="teamId"]')
      )!.nativeElement;
      component.teams$ = of(teams);
      component.objectiveForm.controls['teamId'].setValue(objective.teamId);
      component.objectives$ = of(objective);
      fixture.detectChanges();

      select.click();
      fixture.detectChanges();

      const selectOptions = fixture.debugElement.queryAll(By.css('mat-option'));
      let value: number = +select.getAttribute('ng-reflect-value');
      expect(selectOptions.length).toEqual(3);
      expect(value).toEqual(objective.teamId);
    });

    test('should have input field named Titel', () => {
      const input = fixture.debugElement.query(
        By.css('input[formControlName="title"]')
      )!.nativeElement;

      component.objectives$ = of(objective);
      component.objectiveForm.controls['title'].setValue(objective.title);
      fixture.detectChanges();

      expect(input.value).toEqual(objective.title);
      expect(input.placeholder).toEqual('Titel');
    });

    test('should have input field named description', () => {
      const input = fixture.debugElement.query(
        By.css('textarea[formControlName="description"]')
      )!.nativeElement;

      expect(input.placeholder).toEqual('Beschreibung');
    });

    test('should have besitzer dropdown with values', () => {
      const select = fixture.debugElement.query(
        By.css('mat-select[formControlName="ownerId"]')
      )!.nativeElement;

      component.users$ = of(users);
      component.objectives$ = of(objective);
      fixture.detectChanges();

      select.click();
      fixture.detectChanges();

      const selectOptions = fixture.debugElement.queryAll(By.css('mat-option'));
      let value: number = +select.getAttribute('ng-reflect-value');

      expect(selectOptions.length).toEqual(4);
      expect(value).toEqual(objective.ownerId);
    });

    //Todo Implement test after quarter service is implemented
    test('should have Zyklus dropdown with values', () => {
      const input = fixture.debugElement.query(
        By.css('mat-select[formControlName="quarterId"]')
      )!.nativeElement;

      component.quarters$ = of(quarters);
      fixture.detectChanges();

      input.click();
      fixture.detectChanges();

      const selectOptions = fixture.debugElement.queryAll(By.css('mat-option'));
      expect(selectOptions.length).toEqual(3);
    });

    test('should have 2 buttons', () => {
      const buttons = fixture.debugElement.queryAll(By.css('button'));
      expect(buttons.length).toEqual(2);

      expect(buttons[0].nativeElement.textContent).toContain('Abbrechen');
      expect(buttons[1].nativeElement.textContent).toContain('Erstellen');
    });

    test('disable button if form is invalid', () => {
      const submit = fixture.debugElement.query(
        By.css('button[type="submit"]')
      );

      expect(component.objectiveForm.valid).toBeFalsy();
      expect(submit.nativeElement.disabled).toEqual(true);
    });

    test('enable button if form is valid', () => {
      const submit = fixture.debugElement.query(
        By.css('button[type="submit"]')
      );

      component.objectiveForm.controls['teamId'].setValue(objective.teamId);
      component.objectiveForm.controls['title'].setValue(objective.title);
      component.objectiveForm.controls['description'].setValue(
        objective.description
      );
      component.objectiveForm.controls['ownerId'].setValue(objective.ownerId);
      component.objectiveForm.controls['quarterId'].setValue(
        objective.quarterId
      );
      fixture.detectChanges();

      expect(component.objectiveForm.valid).toBeTruthy();
      expect(submit.nativeElement.disabled).toEqual(false);
    });
  });

  describe('Edit existing Objective', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [
          NoopAnimationsModule,
          HttpClientTestingModule,
          RouterTestingModule,
          ReactiveFormsModule,
          MatInputModule,
          MatSelectModule,
          MatIconModule,
        ],
        declarations: [ObjectiveFormComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              paramMap: of(convertToParamMap({ objectiveId: 1 })),
            },
          },
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(ObjectiveFormComponent);
      component = fixture.componentInstance;
      component.users$ = of(users);
      component.teams$ = of(teams);
      component.objectives$ = of(objective);
      fixture.detectChanges();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should have right title', () => {
      const heading = fixture.nativeElement.querySelector(
        '.fs-2.heading-label'
      ).textContent;
      expect(heading).toContain('Objective bearbeiten');
    });

    test('input field should contains correct value', () => {
      const select = fixture.debugElement.query(
        By.css('mat-select[formControlName="teamId"]')
      )!.nativeElement;

      component.objectives$ = of(objective);
      fixture.detectChanges();

      let value: number = select.getAttribute('ng-reflect-value');
      expect(value).toEqual(2);
    });

    test('should have input field named Titel', () => {
      const input = fixture.debugElement.query(
        By.css('input[formControlName="title"]')
      )!.nativeElement;

      component.objectives$ = of(objective);
      fixture.detectChanges();

      expect(input.value).toEqual(objective.title);
    });

    test('should have input field named description', () => {
      const input = fixture.debugElement.query(
        By.css('textarea[formControlName="description"]')
      )!.nativeElement;

      component.objectives$ = of(objective);
      fixture.detectChanges();

      expect(input.value).toEqual(objective.description);
    });

    test('should have besitzer dropdown with values', () => {
      const input = fixture.debugElement.query(
        By.css('mat-select[formControlName="ownerId"]')
      )!.nativeElement;

      component.users$ = of(users);
      component.objectives$ = of(objective);
      fixture.detectChanges();

      let value: number = +input.getAttribute('ng-reflect-value');
      expect(value).toEqual(objective.ownerId);
    });

    //Todo Implement test after quarter service is implemented
    test.skip('should have Zyklus dropdown with values', () => {
      const input = fixture.debugElement.query(
        By.css('mat-select[formControlName="zyklusID"]')
      )!.nativeElement;

      component.quarters$ = of(quarters);
      fixture.detectChanges();

      input.click();
      fixture.detectChanges();

      const selectOptions = fixture.debugElement.queryAll(By.css('mat-option'));
      expect(selectOptions.length).toEqual(4);
    });

    test('should have 2 buttons', () => {
      const buttons = fixture.debugElement.queryAll(By.css('button'));
      expect(buttons.length).toEqual(2);

      expect(buttons[0].nativeElement.textContent).toContain('Abbrechen');
      expect(buttons[1].nativeElement.textContent).toContain('Aktualisieren');
    });

    test('enable button if form is valid', () => {
      const submit = fixture.debugElement.query(
        By.css('button[type="submit"]')
      );

      component.objectiveForm.controls['teamId'].setValue(objective.teamId);
      component.objectiveForm.controls['title'].setValue(objective.title);
      component.objectiveForm.controls['description'].setValue(
        objective.description
      );
      component.objectiveForm.controls['ownerId'].setValue(objective.ownerId);
      component.objectiveForm.controls['quarterId'].setValue(
        objective.quarterId
      );
      fixture.detectChanges();

      expect(component.objectiveForm.valid).toBeTruthy();
      expect(submit.nativeElement.disabled).toEqual(false);
    });

    test('disable button if form is invalid', () => {
      const submit = fixture.debugElement.query(
        By.css('button[type="submit"]')
      );

      component.objectiveForm.controls['teamId'].setValue(objective.teamId);
      component.objectiveForm.controls['title'].setValue(objective.title);
      component.objectiveForm.controls['description'].setValue(
        objective.description
      );
      component.objectiveForm.controls['ownerId'].setValue(null);
      component.objectiveForm.controls['quarterId'].setValue(
        objective.quarterId
      );
      fixture.detectChanges();

      expect(component.objectiveForm.valid).toBeFalsy();
      expect(submit.nativeElement.disabled).toEqual(true);
    });
  });
});
