import { Component, Input, OnInit } from '@angular/core';
import { Objective } from '../../services/objective.service';
import { Observable } from 'rxjs';
import { KeyResultMeasure, KeyResultService } from '../../services/key-result.service';
import {MenuEntry} from "../../types/menu-entry";

@Component({
  selector: 'app-objective-detail',
  templateUrl: './objective-detail.component.html',
  styleUrls: ['./objective-detail.component.scss'],
})
export class ObjectiveDetailComponent implements OnInit {
  @Input() objective!: Objective;
  keyResultList!: Observable<KeyResultMeasure[]>;

  menuEntries: MenuEntry[] = [
    { displayName: 'Resultat bearbeiten', routeLine: 'result/add' },
    { displayName: 'Resultat duplizieren', routeLine: 'objective/edit' },
    { displayName: 'Details einsehen', routeLine: 'result/add' },
    { displayName: 'Resultat löschen', routeLine: 'result/add' },
    { displayName: 'Messung hinzufügen', routeLine: 'result/add' },
  ];

  constructor(private keyResultService: KeyResultService) {}

  ngOnInit(): void {
    this.keyResultList = this.keyResultService.getKeyResultsOfObjective(
        this.objective.id
    );
  }
}