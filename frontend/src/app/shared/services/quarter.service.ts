import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Quarter } from '../types/model/Quarter';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class QuarterService {
  constructor(private http: HttpClient) {}

  getAllQuarters(): Observable<Quarter[]> {
    return this.http.get<Quarter[]>('/api/v1/quarters');
  }
}
