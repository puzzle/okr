import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AppComponent {
  public overviewClick() {
    console.log('Overview component will be displayed');
  }
  public teamClick() {
    console.log('Team list will be displayed');
  }
}
