import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import cytoscape from 'cytoscape';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { Router } from '@angular/router';
// @ts-ignore
import spread from 'cytoscape-spread';

@Component({
  selector: 'app-diagram',
  templateUrl: './diagram.component.html',
  styleUrl: './diagram.component.scss',
})
export class DiagramComponent implements OnInit, OnChanges {
  @Input()
  overviewEntity!: OverviewEntity[];

  constructor(private router: Router) {}

  ngOnChanges(changes: SimpleChanges) {
    this.generateDiagram(changes['overviewEntity'].currentValue);
  }

  ngOnInit() {
    this.generateDiagram(this.overviewEntity);
  }

  generateDiagram(data: OverviewEntity[]) {
    cytoscape.use(spread); // Register cytoscape-spread extension

    let generatedData = this.generateElements(data);

    var cy = cytoscape({
      container: document.getElementById('cy'), // container to render in

      elements: generatedData,

      zoom: 1,
      zoomingEnabled: true,
      userZoomingEnabled: true,

      style: [
        {
          selector: 'node',
          style: {
            height: 80,
            width: 80,
            'background-color': '#2C97A6',
            label: 'data(label)',
            'text-wrap': 'wrap',
            'text-halign': 'center',
            'text-valign': 'center',
          },
        },
        {
          selector: 'edge',
          style: {
            width: 1,
            'line-color': '#000000',
            'target-arrow-color': '#000000',
            'target-arrow-shape': 'triangle',
            'curve-style': 'bezier',
          },
        },
        {
          selector: '#P',
          style: {
            height: 200,
            width: 200,
            'font-size': '70px',
            'font-family': 'OKRFont, sans-serif',
            color: '#FFFFFF',
            backgroundColor: '#1E5A96',
          },
        },
        {
          selector: '[id^="Team"]', // id which starts with Team
          style: {
            height: 160,
            width: 160,
            'font-size': '32px',
            backgroundColor: '#238BCA',
          },
        },
        {
          selector: '[id^="Ob"]', // id which starts with Ob
          style: {
            height: 160,
            width: 160,
            'font-size': '18px',
            backgroundColor: '#2C97A6',
          },
        },
        {
          selector: '[id^="KR"]', // id which starts with KR
          style: {
            height: 120,
            width: 120,
            'font-size': '14px',
            backgroundColor: '#E5E8EB',
          },
        },
      ],

      layout: {
        name: 'spread',
      },
    });

    cy.on('tap', 'node', (evt) => {
      let node = evt.target;

      // node.style({
      //   'background-color': '#008000',
      //   color: '#FFFFFF'
      // });

      if (node.id().substring(0, 2) == 'KR') {
        node.style({
          'background-color': '#E5E8EB',
          'border-width': 0,
        });
      }

      if (node.id() == 'P') return;
      if (node.id().charAt(0) == 'T') return;
      let type = node.id().charAt(0) == 'O' ? 'Objective' : 'KeyResult';

      this.router.navigate([type.toLowerCase(), node.id().substring(2)]);
    });

    cy.on('mouseover', 'node', function (evt) {
      var node = evt.target;

      if (node.id().substring(0, 2) == 'KR') {
        node.style({
          'background-color': 'white',
          'border-color': '#5D6974',
          'border-width': 1,
        });
      }
    });

    cy.on('mouseout', 'node', function (evt) {
      var node = evt.target;

      if (node.id().substring(0, 2) == 'KR') {
        node.style({
          'background-color': '#E5E8EB',
          'border-width': 0,
        });
      }
    });

    let puzzleEl = cy.$('#P');
    cy.centre(puzzleEl);
  }

  generateElements(data: OverviewEntity[]) {
    let elements: any[] = [];
    let edges: any[] = [];
    let element = {
      // node a
      data: { id: 'P', label: 'puzzle' },
    };
    elements.push(element);

    data.forEach((overViewEntity) => {
      element = {
        data: { id: 'Team' + overViewEntity.team.name, label: overViewEntity.team.name },
      };
      elements.push(element);
      let edge = {
        data: { source: 'Team' + overViewEntity.team.name, target: 'P' },
      };
      edges.push(edge);

      overViewEntity.objectives.forEach((objective) => {
        element = {
          data: { id: 'Ob' + objective.id, label: objective.title },
        };
        elements.push(element);
        edge = {
          data: { source: 'Ob' + objective.id, target: 'Team' + overViewEntity.team.name },
        };
        edges.push(edge);

        objective.keyResults.forEach((keyResult) => {
          element = {
            data: { id: 'KR' + keyResult.id, label: keyResult.title },
          };
          elements.push(element);
          edge = {
            data: { source: 'KR' + keyResult.id, target: 'Ob' + objective.id },
          };
          edges.push(edge);
        });
      });
    });

    return elements.concat(edges);
  }
}
