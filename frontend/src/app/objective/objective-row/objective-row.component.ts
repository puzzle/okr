import {ChangeDetectionStrategy, Component, Input, OnInit} from '@angular/core';
import {Objective} from '../../shared/services/objective.service';
import {MenuEntry} from '../../shared/types/menu-entry';
import {KeyResultMeasure, KeyResultService} from "../../shared/services/key-result.service";
import {Observable} from "rxjs";

@Component({
  selector: 'app-objective-row',
  templateUrl: './objective-row.component.html',
  styleUrls: ['./objective-row.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ObjectiveRowComponent implements OnInit {
  @Input() objective!: Objective;
  keyResultList!: Observable<KeyResultMeasure[]>
  menuEntries: MenuEntry[] = [
    { displayName: 'Resultat hinzufügen', routeLine: 'result/add' },
    { displayName: 'Ziel bearbeiten', routeLine: 'objective/edit' },
    { displayName: 'Ziel duplizieren', routeLine: 'objective/duplicate' },
    { displayName: 'Ziel löschen', routeLine: 'objective/delete' },
  ];
  constructor(private keyResultService: KeyResultService) {}

  ngOnInit(): void {

  }

  public addResult() {
    console.log('Keyresult hinzufügen');
  }

  public editGoal() {
    console.log('Objective bearbeiten');
  }

  public duplicateGoal() {
    console.log('Objective duplizieren');
  }

  public deleteGoal() {
    console.log('Objective löschen');
  }

  public getKeyResults(id: number) {
    this.keyResultList = this.keyResultService.getKeyResultsOfObjective(id);
  }
}
