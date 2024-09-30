import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-puzzle-icon-button',
  templateUrl: './puzzle-icon-button.component.html',
  styleUrl: './puzzle-icon-button.component.scss',
})
export class PuzzleIconButtonComponent {
  @Input({ required: true })
  icon!: string;

  @Input({ required: true })
  alt!: string;

  @Input({ required: false })
  size: number = 24;

  readonly padding = 4;
  imgSize = this.size - 2 * this.padding;

  getStyle() {
    return {
      'border-radius': this.size / 2 + 'px',
      padding: this.padding + 'px',
    };
  }
}
