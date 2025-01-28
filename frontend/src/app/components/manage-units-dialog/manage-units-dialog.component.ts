import { Component, OnInit } from '@angular/core';
import { UnitService } from '../../services/unit.service';

@Component({
  selector: 'app-manage-units-dialog',
  templateUrl: './manage-units-dialog.component.html',
  styleUrl: './manage-units-dialog.component.scss',
  standalone: false
})
export class ManageUnitsDialogComponent implements OnInit {
  constructor(private unitService: UnitService) {
  }

  saveUnits() {

  }

  ngOnInit(): void {
  }
}
