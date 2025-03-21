import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-statistics-bar',
  templateUrl: './statistics-bar.component.html',
  styleUrl: './statistics-bar.component.scss',
  standalone: false
})
export class StatisticsBarComponent {
  @Input() progress = 0;

  @Input() primaryColor = '#000000';

  @Input() secondaryColor = '#000000';
}
