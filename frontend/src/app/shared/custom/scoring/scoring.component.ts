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
import { Observable, of } from 'rxjs';
import { calculateCurrentPercentage, isLastCheckInNegative } from '../../common';
import { CheckInMinOrdinal } from '../../types/model/CheckInMin';
import { CheckInService } from '../../../services/check-in.service';
import { KeyResultMetric } from '../../types/model/KeyResultMetric';
import { KeyResultOrdinal } from '../../types/model/KeyResultOrdinal';
import { keyResult } from '../../testData';
import { KeyResultOrdinalMin } from '../../types/model/KeyResultOrdinalMin';

@Component({
  selector: 'app-scoring',
  templateUrl: './scoring.component.html',
  styleUrls: ['./scoring.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ScoringComponent implements OnInit, AfterViewInit, OnChanges {
  @Input() keyResult!: KeyResultOrdinalMin | KeyResultMetricMin;
  @Input() isDetail!: boolean;
  iconPath: string = 'empty';
  failPercent: number = 0;
  commitPercent: number = 0;
  targetPercent: number = 0;
  labelPercentage: Observable<number>;
  stretched: boolean = false;
  protected readonly isLastCheckInNegative = isLastCheckInNegative;

  @ViewChild('fail')
  private failElement: ElementRef<HTMLSpanElement> | undefined = undefined;
  @ViewChild('commit')
  private commitElement: ElementRef<HTMLSpanElement> | undefined = undefined;
  @ViewChild('target')
  private targetElement: ElementRef<HTMLSpanElement> | undefined = undefined;
  @ViewChild('valueLabel')
  private valueLabel: ElementRef<HTMLSpanElement> | undefined = undefined;

  constructor(
    private changeDetectionRef: ChangeDetectorRef,
    private checkInService: CheckInService,
  ) {
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
    //Define width of scoring elements
    this.failElement!.nativeElement.style.width = this.failPercent + '%';
    this.commitElement!.nativeElement.style.width = this.commitPercent + '%';
    this.targetElement!.nativeElement.style.width = this.targetPercent + '%';

    if (this.valueLabel != undefined && this.keyResult.keyResultType == 'metric') {
      this.labelPercentage.subscribe((value) => {
        this.valueLabel!.nativeElement.style.width = value + '%';
        this.changeDetectionRef.detectChanges();
      });
    }

    // Set color of scoring component
    let scoringClass = this.getScoringColorClassAndSetBorder();
    if (scoringClass !== null) {
      this.targetElement!.nativeElement.classList.add(scoringClass);
      this.commitElement!.nativeElement.classList.add(scoringClass);
      this.failElement!.nativeElement.classList.add(scoringClass);
    }

    //Fill out icon if target percent has reached 100 percent or more
    if (this.stretched) {
      this.iconPath = 'filled';
      this.changeDetectionRef.detectChanges();
    }
  }

  calculatePercentageOrdinal() {
    switch ((this.keyResult as KeyResultOrdinalMin).lastCheckIn!.zone!) {
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
    if ((this.keyResult as KeyResultMetric).lastCheckIn !== null) {
      let keyResultMetric: KeyResultMetricMin = this.castToMetric();
      let percentage = calculateCurrentPercentage(keyResultMetric);
      this.labelPercentage = of(percentage);
      if (percentage < 30) {
        this.stretched = false;
        this.failPercent = (100 / 30) * percentage;
      } else if (percentage < 70) {
        this.stretched = false;
        this.failPercent = 100;
        this.commitPercent = (100 / 40) * (percentage - 30);
      } else if (percentage < 100) {
        this.stretched = false;
        this.failPercent = 100;
        this.commitPercent = 100;
        this.targetPercent = (100 / 30) * (percentage - 70);
      } else if (percentage >= 100) {
        this.stretched = true;
      }
    }
  }

  getScoringColorClassAndSetBorder(): string | null {
    if (this.targetPercent > 100) {
      return 'score-stretch';
    } else if (this.targetPercent > 0 || (this.commitPercent == 100 && this.keyResult.keyResultType === 'metric')) {
      return 'score-green';
    } else if (this.commitPercent > 0 || (this.failPercent == 100 && this.keyResult.keyResultType === 'metric')) {
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
    let classArray: string[] = ['score-red', 'score-green', 'score-yellow', 'score-stretch', 'border-right'];
    for (let classToRemove of classArray) {
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
