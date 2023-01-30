import { Component, Input } from '@angular/core';
import { KeyResultMeasure } from '../../shared/services/key-result.service';
import { RouteService } from '../../shared/services/route.service';

@Component({
  selector: 'app-key-result-overview',
  templateUrl: './key-result-overview.component.html',
  styleUrls: ['./key-result-overview.component.scss'],
})
export class KeyResultOverviewComponent {
  constructor(public routeService: RouteService) {}
  @Input() keyResult!: KeyResultMeasure;
}
