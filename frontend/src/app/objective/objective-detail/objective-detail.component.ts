import {
  ChangeDetectionStrategy,
  Component,
  Input,
  OnInit,
} from '@angular/core';
import { Objective } from '../../shared/services/objective.service';
import { Observable } from 'rxjs';
import { KeyResultMeasure } from '../../shared/services/key-result.service';
import { MenuEntry } from '../../shared/types/menu-entry';

@Component({
  selector: 'app-objective-detail',
  templateUrl: './objective-detail.component.html',
  styleUrls: ['./objective-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveDetailComponent {
  @Input() objective!: Objective;
  @Input() keyResultList!: Observable<KeyResultMeasure[]>;
}
