import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { Objective } from '../../shared/services/objective.service';
import { Observable } from 'rxjs';
import { KeyResultMeasure } from '../../shared/services/key-result.service';
import { RouteService } from '../../shared/services/route.service';

@Component({
  selector: 'app-objective-detail',
  templateUrl: './objective-detail.component.html',
  styleUrls: ['./objective-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveDetailComponent {
  constructor(public routeService: RouteService) {}
  @Input() objective!: Objective;
  @Input() keyResultList!: Observable<KeyResultMeasure[]>;
}
