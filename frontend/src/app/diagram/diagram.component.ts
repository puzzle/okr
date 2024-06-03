import { AfterViewInit, Component, Input, OnDestroy } from '@angular/core';
import { forkJoin, map, Observable, Subject } from 'rxjs';
import { AlignmentLists } from '../shared/types/model/AlignmentLists';
import cytoscape from 'cytoscape';
import {
  generateKeyResultSVG,
  generateNeutralKeyResultSVG,
  generateObjectiveSVG,
  getDraftIcon,
  getNotSuccessfulIcon,
  getOnGoingIcon,
  getSuccessfulIcon,
} from './svgGeneration';
import { KeyresultService } from '../shared/services/keyresult.service';
import { KeyResult } from '../shared/types/model/KeyResult';
import { KeyResultMetric } from '../shared/types/model/KeyResultMetric';
import { calculateCurrentPercentage } from '../shared/common';
import { KeyResultOrdinal } from '../shared/types/model/KeyResultOrdinal';
import { Router } from '@angular/router';
import { AlignmentObject } from '../shared/types/model/AlignmentObject';
import { AlignmentConnection } from '../shared/types/model/AlignmentConnection';

@Component({
  selector: 'app-diagram',
  templateUrl: './diagram.component.html',
  styleUrl: './diagram.component.scss',
})
export class DiagramComponent implements AfterViewInit, OnDestroy {
  private alignmentData$: Subject<AlignmentLists> = new Subject<AlignmentLists>();
  cy!: cytoscape.Core;
  diagramData: any[] = [];
  emptyDiagramData: boolean = false;
  alignmentDataCache: AlignmentLists | null = null;

  constructor(
    private keyResultService: KeyresultService,
    private router: Router,
  ) {}

  @Input()
  get alignmentData(): Subject<AlignmentLists> {
    return this.alignmentData$;
  }

  set alignmentData(alignmentData: AlignmentLists) {
    this.alignmentData$.next(alignmentData);
  }

  ngAfterViewInit() {
    this.alignmentData.subscribe((alignmentData: AlignmentLists): void => {
      let lastItem: AlignmentObject =
        alignmentData.alignmentObjectDtoList[alignmentData.alignmentObjectDtoList.length - 1];

      let shouldUpdate: boolean =
        lastItem?.objectTitle === 'reload'
          ? lastItem?.objectType === 'true'
          : JSON.stringify(this.alignmentDataCache) !== JSON.stringify(alignmentData);

      if (shouldUpdate) {
        if (lastItem?.objectTitle === 'reload') {
          alignmentData.alignmentObjectDtoList.pop();
        }
        this.alignmentDataCache = alignmentData;
        this.diagramData = [];
        this.cleanUpDiagram();
        this.prepareDiagramData(alignmentData);
      }
    });
  }

  ngOnDestroy() {
    this.cleanUpDiagram();
  }

