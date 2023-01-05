import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { Objective } from '../../shared/services/objective.service';
import { concatMap, filter, Observable, of, tap } from 'rxjs';
import { KeyResultMeasure } from '../../shared/services/key-result.service';
import { KeyResultRowComponent } from '../../keyresult/key-result-row/key-result-row.component';

@Component({
  selector: 'app-objective-detail',
  templateUrl: './objective-detail.component.html',
  styleUrls: ['./objective-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveDetailComponent {
  @Input() objective!: Objective;
  @Input() keyResultList!: Observable<KeyResultMeasure[]>;

  removeKeyResult(keyResultId: number) {
    this.keyResultList.subscribe((data) => {
      data.forEach((element, index) => {
        if (element.id == keyResultId) data.splice(index, 1);
        this.keyResultList = of(data);
      });
    });
  }
}
