import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { CheckIn } from '../types/model/CheckIn';
import { KeyResult } from '../types/model/KeyResult';
import { Objective } from '../types/model/Objective';
import { ObjectiveMin } from '../types/model/ObjectiveMin';

@Injectable({
  providedIn: 'root',
})
export class NotifierService {
  closeDetailSubject: Subject<void> = new Subject();
  reopenCheckInHistoryDialog: Subject<{ checkIn: CheckIn | null; deleted: boolean }> = new Subject<{
    checkIn: CheckIn | null;
    deleted: boolean;
  }>();

  keyResultsChanges: Subject<{ keyResult: KeyResult; changeId: number | null; objective: Objective; delete: boolean }> =
    new Subject<{
      keyResult: KeyResult;
      changeId: number | null;
      objective: Objective;
      delete: boolean;
    }>();

  objectivesChanges: Subject<{ objective: ObjectiveMin; teamId: number; delete: boolean; addKeyResult: boolean }> =
    new Subject<{
      objective: ObjectiveMin;
      teamId: number;
      delete: boolean;
      addKeyResult: boolean;
    }>();

  openKeyresultCreation: Subject<ObjectiveMin> = new Subject<ObjectiveMin>();
}
