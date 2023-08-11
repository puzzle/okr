import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatestWith, filter, map, Observable, of, share, startWith, switchMap } from 'rxjs';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Objective, ObjectiveService } from '../../../services/objective.service';
import { KeyResultMeasure, KeyResultService } from '../../../services/key-result.service';
import { User, UserService } from '../../../services/user.service';
import { getNumberOrNull } from '../../../common';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { NUMBER_REGEX, PERCENT_REGEX } from '../../../regexLibrary';
import { comparisonValidator } from '../../../validators';
import { RouteService } from '../../../services/route.service';
import helpTexts from '../../../../../../assets/help-texts.json';
import { MatDialog } from '@angular/material/dialog';
import { HelpDialogComponent, HelpText } from '../../../dialog/help-dialog/help-dialog.component';

@Component({
  selector: 'app-keyresult-form',
  templateUrl: './keyresult-form.component.html',
  styleUrls: ['./keyresult-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KeyresultFormComponent implements OnInit {
  keyresult$!: Observable<KeyResultMeasure>;

  keyResultForm = new FormGroup({
    title: new FormControl<string>('', [Validators.required, Validators.minLength(2), Validators.maxLength(250)]),
    unit: new FormControl<string>('', [Validators.required]),
    expectedEvolution: new FormControl<string>('', [Validators.required]),
    basicValue: new FormControl<number>({ value: 0, disabled: true }),
    targetValue: new FormControl<number>({ value: 0, disabled: true }, [Validators.required]),
    description: new FormControl<string>('', [Validators.maxLength(4096)]),
    owner: new FormControl<User | null>(null, [Validators.required, Validators.nullValidator]),
  });
  public users$!: Observable<User[]>;
  public objective$!: Observable<Objective>;
  public unit$: string[] = ['PERCENT', 'CHF', 'NUMBER'];
  public expectedEvolution$: string[] = ['INCREASE', 'DECREASE', 'MIN', 'MAX'];
  public create!: boolean;
  public filteredUsers$: Observable<User[]> | undefined = of([]);

  constructor(
    private userService: UserService,
    private keyResultService: KeyResultService,
    private objectiveService: ObjectiveService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private router: Router,
    private location: Location,
    private toastr: ToastrService,
    private routeService: RouteService
  ) {}

  ngOnInit(): void {
    //filters the input of the autocomplete field for objectiveowner in order to improve search.
    this.filteredUsers$ = this.keyResultForm.get('owner')?.valueChanges.pipe(
      startWith(''),
      filter((value) => typeof value === 'string'),
      switchMap((value) => this.filter(value as string))
    );
    this.keyResultForm.get('expectedEvolution')!.valueChanges.subscribe((value) => {
      if (value === 'INCREASE' || value === 'DECREASE') {
        this.keyResultForm.get('basicValue')!.setValidators(Validators.required);
      } else {
        this.keyResultForm.get('basicValue')!.disable();
      }
    });
    this.users$ = this.userService.getUsers().pipe(share());
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
          this.enableTargetValue();
          this.enableBasicValue();
          return this.keyResultService.getKeyResultById(keyresultId);
        } else {
          this.create = true;
          let keyresult: KeyResultMeasure = this.keyResultService.getInitKeyResult();
          return this.objective$.pipe(
            map((objective) => {
              keyresult.objectiveId = objective.id!;
              return keyresult;
            })
          );
        }
      })
    );
    this.keyresult$.pipe(combineLatestWith(this.users$)).subscribe(([keyresult, users]) => {
      const { id, objectiveId, ownerFirstname, ownerLastname, measure, progress, ownerId, ...restKeyresult } =
        keyresult;
      this.resetValidatorOfForm(keyresult.unit);
      this.keyResultForm.setValue({ ...restKeyresult, owner: users.find((user) => user.id === ownerId) ?? null });
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
    this.keyresult$
      .pipe(
        map((keyresult) => {
          const { owner, ...rest } = this.keyResultForm.value;
          return {
            ...keyresult,
            ...this.keyResultForm.value,
            ownerId: owner!.id,
          } as KeyResultMeasure;
        })
      )
      .subscribe((keyresult) =>
        this.keyResultService.saveKeyresult(keyresult, this.create).subscribe({
          next: () => {
            this.toastr.success('', 'Key Result gespeichert!', {
              timeOut: 5000,
            });
            this.routeService.navigate('/dashboard');
          },
          error: (e: HttpErrorResponse) => {
            this.toastr.error('Key Result konnte nicht gespeichert werden!', 'Fehlerstatus: ' + e.status, {
              timeOut: 5000,
            });
            return new Error('ups sommething happend');
          },
        })
      );
  }

  enableTargetValue(): void {
    this.keyResultForm.controls['targetValue'].enable();
  }

  enableBasicValue(): void {
    if (
      this.keyResultForm.get('expectedEvolution')!.value === 'INCREASE' ||
      this.keyResultForm.get('expectedEvolution')!.value === 'DECREASE'
    ) {
      this.keyResultForm.controls['basicValue'].enable();
    } else {
      this.keyResultForm.controls['basicValue'].disable();
    }
  }

  resetValidatorOfForm(unit: string | null): void {
    let regex: string | null = null;
    switch (unit) {
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
    if (regex != null) {
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
    this.routeService.back();
  }

  update() {
    this.keyResultForm.controls['targetValue'].updateValueAndValidity();
    this.keyResultForm.controls['basicValue'].updateValueAndValidity();
  }

  openHelpDialog(data: HelpText) {
    const dialogRef = this.dialog.open(HelpDialogComponent, {
      width: '50rem',
      data: data,
      panelClass: 'help-dialog-custom-panel-class',
    });
  }

  protected readonly helpTexts = helpTexts;
}
