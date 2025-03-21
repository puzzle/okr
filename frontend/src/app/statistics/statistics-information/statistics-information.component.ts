import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-statistics-information',
  standalone: false,
  templateUrl: './statistics-information.component.html',
  styleUrl: './statistics-information.component.scss'
})
export class StatisticsInformationComponent {
  @Input() content = '';

  @Input() bottomContent = '';

  @Input() topContent = '';

  @Input() chartValue = '';

  @Input() chartColor = '';
}
