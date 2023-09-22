import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class NotifierService {
  closeDetailSubject: Subject<void> = new Subject();
  constructor() {}
}
