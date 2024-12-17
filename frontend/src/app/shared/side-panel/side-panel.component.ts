import {
  AfterContentInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ConnectedPosition } from '@angular/cdk/overlay'; // ESM

@Component({
  selector: 'app-side-panel',
  templateUrl: './side-panel.component.html',
  styleUrls: ['./side-panel.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SidePanelComponent implements OnInit, AfterContentInit, OnDestroy {
  leaveKeys = ['Escape'];
  right = '-100%';
  loaded = false;
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

  constructor(
    private router: Router,
    private cd: ChangeDetectorRef,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {}
  ngAfterContentInit(): void {
    document.body.classList.add('disable-scrolling');
    this.right = '0';
    this.loaded = true;
    this.cd.markForCheck();
  }

  ngOnDestroy() {
    document.body.classList.remove('disable-scrolling');
  }

  close() {
    this.router.navigate(['../'], { relativeTo: this.route });
  }

  closeOnKeydown($event: KeyboardEvent) {
    if (this.leaveKeys.includes($event.key)) {
      this.close();
    }
  }
}
