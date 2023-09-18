import { Component, Input, OnInit } from "@angular/core";
import { KeyresultMin } from '../../types/model/KeyresultMin';

@Component({
  selector: 'app-scoring',
  templateUrl: './scoring.component.html',
  styleUrls: ['./scoring.component.scss'],
})
export class ScoringComponent implements OnInit {
  @Input keyResult!: KeyresultMin;
  failWidth: string = '50%';
  commitWidth: string = '0';
  targetWidth: string = '0';
  failColor: string = '#ffffff';
  commitColor: string = '#ffffff';
  targetColor: string = '#ffffff';
  isStretched: boolean = false;
  iconPath: string = 'empty';
  metricLabel: string = '';

  constructor() {}

  ngOnInit(): void {
    // if (this.keyResult.lastCheckIn === undefined) {
    //   this.failWidth = this.commitWidth = this.targetWidth = '0';
    // } else {

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
    // let baseline: number = this.keyResult.baseLine;
    let baseline: number = 4;
    // let baseline: number = this.keyResult.stretchGoal;
    let stretchGoal: number = 20;
    // let baseline: number = this.keyResult.lastCheckin.value;
    let checkInValue: number = 17;

    let decimal: number = (checkInValue - baseline) / (stretchGoal - baseline);

    if (decimal < 0.3) {
      this.failWidth = ((decimal * 100) / 30) * 100 + '%';
      this.commitWidth = this.targetWidth = '0';
    } else if (decimal < 0.7) {
      this.failWidth = '100%';
      this.commitWidth = ((decimal * 100 - 30) / 40) * 100 + '%';
      this.targetWidth = '0';
    } else {
      this.targetWidth = decimal > 1.0 ? '100%' : ((decimal * 100 - 70) / 30) * 100 + '%';
      this.failWidth = this.commitWidth = '100%';
    }
    // this.metricLabel = this.keyResult.unit + checkInValue;
    this.metricLabel = 'CHF 23000';
  }

  setColorsOrdinal() {
    if (this.keyResult.lastCheckIn!.value === 'FAIL') {
      this.failWidth = '100%';
      this.failColor = '#BA3838';
      this.commitColor = this.targetColor = '#ffffff';
    } else if (this.keyResult.lastCheckIn!.value === 'COMMIT') {
      this.failWidth = this.commitWidth = '100%';
      this.failColor = this.commitColor = '#FFD600';
      this.targetColor = '#ffffff';
    } else if (this.keyResult.lastCheckIn!.value === 'TARGET') {
      this.failWidth = this.commitWidth = this.targetWidth = '100%';
      this.failColor = this.commitColor = this.targetColor = '#1E8A29';
    } else {
      this.failWidth = this.commitWidth = this.targetWidth = '100%';
      this.setStretchIcon();
    }
  }

  setStretchIcon() {
    this.isStretched = true;
    this.iconPath = 'filled';
    this.failColor = this.commitColor = this.targetColor = 'url("../../../../assets/images/scoring-stars.svg")';
  }
}
