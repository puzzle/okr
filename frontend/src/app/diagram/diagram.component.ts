import { AfterViewInit, Component, Input, OnDestroy } from '@angular/core';
import cytoscape from 'cytoscape';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { Router } from '@angular/router';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { KeyresultMin } from '../shared/types/model/KeyresultMin';
import { calculateCurrentPercentage } from '../shared/common';
import { KeyResultMetricMin } from '../shared/types/model/KeyResultMetricMin';
import { KeyResultOrdinalMin } from '../shared/types/model/KeyResultOrdinalMin';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-diagram',
  templateUrl: './diagram.component.html',
  styleUrl: './diagram.component.scss',
})
export class DiagramComponent implements AfterViewInit, OnDestroy {
  private overviewEntity$ = new BehaviorSubject<OverviewEntity[]>({} as OverviewEntity[]);

  @Input()
  get overviewEntity(): BehaviorSubject<OverviewEntity[]> {
    return this.overviewEntity$;
  }

  set overviewEntity(overviewEntity: OverviewEntity[]) {
    this.overviewEntity$.next(overviewEntity);
  }

  cy!: cytoscape.Core;

  currentNodeBackgroundColor: string = '';
  currentNodeFontColor: string = '';
  currentNodeBorderColor: string = '';

  constructor(private router: Router) {}

  ngAfterViewInit(): void {
    this.overviewEntity.subscribe((overviewEntity) => {
      this.generateDiagram(overviewEntity);
    });
  }

  ngOnDestroy() {
    this.cy.nodes().remove();
    this.cy.edges().remove();
    this.cy.off('all');
    this.cy.removeAllListeners();
  }

