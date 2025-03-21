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

  @Input() chartValue: number | string = '';

  @Input() chartColor: number | string = '';
}
