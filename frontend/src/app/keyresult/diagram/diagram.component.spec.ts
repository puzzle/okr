import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiagramComponent } from './diagram.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import {
  KeyResultService,
  Measure,
} from '../../shared/services/key-result.service';
import * as goalData from '../../shared/testing/mock-data/goals.json';
import { loadAllMeasure } from '../../shared/testing/Loader';

describe('DiagramComponent', () => {
  let component: DiagramComponent;
  let fixture: ComponentFixture<DiagramComponent>;

  let measures: any[] = loadAllMeasure(true);

  const mockKeyResultService = {
    getMeasuresOfKeyResult: jest.fn(),
  };

  beforeEach(() => {
    mockKeyResultService.getMeasuresOfKeyResult.mockReturnValue(of(measures));

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: KeyResultService, useValue: mockKeyResultService },
      ],

      declarations: [DiagramComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DiagramComponent);
    component = fixture.componentInstance;
    component.goal = goalData.goals[0];
    fixture.detectChanges();
  });

  afterEach(() => {
    mockKeyResultService.getMeasuresOfKeyResult.mockReset();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get Key Result by id', () => {
    expect(mockKeyResultService.getMeasuresOfKeyResult).toHaveBeenCalledTimes(
      1
    );
    expect(mockKeyResultService.getMeasuresOfKeyResult).toHaveBeenCalledWith(1);
  });

  it('should create correct Diagram Objects', () => {
    let diagrammObjects: any[] = [];
    diagrammObjects = component.generateDiagrammObjects(measures);
    measures.forEach((measure) => {
      let number = measures.indexOf(measure);
      expect(diagrammObjects[number].y).toEqual(measure.value);
      expect(diagrammObjects[number].x).toEqual(measure.measureDate);
    });
  });

  it('should sort measures ascending by date (lowest date at the top)', () => {
    let diagrammObjects = component.generateDiagrammObjects(measures);

    expect(diagrammObjects[0].x).toEqual('2022-12-01T00:00:00.000Z');
    expect(diagrammObjects[0].y).toEqual(33);
    expect(diagrammObjects[1].x).toEqual('2022-12-23');
    expect(diagrammObjects[1].y).toEqual(0);
    expect(diagrammObjects[2].x).toEqual('2023-01-10T22:00:00Z');
    expect(diagrammObjects[2].y).toEqual(42);
  });

  it('should update chart', () => {
    jest.spyOn(component.diagram, 'update').mockReturnValue();
    expect(component.diagram.data.datasets[0].data.length).toEqual(7);
    let slicedMeasures: Measure[] = measures.slice(0, measures.length - 1);
    component.reloadDiagram(slicedMeasures);
    expect(component.diagram.data.datasets[0].data.length).toEqual(6);
  });
});
