import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  Input,
  OnInit,
  ViewChild,
} from '@angular/core';
import { SvgService } from '../../shared/services/svg/svg.service';

@Component({
  selector: 'app-progress-bar',
  templateUrl: './progress-bar.component.html',
  styleUrls: ['./progress-bar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProgressBarComponent implements OnInit {
  @ViewChild('progressbar')
  progressBar?: ElementRef<HTMLDivElement>;

  @Input() value!: number;
  @Input() colorLow!: string;
  @Input() colorMiddle!: string;
  @Input() colorHigh!: string;
  @Input() limitLow!: number;
  @Input() limitHigh!: number;

  constructor(
    private changeDetectorRef: ChangeDetectorRef,
    private svgService: SvgService
  ) {}

  ngOnInit(): void {
    this.paintProgressBar();
  }

  private static stringToSvgElement(str: string): SVGElement {
    let ele = document.createElement('div');
    ele.innerHTML = str;
    return ele.firstElementChild as SVGElement;
  }

  public addSvgToContainer(
    svg: SVGElement,
    value: number,
    colorLow: string,
    colorMiddle: string,
    colorHigh: string,
    limitLow: number,
    limitHigh: number
  ): void {
    svg.setAttribute('value', value.toString());
    if (value >= limitHigh) {
      svg
        .querySelector('#progress')!
        .setAttribute('style', 'fill: ' + colorHigh);
    } else if (value < limitLow) {
      svg
        .querySelector('#progress')!
        .setAttribute('style', 'fill: ' + colorLow);
    } else {
      svg
        .querySelector('#progress')!
        .setAttribute('style', 'fill: ' + colorMiddle);
    }
    svg.querySelector('#progress')!.setAttribute('width', value.toString());
    svg.classList.add('progress-bar');

    this.progressBar!.nativeElement.append(svg);
    this.changeDetectorRef.markForCheck();
  }

  public paintProgressBar(svgFilename: string = 'progress_bar.svg') {
    this.svgService.getSvg(svgFilename).subscribe((data: string) => {
      this.addSvgToContainer(
        ProgressBarComponent.stringToSvgElement(data),
        this.value,
        this.colorLow,
        this.colorMiddle,
        this.colorHigh,
        this.limitLow,
        this.limitHigh
      );
    });
  }
}
