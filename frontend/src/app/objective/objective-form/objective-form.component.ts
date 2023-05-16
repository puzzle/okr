import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { Objective, ObjectiveService } from '../../shared/services/objective.service';
import { Location } from '@angular/common';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  combineLatestWith,
  filter,
  find,
  first,
  map,
  Observable,
  of,
  share,
  startWith,
  subscribeOn,
  switchMap,
} from 'rxjs';

import { ActivatedRoute, Router } from '@angular/router';
import { User, UserService } from '../../shared/services/user.service';
import { getNumberOrNull } from '../../shared/common';
import { Team, TeamService } from '../../shared/services/team.service';
import { Quarter, QuarterService } from '../../shared/services/quarter.service';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { RouteService } from '../../shared/services/route.service';
import { getXHRResponse } from 'rxjs/internal/ajax/getXHRResponse';
import * as http from 'http';
import { error } from '@angular/compiler-cli/src/transformers/util';

@Component({
  selector: 'app-objective-form',
  templateUrl: './objective-form.component.html',
  styleUrls: ['./objective-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveFormComponent implements OnInit {
  objectives$!: Observable<Objective>;

  objectiveForm = new FormGroup({
    teamId: new FormControl<number | null>(null, [Validators.required, Validators.nullValidator]),
    title: new FormControl<string>('', [Validators.required, Validators.minLength(2), Validators.maxLength(250)]),
    description: new FormControl<string>('', [Validators.maxLength(4096)]),
    owner: new FormControl<User | null>(null, [Validators.required, Validators.nullValidator]),
    quarterId: new FormControl<number | null>(null, [Validators.required, Validators.nullValidator]),
  });
  public users$!: Observable<User[]>;
  public teams$!: Observable<Team[]>;
  public create!: boolean;
  public quarters$!: Observable<Quarter[]>;
  public filteredUsers$: Observable<User[]> | undefined = of([]);

  constructor(
    private userService: UserService,
    private objectiveService: ObjectiveService,
    private teamService: TeamService,
    private quarterService: QuarterService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private toastr: ToastrService,
    private routeService: RouteService
  ) {}

  ngOnInit(): void {
    //filters the input of the autocomplete field for objectiveowner in order to improve search.
    this.filteredUsers$ = this.objectiveForm.get('owner')?.valueChanges.pipe(
      startWith(''),
      filter((value) => typeof value === 'string'),
      switchMap((value) => this.filter(value as string))
    );
    this.users$ = this.userService.getUsers().pipe(share());
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
          const objective = this.objectiveService.getInitObjective();
          objective.teamId = getNumberOrNull(params.get('teamId'));
          return of<Objective>(objective);
        }
      })
    );
    this.objectives$.pipe(combineLatestWith(this.users$)).subscribe(([objective, users]) => {
      const {
        id,
        ownerFirstname,
        ownerLastname,
        teamName,
        quarterLabel,
        progress,
        created,
        ownerId,
        ...restObjective
      } = objective;
      this.objectiveForm.setValue({ ...restObjective, owner: users.find((user) => user.id === ownerId) ?? null });
    });
  }

  //implements the logic how the users are searched
  filter(value: string): Observable<User[]> {
    const filterValue = value.toLowerCase();
    return this.users$.pipe(
      map((users) =>
        users.filter(
          (user) =>
            user.firstname.toLowerCase().includes(filterValue) ||
            user.lastname.toLowerCase().includes(filterValue) ||
            user.username.toLowerCase().includes(filterValue)
        )
      )
    );
  }

  getUserNameById(user: User): string {
    if (user === null || user === undefined) {
      return '';
    }

    return user.firstname + ' ' + user.lastname;
  }

  save() {
    this.objectives$
      .pipe(
        map((objective) => {
          const { owner, ...rest } = this.objectiveForm.value;
          return {
            ...objective,
            ...rest,
            ownerId: owner!.id,
          } as Objective;
        })
      )
      .subscribe((objective) =>
        this.objectiveService.saveObjective(objective, this.create).subscribe({
          next: (response) => {
            if (response.status === 226) {
              this.toastr.warning(
                ' Zyklus kann nicht angepasst werden, da bereits eine Messung erfasst wurde!',
                'Objective gespeichert!',
                {
                  timeOut: 10000,
                }
              );
            } else {
              this.toastr.success('', 'Objective gespeichert!', {
                timeOut: 5000,
              });
            }
            this.router.navigate(['/dashboard'], {
              queryParams: {
                teamFilter: this.route.snapshot.queryParamMap.get('teamFilter'),
                quarterFilter: response.quarterId,
                objectives: response.id,
              },
            });
          },
          error: (e: HttpErrorResponse) => {
            this.toastr.error('Objective konnte nicht gespeichert werden!', 'Fehlerstatus: ' + e.status, {
              timeOut: 5000,
            });
            return new Error('can not process objective');
          },
        })
      );
  }

  navigateBack() {
    this.routeService.back();
  }
}
