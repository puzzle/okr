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
  // unitFormArray: FormArray<Unit> = new FormControlArray<Unit>()<Unit>();

  constructor(private unitService: UnitService) {
  }

  saveUnits() {
    /*
     * this.unitService.checkForNewUnit(this.unitSearchTerm)
     *     .subscribe((result:Unit)=> this.unitService.createUnit(result)
     *         .subscribe((unit)=>  this.keyResultForm.get('metric')
     *             ?.get('unit')
     *             ?.setValue(unit)));
     */
  }

  ngOnInit(): void {
    this.allUnits = this.unitService.getUnits();
  }
}
