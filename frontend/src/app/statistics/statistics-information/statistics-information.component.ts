import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-statistics-information',
  standalone: false,
  templateUrl: './statistics-information.component.html',
  styleUrl: './statistics-information.component.scss'
})
export class StatisticsInformationComponent {
  @Input() bottomContent: any = '';

  @Input() topContent: number | string = '';

  @Input() chartValue = 0;

  @Input() chartColor: number | string = '';

  @Input() chartSizeMultiplier = 1;

  @Input() maxHeightInPx = 50;

  get chartHeight(): string {
    const percentage = this.chartValue / this.maxHeightInPx * 100;
    const heightInPx = percentage * this.maxHeightInPx;
    return heightInPx + 'px';
  }
}
