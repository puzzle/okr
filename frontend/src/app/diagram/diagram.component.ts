import { AfterViewInit, Component, Input, OnDestroy } from '@angular/core';
import cytoscape from 'cytoscape';
import { Router } from '@angular/router';
import { KeyresultMin } from '../shared/types/model/KeyresultMin';
import { calculateCurrentPercentage } from '../shared/common';
import { KeyResultMetricMin } from '../shared/types/model/KeyResultMetricMin';
import { KeyResultOrdinalMin } from '../shared/types/model/KeyResultOrdinalMin';
import { BehaviorSubject } from 'rxjs';
import { Alignment } from '../shared/types/model/Alignment';

@Component({
  selector: 'app-diagram',
  templateUrl: './diagram.component.html',
  styleUrl: './diagram.component.scss',
})
export class DiagramComponent implements AfterViewInit, OnDestroy {
  private alignmentEntity$ = new BehaviorSubject<Alignment[]>({} as Alignment[]);

  @Input()
  get alignmentEntity(): BehaviorSubject<Alignment[]> {
    return this.alignmentEntity$;
  }

  set alignmentEntity(alignmentEntity: Alignment[]) {
    this.alignmentEntity$.next(alignmentEntity);
  }

  cy!: cytoscape.Core;

  currentNodeBackgroundColor: string = '';
  currentNodeFontColor: string = '';
  currentNodeBorderColor: string = '';

  constructor(private router: Router) {}

  ngAfterViewInit(): void {
    this.alignmentEntity.subscribe((alignmentEntity) => {
      this.generateDiagram(alignmentEntity);
    });
  }

  ngOnDestroy() {
    if (this.cy) {
      this.cy.nodes().remove();
      this.cy.edges().remove();
      this.cy.off('all');
      this.cy.removeAllListeners();
    }
  }

  generateDiagram(data: Alignment[]): void {
    let generatedData: any[] = data.length == 0 ? this.emptyPageLogo() : this.generateElements(data);

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

  generateElements(data: Alignment[]): any[] {
    let elements: any[] = [];
    let edges: any[] = [];

    data.forEach((alignment) => {
      let element = {
        data: {
          id: 'Ob' + alignment.alignedObjectiveId,
        },
        style: {
          height: 160,
          backgroundColor: '#2C97A6',
          width: 160,
          'background-image': this.generateObjectiveSVG(
            undefined,
            undefined,
            undefined,
            undefined,
            this.adjustLabel(alignment.alignedObjectiveTitle, 36, 15),
            alignment.alignedObjectiveTeamName,
            '#FFA500',
          ).svg,
        },
      };
      elements.push(element);

      if (alignment.alignmentType == 'objective') {
        let element = {
          data: {
            id: 'Ob' + alignment.targetObjectiveId,
            label:
              this.adjustLabel(alignment.targetObjectiveTitle!, 36, 15) +
              '\n --- \n ' +
              alignment.targetObjectiveTeamName,
          },
          style: {
            'background-color': '#2C97A6',
          },
        };
        elements.push(element);

        let edge = {
          data: { source: 'Ob' + alignment.alignedObjectiveId, target: 'Ob' + alignment.targetObjectiveId },
        };
        edges.push(edge);
      } else if (alignment.alignmentType == 'keyResult') {
        let style = {
          'background-color': '#1E8A29',
          'background-opacity': 0.8,
          color: '#FFFFFF',
          'border-color': '#FFFFFF',
          'border-width': 0,
        };
        let newElement = {
          data: {
            id: 'KR' + alignment.targetKeyResultId,
            label:
              this.adjustLabel(alignment.targetKeyResultTitle!, 25, 12) +
              '\n --- \n ' +
              alignment.targetKeyResultTeamName,
          },
          style: style,
        };
        elements.push(newElement);

        let edge = {
          data: { source: 'Ob' + alignment.alignedObjectiveId, target: 'KR' + alignment.targetKeyResultId },
        };
        edges.push(edge);
      }
    });

    return data && data.length > 0 ? elements.concat(edges) : [];
  }

  adjustLabel(label: string, splitIndex: number, wordMaxLength: number): string {
    let shorterLabel: string = label.length > splitIndex ? this.split_at_index(label, splitIndex) : label;

    return this.splitLongWords(shorterLabel, wordMaxLength);
  }

  split_at_index(value: any, index: any): string {
    let substring = value.substring(0, index);
    if (/\s/.test(substring.substring(substring.length -1))) return substring + '...';
    let lastChar = substring.substring(substring.length - 2);

    if (/\s/.test(lastChar)) {
      substring = value.substring(0, index + 1);
    }

    return substring + '...';
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

  generateObjectiveSVG(
    backgroundColor: string = '#2C97A6',
    strokeColor: string = '#2C97A6',
    strokeWidth: number = 1,
    textColor: string = '#FFFFFF',
    textLabel: string,
    teamName: string,
    teamColor: string = 'green',
  ) {
    let svg = `
<svg xmlns="http://www.w3.org/2000/svg" width="500" height="500">
  <circle cx="250" cy="250" r="250" fill="${backgroundColor}" stroke="${strokeColor}" stroke-width="${strokeWidth}"/>

    <foreignObject x="188" y="210" width="120" height="150" color="${textColor}" font-size="16px" font-family="Arial, sans-serif">
      <div xmlns="http://www.w3.org/1999/xhtml" style="text-align: center">
        ${textLabel}
      </div>
    </foreignObject>

      <text x="250" y="300" dominant-baseline="middle" text-anchor="middle" font-size="16" font-family="Arial, sans-serif">
    <tspan fill="${teamColor}">${teamName}</tspan>
  </text>
</svg>
    `;

    return {
      svg: 'data:image/svg+xml;base64,' + btoa(svg),
    };
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

  emptyPageLogo() {
    let svg = `<svg width="150" height="149" viewBox="0 0 243 242" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path id="Shape" d="M207.411 35.4379C159.962 -11.8138 83.0335 -11.8125 35.5858 35.4408C-11.8619 82.6941 -11.8619 159.306 35.5858 206.559C83.0335 253.812 159.962 253.814 207.411 206.562C230.198 183.87 243 153.092 243 121C243 88.9077 230.198 58.13 207.411 35.4379ZM198.046 77.2333C194.087 89.0106 181.349 124.343 181.349 124.343C177.087 135.858 152.159 135.757 118.311 135.858C118.311 135.858 140.181 132.782 146.499 120.622C151.922 108.686 156.522 96.396 160.269 83.8379C126.604 83.8379 123.597 95.585 123.597 95.585C123.597 95.585 102.334 154.845 94.7301 176.705C35.7621 176.705 45.1885 147.706 45.1885 147.706L74.6826 65.3047H134.42L130.431 77.1728C130.431 77.1728 135.291 65.3047 168.228 65.3047C168.248 65.3047 202.015 65.4459 198.046 77.2333Z" fill="white" fill-opacity="0.5"/>
        </svg>`;

    let element = {
      svg: 'data:image/svg+xml;base64,' + btoa(svg),
    };

    return [
      {
        data: {
          id: 'empty',
        },
        style: {
          height: 149,
          backgroundColor: '#000000',
          width: 150,
          opacity: 0.8,
          'background-image': element.svg,
        },
      },
    ];
  }
}
