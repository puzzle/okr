import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class NotifierService {
  drawerSubject = new Subject<string>();
  constructor() {}
}
