import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-puzzle-icon',
  templateUrl: './puzzle-icon.component.html',
  standalone: false
})
export class PuzzleIconComponent {
  @Input({ required: true })
  icon!: string;

  @Input({ required: true })
  alt!: string;

  @Input({ required: false })
  size = 16;
}
