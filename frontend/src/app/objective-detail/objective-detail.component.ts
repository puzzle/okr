import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Objective } from '../shared/types/model/Objective';

@Component({
  selector: 'app-objective-detail',
  templateUrl: './objective-detail.component.html',
  styleUrls: ['./objective-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveDetailComponent implements OnInit {
  @Input() objective!: Objective;
  @Output() close: EventEmitter<any> = new EventEmitter<any>();
  constructor() {}

  closeDrawer() {
    this.close.emit();
  }
  ngOnInit(): void {
  }
}
