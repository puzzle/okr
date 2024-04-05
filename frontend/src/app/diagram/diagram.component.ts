import { AfterViewInit, Component, Input } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { AlignmentLists } from '../shared/types/model/AlignmentLists';
import cytoscape from 'cytoscape';

@Component({
  selector: 'app-diagram',
  templateUrl: './diagram.component.html',
  styleUrl: './diagram.component.scss',
})
export class DiagramComponent implements AfterViewInit {
  private alignmentData$ = new BehaviorSubject<AlignmentLists>({} as AlignmentLists);
  cy!: cytoscape.Core;

  @Input()
  get alignmentData(): BehaviorSubject<AlignmentLists> {
    return this.alignmentData$;
  }

  set alignmentData(alignmentData: AlignmentLists) {
    this.alignmentData$.next(alignmentData);
  }

  ngAfterViewInit() {
    this.alignmentData.subscribe((alignmentData: AlignmentLists): void => {
      this.generateDiagram(alignmentData);
    });
  }

  generateDiagram(alignmentData: AlignmentLists): void {
    let alignmentElements: any[] = this.generateElements(alignmentData);

    this.cy = cytoscape({
      container: document.getElementById('cy'),
      elements: alignmentElements,

      zoom: 1,
      zoomingEnabled: true,
      userZoomingEnabled: true,

      style: [
        {
          selector: '[id^="Ob"]',
          style: {
            label: 'data(id)',
            height: 160,
            width: 160,
          },
        },
        {
          selector: '[id^="KR"]',
          style: {
            label: 'data(id)',
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
  }

  generateElements(alignmentData: AlignmentLists) {
    let elements: any[] = [];
    let edges: any[] = [];
    alignmentData.alignmentObjectDtoList.forEach((alignmentObject) => {
      if (alignmentObject.objectType == 'objective') {
        let element = {
          data: {
            id: 'Ob' + alignmentObject.objectId,
            label: alignmentObject.objectTitle,
          },
          style: {
            // SVG config
          },
        };
        elements.push(element);
      } else {
        let element = {
          data: {
            id: 'KR' + alignmentObject.objectId,
            label: alignmentObject.objectTitle,
          },
          style: {
            // SVG config
          },
        };
        elements.push(element);
      }
    });

    alignmentData.alignmentConnectionDtoList.forEach((alignmentConnection) => {
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

    return elements.concat(edges);
  }
}
