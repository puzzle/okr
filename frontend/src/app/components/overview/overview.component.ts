import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { OverviewService } from '../../services/overview.service';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class OverviewComponent {
  private readonly overviewService = inject(OverviewService);

  readonly data = this.overviewService.data;
}
