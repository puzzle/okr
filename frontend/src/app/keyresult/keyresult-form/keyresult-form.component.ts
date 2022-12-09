import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { map, Observable, of, switchMap } from 'rxjs';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  Objective,
  ObjectiveService,
} from '../../shared/services/objective.service';
import {
  ExpectedEvolution,
  KeyResultMeasure,
  KeyResultService,
  Unit,
} from '../../shared/services/key-result.service';
import { User, UserService } from '../../shared/services/user.service';
import { getNumberOrNull } from '../../shared/common';

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
    unit: new FormControl<string>('PERCENT', [Validators.required]),
    expectedEvolution: new FormControl<string>('INCREASE', [
      Validators.required,
    ]),
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
  public unit$: Unit[] = [
    { id: 0, unit: 'PERCENT' },
    { id: 1, unit: 'CHF' },
    { id: 2, unit: 'NUMBER' },
    { id: 3, unit: 'BINARY' },
  ];
  public expectedEvolution$: ExpectedEvolution[] = [
    { id: 0, expectedEvolution: 'INCREASE' },
    { id: 1, expectedEvolution: 'DECREASE' },
    { id: 2, expectedEvolution: 'CONSTANT' },
  ];
  public create!: boolean;

  constructor(
    private userService: UserService,
    private keyResultService: KeyResultService,
    private objectiveService: ObjectiveService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.users$ = this.userService.getUsers();
    this.objective$ = this.route.paramMap.pipe(
      switchMap((params) => {
        const objectiveId = getNumberOrNull(params.get('objectiveId'));
        if (objectiveId) {
          return this.objectiveService.getObjectiveById(objectiveId);
        } else {
          throw Error('Objective with Id' + objectiveId + "doesn't exist");
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
          return of<KeyResultMeasure>(this.keyResultService.getInitKeyResult());
        }
      })
    );

    this.keyresult$.subscribe((keyresult) => {
      const {
        id,
        objectiveId,
        ownerFirstname,
        ownerLastname,
        quarterId,
        quarterNumber,
        quarterYear,
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
          next: () => this.router.navigate(['/dashboard']),
          error: () => {
            console.log('Can not save this keyresult: ', keyresult);
            // window.alert('Can not save this keyresult: ');
            return new Error('ups sommething happend');
          },
        })
      );
  }
}
