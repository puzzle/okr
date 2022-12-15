import {
  ChangeDetectionStrategy,
  Component,
  Input,
  OnInit,
} from '@angular/core';
import { Objective } from '../../shared/services/objective.service';
import { MenuEntry } from '../../shared/types/menu-entry';
import {
  KeyResultMeasure,
  KeyResultService,
} from '../../shared/services/key-result.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-objective-row',
  templateUrl: './objective-row.component.html',
  styleUrls: ['./objective-row.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveRowComponent implements OnInit {
  @Input() objective!: Objective;
  keyResultList: Observable<KeyResultMeasure[]> = new BehaviorSubject([]);
  menuEntries!: MenuEntry[];
  constructor(
    private keyResultService: KeyResultService,
    private router: Router
  ) {}

  public getKeyResults(id: number) {
    this.keyResultList = this.keyResultService.getKeyResultsOfObjective(id);
  }
  ngOnInit(): void {
    this.menuEntries = [
      {
        displayName: 'KeyResult hinzufügen',
        routeLine: 'objective/' + this.objective.id + '/keyresult/new',
      },
      {
        displayName: 'Objective bearbeiten',
        routeLine: 'objectives/edit/' + this.objective.id,
      },
      {
        displayName: 'Objective duplizieren',
        routeLine: 'objective/duplicate',
      },
      { displayName: 'Objective löschen', routeLine: 'objective/delete' },
    ];
  }

  redirect(menuEntry: MenuEntry) {
    this.router.navigate([menuEntry.routeLine]);
  }
}
