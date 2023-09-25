import { AfterViewInit, ChangeDetectionStrategy, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { KeyresultMin } from '../../types/model/KeyresultMin';

@Component({
  selector: 'app-scoring2',
  templateUrl: './scoring2.component.html',
  styleUrls: ['./scoring2.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Scoring2Component implements OnInit, AfterViewInit {
  @Input() keyResult!: KeyresultMin;
  iconPath: string = 'empty';

  @ViewChild('fail')
  private failElement: ElementRef<HTMLSpanElement> | undefined = undefined;
  @ViewChild('commit')
  private commitElement: ElementRef<HTMLSpanElement> | undefined = undefined;
  @ViewChild('target')
  private targetElement: ElementRef<HTMLSpanElement> | undefined = undefined;

  ngOnInit() {
    // check if metric or ordinal and calculate failPercent, commitPercent and targetPercent:
    // example metric: 50% ==>
    //   failPercent = 100;
    //   commitPercent = 50;
    //   targetPercent = 0;
  }

  ngAfterViewInit(): void {
    // use failPercent, commitPercent and targetPercent for set the backgrounds
    if (this.failElement) {
      this.failElement.nativeElement.classList.add('score-yellow');
    }
    if (this.commitElement) {
      this.commitElement.nativeElement.classList.add('score-test');
      this.commitElement.nativeElement.attributeStyleMap.set('background-size', '50%');
    }
    if (this.targetElement) {
    }
  }
}
