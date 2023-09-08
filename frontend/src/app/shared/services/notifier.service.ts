import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { ObjectiveMin } from '../types/model/ObjectiveMin';

@Injectable({
  providedIn: 'root',
})
export class NotifierService {
  drawerSubject = new Subject<ObjectiveMin>();
  constructor() {}
}
