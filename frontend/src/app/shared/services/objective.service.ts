import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { KeyResultObjective } from '../types/model/KeyResultObjective';
import { ObjectiveDTO } from '../types/DTOs/ObjectiveDTO';
import { Objective } from '../types/model/Objective';

@Injectable({
  providedIn: 'root',
})
export class ObjectiveService {
  constructor(private httpClient: HttpClient) {}

  getFullObjective(id: number) {
    return this.httpClient.get<Objective>('/api/v2/objectives/' + id);
  }

  createObjective(objectiveDTO: ObjectiveDTO) {
    return this.httpClient.post('/api/v2/objectives', objectiveDTO);
  }

  updateObjective(objectiveDTO: ObjectiveDTO) {
    return this.httpClient.put(`/api/v2/objectives/${objectiveDTO.id}`, objectiveDTO);
  }
}
