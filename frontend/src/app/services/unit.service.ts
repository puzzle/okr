import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Unit } from '../shared/types/enums/unit';
import { DialogService } from './dialog.service';
import { filter, map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UnitService {
  private readonly API_URL = '/api/v2/units';

  private httpClient = inject(HttpClient);

  private dialogService = inject(DialogService);

  getUnits() {
    return this.httpClient.get<Unit[]>(this.API_URL);
  }

  getAllFromUser() {
    return this.httpClient.get<Unit[]>(this.API_URL + '/user');
  }

  createUnit(unit: Unit) {
    return this.httpClient.post<Unit>(this.API_URL, unit);
  }

  updateUnit(unit: Unit) {
    return this.httpClient.put<Unit>(this.API_URL + '/' + unit.id, unit);
  }

  checkForNewUnit(unitName: string): Observable<Unit> {
    const newUnit = { unitName: unitName,
      isDefault: false } as Unit;

    return this.dialogService
      .openConfirmDialog('CONFIRMATION.UNIT_CREATE')
      .afterClosed()
      .pipe(filter((result) => result), map(() => newUnit));
  }
}
