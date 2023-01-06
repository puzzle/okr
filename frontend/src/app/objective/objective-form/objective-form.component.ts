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
import { Team, TeamService } from '../../shared/services/team.service';
import { Quarter, QuarterService } from '../../shared/services/quarter.service';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';

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
      Validators.maxLength(250),
    ]),
    description: new FormControl<string>('', [
      Validators.required,
      Validators.maxLength(4096),
      Validators.minLength(2),
    ]),
    ownerId: new FormControl<number | null>(null, [
      Validators.required,
      Validators.nullValidator,
    ]),
    quarterId: new FormControl<number | null>(null, [
      Validators.required,
      Validators.nullValidator,
    ]),
  });
  public users$!: Observable<User[]>;
  public teams$!: Observable<Team[]>;
  public create!: boolean;
  public quarters$!: Observable<Quarter[]>;
  constructor(
    private userService: UserService,
    private objectiveService: ObjectiveService,
    private teamService: TeamService,
    private quarterService: QuarterService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.users$ = this.userService.getUsers();
    this.quarters$ = this.quarterService.getQuarters();
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
        quarterLabel,
        progress,
        created,
        ...restObjective
      } = objective;
      this.objectiveForm.setValue(restObjective);
    });
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
          next: () => {
            this.toastr.success(
              'Alles hat funktioniert!',
              'Objective verarbeitet!',
              {
                timeOut: 5000,
              }
            );
            this.router.navigate(['/dashboard']);
          },
          error: (e: HttpErrorResponse) => {
            this.toastr.error(
              'Objective konnte nicht verarbeitet werden!',
              'Fehlerstatus: ' + e.status,
              {
                timeOut: 5000,
              }
            );
            return new Error('can not process objective');
          },
        })
      );
  }
  navigateBack() {
    this.location.back();
  }
}
