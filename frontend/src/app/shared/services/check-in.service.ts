import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CheckInMin } from '../types/model/CheckInMin';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CheckInService {
  constructor(private httpclient: HttpClient) {}

  getAllCheckInOfKeyResult(keyResultId: number): Observable<CheckInMin[]> {
    return this.httpclient.get<CheckInMin[]>(`/api/v2/keyresults/${keyResultId}/checkins`);
  }
}
