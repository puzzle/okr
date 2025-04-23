import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-statistics-card',
  templateUrl: './statistics-card.component.html',
  styleUrl: './statistics-card.component.scss',
  standalone: false
})
export class StatisticsCardComponent {
  @Input() title = '';

  @Input() barProgress?: number;

  @Input() barColorPreset?: string;
}
