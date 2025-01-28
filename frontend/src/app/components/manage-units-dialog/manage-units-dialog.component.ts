import { Component, OnInit } from '@angular/core';
import { UnitService } from '../../services/unit.service';
import { Unit } from '../../shared/types/enums/unit';
import { map, Observable, ReplaySubject } from 'rxjs';
import { FormArray, FormGroup } from '@angular/forms';
import { FormControlsOf, Item } from '../action-plan/action-plan.component';

@Component({
  selector: 'app-manage-units-dialog',
  templateUrl: './manage-units-dialog.component.html',
  styleUrl: './manage-units-dialog.component.scss',
  standalone: false
})
export class ManageUnitsDialogComponent implements OnInit {
  allUnits = new Observable<Unit[]>();

  actinPlanAddItemSubject = new ReplaySubject<Item | undefined>();

  fg: FormGroup = new FormGroup({
    unitFormArray: new FormArray<FormGroup<FormControlsOf<Item>>>([])
  });

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
    this.unitService.getAllFromUser()
      .pipe(map((arr) => arr.map((u) => {
        return { id: u.id,
          item: u.unitName,
          isChecked: false } as Item;
      })))
      .subscribe((units) => units.forEach((unit) => this.actinPlanAddItemSubject.next(unit)));
  }
}
