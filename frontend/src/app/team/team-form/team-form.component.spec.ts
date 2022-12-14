import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamFormComponent } from './team-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { Team } from '../../shared/services/team.service';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

let component: TeamFormComponent;
let fixture: ComponentFixture<TeamFormComponent>;
let team: Team = {
  id: 1,
  name: 'Team1',
};

describe('TeamFormComponent', () => {
  describe('Create new Team', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [
          HttpClientTestingModule,
          RouterTestingModule,
          FormsModule,
          ReactiveFormsModule,
        ],
        declarations: [TeamFormComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(TeamFormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test.skip('should isCreating set on true default', () => {
      // expect(component.isCreating).toEqual(true);
    });

    test.skip('should have right title', () => {
      const heading = fixture.nativeElement.querySelector('p').textContent;
      expect(heading).toContain('Team erstellen');
    });

    test.skip('should have 1 input field with right placeholder', () => {
      const inputFiled = fixture.debugElement.query(By.css('.teamname-input'));

      expect(inputFiled.nativeElement.placeholder).toEqual('Team Name');
      expect(inputFiled.nativeElement.value).toEqual('');
    });

    test.skip('should have 2 buttons', () => {
      const buttons = fixture.debugElement.queryAll(By.css('button'));
      expect(buttons.length).toEqual(2);

      expect(buttons[0].nativeElement.textContent).toContain('Abbrechen');
      expect(buttons[1].nativeElement.textContent).toContain('Speichern');
    });

    test.skip('should right dis- and enable button', () => {
      const buttons = fixture.debugElement.query(By.css('.create-button'));
      const inputField = fixture.debugElement.query(By.css('.teamname-input'));

      component.teamForm.get('teamName')?.setValue('');
      fixture.detectChanges();

      expect(component.teamForm.valid).toBeFalsy();
      expect(inputField.nativeElement.value).toEqual('');
      expect(buttons.nativeElement.disabled).toEqual(true);

      component.teamForm.get('teamName')?.setValue('Team Name 1');
      fixture.detectChanges();

      expect(component.teamForm.valid).toBeTruthy();
      expect(inputField.nativeElement.value).toEqual('Team Name 1');
      expect(buttons.nativeElement.disabled).toEqual(false);
    });
  });

  describe('Edit existing team', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [
          HttpClientTestingModule,
          RouterTestingModule,
          FormsModule,
          ReactiveFormsModule,
        ],
        declarations: [TeamFormComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { params: { id: 1 } } },
          },
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(TeamFormComponent);
      component = fixture.componentInstance;

      window.history.pushState(team, '');
      component.teamObject = of(team);

      fixture.detectChanges();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test.skip('should isCreating set on false', () => {
      // expect(component.isCreating).toEqual(false);
    });

    test.skip('should have right title', () => {
      const heading = fixture.nativeElement.querySelector('p').textContent;
      expect(heading).toContain('Team Name bearbeiten');
    });

    test.skip('should have 1 input field with right placeholder', () => {
      const inputFileds = fixture.debugElement.query(By.css('.teamname-input'));

      expect(inputFileds.nativeElement.placeholder).toContain('Team Name');
      expect(inputFileds.nativeElement.value).toEqual('Team1');
    });

    test.skip('should have 2 buttons', () => {
      const buttons = fixture.debugElement.queryAll(By.css('button'));
      expect(buttons.length).toEqual(2);

      expect(buttons[0].nativeElement.textContent).toEqual(' Abbrechen ');
      expect(buttons[1].nativeElement.textContent).toContain(
        'Änderungen speichern'
      );
    });

    test.skip('should right dis- and enable button', () => {
      const buttons = fixture.debugElement.query(By.css('.create-button'));
      const inputField = fixture.debugElement.query(By.css('.teamname-input'));

      expect(component.teamForm.valid).toBeTruthy();
      expect(inputField.nativeElement.value).toEqual('Team1');
      expect(buttons.nativeElement.disabled).toEqual(false);

      component.teamForm.get('teamName')?.setValue('');
      fixture.detectChanges();

      expect(component.teamForm.valid).toBeFalsy();
      expect(inputField.nativeElement.value).toEqual('');
      expect(buttons.nativeElement.disabled).toEqual(true);
    });
  });
});
