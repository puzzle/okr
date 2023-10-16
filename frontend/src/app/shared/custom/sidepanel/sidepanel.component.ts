import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ConnectedPosition } from '@angular/cdk/overlay'; // ESM

@Component({
  selector: 'app-sidepanel',
  templateUrl: './sidepanel.component.html',
  styleUrls: ['./sidepanel.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SidepanelComponent implements OnInit, AfterViewInit, OnDestroy {
  right = '-100%';
  @ViewChild('sidebar')
  sidebar!: ElementRef<HTMLDivElement>;
  position: ConnectedPosition[] = [
    {
      originX: 'end',
      originY: 'bottom',
      overlayX: 'end',
      overlayY: 'top',
    },
  ];
  modelType = '';
  id: number = 0;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe((data) => {
      this.modelType = data['type'];
      this.id = data['id'];
    });
  }

  ngAfterViewInit(): void {
    document.body.classList.add('disable-scrolling');
    this.right = '0';
  }

  ngOnDestroy() {
    document.body.classList.remove('disable-scrolling');
  }

  close() {
    this.router.navigate(['/']);
  }
}
