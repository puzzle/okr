import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';

@Component({
  selector: 'app-key-result-form',
  templateUrl: './key-result-form.component.html',
  styleUrls: ['./key-result-form.component.scss'],
})
export class KeyResultFormComponent implements OnInit {
  keyResultForm = new FormGroup({
    title: new FormControl(''),
  });

  matcher = new ErrorStateMatcher();
  constructor() {}

  ngOnInit(): void {}

  submit($event: any) {
    console.log($event.value);
  }
}
