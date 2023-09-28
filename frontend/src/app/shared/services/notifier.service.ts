import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { KeyResult } from '../types/model/KeyResult';
import { Objective } from '../types/model/Objective';

@Injectable({
  providedIn: 'root',
})
export class NotifierService {
  closeDetailSubject: Subject<void> = new Subject();

  keyResultsChanges: Subject<{ keyResult: KeyResult; changeId: number | null; objective: Objective }> = new Subject<{
    keyResult: KeyResult;
    changeId: number | null;
    objective: Objective;
  }>();
  deleteKeyResult: Subject<KeyResult> = new Subject<KeyResult>();
}
