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
  ViewChild
} from '@angular/core';
import { Zone } from '../../types/enums/zone';
import { KeyResultMetricMin } from '../../types/model/key-result-metric-min';
import { Observable } from 'rxjs';
import { calculateCurrentPercentage } from '../../common';
import { KeyResultOrdinalMin } from '../../types/model/key-result-ordinal-min';
import { CheckInOrdinalMin } from '../../types/model/check-in-ordinal-min';

@Component({
  selector: 'app-scoring',
  templateUrl: './scoring.component.html',
  styleUrls: ['./scoring.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class ScoringComponent implements OnInit, AfterViewInit, OnChanges {
  @Input() keyResult!: KeyResultOrdinalMin | KeyResultMetricMin;

  @Input() isDetail!: boolean;

  iconPath = 'empty';

  failPercent = 0;

  commitPercent = 0;

  targetPercent = 0;

  labelPercentage: Observable<number>;

  stretched = false;

  @ViewChild('fail')
  private failElement: ElementRef<HTMLSpanElement> | undefined = undefined;

  @ViewChild('commit')
  private commitElement: ElementRef<HTMLSpanElement> | undefined = undefined;

  @ViewChild('target')
  private targetElement: ElementRef<HTMLSpanElement> | undefined = undefined;

  constructor(private changeDetectionRef: ChangeDetectorRef) {
    this.labelPercentage = new Observable<number>();
  }

  ngOnInit() {
    this.stretched = false;
    if (this.keyResult.lastCheckIn) {
      if (this.keyResult.keyResultType === 'metric') {
        this.calculatePercentageMetric();
      } else {
        this.calculatePercentageOrdinal();
      }
    }
  }

  ngAfterViewInit(): void {
    // Define width of scoring elements
    this.failElement!.nativeElement.style.width = this.failPercent + '%';
    this.commitElement!.nativeElement.style.width = this.commitPercent + '%';
    this.targetElement!.nativeElement.style.width = this.targetPercent + '%';

    // Set color of scoring component
    const scoringClass = this.getScoringColorClassAndSetBorder();
    if (scoringClass !== null) {
      this.targetElement!.nativeElement.classList.add(scoringClass);
      this.commitElement!.nativeElement.classList.add(scoringClass);
      this.failElement!.nativeElement.classList.add(scoringClass);
    }

    // Fill out icon if target percent has reached 100 percent or more
    if (this.stretched) {
      this.iconPath = 'filled';
      this.changeDetectionRef.detectChanges();
    }
  }

  calculatePercentageOrdinal() {
    switch ((this.keyResult.lastCheckIn as CheckInOrdinalMin).zone!) {
      case Zone.STRETCH:
        this.stretched = true;
        break;
      case Zone.TARGET:
        this.failPercent = 100;
        this.commitPercent = 100;
        this.targetPercent = 100;
        this.stretched = false;
        break;
      case Zone.COMMIT:
        this.failPercent = 100;
        this.commitPercent = 100;
        this.stretched = false;
        break;
      case Zone.FAIL:
        this.failPercent = 100;
        this.stretched = false;
        break;
    }
  }

  calculatePercentageMetric() {
    if (this.keyResult.lastCheckIn !== null) {
      const keyResult = this.keyResult as KeyResultMetricMin;
      const lastCheckIn = keyResult.lastCheckIn?.value!;

      if (lastCheckIn < keyResult.commitValue) {
        this.stretched = false;
        this.failPercent = (lastCheckIn - keyResult.baseline) / (keyResult.commitValue - keyResult.baseline) * 100;
      } else if (lastCheckIn < keyResult.targetValue) {
        this.stretched = false;
        this.failPercent = 100;
        this.commitPercent = (lastCheckIn - keyResult.commitValue) / (keyResult.targetValue - keyResult.commitValue) * 100;
      } else if (lastCheckIn < keyResult.stretchGoal) {
        this.stretched = false;
        this.failPercent = 100;
        this.commitPercent = 100;
        this.targetPercent = (lastCheckIn - keyResult.targetValue) / (keyResult.stretchGoal - keyResult.targetValue) * 100;
      } else if (lastCheckIn >= keyResult.stretchGoal) {
        this.stretched = true;
      }
    }
  }

  getScoringColorClassAndSetBorder(): string | null {
    if (this.targetPercent > 100) {
      return 'score-stretch';
    } else if (this.targetPercent > 0 || this.commitPercent == 100 && this.keyResult.keyResultType === 'metric') {
      return 'score-green';
    } else if (this.commitPercent > 0 || this.failPercent == 100 && this.keyResult.keyResultType === 'metric') {
      return 'score-yellow';
    } else if (this.failPercent >= 3.3333) {
      // 3.3333% because if lower fail is not visible in overview and we display !
      return 'score-red';
    } else {
      return null;
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
    const classArray: string[] = [
      'score-red',
      'score-green',
      'score-yellow',
      'score-stretch',
      'border-right'
    ];
    for (const classToRemove of classArray) {
      this.commitElement?.nativeElement.classList.remove(classToRemove);
      this.targetElement?.nativeElement.classList.remove(classToRemove);
      this.failElement?.nativeElement.classList.remove(classToRemove);
    }
  }

  castToMetric(): KeyResultMetricMin {
    return this.keyResult as KeyResultMetricMin;
  }

  protected readonly calculateCurrentPercentage = calculateCurrentPercentage;
}
