import { Component } from '@angular/core';
import { isMobileDevice } from '../../common';
import { ConfigService } from '../../../services/config.service';
import { BehaviorSubject, map, Observable, Subject, Subscription } from 'rxjs';

@Component({
  selector: 'app-okr-tangram',
  templateUrl: 'okr-tangram.component.html',
  styleUrl: 'okr-tangram.component.scss',
})
export class OkrTangramComponent {
  private readonly DEFAULT_TRIANGLE_SRC = 'assets/images/empty.svg';
  trianglesSrc$ = new Observable<string>();

  constructor(private readonly configService: ConfigService) {
    this.trianglesSrc$ = this.configService.config$.pipe(
      map((config) => config.triangles || this.DEFAULT_TRIANGLE_SRC),
    );
  }
}
