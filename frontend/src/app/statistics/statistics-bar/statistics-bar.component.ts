import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-statistics-bar',
  templateUrl: './statistics-bar.component.html',
  styleUrl: './statistics-bar.component.scss',
  standalone: false
})
export class StatisticsBarComponent {
  @Input() progress = 0;

  @Input() colorPreset?: string;

  getBarClass() {
    const base = 'progress-bar-color';
    return base + (this.colorPreset ?? '__progress');
  }
}
