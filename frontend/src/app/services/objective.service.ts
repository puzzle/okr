import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Objective } from "../shared/types/model/Objective";
import { Observable } from "rxjs";
import { KeyResultDTO } from "../shared/types/DTOs/KeyResultDTO";
import { User } from "../shared/types/model/User";
import { CheckIn } from "../shared/types/model/CheckIn";
import { Action } from "../shared/types/model/Action";

@Injectable({
  providedIn: "root"
})
export class ObjectiveService {
  constructor (private httpClient: HttpClient) {}

  getFullObjective (id: number) {
    return this.httpClient.get<Objective>("/api/v2/objectives/" + id);
  }

  getAllKeyResultsByObjective (id: number): Observable<KeyResultDTO[]> {
    return this.httpClient.get<KeyResultDTO[]>("api/v2/objectives/" + id + "/keyResults");
  }

  createObjective (objectiveDTO: Objective): Observable<Objective> {
    return this.httpClient.post<Objective>("/api/v2/objectives", objectiveDTO);
  }

  updateObjective (objectiveDTO: Objective): Observable<Objective> {
    return this.httpClient.put<Objective>(`/api/v2/objectives/${objectiveDTO.id}`, objectiveDTO);
  }

  deleteObjective (objectiveId: number): Observable<Objective> {
    return this.httpClient.delete<Objective>(`/api/v2/objectives/${objectiveId}`);
  }

  duplicateObjective (objectiveId: number,
    duplicateObjectiveDto: {
      keyResults: {
        owner: User;
        modifiedOn: Date | null | undefined;
        keyResultType: string | undefined;
        description: string;
        actionList: Action[] | null;
        id: undefined;
        lastCheckIn: CheckIn | null | undefined;
        title: string;
        version: number;
        createdOn: Date | null | undefined;
        objective: Objective;
      }[];
      objective: any;
    }): Observable<Objective> {
    return this.httpClient.post<Objective>(`/api/v2/objectives/${objectiveId}`, duplicateObjectiveDto);
  }
}
