import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { map, Observable, switchMap } from 'rxjs';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  Objective,
  ObjectiveService,
} from '../../shared/services/objective.service';
import {
  KeyResultMeasure,
  KeyResultService,
} from '../../shared/services/key-result.service';
import { User, UserService } from '../../shared/services/user.service';
import { getNumberOrNull } from '../../shared/common';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-keyresult-form',
  templateUrl: './keyresult-form.component.html',
  styleUrls: ['./keyresult-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KeyresultFormComponent implements OnInit {
  keyresult$!: Observable<KeyResultMeasure>;

  keyResultForm = new FormGroup({
    title: new FormControl<string>('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(20),
    ]),
    unit: new FormControl<string>('', [Validators.required]),
    expectedEvolution: new FormControl<string>('', [Validators.required]),
    basicValue: new FormControl<number>(0, Validators.required),
    targetValue: new FormControl<number>(0, Validators.required),
    description: new FormControl<string>('', [Validators.maxLength(4096)]),
    ownerId: new FormControl<number>(0, [
      Validators.required,
      Validators.min(1),
    ]),
  });
  public users$!: Observable<User[]>;
  public objective$!: Observable<Objective>;
  public unit$: string[] = ['PERCENT', 'CHF', 'NUMBER', 'BINARY'];
  public expectedEvolution$: string[] = ['INCREASE', 'DECREASE', 'CONSTANT'];
  public create!: boolean;

  constructor(
    private userService: UserService,
    private keyResultService: KeyResultService,
    private objectiveService: ObjectiveService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.users$ = this.userService.getUsers();
    this.objective$ = this.route.paramMap.pipe(
      switchMap((params) => {
        const objectiveId = getNumberOrNull(params.get('objectiveId'));
        if (objectiveId) {
          return this.objectiveService.getObjectiveById(objectiveId);
        } else {
          throw Error('Objective with Id ' + objectiveId + " doesn't exist");
        }
      })
    );
    this.keyresult$ = this.route.paramMap.pipe(
      switchMap((params) => {
        const keyresultId = getNumberOrNull(params.get('keyresultId'));
        if (keyresultId) {
          this.create = false;
          return this.keyResultService.getKeyResultById(keyresultId);
        } else {
          this.create = true;
          let keyresult: KeyResultMeasure =
            this.keyResultService.getInitKeyResult();
          return this.objective$.pipe(
            map((objective) => {
              keyresult.objectiveId = objective.id!;
              return keyresult;
            })
          );
        }
      })
    );

    this.keyresult$.subscribe((keyresult) => {
      const {
        id,
        objectiveId,
        ownerFirstname,
        ownerLastname,
        measure,
        progress,
        ...restKeyresult
      } = keyresult;
      this.keyResultForm.setValue(restKeyresult);
    });
  }

  save() {
    this.keyresult$
      .pipe(
        map((keyresult) => {
          return {
            ...keyresult,
            ...this.keyResultForm.value,
          } as KeyResultMeasure;
        })
      )
      .subscribe((keyresult) =>
        this.keyResultService.saveKeyresult(keyresult, this.create).subscribe({
          next: () => {
            this.router.navigate(['/dashboard']);
            this.toastr.success('', 'Key Result gespeichert!', {
              timeOut: 5000,
            });
          },
          error: (e: HttpErrorResponse) => {
            this.toastr.error(
              'Key Result konnte nicht gespeichert werden!',
              'Fehlerstatus: ' + e.status,
              {
                timeOut: 5000,
              }
            );
            console.log('Can not save this Key Result: ', keyresult);
            return new Error('ups sommething happend');
          },
        })
      );
  }

  navigateBack() {
    this.location.back();
  }
}
