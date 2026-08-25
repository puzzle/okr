import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output, inject, effect } from '@angular/core';
import { QuarterService } from '../../../services/quarter.service';
import { ActivatedRoute, Router } from '@angular/router';
import { getValueFromQuery } from '../../common';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs';
import { Quarter } from '../../types/model/quarter';

@Component({
  selector: 'app-quarter-filter',
  templateUrl: './quarter-filter.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class QuarterFilterComponent {
  private readonly quarterService = inject(QuarterService);

  private readonly router = inject(Router);

  private readonly route = inject(ActivatedRoute);

  @Input() showBacklog = true;

  @Output() quarterLabel$ = new EventEmitter<string>();

  quarters = toSignal(this.quarterService.getAllQuarters(), { initialValue: [] });

  currentQuarterId = toSignal(this.route.queryParams.pipe(map((params) => getValueFromQuery(params['quarter'])[0])), { initialValue: -1 }); // TODO: this needs to be updated in the ticket: Query Params Service #1822

  constructor() {
    effect(() => this.emitQuarterLabel(this.currentQuarterId(), this.quarters()));
  }

  changeDisplayedQuarter(newId: number) {
    this.router.navigate([], {
      queryParams: { quarter: newId },
      queryParamsHandling: 'merge'
    });
  }

  emitQuarterLabel(targetQuarterId: number, availableQuarters: Quarter[]) {
    if (targetQuarterId !== -1 && availableQuarters.length > 0) {
      const matchedQuarter = availableQuarters.find((q) => q.id === targetQuarterId);
      const quarterLabel = matchedQuarter?.label || '';

      this.quarterLabel$.emit(quarterLabel);
    }
  }
}
