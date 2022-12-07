import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { Objective } from '../../shared/services/objective.service';
import { MenuEntry } from '../../shared/types/menu-entry';
import {
  KeyResultMeasure,
  KeyResultService,
} from '../../shared/services/key-result.service';
import { BehaviorSubject, Observable } from 'rxjs';

@Component({
  selector: 'app-objective-row',
  templateUrl: './objective-row.component.html',
  styleUrls: ['./objective-row.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveRowComponent {
  @Input() objective!: Objective;
  keyResultList: Observable<KeyResultMeasure[]> = new BehaviorSubject([]);
  menuEntries: MenuEntry[] = [
    { displayName: 'Resultat hinzufügen', routeLine: 'result/add' },
    { displayName: 'Ziel bearbeiten', routeLine: 'objective/edit' },
    { displayName: 'Ziel duplizieren', routeLine: 'objective/duplicate' },
    { displayName: 'Ziel löschen', routeLine: 'objective/delete' },
  ];
  constructor(private keyResultService: KeyResultService) {}

  public getKeyResults(id: number) {
    this.keyResultList = this.keyResultService.getKeyResultsOfObjective(id);
  }
}
