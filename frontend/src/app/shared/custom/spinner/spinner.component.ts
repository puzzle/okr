import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styleUrl: './spinner.component.scss',
  standalone: false
})
export class SpinnerComponent {
  @Input({ required: false })
  text: string | undefined;
}
