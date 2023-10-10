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
import { map, Observable } from 'rxjs';
import { ConnectedPosition, ScrollStrategyOptions } from '@angular/cdk/overlay'; // ESM

@Component({
  selector: 'app-sidepanel',
  templateUrl: './sidepanel.component.html',
  styleUrls: ['./sidepanel.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SidepanelComponent implements OnInit, AfterViewInit, OnDestroy {
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
  id$!: Observable<number>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe((data) => {
      this.modelType = data['type'];
    });
    this.id$ = this.activatedRoute.data.pipe(map((data) => data['id']));
  }

  ngAfterViewInit(): void {
    document.body.classList.add('disable-scrolling');
    this.sidebar.nativeElement.classList.add('sidebar-show');
  }

  ngOnDestroy() {
    document.body.classList.remove('disable-scrolling');
  }

  close() {
    this.router.navigate(['/']);
  }
}
