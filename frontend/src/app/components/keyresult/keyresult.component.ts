import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { KeyresultMin } from '../../shared/types/model/KeyresultMin';
import { Router } from '@angular/router';
import { DATE_FORMAT } from '../../shared/constantLibary';
import { CheckInMinOrdinal } from '../../shared/types/model/CheckInMin';

@Component({
  selector: 'app-keyresult',
  templateUrl: './keyresult.component.html',
  styleUrls: ['./keyresult.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KeyresultComponent {
  @Input() keyResult!: KeyresultMin;
  protected readonly DATE_FORMAT = DATE_FORMAT;

  constructor(private router: Router) {}

  ngOnInit() {
    if (this.keyResult.lastCheckIn) {
      const lastCheckIn = this.keyResult.lastCheckIn as { value: any };
      this.keyResult.lastCheckIn as CheckInMinOrdinal;
      this.keyResult.lastCheckIn = {
        ...this.keyResult.lastCheckIn,
        zone: lastCheckIn.value,
      };
    }
  }

  openDrawer() {
    this.router.navigate(['details/keyresult', this.keyResult.id]);
  }
}
