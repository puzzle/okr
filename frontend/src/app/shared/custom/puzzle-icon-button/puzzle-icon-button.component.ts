import { Component, input, computed } from '@angular/core';

@Component({
  selector: 'app-puzzle-icon-button',
  templateUrl: './puzzle-icon-button.component.html',
  styleUrl: './puzzle-icon-button.component.scss',
  standalone: false
})
export class PuzzleIconButtonComponent {
  icon = input.required<string>();

  alt = input.required<string>();

  size = input<number>(24);

  disabled = input<boolean>(false);

  readonly padding = 4;

  imgSize = computed(() => this.size() - 2 * this.padding);

  style = computed(() => {
    return {
      'border-radius': `${this.size() / 2}px`,
      padding: `${this.padding}px`
    };
  });
}
