import { Component } from '@angular/core';
import { isMobileDevice } from '../../common';
import { ConfigService } from '../../../services/config.service';
import { BehaviorSubject, Subscription } from 'rxjs';

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

  private subscription?: Subscription;
  trianglesSrc$ = new BehaviorSubject<String>('assets/images/empty.svg');

  constructor(private configService: ConfigService) {}

  ngOnInit(): void {
    this.subscription = this.configService.config$.subscribe((config) => {
      if (config.triangles) {
        this.trianglesSrc$.next(config.triangles);
      }
    });
  }
}
