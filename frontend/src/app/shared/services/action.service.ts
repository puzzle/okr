import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Action } from '../types/model/Action';

@Injectable({
  providedIn: 'root',
})
export class ActionService {
  constructor(private httpClient: HttpClient) {}

  updateActions(actionList: Action[]): Observable<Action> {
    return this.httpClient.put<Action>(`/api/v2/action`, actionList);
  }

  deleteAction(actionId: number): Observable<Action> {
    return this.httpClient.delete<Action>(`/api/v2/action/${actionId}`);
  }
}
