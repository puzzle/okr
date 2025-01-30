import { Component, OnInit } from '@angular/core';
import { UnitService } from '../../services/unit.service';
import { Unit } from '../../shared/types/enums/unit';
import { forkJoin, map, Observable, ReplaySubject } from 'rxjs';
import { FormArray, FormGroup } from '@angular/forms';
import { FormControlsOf, Item } from '../action-plan/action-plan.component';
import { MatDialogRef } from '@angular/material/dialog';
import { UnitTransformationPipe } from '../../shared/pipes/unit-transformation/unit-transformation.pipe';

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

  constructor(private unitService: UnitService, private dialogRef: MatDialogRef<ManageUnitsDialogComponent>, private unitPipe: UnitTransformationPipe) {
  }

  getChangedItems() {
    const itemControls = (this.fg.get('unitFormArray') as FormArray)?.controls as FormGroup<FormControlsOf<Item>>[];
    return itemControls.filter((c) => c.dirty)
      .map((c) => c.getRawValue() as Item);
  }

  submit() {
    const items = this.getChangedItems();

    const units = items.map((i) => {
      return { id: i.id,
        unitName: i.item } as Unit;
    });
    const allObservables = this.getNewUnits(units)
      .concat(this.getUpdatableUnits(units));
    if (allObservables.length === 0) {
      this.dialogRef.close();
      return;
    }
    forkJoin(allObservables)
      .subscribe((complete) => {
        this.dialogRef.close();
      });
  }

  getUpdatableUnits(units: Unit[]) {
    return units.filter((u) => u.id)
      .map((u) => this.unitService.updateUnit(u));
  }

  getNewUnits(units: Unit[]) {
    return units.filter((u) => !u.id)
      .map((u) => this.unitService.createUnit(u));
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

  getFormatedUnitSymbol(unit: Unit) {
    const s = this.unitPipe.transformLabel(unit.unitName);
    if (s.trim() === unit.unitName || s.trim() === '') {
      return '';
    }
    return `(${s})`;
  }
}
