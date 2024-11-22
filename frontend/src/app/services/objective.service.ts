import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Objective } from '../shared/types/model/Objective';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ObjectiveService {
  constructor(private httpClient: HttpClient) {}

  getFullObjective(id: number) {
    return this.httpClient.get<Objective>('/api/v2/objectives/' + id);
  }

  createObjective(objectiveDTO: Objective): Observable<Objective> {
    return this.httpClient.post<Objective>('/api/v2/objectives', objectiveDTO);
  }

  updateObjective(objectiveDTO: Objective): Observable<Objective> {
    return this.httpClient.put<Objective>(`/api/v2/objectives/${objectiveDTO.id}`, objectiveDTO);
  }

  deleteObjective(objectiveId: number): Observable<Objective> {
    return this.httpClient.delete<Objective>(`/api/v2/objectives/${objectiveId}`);
  }

  duplicateObjective(objectiveId: number, objectiveDTO: any): Observable<Objective> {
    return this.httpClient.post<Objective>(`/api/v2/objectives/${objectiveId}`, objectiveDTO);
  }
}
