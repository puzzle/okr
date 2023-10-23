import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Action } from '../types/model/Action';

@Injectable({
  providedIn: 'root',
})
export class ActionService {
  constructor(private httpClient: HttpClient) {}

  getActionsFromKeyResult(keyResultId: number) {
    return this.httpClient.get<Action[]>('/api/v2/action/' + keyResultId);
  }

  createAction(actionDto: Action): Observable<Action> {
    return this.httpClient.post<Action>('/api/v2/action', actionDto);
  }

  updateAction(actionDto: Action): Observable<Action> {
    return this.httpClient.put<Action>(`/api/v2/action/${actionDto.id}`, actionDto);
  }

  deleteAction(actionId: number): Observable<Action> {
    return this.httpClient.delete<Action>(`/api/v2/action/${actionId}`);
  }
}
