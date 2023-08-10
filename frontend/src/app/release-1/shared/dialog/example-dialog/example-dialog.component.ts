import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-example-dialog',
  templateUrl: './example-dialog.component.html',
  styleUrls: ['./example-dialog.component.scss'],
})
export class ExampleDialogComponent implements OnInit {
  cars = ['Volvo', 'Saab', 'Mercedes', 'Audi'];
  selected = '';

  constructor() {}

  ngOnInit(): void {}
}
