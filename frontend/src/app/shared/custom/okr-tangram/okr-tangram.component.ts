import { Component } from '@angular/core';
import { isMobileDevice } from '../../common';

@Component({
  selector: 'app-okr-tangram',
  templateUrl: 'okr-tangram.component.html',
  styleUrl: 'okr-tangram.component.scss',
})
export class OkrTangramComponent {
  private readonly MOBILE_WIDTH = 100;
  private readonly DESKTOP_WIDTH = 274;
  getWidth() {
    return isMobileDevice() ? this.MOBILE_WIDTH : this.DESKTOP_WIDTH;
  }
}
