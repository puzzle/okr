import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { map, Observable, switchMap } from 'rxjs';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  Objective,
  ObjectiveService,
} from '../../../services/objective.service';
import {
  KeyResultMeasure,
  KeyResultService,
} from '../../../services/key-result.service';
import { User, UserService } from '../../../services/user.service';
import { getNumberOrNull } from '../../../common';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { NUMBER_REGEX, PERCENT_REGEX } from '../../../regexLibrary';
import { comparisonValidator } from '../../../validators';

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
      Validators.maxLength(250),
    ]),
    unit: new FormControl<string>('', [Validators.required]),
    expectedEvolution: new FormControl<string>('', [Validators.required]),
    basicValue: new FormControl<number>({ value: 0, disabled: true }, [
      Validators.required,
    ]),
    targetValue: new FormControl<number>({ value: 0, disabled: true }, [
      Validators.required,
    ]),
    description: new FormControl<string>('', [Validators.maxLength(4096)]),
    ownerId: new FormControl<number | null>(null, [
      Validators.required,
      Validators.nullValidator,
    ]),
  });
  public users$!: Observable<User[]>;
  public objective$!: Observable<Objective>;
  public unit$: string[] = ['PERCENT', 'CHF', 'NUMBER'];
  public expectedEvolution$: string[] = [
    'INCREASE',
    'DECREASE',
    'CONSTANT',
    'NONE',
  ];
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
          this.enableTargetAndBasicValue();
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

  enableTargetAndBasicValue(): void {
    this.keyResultForm.controls['basicValue'].enable();
    this.keyResultForm.controls['targetValue'].enable();
  }

  resetValidatorOfForm(): void {
    let regex: string | null = null;
    switch (this.keyResultForm.controls['unit'].value) {
      case 'NUMBER': {
        regex = NUMBER_REGEX;
        break;
      }
      case 'PERCENT': {
        regex = PERCENT_REGEX;
        break;
      }
      case 'CHF': {
        regex = NUMBER_REGEX;
        break;
      }
    }
    this.setValidatorsWithRegex(regex);
    this.keyResultForm.controls['basicValue'].updateValueAndValidity();
    this.keyResultForm.controls['targetValue'].updateValueAndValidity();
  }

  setValidatorsWithRegex(regex: string | null) {
    if (regex) {
      this.keyResultForm.controls['basicValue'].setValidators([
        Validators.required,
        Validators.pattern(regex),
        comparisonValidator(this.keyResultForm.controls['targetValue']),
      ]);
      this.keyResultForm.controls['targetValue'].setValidators([
        Validators.required,
        Validators.pattern(regex),
        comparisonValidator(this.keyResultForm.controls['basicValue']),
      ]);
      return;
    }
    this.keyResultForm.controls['basicValue'].setValidators(null);
    this.keyResultForm.controls['targetValue'].setValidators(null);
  }

  navigateBack() {
    this.location.back();
  }

  update() {
    this.keyResultForm.controls['targetValue'].updateValueAndValidity();
    this.keyResultForm.controls['basicValue'].updateValueAndValidity();
  }
}
