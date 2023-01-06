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
import {ActivatedRoute, Params, Router} from '@angular/router';
import { Location } from '@angular/common';
import { getNumberOrNull } from '../../shared/common';

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
  isSelected: boolean = false;
  constructor(
    private keyResultService: KeyResultService,
    private router: Router,
    private route: ActivatedRoute,
    private location: Location
  ) {}

  public getKeyResults(id: number) {
      this.router.navigate([],
          {
            relativeTo: this.route,
            queryParams: { objectives: this.objective.id },
            queryParamsHandling: 'merge'
          });
    this.keyResultList = this.keyResultService.getKeyResultsOfObjective(id);
  }
  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params['objectives'] !== undefined) {
      const selectedObjectives: string[] = params['objectives'].split(',');
       if (selectedObjectives.some(x => getNumberOrNull(x) === this.objective.id)) {
         this.isSelected = true;
       }
      }
     })
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
