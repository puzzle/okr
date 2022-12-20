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
import { OkrQuarter, Team } from '../../shared/services/team.service';
import { MatIconModule } from '@angular/material/icon';
import { User } from '../../shared/services/user.service';

describe('ObjectiveFormComponent', () => {
  let component: ObjectiveFormComponent;
  let fixture: ComponentFixture<ObjectiveFormComponent>;
  let objective = {
    id: 1,
    title: '',
    description: '',
    teamId: 1,
    quarterId: 1,
    ownerId: 1,
    ownerFirstname: '',
    ownerLastname: '',
    quarterYear: 2022,
    quarterNumber: 1,
    teamName: '',
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
  let quarters: OkrQuarter[] = [
    { quarter: '1' },
    { quarter: '2' },
    { quarter: '3' },
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
      component.objectives$ = of(objective);
      component.teams$ = of(teams);
      fixture.detectChanges();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should have right title', () => {
      const heading = fixture.nativeElement.querySelector(
        '.fs-2.heading-label'
      ).textContent;
      expect(heading).toContain('Objective hinzufügen');
    });

    test('should have input team dopdown', () => {
      const select = fixture.debugElement.query(
        By.css('mat-select[formControlName="teamId"]')
      )!.nativeElement;
      component.teams$ = of(teams);
      fixture.detectChanges();

      select.click();
      fixture.detectChanges();

      const selectOptions = fixture.debugElement.queryAll(By.css('mat-option'));
      expect(selectOptions.length).toEqual(3);
    });

    test('should have inputfield named Titel', () => {
      const input = fixture.debugElement.query(
        By.css('input[formControlName="title"]')
      )!.nativeElement;

      expect(input.placeholder).toEqual('Titel');
    });

    test('should have inputfield named description', () => {
      const input = fixture.debugElement.query(
        By.css('textarea[formControlName="description"]')
      )!.nativeElement;

      expect(input.placeholder).toEqual('Beschreibung');
    });

    test('should have besitzer dropdown with values', () => {
      const input = fixture.debugElement.query(
        By.css('mat-select[formControlName="ownerId"]')
      )!.nativeElement;

      component.users$ = of(users);
      fixture.detectChanges();

      input.click();
      fixture.detectChanges();

      const selectOptions = fixture.debugElement.queryAll(By.css('mat-option'));
      expect(selectOptions.length).toEqual(4);
    });

    test('should have Zyklus dropdown with values', () => {
      const input = fixture.debugElement.query(
        By.css('mat-select[formControlName="ownerId"]')
      )!.nativeElement;

      component.quarterList = quarters;
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
      expect(buttons[1].nativeElement.textContent).toContain('Speichern');
    });

    test('should right dis- and enable button', () => {
      const buttons = fixture.debugElement.query(
        By.css('button[type="submit"]')
      );
      const inputField = fixture.debugElement.query(
        By.css('input[formControlName="name"]')
      );

      // component.objectiveForm.get('name')?.setValue('');
      fixture.detectChanges();

      expect(component.objectiveForm.valid).toBeFalsy();
      expect(inputField.nativeElement.value).toEqual('');
      expect(buttons.nativeElement.disabled).toEqual(true);

      // component.objectiveForm.get('name')?.setValue('Team Name 1');
      fixture.detectChanges();
      expect(component.objectiveForm.valid).toBeTruthy();
      expect(inputField.nativeElement.value).toEqual('Team Name 1');
      expect(buttons.nativeElement.disabled).toEqual(false);
    });
  });
});
