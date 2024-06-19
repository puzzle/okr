import { AfterViewInit, Component, Input, OnDestroy } from '@angular/core';
import { map, Observable, of, Subject, zip } from 'rxjs';
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
import { Zone } from '../shared/types/enums/Zone';
import { ObjectiveState } from '../shared/types/enums/ObjectiveState';
import { RefreshDataService } from '../shared/services/refresh-data.service';

@Component({
  selector: 'app-diagram',
  templateUrl: './diagram.component.html',
  styleUrl: './diagram.component.scss',
})
export class DiagramComponent implements AfterViewInit, OnDestroy {
  @Input()
  public alignmentData$: Subject<AlignmentLists> = new Subject<AlignmentLists>();
  cy!: cytoscape.Core;
  diagramData: any[] = [];
  alignmentDataCache: AlignmentLists | null = null;
  reloadRequired: boolean | null | undefined = false;

  constructor(
    private keyResultService: KeyresultService,
    private refreshDataService: RefreshDataService,
    private router: Router,
  ) {}

  ngAfterViewInit(): void {
    this.refreshDataService.reloadAlignmentSubject.subscribe((value: boolean | null | undefined): void => {
      this.reloadRequired = value;
    });

    this.alignmentData$.subscribe((alignmentData: AlignmentLists): void => {
      if (this.reloadRequired == true || JSON.stringify(this.alignmentDataCache) !== JSON.stringify(alignmentData)) {
        this.reloadRequired = undefined;
        this.alignmentDataCache = alignmentData;
        this.diagramData = [];
        this.cleanUpDiagram();
        this.prepareDiagramData(alignmentData);
      }
    });
  }

  ngOnDestroy(): void {
    this.cleanUpDiagram();
    this.alignmentData$.unsubscribe();
    this.refreshDataService.reloadAlignmentSubject.unsubscribe();
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
    if (alignmentData.alignmentObjectDtoList.length != 0) {
      this.generateNodes(alignmentData);
    }
  }

  generateNodes(alignmentData: AlignmentLists): void {
    let observableArray: any[] = [];
    let diagramElements: any[] = [];
    alignmentData.alignmentObjectDtoList.forEach((alignmentObject: AlignmentObject) => {
      if (alignmentObject.objectType == 'objective') {
        let node = {
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
        diagramElements.push(node);
        observableArray.push(of(node));
      } else {
        let observable: Observable<void> = this.keyResultService.getFullKeyResult(alignmentObject.objectId).pipe(
          map((keyResult: KeyResult) => {
            let keyResultState: string | undefined;

            if (keyResult.keyResultType == 'metric') {
              let metricKeyResult: KeyResultMetric = keyResult as KeyResultMetric;
              let percentage: number = calculateCurrentPercentage(metricKeyResult);
              keyResultState = this.generateMetricKeyResultState(percentage);
            } else {
              let ordinalKeyResult: KeyResultOrdinal = keyResult as KeyResultOrdinal;
              keyResultState = ordinalKeyResult.lastCheckIn?.value.toString();
            }
            let element = this.generateKeyResultElement(alignmentObject, keyResultState);
            diagramElements.push(element);
          }),
        );
        observableArray.push(observable);
      }
    });

    zip(observableArray).subscribe(async () => {
      await this.generateConnections(alignmentData, diagramElements);
    });
  }

  generateMetricKeyResultState(percentage: number): string | undefined {
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
    return keyResultState;
  }

  generateKeyResultElement(alignmentObject: AlignmentObject, keyResultState: string | undefined) {
    return {
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
  }

  async generateConnections(alignmentData: AlignmentLists, diagramElements: any[]) {
    let edges: any[] = [];
    alignmentData.alignmentConnectionDtoList.forEach((alignmentConnection: AlignmentConnection) => {
      let edge = {
        data: {
          source: 'Ob' + alignmentConnection.alignedObjectiveId,
          target:
            alignmentConnection.targetKeyResultId == null
              ? 'Ob' + alignmentConnection.targetObjectiveId
              : 'KR' + alignmentConnection.targetKeyResultId,
        },
      };
      edges.push(edge);
    });
    this.diagramData = diagramElements.concat(edges);

    // Sometimes the DOM Element #cy is not ready when cytoscape tries to generate the diagram
    // To avoid this, we use here a setTimeout()
    setTimeout(() => this.generateDiagram(), 0);
  }

  generateObjectiveSVG(title: string, teamName: string, state: string): string {
    switch (state) {
      case ObjectiveState.ONGOING:
        return generateObjectiveSVG(title, teamName, getOnGoingIcon);
      case ObjectiveState.SUCCESSFUL:
        return generateObjectiveSVG(title, teamName, getSuccessfulIcon);
      case ObjectiveState.NOTSUCCESSFUL:
        return generateObjectiveSVG(title, teamName, getNotSuccessfulIcon);
      default:
        return generateObjectiveSVG(title, teamName, getDraftIcon);
    }
  }

  generateKeyResultSVG(title: string, teamName: string, state: string | undefined): string {
    switch (state) {
      case Zone.FAIL:
        return generateKeyResultSVG(title, teamName, '#BA3838', 'white');
      case Zone.COMMIT:
        return generateKeyResultSVG(title, teamName, '#FFD600', 'black');
      case Zone.TARGET:
        return generateKeyResultSVG(title, teamName, '#1E8A29', 'black');
      case Zone.STRETCH:
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
