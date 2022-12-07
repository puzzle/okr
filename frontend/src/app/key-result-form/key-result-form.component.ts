import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';

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
    baseValue: new FormControl(''),
    unit: new FormControl(''),
    expectedEvolution: new FormControl(''),
  });

  units = Object.values(Unit);
  expectedEvolutions = Object.values(ExpectedEvolution);
  constructor() {}

  ngOnInit(): void {}

  submit() {
    console.log(this.keyResultForm.value);
  }
}
