import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IUnit } from '../shared/types/enums/unit';

@Injectable({
  providedIn: 'root'
})
export class UnitService {
  private readonly API_URL = '/api/v2/units';

  constructor(private httpClient: HttpClient) { }

  getUnits() {
    return this.httpClient.get<IUnit[]>(this.API_URL);
  }
}
