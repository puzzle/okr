import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Quarter {
  id: number;
  label: string;
}

export interface StartEndDateDTO {
  startDate: Date;
  endDate: Date;
}
@Injectable({
  providedIn: 'root',
})
export class QuarterService {
  constructor(private httpClient: HttpClient) {}

  public getQuarters(): Observable<Quarter[]> {
    return this.httpClient.get<Quarter[]>('api/v1/quarters');
  }

  public getStartAndEndDateOfKeyresult(
    keyResultId: number
  ): Observable<StartEndDateDTO> {
    return this.httpClient.get<StartEndDateDTO>(
      'api/v1/quarters/dates/' + keyResultId
    );
  }
}
