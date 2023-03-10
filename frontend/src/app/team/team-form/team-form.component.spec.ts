import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamFormComponent } from './team-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { Team, TeamService } from '../../shared/services/team.service';
import { ActivatedRoute } from '@angular/router';
import { of, throwError } from 'rxjs';
import { MatInputModule } from '@angular/material/input';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import * as teamsData from '../../shared/testing/mock-data/teams.json';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';

let component: TeamFormComponent;
let fixture: ComponentFixture<TeamFormComponent>;

let team: Team = teamsData.teams[0];

const mockToastrService = {
  success: jest.fn(),
  error: jest.fn(),
};

const mockTeamService = {
  save: jest.fn(),
  getTeam: jest.fn(),
  getInitTeam: jest.fn(),
};

describe('TeamFormComponent', () => {
  describe('Create new Team', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [
          ToastrModule.forRoot(),
          NoopAnimationsModule,
          HttpClientTestingModule,
          RouterTestingModule,
          ReactiveFormsModule,
          MatInputModule,
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

    test('should have right title', () => {
      const heading = fixture.nativeElement.querySelector('p').textContent;
      expect(heading).toContain('Team erstellen');
    });

    test('should have 1 input field with right placeholder', () => {
      const inputFiled = fixture.debugElement.query(By.css('input[formControlName="name"]'));

      expect(inputFiled.nativeElement.placeholder).toEqual('z.B. DevTree');
      expect(inputFiled.nativeElement.value).toEqual('');
    });

    test('should have 2 buttons', () => {
      const buttons = fixture.debugElement.queryAll(By.css('button'));
      expect(buttons.length).toEqual(2);

      expect(buttons[0].nativeElement.textContent).toContain('Abbrechen');
      expect(buttons[1].nativeElement.textContent).toContain('Speichern');
    });

    test('should right dis- and enable button', () => {
      const buttons = fixture.debugElement.query(By.css('button[type="submit"]'));
      const inputField = fixture.debugElement.query(By.css('input[formControlName="name"]'));

      component.teamForm.get('name')?.setValue('');
      fixture.detectChanges();

      expect(component.teamForm.valid).toBeFalsy();
      expect(inputField.nativeElement.value).toEqual('');
      expect(buttons.nativeElement.disabled).toEqual(true);

      component.teamForm.get('name')?.setValue('Team Name 1');
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
          ToastrModule.forRoot(),
          NoopAnimationsModule,
          HttpClientTestingModule,
          RouterTestingModule,
          ReactiveFormsModule,
          MatInputModule,
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
      component.teamObject = of(team);
      fixture.detectChanges();
    });

    test('should create', () => {
      expect(component).toBeTruthy();
    });

    test('should have right title', () => {
      const heading = fixture.nativeElement.querySelector('p').textContent;
      expect(heading).toContain('Team Name bearbeiten');
    });

    test('should have 1 input field with right placeholder', () => {
      const inputFields = fixture.debugElement.query(By.css('input[formControlName="name"]'));

      component.teamObject = of(team);
      fixture.detectChanges();

      expect(inputFields.nativeElement.placeholder).toContain('z.B. DevTree');
      expect(inputFields.nativeElement.value).toEqual('Team 1');
    });

    test('should have 2 buttons', () => {
      const buttons = fixture.debugElement.queryAll(By.css('button'));
      expect(buttons.length).toEqual(2);

      expect(buttons[0].nativeElement.textContent).toEqual(' Abbrechen ');
      expect(buttons[1].nativeElement.textContent).toContain('Änderungen speichern');
    });

    test('should right dis- and enable button', () => {
      const buttons = fixture.debugElement.query(By.css('.create-button'));
      const inputField = fixture.debugElement.query(By.css('input[formControlName="name"]'));

      component.teamForm.get('name')?.setValue(team.name);
      fixture.detectChanges();
      expect(component.teamForm.valid).toBeTruthy();
      expect(inputField.nativeElement.value).toEqual('Team 1');
      expect(buttons.nativeElement.disabled).toEqual(false);

      component.teamForm.get('name')?.setValue('');
      fixture.detectChanges();

      expect(component.teamForm.valid).toBeFalsy();
      expect(inputField.nativeElement.value).toEqual('');
      expect(buttons.nativeElement.disabled).toEqual(true);
    });
  });

  describe('check if component makes call to toastrservice', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [
          ToastrModule.forRoot(),
          NoopAnimationsModule,
          HttpClientTestingModule,
          RouterTestingModule,
          ReactiveFormsModule,
          MatInputModule,
        ],
        declarations: [TeamFormComponent],
        providers: [
          { provide: ToastrService, useValue: mockToastrService },
          { provide: TeamService, useValue: mockTeamService },
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(TeamFormComponent);
      component = fixture.componentInstance;

      component.teamForm.get('name')?.setValue(team.name);
      fixture.detectChanges();
    });

    afterEach(() => {
      mockToastrService.success.mockReset();
      mockToastrService.error.mockReset();
    });

    test('should display success notification', () => {
      mockTeamService.save.mockReturnValue(of(team));

      const createbutton = fixture.debugElement.query(By.css('.create-button'));
      createbutton.nativeElement.click();
      fixture.detectChanges();

      expect(mockToastrService.success).toHaveBeenCalledTimes(1);
      expect(mockToastrService.error).not.toHaveBeenCalled();
      expect(mockToastrService.success).toHaveBeenCalledWith('', 'Team gespeichert!', { timeOut: 5000 });
    });

    test('should display error notification', () => {
      mockTeamService.save.mockReturnValue(
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
        'Team konnte nicht gespeichert werden!',
        'Fehlerstatus: 500',
        { timeOut: 5000 }
      );
    });
  });
});
