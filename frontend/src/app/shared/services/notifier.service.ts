import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { ObjectiveMin } from '../types/model/ObjectiveMin';
import { KeyresultMin } from '../types/model/KeyresultMin';
import { SidenavModel } from '../types/model/SidenavModel';

@Injectable({
  providedIn: 'root',
})
export class NotifierService {
  closeDetailSubject: Subject<void> = new Subject();
  constructor() {}
}
