import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import { KeyresultMin } from '../../types/model/KeyresultMin';
import { Zone } from '../../types/enums/Zone';
import { KeyResultMetricMin } from '../../types/model/KeyResultMetricMin';

@Component({
  selector: 'app-scoring',
  templateUrl: './scoring.component.html',
  styleUrls: ['./scoring.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ScoringComponent implements OnInit, AfterViewInit, OnChanges {
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
    switch (this.keyResult.lastCheckIn?.value) {
      case Zone.STRETCH:
        this.failPercent = 100;
        this.commitPercent = 100;
        this.targetPercent = 101;
        break;
      case Zone.TARGET:
        this.failPercent = 100;
        this.commitPercent = 100;
        this.targetPercent = 100;
        break;
      case Zone.COMMIT:
        this.failPercent = 100;
        this.commitPercent = 100;
        break;
      case Zone.FAIL:
        this.failPercent = 100;
        break;
    }
  }

  calculatePercentageMetric() {
    if (this.keyResult.lastCheckIn !== null) {
      let percentage: number = this.calculateCurrentPercentage();
      switch (true) {
        case percentage >= 100:
          this.failPercent = 100;
          this.commitPercent = 100;
          this.targetPercent = 101;
          break;
        case percentage > 70:
          this.failPercent = 100;
          this.commitPercent = 100;
          this.targetPercent = (100 / 30) * (percentage - 70);
          break;
        case percentage > 30:
          this.failPercent = 100;
          this.commitPercent = (100 / 40) * (percentage - 30);
          break;
        default:
          this.failPercent = (100 / 30) * percentage;
      }
    }
  }

  calculateCurrentPercentage(): number {
    let castedKeyResult: KeyResultMetricMin = this.keyResult as KeyResultMetricMin;
    let value: number = +castedKeyResult.lastCheckIn?.value!;
    let baseline: number = +castedKeyResult.baseline;
    let stretchGoal: number = +castedKeyResult.stretchGoal;
    return (Math.abs(value - baseline) / Math.abs(stretchGoal - baseline)) * 100;
  }

  getScoringColorClassAndSetBorder(): string | null {
    switch (true) {
      case this.targetPercent > 100:
        return 'score-stretch';
      case this.targetPercent > 0:
        this.targetElement!.nativeElement.classList.add('border-right');
        return 'score-green';
      case this.commitPercent > 0:
        this.commitElement!.nativeElement.classList.add('border-right');
        return 'score-yellow';
      case this.failPercent > 0:
        this.failElement!.nativeElement.classList.add('border-right');
        return 'score-red';
      default:
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

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['keyResult']?.currentValue !== undefined || changes['keyResult']?.currentValue !== null) {
      if (this.commitElement != undefined) {
        this.resetPercentagesToZero();
        this.removeStyleClass();
        this.iconPath = 'empty';
        this.ngOnInit();
        this.ngAfterViewInit();
      }
    }
  }

  resetPercentagesToZero() {
    this.commitPercent = 0;
    this.targetPercent = 0;
    this.failPercent = 0;
  }

  removeStyleClass() {
    let classArray: string[] = ['score-red', 'score-green', 'score-yellow', 'score-stretch'];
    for (let classToRemove of classArray) {
      this.commitElement?.nativeElement.classList.remove(classToRemove);
      this.targetElement?.nativeElement.classList.remove(classToRemove);
      this.failElement?.nativeElement.classList.remove(classToRemove);
    }
  }
}
