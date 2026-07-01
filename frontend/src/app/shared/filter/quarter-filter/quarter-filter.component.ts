import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output, inject, effect } from '@angular/core';
import { QuarterService } from '../../../services/quarter.service';
import { ActivatedRoute, Router } from '@angular/router';
import { RefreshDataService } from '../../../services/refresh-data.service';
import { getValueFromQuery } from '../../common';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs';

@Component({
  selector: 'app-quarter-filter',
  templateUrl: './quarter-filter.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class QuarterFilterComponent {
  private quarterService = inject(QuarterService);

  private router = inject(Router);

  private route = inject(ActivatedRoute);

  private refreshDataService = inject(RefreshDataService);

  @Input() showBacklog = true;

  @Output() quarterLabel$ = new EventEmitter<string>();

  quarters = toSignal(this.quarterService.getAllQuarters(), { initialValue: [] });

  currentQuarterId = toSignal(this.route.queryParams.pipe(map((params) => Number(getValueFromQuery(params['quarter'])[0]))), { initialValue: -1 });

  constructor() {
    effect(() => {
      const id = this.currentQuarterId();
      const quartersList = this.quarters();

      if (id !== -1 && quartersList.length > 0) {
        const label = quartersList.find((e) => e.id === id)?.label || '';
        this.quarterLabel$.emit(label);
      }
    });
  }

  changeDisplayedQuarter(newId: number) {
    this.router.navigate([], {
      queryParams: { quarter: newId },
      queryParamsHandling: 'merge'
    });
  }
}
