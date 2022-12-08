import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import {
  Objective,
  ObjectiveService,
} from '../../shared/services/objective.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { map, Observable, of, switchMap } from 'rxjs';
import { Team, TeamService } from '../../team/team.service';
import { ActivatedRoute, Router } from '@angular/router';
import { User, UserService } from '../../shared/services/user.service';
import { getNumberOrNull } from '../../shared/common';
import { OkrQuarter } from '../../shared/services/team.service';

@Component({
  selector: 'app-objective-form',
  templateUrl: './objective-form.component.html',
  styleUrls: ['./objective-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveFormComponent implements OnInit {
  objectives$!: Observable<Objective>;

  objectiveForm = new FormGroup({
    teamId: new FormControl<number | null>(null, [
      Validators.required,
      Validators.nullValidator,
    ]),
    title: new FormControl<string>('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(20),
    ]),
    description: new FormControl<string>('', [
      Validators.required,
      Validators.maxLength(500),
      Validators.minLength(2),
    ]),
    ownerId: new FormControl<number>(0, [Validators.required]),
    // To Do: Implement quarterService as described below
    quarterId: new FormControl<number>(1),
  });
  public users$!: Observable<User[]>;
  public teams$!: Observable<Team[]>;

  public objective: Objective = {
    id: 1,
    title: 'Objective name',
    description: 'description',
    quarterYear: 2022,
    quarterId: 1,
    quarterNumber: 2,
    teamId: 1,
    progress: 22,
    ownerId: 21,
    ownerFirstname: 'Vorname',
    ownerLastname: 'Nachname',
  };
  public create!: boolean;
  // To Do: implement quarterService, which generates quarter and returns year and number in order to create quarter with id in backend.
  public quarterList!: OkrQuarter[] | undefined;
  constructor(
    private userService: UserService,
    private objectiveService: ObjectiveService,
    private teamService: TeamService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.users$ = this.userService.getUsers();
    this.quarterList = this.teamService.getQuarter();
    this.teams$ = this.teamService.getTeams();
    this.objectives$ = this.route.paramMap.pipe(
      switchMap((params) => {
        const objectiveId = getNumberOrNull(params.get('id'));
        if (objectiveId) {
          this.create = false;
          return this.objectiveService.getObjectiveById(objectiveId);
        } else {
          this.create = true;
          return of<Objective>(this.objectiveService.getInitObjective());
        }
      })
    );
  }

  changeQuarterFilter(value: OkrQuarter) {
    console.log(value);
  }

  save() {
    this.objectives$
      .pipe(
        map((objective) => {
          return {
            ...objective,
            ...this.objectiveForm.value,
          } as Objective;
        })
      )
      .subscribe((objective) =>
        this.objectiveService.saveObjective(objective).subscribe({
          next: () => this.router.navigate(['/dashboard']),
          error: () => {
            console.log('Can not save this objective: ', objective);
            window.alert('Can not save this objective: ');
            return new Error('can not save objective');
          },
        })
      );
  }
}
