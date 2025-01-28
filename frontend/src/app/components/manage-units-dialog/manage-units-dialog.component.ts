import { Component, OnInit } from '@angular/core';
import { UnitService } from '../../services/unit.service';
import { Unit } from '../../shared/types/enums/unit';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-manage-units-dialog',
  templateUrl: './manage-units-dialog.component.html',
  styleUrl: './manage-units-dialog.component.scss',
  standalone: false
})
export class ManageUnitsDialogComponent implements OnInit {
  allUnits = new Observable<Unit[]>();

  constructor(private unitService: UnitService) {
  }

  saveUnits() {

  }

  ngOnInit(): void {
    this.allUnits = this.unitService.getUnits();
    this.allUnits.subscribe((units) => console.log(units));
  }
}
