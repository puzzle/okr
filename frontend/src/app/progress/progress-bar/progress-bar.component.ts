import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  Input,
  OnChanges,
  OnInit,
  ViewChild,
} from '@angular/core';

@Component({
  selector: 'app-progress-bar',
  templateUrl: './progress-bar.component.html',
  styleUrls: ['./progress-bar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProgressBarComponent implements OnInit, OnChanges {
  @ViewChild('progressbar') progressBar?: ElementRef;

  @Input() value!: number;
  @Input() colorLow!: string;
  @Input() colorMiddle!: string;
  @Input() colorHigh!: string;
  @Input() limitLow!: number;
  @Input() limitHigh!: number;
  color!: string;

  ngOnInit(): void {
    this.setColor();
  }

  ngOnChanges(): void {
    this.setColor();
  }

  public setColor() {
    if (this.value >= this.limitHigh) {
      this.color = this.colorHigh;
    } else if (this.value < this.limitLow) {
      this.color = this.colorLow;
    } else {
      this.color = this.colorMiddle;
    }
  }
}
