import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Objective } from '../shared/types/model/Objective';
import { Observable } from 'rxjs';
import { KeyResult } from '../shared/types/model/KeyResult';
import { KeyResultDTO } from '../shared/types/DTOs/KeyResultDTO';

@Injectable({
  providedIn: 'root',
})
export class ObjectiveService {
  constructor(private httpClient: HttpClient) {}

  getFullObjective(id: number) {
    return this.httpClient.get<Objective>('/api/v2/objectives/' + id);
  }

  getAllKeyResultsByObjective(id: number): Observable<KeyResultDTO[]> {
    return this.httpClient.get<KeyResultDTO[]>('api/v2/objectives/' + id + '/keyResults');
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
