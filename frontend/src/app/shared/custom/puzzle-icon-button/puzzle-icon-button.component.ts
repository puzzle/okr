import { Component, input } from '@angular/core';

@Component({
  selector: 'app-puzzle-icon-button',
  templateUrl: './puzzle-icon-button.component.html',
  styleUrl: './puzzle-icon-button.component.scss',
  standalone: false
})
export class PuzzleIconButtonComponent {
  icon = input.required<string>();

  alt = input.required<string>();

  disabled = input<boolean>(false);
}
