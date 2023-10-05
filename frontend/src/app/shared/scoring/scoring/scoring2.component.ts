import { AfterViewInit, ChangeDetectionStrategy, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { KeyresultMin } from '../../types/model/KeyresultMin';
import { Zone } from '../../types/enums/Zone';
import { KeyResultMetricMin } from '../../types/model/KeyResultMetricMin';

@Component({
  selector: 'app-scoring2',
  templateUrl: './scoring2.component.html',
  styleUrls: ['./scoring2.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Scoring2Component implements OnInit, AfterViewInit {
  @Input() keyResult!: KeyresultMin;
  iconPath: string = 'empty';
  failPercent: number = 0;
  commitPercent: number = 0;
  targetPercent: number = 0;

  @ViewChild('fail')
  private failElement: ElementRef<HTMLSpanElement> | undefined = undefined;
  @ViewChild('commit')
  private commitElement: ElementRef<HTMLSpanElement> | undefined = undefined;
  @ViewChild('target')
  private targetElement: ElementRef<HTMLSpanElement> | undefined = undefined;

  ngOnInit() {
    if (this.keyResult.keyResultType === 'metric') {
      this.calculatePercentageMetric();
    } else {
      this.calculatePercentageOrdinal();
    }
  }

  calculatePercentageOrdinal() {
    //TODO: Check why the lastCheckIn value is null
    if (this.keyResult.lastCheckIn?.value === Zone.STRETCH) {
      this.failPercent = 100;
      this.commitPercent = 100;
      this.targetPercent = 100;
    } else if (this.keyResult.lastCheckIn?.value === Zone.COMMIT) {
      this.failPercent = 100;
      this.commitPercent = 100;
    } else if (this.keyResult.lastCheckIn?.value === Zone.FAIL) {
      this.failPercent = 100;
    }
  }

  calculatePercentageMetric() {
    let castedKeyResult = this.keyResult as KeyResultMetricMin;
    if (this.keyResult.lastCheckIn !== null) {
      let percentage =
        ((+castedKeyResult.lastCheckIn!.value! - castedKeyResult.baseline) /
          (castedKeyResult.stretchGoal - castedKeyResult.baseline)) *
        100;
      if (percentage > 100) {
        this.failPercent = 100;
        this.commitPercent = 100;
        this.targetPercent = 100;
      } else if (percentage > 70) {
        this.failPercent = 100;
        this.commitPercent = 100;
        this.targetPercent = (100 / 30) * (percentage - 70);
      } else if (percentage > 30) {
        this.failPercent = 100;
        this.commitPercent = (100 / 40) * (percentage - 30);
      } else {
        this.failPercent = (100 / 30) * percentage;
      }
    }
  }

  ngAfterViewInit(): void {
    // use failPercent, commitPercent and targetPercent for set the backgrounds
    if (this.failElement) {
      this.failElement.nativeElement.classList.add('score-red');
      this.failElement.nativeElement.style.width = this.failPercent + '%';
    }
    if (this.commitElement) {
      this.commitElement.nativeElement.classList.add('score-yellow');
      this.commitElement.nativeElement.style.width = this.commitPercent + '%';
    }
    if (this.targetElement) {
      this.targetElement.nativeElement.classList.add('score-green');
      this.targetElement.nativeElement.style.width = this.targetPercent + '%';
    }
  }
}
