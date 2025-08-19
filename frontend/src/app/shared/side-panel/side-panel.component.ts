import { AfterContentInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, ElementRef, OnDestroy, ViewChild, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ConnectedPosition } from '@angular/cdk/overlay'; // ESM

@Component({
  selector: 'app-side-panel',
  templateUrl: './side-panel.component.html',
  styleUrls: ['./side-panel.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class SidePanelComponent implements AfterContentInit, OnDestroy {
  private router = inject(Router);

  private cd = inject(ChangeDetectorRef);

  private route = inject(ActivatedRoute);

  leaveKeys = ['Escape'];

  right = '-100%';

  loaded = false;

  @ViewChild('sidebar')
  sidebar!: ElementRef<HTMLDivElement>;

  position: ConnectedPosition[] = [{
    originX: 'end',
    originY: 'bottom',
    overlayX: 'end',
    overlayY: 'top'
  }];

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
