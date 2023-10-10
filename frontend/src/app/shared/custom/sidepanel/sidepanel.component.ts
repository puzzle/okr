import { AfterViewInit, ChangeDetectionStrategy, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { map, Observable } from 'rxjs';
import { Objective } from '../../types/model/Objective';
import { ScrollStrategyOptions } from '@angular/cdk/overlay';

@Component({
  selector: 'app-sidepanel',
  templateUrl: './sidepanel.component.html',
  styleUrls: ['./sidepanel.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SidepanelComponent implements OnInit, AfterViewInit {
  @ViewChild('sidebar')
  sidebar!: ElementRef<HTMLDivElement>;

  modelType = '';
  id$!: Observable<number | Objective>;

  constructor(
    private activatedRoute: ActivatedRoute,
    public sso: ScrollStrategyOptions,
    private router: Router,
  ) {}

  closeBackdrop() {
    this.router.navigate(['/']);
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe((data) => (this.modelType = data['type']));
    this.id$ = this.activatedRoute.data.pipe(map((data) => data['id']));
  }

  ngAfterViewInit(): void {
    setTimeout(this.displaySidebar, 0, this.sidebar);
  }

  displaySidebar(sidebar: ElementRef<HTMLDivElement>) {
    sidebar.nativeElement.classList.add('sidebar-show');
  }

  getId(): Observable<number> {
    return this.id$ as Observable<number>;
  }
}
