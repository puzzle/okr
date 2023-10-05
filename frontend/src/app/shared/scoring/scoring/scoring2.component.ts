import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  Input,
  OnInit,
  ViewChild,
} from '@angular/core';
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

  constructor(private changeDetectionRef: ChangeDetectorRef) {}

  ngOnInit() {
    if (this.keyResult.keyResultType === 'metric') {
      this.calculatePercentageMetric();
    } else {
      this.calculatePercentageOrdinal();
    }
  }

  calculatePercentageOrdinal() {
    if (this.keyResult.lastCheckIn?.value === Zone.STRETCH) {
      this.failPercent = 100;
      this.commitPercent = 100;
      this.targetPercent = 101;
    } else if (this.keyResult.lastCheckIn?.value === Zone.TARGET) {
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
      if (percentage >= 100) {
        this.failPercent = 100;
        this.commitPercent = 100;
        this.targetPercent = 101;
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

  getScoringColorClassAndSetBorder(): string | null {
    if (this.targetPercent > 100) {
      return 'score-stretch';
    } else if (this.targetPercent > 0) {
      this.targetElement!.nativeElement.classList.add('border-right');
      return 'score-green';
    } else if (this.commitPercent > 0) {
      this.commitElement!.nativeElement.classList.add('border-right');
      return 'score-yellow';
    } else if (this.failPercent > 0) {
      this.failElement!.nativeElement.classList.add('border-right');
      return 'score-red';
    } else {
      return null;
    }
  }

  ngAfterViewInit(): void {
    //Define width of scoring elements
    this.failElement!.nativeElement.style.width = this.failPercent + '%';
    this.commitElement!.nativeElement.style.width = this.commitPercent + '%';
    this.targetElement!.nativeElement.style.width = this.targetPercent + '%';

    // Set color of scoring component
    let scoringClass = this.getScoringColorClassAndSetBorder();
    if (scoringClass !== null) {
      this.targetElement!.nativeElement.classList.add(scoringClass);
      this.commitElement!.nativeElement.classList.add(scoringClass);
      this.failElement!.nativeElement.classList.add(scoringClass);
    }

    //Fill out icon if target percent has reached 100 percent or more
    if (this.targetPercent > 100) {
      this.iconPath = 'filled';
      this.changeDetectionRef.detectChanges();
    }
  }
}
