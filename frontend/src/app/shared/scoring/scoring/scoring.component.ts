import { Component, OnInit } from '@angular/core';
import { KeyresultMin } from '../../types/model/KeyresultMin';

@Component({
  selector: 'app-scoring',
  templateUrl: './scoring.component.html',
  styleUrls: ['./scoring.component.scss'],
})
export class ScoringComponent implements OnInit {
  // @Input keyResult!: KeyresultMin;
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
  isFailZone: boolean = false;
  isCommitZone: boolean = false;
  isTargetZone: boolean = false;

  zonewith: number = '';

  constructor() {}

  ngOnInit(): void {
    // if (this.keyResult.lastCheckIn === undefined) {
    //   this.failWidth = this.commitWidth = this.targetWidth = '0';
    // } else {
    this.isOverview;
    // if (keyResult.type === 'metric') {
    this.calculatePercentagesMetric();

    this.setBarColorsMetric();

    // }
    // else {
    // this.setColorsOrdinal();
    // }
    // }
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
    // this.metricLabel = this.keyResult.unit + checkInValue;
    this.metricLabel = 'CHF 230000';

    // let baseline: number = this.keyResult.baseLine;
    let baseline: number = 0;
    // let stretchGoal: number = this.keyResult.stretchGoal;
    let stretchGoal: number = 10;
    // let checkInValue: number = this.keyResult.lastCheckin.value;
    let checkInValue: number = 7;

    let decimal: number = (checkInValue - baseline) / (stretchGoal - baseline);

    console.log(decimal);

    // this.labelWidth = decimal * 100 + "%";

    if (decimal < 0.3) {
      let percent = (decimal * 100) / 30;
      this.failWidth = percent * 100 + '%';
      if (this.failWidth != '100%' && this.failWidth != '0') {
        this.endLineFail = 'endLine';
      }
      this.labelWidth = this.zonewith * percent + 'px';
      this.commitWidth = this.targetWidth = '0';
      this.isFailZone = true;
    } else if (decimal < 0.7) {
      this.failWidth = '100%';
      this.commitWidth = ((decimal * 100 - 30) / 40) * 100 + '%';
      if (this.commitWidth != '100%' && this.commitWidth != '0') {
        this.endLineCommit = 'endLine';
      }
      this.targetWidth = '0';
      this.isCommitZone = true;

      let percent = (decimal * 100 - 30) / 40;
      console.log(percent);
      this.labelWidth = this.zonewith + this.zonewith * percent + 'px';
      console.log(this.labelWidth);
    } else {
      this.targetWidth = decimal > 1.0 ? '100%' : ((decimal * 100 - 70) / 30) * 100 + '%';

      if (this.targetWidth != '100%' && this.targetWidth != '0') {
        this.endLineTarget = 'endLine';
      }
      this.labelWidth = 98 + 98 + (98 * (decimal * 100 - 70)) / 30 + 'px';
      this.failWidth = this.commitWidth = '100%';
      this.isTargetZone = true;
    }
  }

  setColorsOrdinal() {
    let value = 'FAIL';

    // if (this.keyResult.lastCheckIn!.value === 'FAIL') {
    if (value === 'FAIL') {
      this.failWidth = '100%';
      this.failColor = '#BA3838';
      this.commitColor = this.targetColor = '#ffffff';
      // } else if (this.keyResult.lastCheckIn!.value === 'COMMIT') {
    } else if (value === 'COMMIT') {
      this.failWidth = this.commitWidth = '100%';
      this.failColor = this.commitColor = '#FFD600';
      this.targetColor = '#ffffff';
      // } else if (this.keyResult.lastCheckIn!.value === 'TARGET') {
    } else if (value === 'TARGET') {
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
