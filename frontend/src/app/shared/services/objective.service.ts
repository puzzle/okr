import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Objective } from '../types/model/Objective';
import { Observable } from 'rxjs';
import { Completed } from '../types/model/Completed';
import { AlignmentPossibility } from '../types/model/AlignmentPossibility';

@Injectable({
  providedIn: 'root',
})
export class ObjectiveService {
  constructor(private httpClient: HttpClient) {}

  getFullObjective(id: number) {
    return this.httpClient.get<Objective>('/api/v2/objectives/' + id);
  }

  getAlignmentPossibilities(quarterId: number): Observable<AlignmentPossibility[]> {
    return this.httpClient.get<AlignmentPossibility[]>('/api/v2/objectives/alignmentPossibilities/' + quarterId);
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

  createCompleted(completed: Completed): Observable<Completed> {
    return this.httpClient.post<Completed>('/api/v2/completed', completed);
  }

  deleteCompleted(objectiveId: number): Observable<Completed> {
    return this.httpClient.delete<Completed>('/api/v2/completed/' + objectiveId);
  }
}
