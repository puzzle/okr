import { AfterViewInit, ChangeDetectionStrategy, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { map, Observable } from 'rxjs';
import { ConnectedPosition, ScrollStrategyOptions } from '@angular/cdk/overlay'; // ESM

@Component({
  selector: 'app-sidepanel',
  templateUrl: './sidepanel.component.html',
  styleUrls: ['./sidepanel.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SidepanelComponent implements OnInit, AfterViewInit {
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
    readonly sso: ScrollStrategyOptions,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe((data) => (this.modelType = data['type']));
    this.id$ = this.activatedRoute.data.pipe(map((data) => data['id']));
  }

  ngAfterViewInit(): void {
    this.sidebar.nativeElement.classList.add('sidebar-show');
  }

  close() {
    this.router.navigate(['/']);
  }
}