  generateDiagram(): void {
    this.cy = cytoscape({
      container: document.getElementById('cy'),
      elements: this.diagramData,

      zoom: 1,
      zoomingEnabled: true,
      userZoomingEnabled: true,
      wheelSensitivity: 0.3,

      style: [
        {
          selector: '[id^="Ob"]',
          style: {
            height: 160,
            width: 160,
          },
        },
        {
          selector: '[id^="KR"]',
          style: {
            height: 120,
            width: 120,
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
      ],

      layout: {
        name: 'cose',
      },
    });

    this.cy.on('tap', 'node', (evt: cytoscape.EventObject) => {
      let node = evt.target;
      node.style({
        'border-width': 0,
      });

      let type: string = node.id().charAt(0) == 'O' ? 'objective' : 'keyresult';
      this.router.navigate([type.toLowerCase(), node.id().substring(2)]);
    });

    this.cy.on('mouseover', 'node', (evt: cytoscape.EventObject) => {
      let node = evt.target;
      node.style({
        'border-color': '#1E5A96',
        'border-width': 2,
      });
    });

    this.cy.on('mouseout', 'node', (evt: cytoscape.EventObject) => {
      evt.target.style({
        'border-width': 0,
      });
    });
  }

  prepareDiagramData(alignmentData: AlignmentLists): void {
    if (alignmentData.alignmentObjectDtoList.length == 0) {
      this.emptyDiagramData = true;
    } else {
      this.emptyDiagramData = false;
      this.generateElements(alignmentData);
    }
  }

  generateElements(alignmentData: AlignmentLists): void {
    let observableArray: any[] = [];
    let diagramElements: any[] = [];
    alignmentData.alignmentObjectDtoList.forEach((alignmentObject: AlignmentObject) => {
      if (alignmentObject.objectType == 'objective') {
        let observable: Observable<any> = new Observable((observer) => {
          let element = {
            data: {
              id: 'Ob' + alignmentObject.objectId,
            },
            style: {
              'background-image': this.generateObjectiveSVG(
                alignmentObject.objectTitle,
                alignmentObject.objectTeamName,
                alignmentObject.objectState!,
              ),
            },
          };
          diagramElements.push(element);
          observer.next(element);
          observer.complete();
        });
        observableArray.push(observable);
      } else {
        let observable: Observable<void> = this.keyResultService.getFullKeyResult(alignmentObject.objectId).pipe(
          map((keyResult: KeyResult) => {
            if (keyResult.keyResultType == 'metric') {
              let metricKeyResult: KeyResultMetric = keyResult as KeyResultMetric;
              let percentage: number = calculateCurrentPercentage(metricKeyResult);

              let keyResultState: string | undefined;
              if (percentage < 30) {
                keyResultState = 'FAIL';
              } else if (percentage < 70) {
                keyResultState = 'COMMIT';
              } else if (percentage < 100) {
                keyResultState = 'TARGET';
              } else if (percentage >= 100) {
                keyResultState = 'STRETCH';
              } else {
                keyResultState = undefined;
              }
              let element = {
                data: {
                  id: 'KR' + alignmentObject.objectId,
                },
                style: {
                  'background-image': this.generateKeyResultSVG(
                    alignmentObject.objectTitle,
                    alignmentObject.objectTeamName,
                    keyResultState,
                  ),
                },
              };
              diagramElements.push(element);
            } else {
              let ordinalKeyResult: KeyResultOrdinal = keyResult as KeyResultOrdinal;
              let keyResultState: string | undefined = ordinalKeyResult.lastCheckIn?.value.toString();

              let element = {
                data: {
                  id: 'KR' + alignmentObject.objectId,
                },
                style: {
                  'background-image': this.generateKeyResultSVG(
                    alignmentObject.objectTitle,
                    alignmentObject.objectTeamName,
                    keyResultState,
                  ),
                },
              };
              diagramElements.push(element);
            }
          }),
        );
        observableArray.push(observable);
      }
    });

    forkJoin(observableArray).subscribe(() => {
      this.generateConnections(alignmentData, diagramElements);
    });
  }

  generateConnections(alignmentData: AlignmentLists, diagramElements: any[]): void {
    let edges: any[] = [];
    alignmentData.alignmentConnectionDtoList.forEach((alignmentConnection: AlignmentConnection) => {
      if (alignmentConnection.targetKeyResultId == null) {
        let edge = {
          data: {
            source: 'Ob' + alignmentConnection.alignedObjectiveId,
            target: 'Ob' + alignmentConnection.targetObjectiveId,
          },
        };
        edges.push(edge);
      } else {
        let edge = {
          data: {
            source: 'Ob' + alignmentConnection.alignedObjectiveId,
            target: 'KR' + alignmentConnection.targetKeyResultId,
          },
        };
        edges.push(edge);
      }
    });
    this.diagramData = diagramElements.concat(edges);
    this.generateDiagram();
  }

  generateObjectiveSVG(title: string, teamName: string, state: string): string {
    switch (state) {
      case 'ONGOING':
        return generateObjectiveSVG(title, teamName, getOnGoingIcon);
      case 'SUCCESSFUL':
        return generateObjectiveSVG(title, teamName, getSuccessfulIcon);
      case 'NOTSUCCESSFUL':
        return generateObjectiveSVG(title, teamName, getNotSuccessfulIcon);
      default:
        return generateObjectiveSVG(title, teamName, getDraftIcon);
    }
  }

  generateKeyResultSVG(title: string, teamName: string, state: string | undefined): string {
    switch (state) {
      case 'FAIL':
        return generateKeyResultSVG(title, teamName, '#BA3838', 'white');
      case 'COMMIT':
        return generateKeyResultSVG(title, teamName, '#FFD600', 'black');
      case 'TARGET':
        return generateKeyResultSVG(title, teamName, '#1E8A29', 'black');
      case 'STRETCH':
        return generateKeyResultSVG(title, teamName, '#1E5A96', 'white');
      default:
        return generateNeutralKeyResultSVG(title, teamName);
    }
  }

  cleanUpDiagram() {
    if (this.cy) {
      this.cy.edges().remove();
      this.cy.nodes().remove();
      this.cy.removeAllListeners();
    }
  }
}
