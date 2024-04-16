import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-puzzle-icon',
  templateUrl: './puzzle-icon.component.html',
})
export class PuzzleIconComponent {
  @Input({ required: true })
  icon!: string;

  @Input({ required: true })
  alt!: string;

  @Input({ required: false })
  size: number = 16;
}