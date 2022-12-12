import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import {
  Objective,
  ObjectiveService,
} from '../../shared/services/objective.service';
import { Location } from '@angular/common';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { map, Observable, of, switchMap } from 'rxjs';

import { ActivatedRoute, Router } from '@angular/router';
import { User, UserService } from '../../shared/services/user.service';
import { getNumberOrNull } from '../../shared/common';
import {
  OkrQuarter,
  Team,
  TeamService,
} from '../../shared/services/team.service';

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
      Validators.maxLength(4096),
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
    teamName: 'Team Name',
    progress: 22,
    ownerId: 21,
    ownerFirstname: 'Vorname',
    ownerLastname: 'Nachname',
    created: '',
  };
  public create!: boolean;
  // To Do: implement quarterService, which generates quarter and returns year and number in order to create quarter with id in backend.
  public quarterList!: OkrQuarter[] | undefined;
  constructor(
    private userService: UserService,
    private objectiveService: ObjectiveService,
    private teamService: TeamService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.users$ = this.userService.getUsers();
    // getQuarter should not be in teamService.ts
    this.quarterList = this.teamService.getQuarter();
    this.teams$ = this.teamService.getTeams();
    this.objectives$ = this.route.paramMap.pipe(
      switchMap((params) => {
        const objectiveId = getNumberOrNull(params.get('objectiveId'));
        if (objectiveId) {
          this.create = false;
          return this.objectiveService.getObjectiveById(objectiveId);
        } else {
          this.create = true;
          return of<Objective>(this.objectiveService.getInitObjective());
        }
      })
    );
    this.objectives$.subscribe((objective) => {
      const {
        id,
        ownerFirstname,
        ownerLastname,
        teamName,
        quarterNumber,
        quarterYear,
        progress,
        ...restObjective
      } = objective;
      this.objectiveForm.setValue(restObjective);
    });
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
        this.objectiveService.saveObjective(objective, this.create).subscribe({
          next: () => this.router.navigate(['/dashboard']),
          error: () => {
            console.log('Can not save this objective: ', objective);
            window.alert('Can not save this objective: ');
            return new Error('can not save objective');
          },
        })
      );
  }
  navigateBack() {
    this.location.back();
  }
}
