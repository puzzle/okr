import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { KeyresultMin } from '../../types/model/KeyresultMin';
import { Router } from '@angular/router';
import { KeyResultMetricMin } from '../../types/model/KeyResultMetricMin';
import { KeyResultOrdinalMin } from '../../types/model/KeyResultOrdinalMin';

@Component({
  selector: 'app-scoring',
  templateUrl: './scoring.component.html',
  styleUrls: ['./scoring.component.scss'],
})
export class ScoringComponent implements OnInit, AfterViewInit {
  @Input() keyResult!: KeyresultMin;
  failWidth: string = '50%';
  commitWidth: string = '0';
  targetWidth: string = '0';
  failColor: string = '#ffffff';
  commitColor: string = '#ffffff';
  targetColor: string = '#ffffff';
  iconPath: string = 'empty';
  metricLabel: string = '';
  isOverview: boolean = false;
  labelWidth: string = '0';
  endLineFail: string = '';
  endLineCommit: string = '';
  endLineTarget: string = '';
  zoneWidth: number = 0;
  checkInValue: number | string = 0;

  resizeObserver: ResizeObserver = new ResizeObserver(() => {
    this.resizeWidth();
  });

  constructor(private router: Router) {}

  ngOnInit(): void {
    let url = this.router.url;
    this.isOverview = !url.includes('keyresult');
    if (this.keyResult.lastCheckIn === undefined || this.keyResult.lastCheckIn === null) {
      this.failWidth = this.commitWidth = this.targetWidth = '0';
      return;
    }
    let keyResultCardWidth = document.querySelector('.key-result')!.clientWidth;
    this.zoneWidth = this.isOverview ? (keyResultCardWidth / 100) * 24 : (keyResultCardWidth / 100) * 33;
    this.checkInValue = this.keyResult.lastCheckIn!.value;
    if (this.keyResult.keyResultType === 'metric') {
      this.calculatePercentagesMetric();
      this.setBarColorsMetric();
    } else {
      this.keyResult = this.keyResult as KeyResultOrdinalMin;
      this.setColorsOrdinal();
    }
  }

  ngAfterViewInit() {
    this.resizeObserver.observe(document.querySelector('.key-result')!);
  }

  resizeWidth() {
    let keyResultCardWidth = document.querySelector('.key-result')!.clientWidth;
    this.zoneWidth = this.isOverview ? (keyResultCardWidth / 100) * 24 : (keyResultCardWidth / 100) * 33;
    this.calculatePercentagesMetric();
    document.getElementById('fail-card' + this.keyResult.id)!.style.width = this.zoneWidth + 'px';
    document.getElementById('commit-card' + this.keyResult.id)!.style.width = this.zoneWidth + 'px';
    document.getElementById('target-card' + this.keyResult.id)!.style.width = this.zoneWidth + 'px';
    document.getElementById('unit-label-div' + this.keyResult.id)!.style.width = this.zoneWidth * 3 + 'px';
    document.getElementById('unit-label' + this.keyResult.id)!.style.width = this.labelWidth;
    document.getElementById('unit-label' + this.keyResult.id)!.style.maxWidth = this.zoneWidth * 3 + 'px';
  }

  calculatePercentagesMetric() {
    let keyResult: KeyResultMetricMin = this.keyResult as KeyResultMetricMin;
    let baseline: number = keyResult.baseline;
    let stretchGoal: number = keyResult.stretchGoal;
    let checkInValue: number = this.checkInValue as number;
    this.metricLabel = keyResult.unit + ' ' + checkInValue;

    if (baseline > stretchGoal) {
      baseline = stretchGoal;
      stretchGoal = keyResult.baseline;
    }

    let decimal: number = (checkInValue - baseline) / (stretchGoal - baseline);

    if (decimal < 0.3) {
      let percent = (decimal * 100) / 30;
      this.failWidth = percent * 100 + '%';
      this.labelWidth = this.zoneWidth * percent + 'px';
      this.commitWidth = this.targetWidth = '0';
      if (this.failWidth != '100%' && this.failWidth != '0') {
        this.endLineFail = 'endLine';
      }
    } else if (decimal < 0.7) {
      let percent = (decimal * 100 - 30) / 40;
      this.failWidth = '100%';
      this.commitWidth = percent * 100 + '%';
      this.targetWidth = '0';
      this.labelWidth = this.zoneWidth + this.zoneWidth * percent + 'px';
      if (this.commitWidth != '100%' && this.commitWidth != '0') {
        this.endLineCommit = 'endLine';
      }
    } else {
      let percent = (decimal * 100 - 70) / 30;
      this.targetWidth = decimal > 1.0 ? '100%' : percent * 100 + '%';
      this.labelWidth = this.zoneWidth + this.zoneWidth + this.zoneWidth * percent + 'px';
      this.failWidth = this.commitWidth = '100%';
      if (this.targetWidth != '100%' && this.targetWidth != '0') {
        this.endLineTarget = 'endLine';
      }
    }
  }

  setBarColorsMetric() {
    if (this.failWidth !== '100%') {
      this.failColor = '#BA3838';
      this.commitColor = this.targetColor = '#ffffff';
    } else if (this.commitWidth !== '100%') {
      this.failColor = this.commitColor = '#FFD600';
      this.targetColor = '#ffffff';
    } else if (this.targetWidth !== '100%') {
      this.failColor = this.commitColor = this.targetColor = '#1E8A29';
    } else {
      this.setStretchIcon();
    }
  }

  setColorsOrdinal() {
    let value = this.checkInValue as string;
    switch (value) {
      case 'FAIL':
        this.setOnFailColor();
        break;
      case 'COMMIT':
        this.setOnCommitColor();
        break;
      case 'TARGET':
        this.setOnTargetColor();
        break;
      default:
        this.failWidth = this.commitWidth = this.targetWidth = '100%';
        this.setStretchIcon();
    }
  }

  setOnFailColor() {
    this.failWidth = '100%';
    this.failColor = '#BA3838';
    this.commitColor = this.targetColor = '#ffffff';
  }

  setOnCommitColor() {
    this.failWidth = this.commitWidth = '100%';
    this.failColor = this.commitColor = '#FFD600';
    this.targetColor = '#ffffff';
  }

  setOnTargetColor() {
    this.failWidth = this.commitWidth = this.targetWidth = '100%';
    this.failColor = this.commitColor = this.targetColor = '#1E8A29';
  }

  setStretchIcon() {
    this.iconPath = 'filled';
    this.failColor = this.commitColor = this.targetColor = 'url("../../../../assets/images/scoring-stars.svg")';
  }
}
