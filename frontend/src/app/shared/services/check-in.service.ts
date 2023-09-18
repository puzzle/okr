import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class CheckInService {
  constructor(private httpclient: HttpClient) {}

  getAllCheckInOfKeyResult(keyResultId: number) {
    return this.httpclient.get(`/api/v2/keyresults/${keyResultId}/checkins`);
  }
}
