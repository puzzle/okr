import { AfterViewInit, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import cytoscape from 'cytoscape';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { Router } from '@angular/router';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { KeyresultMin } from '../shared/types/model/KeyresultMin';
import { calculateCurrentPercentage } from '../shared/common';
import { KeyResultMetricMin } from '../shared/types/model/KeyResultMetricMin';
import { KeyResultOrdinalMin } from '../shared/types/model/KeyResultOrdinalMin';

@Component({
  selector: 'app-diagram',
  templateUrl: './diagram.component.html',
  styleUrl: './diagram.component.scss',
})
export class DiagramComponent implements AfterViewInit, OnChanges {
  @Input()
  overviewEntity!: OverviewEntity[];

  currentNodeBackgroundColor: string = '';
  currentNodeFontColor: string = '';

  constructor(private router: Router) {}

  ngAfterViewInit(): void {
    this.generateDiagram(this.overviewEntity);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.generateDiagram(changes['overviewEntity'].currentValue);
  }

  generateDiagram(data: OverviewEntity[]): void {
    if (data.length == 0) return;
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
          selector: '[id^="Ob"]', // id which starts with Ob
          style: {
            height: 160,
            width: 160,
            'font-size': '18px',
            backgroundColor: '#2C97A6',
            'text-max-width': '120',
          },
        },
        {
          selector: '[id^="KR"]', // id which starts with KR
          style: {
            height: 120,
            width: 120,
            'font-size': '14px',
            backgroundColor: '#E5E8EB',
            'text-max-width': '80',
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
          'background-color': this.currentNodeBackgroundColor,
          color: this.currentNodeFontColor,
          'border-width': 0,
        });
      } else if (node.id().substring(0, 2) == 'Ob') {
        node.style({
          'background-color': this.currentNodeBackgroundColor,
          color: this.currentNodeFontColor,
        });
      }
      if (node.id() == 'P') return;
      if (node.id().charAt(0) == 'T') return;
      let type: string = node.id().charAt(0) == 'O' ? 'Objective' : 'KeyResult';

      this.router.navigate([type.toLowerCase(), node.id().substring(2)]);
    });

    cy.on('mouseover', 'node', (evt: cytoscape.EventObject): void => {
      let node = evt.target;

      let backgroundRGB = this.extractRGBNumbers(node.style().backgroundColor);
      this.currentNodeBackgroundColor = this.rgbToHex(backgroundRGB![0], backgroundRGB![1], backgroundRGB![2]);
      let fontRGB = this.extractRGBNumbers(node.style().color);
      this.currentNodeFontColor = this.rgbToHex(fontRGB![0], fontRGB![1], fontRGB![2]);

      if (node.id().substring(0, 2) == 'KR') {
        node.style({
          'background-color': 'white',
          color: '#000000',
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
          'background-color': this.currentNodeBackgroundColor,
          color: this.currentNodeFontColor,
          'border-width': 0,
        });
      } else if (node.id().substring(0, 2) == 'Ob') {
        node.style({
          'background-color': this.currentNodeBackgroundColor,
          color: this.currentNodeFontColor,
        });
      }
    });
  }

  generateElements(data: OverviewEntity[]): any[] {
    let elements: any[] = [];
    let edges: any[] = [];
    data.forEach((overViewEntity: OverviewEntity): void => {
      overViewEntity.objectives.forEach((objective: ObjectiveMin): void => {
        let element = {
          data: {
            id: 'Ob' + objective.id,
            label: this.adjustLabel(objective.title, 36, 15) + '\n \n ' + overViewEntity.team.name,
          },
          style: {
            'background-color': '#2C97A6',
          },
        };
        elements.push(element);
        // if (overViewEntity.team.name == 'Puzzle ITC') {
        objective.keyResults.forEach((keyResult): void => {
          let style = this.generateStyle(keyResult);
          element = {
            data: {
              id: 'KR' + keyResult.id,
              label: this.adjustLabel(keyResult.title, 25, 12) + '\n \n ' + overViewEntity.team.name,
            },
            style: style,
          };
          elements.push(element);
          let edge = {
            data: { source: 'KR' + keyResult.id, target: 'Ob' + objective.id },
          };
          edges.push(edge);
        });
        // }
      });
    });

    // Static alignment demo
    let edge = {
      data: { source: 'Ob' + 19, target: 'KR' + 20 },
    };
    edges.push(edge);

    edge = {
      data: { source: 'Ob' + 21, target: 'Ob' + 11 },
    };
    edges.push(edge);
    edge = {
      data: { source: 'Ob' + 20, target: 'Ob' + 13 },
    };
    edges.push(edge);
    edge = {
      data: { source: 'Ob' + 23, target: 'KR' + 27 },
    };
    edges.push(edge);
    edge = {
      data: { source: 'Ob' + 14, target: 'Ob' + 12 },
    };
    edges.push(edge);
    edge = {
      data: { source: 'Ob' + 22, target: 'Ob' + 11 },
    };
    edges.push(edge);
    edge = {
      data: { source: 'Ob' + 17, target: 'KR' + 28 },
    };
    edges.push(edge);
    edge = {
      data: { source: 'Ob' + 18, target: 'KR' + 27 },
    };
    edges.push(edge);
    edge = {
      data: { source: 'Ob' + 15, target: 'Ob' + 11 },
    };
    edges.push(edge);

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

  generateStyle(keyResult: KeyresultMin) {
    let style = {
      'background-color': '#1E8A29',
      'background-opacity': 0.8,
      color: '#FFFFFF',
    };
    if (keyResult.keyResultType == 'metric') {
      let percentages = calculateCurrentPercentage(keyResult as KeyResultMetricMin);
      if (percentages < 30) {
        style = {
          'background-color': '#BA3838',
          'background-opacity': 0.8,
          color: '#FFFFFF',
        };
      } else if (percentages < 70) {
        style = {
          'background-color': '#FFD600',
          'background-opacity': 0.8,
          color: '#000000',
        };
      } else if (percentages < 100) {
        style = {
          'background-color': '#1E8A29',
          'background-opacity': 0.8,
          color: '#FFFFFF',
        };
      } else if (percentages >= 100) {
        style = {
          'background-color': '#000000',
          'background-opacity': 0.8,
          color: '#FFFFFF',
        };
      }
    } else {
      let ordinalKeyResult = keyResult as KeyResultOrdinalMin;
      switch (ordinalKeyResult.lastCheckIn?.value) {
        case 'FAIL':
          style = {
            'background-color': '#BA3838',
            'background-opacity': 0.8,
            color: '#FFFFFF',
          };
          break;
        case 'COMMIT':
          style = {
            'background-color': '#FFD600',
            'background-opacity': 0.8,
            color: '#000000',
          };
          break;
        case 'TARGET':
          style = {
            'background-color': '#1E8A29',
            'background-opacity': 0.8,
            color: '#FFFFFF',
          };
          break;
        case 'STRETCH':
          style = {
            'background-color': '#000000',
            'background-opacity': 0.8,
            color: '#FFFFFF',
          };
          break;
      }
    }

    return style;
  }

  componentToHex(c: any) {
    var hex = c.toString(16);
    return hex.length == 1 ? '0' + hex : hex;
  }

  rgbToHex(r: any, g: any, b: any) {
    return '#' + this.componentToHex(r) + this.componentToHex(g) + this.componentToHex(b);
  }

  extractRGBNumbers(rgbString: string) {
    let match = rgbString.match(/(\d+),\s*(\d+),\s*(\d+)/);
    if (match) {
      let numbers = [parseInt(match[1]), parseInt(match[2]), parseInt(match[3])];
      return numbers;
    } else {
      return null;
    }
  }
}
