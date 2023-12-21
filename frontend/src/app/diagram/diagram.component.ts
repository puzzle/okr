import { Component, OnInit } from '@angular/core';
import * as go from 'gojs';
import { ContinuousForceDirectedLayout } from './continuous-force-directed-layout';
import { Router } from '@angular/router';

@Component({
  selector: 'app-diagram',
  templateUrl: './diagram.component.html',
  styleUrls: ['./diagram.component.scss'],
})
export class DiagramComponent implements OnInit {
  private myDiagram: go.Diagram = new go.Diagram();

  constructor(private router: Router) {}

  ngOnInit() {
    const layout = new ContinuousForceDirectedLayout(this.myDiagram, this.router);
    layout.eventListener();
    layout.init();
    this.myDiagram.layout = layout;
  }
}
