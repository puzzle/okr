import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamFormComponent } from './team-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { Team } from '../../shared/services/team.service';
import { ActivatedRoute } from '@angular/router';

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
        imports: [HttpClientTestingModule, RouterTestingModule, FormsModule],
        declarations: [TeamFormComponent],
      }).compileComponents();

      fixture = TestBed.createComponent(TeamFormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should isCreating set on true default', () => {
      expect(component.isCreating).toEqual(true);
    });

    test('should have right title', () => {
      const heading = fixture.nativeElement.querySelector('p').textContent;
      expect(heading).toEqual('Team erstellen');
    });

    test('should have 1 input filed with right placeholder', () => {
      const inputFileds = fixture.debugElement.queryAll(By.css('input'));
      expect(inputFileds.length).toEqual(1);

      expect(inputFileds[0].nativeElement.placeholder).toEqual(
        'Team hinzufügen'
      );
      expect(inputFileds[0].nativeElement.value).toEqual('');
    });

    test('should have 2 buttons', () => {
      const buttons = fixture.debugElement.queryAll(By.css('button'));
      expect(buttons.length).toEqual(2);

      expect(buttons[0].nativeElement.textContent).toEqual(' Abbrechen ');
      expect(buttons[1].nativeElement.textContent).toEqual(' Speichern ');
    });

    test('should right dis- and enable button', () => {
      const buttons = fixture.debugElement.queryAll(By.css('button'));
      const inputField = fixture.debugElement.query(By.css('.create-input'));
      inputField.nativeElement.value = '';
      expect(inputField.nativeElement.value).toEqual('');

      expect(buttons[1].nativeElement.disabled).toEqual(false);

      inputField.nativeElement.value = 'Team 1';

      expect(buttons[1].nativeElement.disabled).toEqual(true);
    });
  });

  describe('Edit existing team', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule, RouterTestingModule, FormsModule],
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
      component.teamObject = team;
      component.teamname = team.name;

      fixture.detectChanges();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should isCreating set on false', () => {
      expect(component.isCreating).toEqual(false);
    });

    test('should have right title', () => {
      const heading = fixture.nativeElement.querySelector('p').textContent;
      expect(heading).toEqual('Team Name bearbeiten');
    });

    test('should have 1 input field with right placeholder', () => {
      const inputFileds = fixture.debugElement.queryAll(By.css('input'));
      expect(inputFileds.length).toEqual(1);

      expect(inputFileds[0].nativeElement.placeholder).toEqual(
        'Team bearbeiten'
      );
      expect(inputFileds[0].nativeElement.value).toEqual('Team1');
    });

    test('should have 2 buttons', () => {
      const buttons = fixture.debugElement.queryAll(By.css('button'));
      expect(buttons.length).toEqual(2);

      expect(buttons[0].nativeElement.textContent).toEqual(' Abbrechen ');
      expect(buttons[1].nativeElement.textContent).toEqual(
        ' Änderungen speichern '
      );
    });

    test('should right dis- and enable button', () => {
      const buttons = fixture.debugElement.queryAll(By.css('button'));
      const inputField = fixture.debugElement.query(By.css('.edit-input'));

      expect(inputField.nativeElement.value).toBeNaN();
      expect(buttons[1].nativeElement.getAttribute('disabled')).toEqual(null);

      inputField.nativeElement.value = '';

      expect(inputField.nativeElement.value).toEqual('');

      expect(buttons[1].nativeElement.getAttribute('disabled')).toEqual('');

      inputField.nativeElement.value = 'Team 1';

      expect(buttons[1].nativeElement.getAttribute('disabled')).toEqual(null);
    });
  });
});
