import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ObjectiveDTO } from '../types/DTOs/ObjectiveDTO';
import { Objective } from '../types/model/Objective';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ObjectiveService {
  constructor(private httpClient: HttpClient) {}

  getFullObjective(id: number) {
    return this.httpClient.get<Objective>('/api/v2/objectives/' + id);
  }

  createObjective(objectiveDTO: ObjectiveDTO): Observable<ObjectiveDTO> {
    return this.httpClient.post<ObjectiveDTO>('/api/v2/objectives', objectiveDTO);
  }

  updateObjective(objectiveDTO: ObjectiveDTO): Observable<ObjectiveDTO> {
    return this.httpClient.put<ObjectiveDTO>(`/api/v2/objectives/${objectiveDTO.id}`, objectiveDTO);
  }
}
