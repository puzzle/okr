import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Action } from '../shared/types/model/action';

@Injectable({
  providedIn: 'root'
})
export class ActionService {
  private httpClient = inject(HttpClient);


  updateActions(actionList: Action[]): Observable<Action> {
    return this.httpClient.put<Action>('/api/v2/action', actionList);
  }

  deleteAction(actionId: number): Observable<Action> {
    return this.httpClient.delete<Action>(`/api/v2/action/${actionId}`);
  }
}
