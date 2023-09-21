import { Component, Input, OnInit } from '@angular/core';
import { KeyresultMin } from '../../types/model/KeyresultMin';
import { Router } from '@angular/router';
import { KeyResultMetricMin } from '../../types/model/KeyResultMetricMin';
import { KeyResultOrdinalMin } from '../../types/model/KeyResultOrdinalMin';

@Component({
  selector: 'app-scoring',
  templateUrl: './scoring.component.html',
  styleUrls: ['./scoring.component.scss'],
})
export class ScoringComponent implements OnInit {
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

  constructor(private router: Router) {}

  ngOnInit(): void {
    let url = this.router.url;
    if (url.includes('keyresult')) {
      this.isOverview = false;
      this.zoneWidth = 98;
    } else {
      this.isOverview = true;
      this.zoneWidth = 63;
    }

    if (this.keyResult.lastCheckIn === undefined || this.keyResult.lastCheckIn === null) {
      this.failWidth = this.commitWidth = this.targetWidth = '0';
    } else {
      this.checkInValue = this.keyResult.lastCheckIn!.value;
      if (this.keyResult.keyResultType === 'metric') {
        this.calculatePercentagesMetric();
        this.setBarColorsMetric();
      } else {
        this.keyResult = this.keyResult as KeyResultOrdinalMin;
        this.setColorsOrdinal();
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

  calculatePercentagesMetric() {
    let keyResult: KeyResultMetricMin = this.keyResult as KeyResultMetricMin;
    let baseline: number = keyResult.baseLine;
    let stretchGoal: number = keyResult.stretchGoal;
    let checkInValue: number = this.checkInValue as number;
    this.metricLabel = keyResult.unit + checkInValue;

    if (baseline > stretchGoal) {
      baseline = stretchGoal;
      stretchGoal = keyResult.baseLine;
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

  setColorsOrdinal() {
    if ((this.checkInValue as string) === 'FAIL') {
      this.failWidth = '100%';
      this.failColor = '#BA3838';
      this.commitColor = this.targetColor = '#ffffff';
    } else if ((this.checkInValue as string) === 'COMMIT') {
      this.failWidth = this.commitWidth = '100%';
      this.failColor = this.commitColor = '#FFD600';
      this.targetColor = '#ffffff';
    } else if ((this.checkInValue as string) === 'TARGET') {
      this.failWidth = this.commitWidth = this.targetWidth = '100%';
      this.failColor = this.commitColor = this.targetColor = '#1E8A29';
    } else {
      this.failWidth = this.commitWidth = this.targetWidth = '100%';
      this.setStretchIcon();
    }
  }

  setStretchIcon() {
    this.iconPath = 'filled';
    this.failColor = this.commitColor = this.targetColor = 'url("../../../../assets/images/scoring-stars.svg")';
  }
}
