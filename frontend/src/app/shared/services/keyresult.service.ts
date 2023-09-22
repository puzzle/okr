import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { KeyResult } from '../types/model/KeyResult';
import { map, Observable, of } from 'rxjs';
import { KeyResultOrdinal } from '../types/model/KeyResultOrdinal';
import { CheckIn } from '../types/model/CheckIn';
import { Objective } from '../types/model/Objective';
import { Quarter } from '../types/model/Quarter';
import { State } from '../types/enums/State';

@Injectable({
  providedIn: 'root',
})
export class KeyresultService {
  constructor(private httpClient: HttpClient) {}

  getFullKeyResult(keyresultId: number): Observable<KeyResult> {
    return of({
      id: 101,
      title: 'Ausbauen des Früchtesortiments',
      description: 'Dient zur Gesunderhaltung der Members',
      commitZone: 'Äpfel',
      targetZone: 'Äpfel und Birnen',
      stretchZone: 'Äpfel, Birnen, Bananen und Erdberen',
      owner: { id: 1, firstName: 'firstname', lastName: 'lastname' },
      keyResultType: 'ordinal',
      objective: {
        id: 301,
        state: State.DRAFT,
        quarter: {
          id: 1,
          label: 'GJ 23/24-Q1',
          startDate: new Date(),
          endDate: new Date(),
        } as Quarter,
      } as Objective,
      lastCheckIn: {
        id: 745,
        value: 'FAIL',
        confidence: 8,
        createdOn: new Date(),
        modifiedOn: new Date(),
        changeInfo: 'info',
        initiatives: 'some',
      } as CheckIn,
      createdOn: new Date(),
      modifiedOn: new Date(),
    } as KeyResultOrdinal);
  }
}
