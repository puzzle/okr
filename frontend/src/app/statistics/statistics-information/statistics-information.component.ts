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

  @Input() chartColor = '';

  @Input() chartSizeMultiplier = 1;

  @Input() maxHeightInPx = 50;

  get chartHeight(): string {
    // Chart value is already a percentage. We can expect it to never be higher than 1.
    const heightInPx = this.chartValue * this.maxHeightInPx;
    return heightInPx + 'px';
  }
}
