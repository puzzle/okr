import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {map, Observable, of, switchMap} from "rxjs";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Objective, ObjectiveService} from "../../shared/services/objective.service";
import {ExpectedEvolution, KeyResultMeasure, KeyResultService, Unit} from "../../shared/services/key-result.service";
import {User, UserService} from "../../shared/services/user.service";
import {getNumberOrNull} from "../../shared/common";

@Component({
  selector: 'app-keyresult-form',
  templateUrl: './keyresult-form.component.html',
  styleUrls: ['./keyresult-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KeyresultFormComponent implements OnInit {
  keyresult$!: Observable<KeyResultMeasure>;

  keyResultForm = new FormGroup({
    title: new FormControl<string>('',
        [Validators.required, Validators.minLength(2), Validators.maxLength(20)]),
    unit: new FormControl<Unit>(Unit.PERCENT,
        [Validators.required]),
    expectedEvolution: new FormControl<ExpectedEvolution>(ExpectedEvolution.INCREASE,
        [Validators.required]),
    basicValue: new FormControl<number>(0),
    targetValue: new FormControl<number>(0),
    description: new FormControl<string>('',
        [Validators.maxLength(500)]),
      owner: new FormControl<number>(0, [Validators.required])
  });
    public users$!: Observable<User[]>;
    public objective: Objective = {
        id: 1,
        title: 'Objective name',
        description: 'description',
        quarterYear: 2022,
        quarterId: 1,
        quarterNumber: 2,
        progress: 22,
        ownerId: 21,
        ownerFirstname: 'Vorname',
        ownerLastname: 'Nachname',
        created: '2022'
    };
    public unit$ = Unit;
    public expectedEvolution$ = ExpectedEvolution;
    public create!: boolean

  constructor(
      private userService: UserService,
      private keyResultService: KeyResultService,
      private objectiveService: ObjectiveService,
      private route: ActivatedRoute,
      private router: Router
  ) {}

  ngOnInit(): void {
      this.users$ = this.userService.getUsers();
    this.keyresult$ = this.route.paramMap.pipe(
        switchMap(params => {
          const keyresultId = getNumberOrNull(params.get('id'));
          if (keyresultId) {
              this.create = false
            return this.keyResultService.getKeyResultById(keyresultId);
          } else {
              this.create = true
            return of<KeyResultMeasure>(this.keyResultService.getInitKeyResult());
          }
        })
    );
  }

  save() {
    this.keyresult$.pipe(
        map(keyresult => {
          return {
            ...keyresult,
            ...this.keyResultForm.value,
          } as KeyResultMeasure;
        })
    ).subscribe(keyresult =>
        this.keyResultService.saveKeyresult(keyresult).subscribe({
          next: () => this.router.navigate(['/dashboard']),
          error: () => {
            console.log('Can not save this keyresult: ', keyresult);
            window.alert("Can not save this keyresult: ")
            return new Error('ups sommething happend');
          }
        })
    );
  }
}
