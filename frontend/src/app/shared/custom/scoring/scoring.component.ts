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
import { calculateCurrentPercentage } from '../../common';

@Component({
  selector: 'app-scoring',
  templateUrl: './scoring.component.html',
  styleUrls: ['./scoring.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ScoringComponent implements OnInit, AfterViewInit, OnChanges {
  @Input() keyResult!: KeyresultMin;
  @Input() isDetail!: boolean;
  iconPath: string = 'empty';
  failPercent: number = 0;
  commitPercent: number = 0;
  targetPercent: number = 0;
  labelPercentage: Observable<number>;
  stretched: boolean = false;

  @ViewChild('fail')
  private failElement: ElementRef<HTMLSpanElement> | undefined = undefined;
  @ViewChild('commit')
  private commitElement: ElementRef<HTMLSpanElement> | undefined = undefined;
  @ViewChild('target')
  private targetElement: ElementRef<HTMLSpanElement> | undefined = undefined;
  @ViewChild('valueLabel')
  private valueLabel: ElementRef<HTMLSpanElement> | undefined = undefined;

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
    if (this.keyResult.lastCheckIn) {
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
  }

  calculatePercentageOrdinal() {
    switch (this.keyResult.lastCheckIn?.value) {
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
    let KeyResultMetric: KeyResultMetricMin = this.castToMetric();

    let percentage = calculateCurrentPercentage(KeyResultMetric);
    this.labelPercentage = of(percentage);
    switch (true) {
      case percentage >= 100:
        this.stretched = true;
        break;
      case percentage > 70:
        this.stretched = false;
        this.failPercent = 100;
        this.commitPercent = 100;
        this.targetPercent = (100 / 30) * (percentage - 70);
        break;
      case percentage > 30:
        this.stretched = false;
        this.failPercent = 100;
        this.commitPercent = (100 / 40) * (percentage - 30);
        break;
      default:
        this.stretched = false;
        this.failPercent = (100 / 30) * percentage;
    }
  }

  getScoringColorClassAndSetBorder(): string | null {
    if (this.targetPercent > 100) {
      return 'score-stretch';
    } else if (this.targetPercent > 0) {
      this.setBorder(this.targetElement!);
      return 'score-green';
    } else if (this.commitPercent > 0) {
      this.setBorder(this.commitElement!);
      return 'score-yellow';
    } else if (this.failPercent > 0) {
      this.setBorder(this.failElement!);
      return 'score-red';
    } else {
      return null;
    }
  }

  setBorder(element: ElementRef<HTMLSpanElement>) {
    if (this.keyResult.keyResultType != 'ordinal') {
      element.nativeElement.classList.add('border-right');
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
}
