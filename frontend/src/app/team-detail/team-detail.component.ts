import { Component, Input, OnInit } from '@angular/core';
import { Team } from '../dashboard/team.service';
import { Observable } from 'rxjs';
import { ObjectiveService } from './objective.service';
import { MenuEntry } from '../models/MenuEntry';
import { Objective } from '../models/Objective';

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
    { displayName: 'Ziel duplizieren', routeLine: 'result/add' },
    { displayName: 'Ziel löschen', routeLine: 'result/add' },
  ];

  constructor(public objectiveService: ObjectiveService) {}

  ngOnInit(): void {
    this.objectiveList = this.objectiveService.getObjectivesOfTeam(
      this.team.id
    );
  }
}
