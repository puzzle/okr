import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiagramComponent } from './diagram.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable, of } from 'rxjs';
import {
  KeyResultService,
  Measure,
} from '../../shared/services/key-result.service';
import * as measureData from '../../shared/testing/mock-data/measure.json';

describe('DiagramComponent', () => {
  let component: DiagramComponent;
  let fixture: ComponentFixture<DiagramComponent>;

  let measures: Observable<Measure[]> = of(measureData.measures);

  const mockKeyResultService = {
    getMeasuresOfKeyResult: jest.fn(),
  };

  beforeEach(() => {
    mockKeyResultService.getMeasuresOfKeyResult.mockReturnValue(measures);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: KeyResultService, useValue: mockKeyResultService },
      ],

      declarations: [DiagramComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DiagramComponent);
    component = fixture.componentInstance;
    component.keyResultId = 1;
    fixture.detectChanges();
  });

  afterEach(() => {
    mockKeyResultService.getMeasuresOfKeyResult.mockReset();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get keyresult by id', () => {
    expect(mockKeyResultService.getMeasuresOfKeyResult).toHaveBeenCalledTimes(
      1
    );
    expect(mockKeyResultService.getMeasuresOfKeyResult).toHaveBeenCalledWith(1);
  });

  it('should set measures in component', () => {
    expect(component.measures).toEqual(measures);
  });

  it('should create correct Diagram Objects', () => {
    let diagrammObjects: any[] = [];
    let measuresOriginal: Measure[] = [];
    measures.subscribe((item) => {
      diagrammObjects = component.generateDiagrammObjects(item);
      measuresOriginal = item;
    });
    measuresOriginal.forEach((measure) => {
      let number = measuresOriginal.indexOf(measure);
      expect(diagrammObjects[number].y).toEqual(measure.value);
      expect(diagrammObjects[number].x).toEqual(measure.measureDate);
    });
  });

  it('should sort measures ascending by date (lowest date at the top)', () => {
    let diagrammObjects: any[] = [];
    measures.subscribe((item) => {
      diagrammObjects = component.generateDiagrammObjects(item);
    });
    expect(diagrammObjects[1].x).toEqual('2023-01-21');
    expect(diagrammObjects[2].x).toEqual('2023-03-18');
  });
});
