import { Component, Input, OnInit } from '@angular/core';
import * as go from 'gojs';
import { ContinuousForceDirectedLayout } from './continuous-force-directed-layout';
import { Subject } from 'rxjs';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { Router } from '@angular/router';

@Component({
  selector: 'app-diagram',
  templateUrl: './diagram.component.html',
  styleUrls: ['./diagram.component.scss'],
})
export class DiagramComponent implements OnInit {
  private myDiagram: go.Diagram = new go.Diagram();
  private overviewEntities$: Subject<OverviewEntity[]> = new Subject<OverviewEntity[]>();
  private myLayout: any;

  @Input()
  get overviewEntity(): Subject<OverviewEntity[]> | null {
    return this.overviewEntities$;
  }

  set overviewEntity(overviewEntity: OverviewEntity[] | Subject<OverviewEntity[]> | null) {
    if (Array.isArray(overviewEntity)) {
      // If an array is passed, assume it's the data and update the Subject
      this.overviewEntities$.next(overviewEntity);
    } else if (overviewEntity instanceof Subject) {
      // If a Subject is passed, directly update the internal Subject
      this.overviewEntities$ = overviewEntity;
    }
    // Null check can be added if you want to handle null cases
  }

  constructor(private router: Router) {
    this.overviewEntities$.subscribe((hey) => {
      const subscription = this.overviewEntities$.asObservable().subscribe((value: OverviewEntity[]) => {
        this.myLayout.updateDiagram(value);
        subscription.unsubscribe();
      });
    });
  }

  ngOnInit() {
    const subscription = this.overviewEntities$.asObservable().subscribe((value: OverviewEntity[]) => {
      this.myLayout = new ContinuousForceDirectedLayout(this.myDiagram, value, this.router);
      this.myLayout.eventListener();
      this.myLayout.init();
      this.myDiagram.layout = this.myLayout;

      subscription.unsubscribe();
    });
  }
}
