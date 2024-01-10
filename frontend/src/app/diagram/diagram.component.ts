import { AfterViewInit, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import cytoscape from 'cytoscape';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { Router } from '@angular/router';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { KeyresultMin } from '../shared/types/model/KeyresultMin';

@Component({
  selector: 'app-diagram',
  templateUrl: './diagram.component.html',
  styleUrl: './diagram.component.scss',
})
export class DiagramComponent implements AfterViewInit, OnChanges {
  @Input()
  overviewEntity!: OverviewEntity[];

  constructor(private router: Router) {}

  ngAfterViewInit(): void {
    this.generateDiagram(this.overviewEntity);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.generateDiagram(changes['overviewEntity'].currentValue);
  }

  generateDiagram(data: OverviewEntity[]): void {
    let generatedData: any[] = this.generateElements(data);

    let cy = cytoscape({
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
            'text-max-width': '140',
          },
        },
        {
          selector: '[id^="Ob"]', // id which starts with Ob
          style: {
            height: 160,
            width: 160,
            'font-size': '18px',
            backgroundColor: '#2C97A6',
            'text-max-width': '140',
          },
        },
        {
          selector: '[id^="KR"]', // id which starts with KR
          style: {
            height: 120,
            width: 120,
            'font-size': '14px',
            backgroundColor: '#E5E8EB',
            'text-max-width': '100',
          },
        },
      ],

      layout: {
        name: 'cose',
      },
    });

    cy.on('tap', 'node', (evt: cytoscape.EventObject): void => {
      let node = evt.target;

      if (node.id().substring(0, 2) == 'KR') {
        node.style({
          'background-color': '#E5E8EB',
          'border-width': 0,
        });
      } else if (node.id().substring(0, 2) == 'Ob') {
        node.style({
          color: '#000000',
          'background-color': '#2C97A6',
        });
      }

      if (node.id() == 'P') return;
      if (node.id().charAt(0) == 'T') return;
      let type: string = node.id().charAt(0) == 'O' ? 'Objective' : 'KeyResult';

      this.router.navigate([type.toLowerCase(), node.id().substring(2)]);
    });

    cy.on('mouseover', 'node', (evt: cytoscape.EventObject): void => {
      let node = evt.target;

      if (node.id().substring(0, 2) == 'KR') {
        node.style({
          'background-color': 'white',
          'border-color': '#5D6974',
          'border-width': 1,
        });
      } else if (node.id().substring(0, 2) == 'Ob') {
        node.style({
          color: '#FFFFFF',
          'background-color': '#1d7e8c',
        });
      }
    });

    cy.on('mouseout', 'node', (evt: cytoscape.EventObject): void => {
      let node = evt.target;

      if (node.id().substring(0, 2) == 'KR') {
        node.style({
          'background-color': '#E5E8EB',
          'border-width': 0,
        });
      } else if (node.id().substring(0, 2) == 'Ob') {
        node.style({
          color: '#000000',
          'background-color': '#2C97A6',
        });
      }
    });
  }

  generateElements(data: OverviewEntity[]): any[] {
    let elements: any[] = [];
    let edges: any[] = [];
    let element = {
      data: { id: 'P', label: 'puzzle' },
    };
    elements.push(element);

    data.forEach((overViewEntity: OverviewEntity): void => {
      element = {
        data: {
          id: 'Team' + overViewEntity.team.name,
          label: this.splitLongWords(overViewEntity.team.name, 6),
        },
      };
      elements.push(element);
      let edge = {
        data: { source: 'Team' + overViewEntity.team.name, target: 'P' },
      };
      edges.push(edge);

      overViewEntity.objectives.forEach((objective: ObjectiveMin): void => {
        element = {
          data: {
            id: 'Ob' + objective.id,
            label: this.adjustLabel(objective.title, 44, 15),
          },
        };
        elements.push(element);
        edge = {
          data: { source: 'Ob' + objective.id, target: 'Team' + overViewEntity.team.name },
        };
        edges.push(edge);

        objective.keyResults.forEach((keyResult: KeyresultMin): void => {
          element = {
            data: {
              id: 'KR' + keyResult.id,
              label: this.adjustLabel(keyResult.title, 49, 12),
            },
          };
          elements.push(element);
          edge = {
            data: { source: 'KR' + keyResult.id, target: 'Ob' + objective.id },
          };
          edges.push(edge);
        });
      });
    });

    // let edge = {
    //   data: { source: 'Ob' + 10, target: 'KR' + 4 },
    // };
    // edges.push(edge);
    //
    // edge = {
    //   data: { source: 'Ob' + 6, target: 'Ob' + 4 },
    // };
    // edges.push(edge);

    return data && data.length > 0 ? elements.concat(edges) : [];
  }

  adjustLabel(label: string, splitIndex: number, wordMaxLength: number): string {
    let shorterLabel: string = label.length > splitIndex ? this.split_at_index(label, splitIndex) : label;

    return this.splitLongWords(shorterLabel, wordMaxLength);
  }

  split_at_index(value: any, index: any): string {
    return value.substring(0, index) + '...';
  }

  splitLongWords(inputString: string, maxLength: number): string {
    const words: string[] = inputString.split(' ');
    const modifiedWords: string[] = words.map((word: string): string => {
      if (word.length > maxLength) {
        const splitWord: string[] = [];
        for (let i: number = 0; i < word.length; i += maxLength) {
          if (word.substring(i, i + maxLength + 2).includes('...')) {
            splitWord.push(word);
            break;
          } else {
            splitWord.push(word.substring(i, i + maxLength));
          }
        }
        return splitWord.join('-\n');
      }
      return word;
    });
    return modifiedWords.join(' ');
  }
}
