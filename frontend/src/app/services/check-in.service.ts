import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CheckIn } from '../shared/types/model/check-in';

@Injectable({
  providedIn: 'root'
})
export class CheckInService {
  constructor(private httpclient: HttpClient) {}

  getAllCheckInOfKeyResult(keyResultId: number): Observable<CheckIn[]> {
    return this.httpclient.get<CheckIn[]>(`/api/v2/keyresults/${keyResultId}/checkins`);
  }

  saveCheckIn(checkIn: CheckIn) {
    if (checkIn.id) {
      return this.httpclient.put<any>('/api/v2/checkins/' + checkIn.id, checkIn);
    } else {
      return this.httpclient.post<any>('/api/v2/checkins', checkIn);
    }
  }
}
