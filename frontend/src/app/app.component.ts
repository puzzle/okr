import { ChangeDetectionStrategy, Component } from '@angular/core';
import { StyleService } from './style.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AppComponent {
  constructor(private readonly styleService: StyleService) {}
  public getBodyClass() {
    return this.styleService.bodyClass;
  }
}
