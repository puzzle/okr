import { Component, Input, OnInit } from '@angular/core';
import { Team } from '../../services/team.service';
import { Observable } from 'rxjs';
import { Objective, ObjectiveService } from '../../services/objective.service';
import {MenuEntry} from "../../types/menu-entry";

@Component({
  selector: 'app-team-detail',
  templateUrl: './team-detail.component.html',
  styleUrls: ['./team-detail.component.scss'],
})
export class TeamDetailComponent implements OnInit {
  @Input() team!: Team;
  objectiveList!: Observable<Objective[]>;

  menuEntries: MenuEntry[] = [
    { displayName: 'Resultat hinzufügen', routeLine: 'result/add' },
    { displayName: 'Ziel bearbeiten', routeLine: 'objective/edit' },
    { displayName: 'Ziel duplizieren', routeLine: 'objective/duplicate' },
    { displayName: 'Ziel löschen', routeLine: 'objective/delete' },
  ];

  constructor(public objectiveService: ObjectiveService) {}

  ngOnInit(): void {
    this.objectiveList = this.objectiveService.getObjectivesOfTeam(
      this.team.id
    );
  }
}
