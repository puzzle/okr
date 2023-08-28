import { Injectable } from '@angular/core';
import { State } from '../types/enums/State';
import { of } from 'rxjs';
import { Objective } from '../models/Objective';

@Injectable({
  providedIn: 'root',
})
export class OverviewService {
  constructor() {}

  getObjectiveWithKeyresults() {
    return of<Objective>({ id: 1, title: 'Increase User Engagement', state: State.DRAFT });
  }
}
