import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { User, UserService } from './user.service';
import { Observable } from 'rxjs';

export enum Unit {
  PERCENT = 'Prozent',
  CHF = 'CHF',
  NUMBER = 'Nummer',
  BINARY = 'Binär',
}

export enum ExpectedEvolution {
  INCREASE = 'Steigern',
  DECREASE = 'Verringern',
  CONSTANT = 'Konstant',
}

@Component({
  selector: 'app-key-result-form',
  templateUrl: './key-result-form.component.html',
  styleUrls: ['./key-result-form.component.scss'],
})
export class KeyResultFormComponent implements OnInit {
  keyResultForm = new FormGroup({
    title: new FormControl(''),
    unit: new FormControl(''),
    expectedEvolution: new FormControl(''),
    baseValue: new FormControl(''),
    targetValue: new FormControl(''),
    description: new FormControl(''),
    owner: new FormControl(''),
  });

  public users$!: Observable<User[]>;

  public Unit = Unit;
  public ExpectedEvolution = ExpectedEvolution;

  constructor(private _userService: UserService) {}

  ngOnInit(): void {
    this.users$ = this._userService.getAllUsers();
  }

  submit() {
    console.log(this.keyResultForm.value);
  }
}