  generateDiagram(data: OverviewEntity[]): void {
    if (data.length == 0) return;
    let generatedData: any[] = this.generateElements(data);

    this.cy = cytoscape({
      container: document.getElementById('cy'),

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

    this.cy.on('tap', 'node', (evt: cytoscape.EventObject): void => {
      let node = evt.target;
      this.handleMouseLeaveEvent(node);
      let type: string = node.id().charAt(0) == 'O' ? 'Objective' : 'KeyResult';

      this.router.navigate([type.toLowerCase(), node.id().substring(2)]);
    });

    this.cy.on('mouseover', 'node', (evt: cytoscape.EventObject): void => {
      let node = evt.target;
      let nodeId = node.id();
      if (!nodeId) return;

      let backgroundRGB = this.extractRGBNumbers(node.style().backgroundColor);
      this.currentNodeBackgroundColor = this.rgbToHex(backgroundRGB![0], backgroundRGB![1], backgroundRGB![2]);
      let fontRGB = this.extractRGBNumbers(node.style().color);
      this.currentNodeFontColor = this.rgbToHex(fontRGB![0], fontRGB![1], fontRGB![2]);
      let borderRGB = this.extractRGBNumbers(node.style().borderColor);
      this.currentNodeBorderColor = this.rgbToHex(borderRGB![0], borderRGB![1], borderRGB![2]);

      if (nodeId.substring(0, 2) == 'KR') {
        node.style({
          'background-color': '#FFFFFF',
          color: '#000000',
          'border-color': '#1E5A96',
          'border-width': 2,
        });
      } else if (nodeId.substring(0, 2) == 'Ob') {
        node.style({
          color: '#FFFFFF',
          'background-color': '#1d7e8c',
        });
      }
    });

    this.cy.on('mouseout', 'node', (evt: cytoscape.EventObject): void => {
      this.handleMouseLeaveEvent(evt.target);
    });

    // this.cy.add([
    //   {
    //     group: 'nodes',
    //     data: [
    //       {
    //         id: 'NR1',
    //         label: 'Yanick',
    //         tag: 'othername',
    //       },
    //     ],
    //   },
    //   {
    //     group: 'nodes',
    //     data: [
    //       {
    //         id: 'NR1',
    //         label: 'Yanick',
    //       },
    //       {
    //         id: 'NR2',
    //         label: 'Lias',
    //       },
    //     ],
    //   },
    //   // { group: 'nodes', data: {id: 'kuchen',
    //   //     label: 'Sehr lustig <br/><span style="color: red;">das Team</span>'}
    //   // },
    //   // { group: 'nodes', data: {id: 'asdf', label: {
    //   //       return: '<h1 class="material-icons">Du bist ja lustig</h1>'
    //   //     }
    //   // }},
    // ]);
  }

  handleMouseLeaveEvent(node: any) {
    let nodeId = node.id();
    if (!nodeId) return;
    if (nodeId.substring(0, 2) == 'KR') {
      node.style({
        'background-color': this.currentNodeBackgroundColor,
        color: this.currentNodeFontColor,
        'border-width': this.currentNodeBackgroundColor == '#ffffff' ? 1 : 0,
        'border-color': this.currentNodeBorderColor,
      });
    } else if (nodeId.substring(0, 2) == 'Ob') {
      node.style({
        'background-color': this.currentNodeBackgroundColor,
        color: this.currentNodeFontColor,
      });
    }
  }

  generateElements(data: OverviewEntity[]): any[] {
    let elements: any[] = [];
    let edges: any[] = [];
    data.forEach((overViewEntity: OverviewEntity): void => {
      overViewEntity.objectives.forEach((objective: ObjectiveMin): void => {
        let element = {
          data: {
            id: 'Ob' + objective.id,
            label: this.adjustLabel(objective.title, 36, 15) + '\n --- \n ' + overViewEntity.team.name,
          },
          style: {
            'background-color': '#2C97A6',
          },
        };
        elements.push(element);
        if (overViewEntity.team.name == 'Puzzle ITC') {
          objective.keyResults.forEach((keyResult): void => {
            let style = this.generateStyle(keyResult);
            element = {
              data: {
                id: 'KR' + keyResult.id,
                label: this.adjustLabel(keyResult.title, 25, 12) + '\n --- \n ' + overViewEntity.team.name,
              },
              style: style,
            };
            elements.push(element);
            let edge = {
              data: { source: 'KR' + keyResult.id, target: 'Ob' + objective.id },
            };
            edges.push(edge);
          });
        }
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
      'border-color': '#FFFFFF',
      'border-width': 0,
    };
    if (keyResult.keyResultType == 'metric') {
      if (!keyResult.lastCheckIn) {
        style = {
          'background-color': '#FFFFFF',
          'background-opacity': 1,
          color: '#000000',
          'border-color': '#000000',
          'border-width': 1,
        };
      } else {
        let percentages = calculateCurrentPercentage(keyResult as KeyResultMetricMin);
        if (percentages < 30) {
          style = {
            'background-color': '#BA3838',
            'background-opacity': 0.8,
            color: '#FFFFFF',
            'border-color': '#FFFFFF',
            'border-width': 0,
          };
        } else if (percentages < 70) {
          style = {
            'background-color': '#FFD600',
            'background-opacity': 0.8,
            color: '#000000',
            'border-color': '#FFFFFF',
            'border-width': 0,
          };
        } else if (percentages < 100) {
          style = {
            'background-color': '#1E8A29',
            'background-opacity': 0.8,
            color: '#FFFFFF',
            'border-color': '#FFFFFF',
            'border-width': 0,
          };
        } else if (percentages >= 100) {
          style = {
            'background-color': '#1E5A96',
            'background-opacity': 1,
            color: '#FFFFFF',
            'border-color': '#1E5A96',
            'border-width': 0,
          };
        }
      }
    } else {
      if (!keyResult.lastCheckIn) {
        style = {
          'background-color': '#FFFFFF',
          'background-opacity': 1,
          color: '#000000',
          'border-color': '#000000',
          'border-width': 1,
        };
      } else {
        let ordinalKeyResult = keyResult as KeyResultOrdinalMin;
        switch (ordinalKeyResult.lastCheckIn?.value) {
          case 'FAIL':
            style = {
              'background-color': '#BA3838',
              'background-opacity': 0.8,
              color: '#FFFFFF',
              'border-color': '#FFFFFF',
              'border-width': 0,
            };
            break;
          case 'COMMIT':
            style = {
              'background-color': '#FFD600',
              'background-opacity': 0.8,
              color: '#000000',
              'border-color': '#FFFFFF',
              'border-width': 0,
            };
            break;
          case 'TARGET':
            style = {
              'background-color': '#1E8A29',
              'background-opacity': 0.8,
              color: '#FFFFFF',
              'border-color': '#FFFFFF',
              'border-width': 0,
            };
            break;
          case 'STRETCH':
            style = {
              'background-color': '#1E5A96',
              'background-opacity': 1,
              color: '#FFFFFF',
              'border-color': '#1E5A96',
              'border-width': 0,
            };
            break;
        }
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
      return [parseInt(match[1]), parseInt(match[2]), parseInt(match[3])];
    } else {
      return null;
    }
  }
}
