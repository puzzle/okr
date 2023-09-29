import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CheckInMin } from '../types/model/CheckInMin';
import { Observable } from 'rxjs';
import { CheckIn } from '../types/model/CheckIn';

@Injectable({
  providedIn: 'root',
})
export class CheckInService {
  constructor(private httpclient: HttpClient) {}

  getAllCheckInOfKeyResult(keyResultId: number): Observable<CheckInMin[]> {
    return this.httpclient.get<CheckInMin[]>(`/api/v2/keyresults/${keyResultId}/checkins`);
  }

  createCheckIn(checkIn: any): Observable<CheckIn> {
    return this.httpclient.post<any>('/api/v2/checkIns', checkIn);
  }

  updateCheckIn(checkIn: any, id: number): Observable<CheckIn> {
    return this.httpclient.put<any>('/api/v2/checkIns/' + id, checkIn);
  }
}
