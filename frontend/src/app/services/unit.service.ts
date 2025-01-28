import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Unit } from '../shared/types/enums/unit';
import { DialogService } from './dialog.service';
import { filter, map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UnitService {
  private readonly API_URL = '/api/v2/units';

  constructor(private httpClient: HttpClient, private dialogService: DialogService) { }

  getUnits() {
    return this.httpClient.get<Unit[]>(this.API_URL);
  }

  createUnit(unit: Unit) {
    return this.httpClient.post<Unit>(this.API_URL, unit);
  }

  checkForNewUnit(unitName: string): Observable<Unit> {
    const newUnit = { unitName: unitName,
      isDefault: false } as Unit;
    /*
     * this.unitOptions.push(newUnit);
     * this.keyResultForm.get('metric')
     *     ?.get('unit')
     *     ?.setValue(newUnit);
     */

    return this.dialogService
      .openConfirmDialog('CONFIRMATION.UNIT_CREATE')
      .afterClosed()
      .pipe(filter((result) => result), map(() => newUnit));
    /*
     * .subscribe((result) => {
     *   if (result) {
     *      return this.createUnit(newUnit)
     *         // .subscribe((unit: Unit) => this.keyResultForm.get('metric')
     *         //     ?.get('unit')
     *         //     ?.setValue(unit));
     *   }
     *   return undefined;
     * });
     */
  }
}
