import { DiagramComponent } from './diagram.component';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AlignmentLists } from '../shared/types/model/AlignmentLists';
import { alignmentLists, alignmentListsKeyResult, keyResult, keyResultMetric } from '../shared/testData';
import * as functions from './svgGeneration';
import { getDraftIcon, getNotSuccessfulIcon, getOnGoingIcon, getSuccessfulIcon } from './svgGeneration';
import { of } from 'rxjs';
import { KeyresultService } from '../shared/services/keyresult.service';
import { ParseUnitValuePipe } from '../shared/pipes/parse-unit-value/parse-unit-value.pipe';

const keyResultServiceMock = {
  getFullKeyResult: jest.fn(),
};

describe('DiagramComponent', () => {
  let component: DiagramComponent;
  let fixture: ComponentFixture<DiagramComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DiagramComponent],
      imports: [HttpClientTestingModule],
      providers: [{ provide: KeyresultService, useValue: keyResultServiceMock }, ParseUnitValuePipe],
    });
    fixture = TestBed.createComponent(DiagramComponent);
    URL.createObjectURL = jest.fn();
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call cleanUpDiagram when ngOnDestroy gets called', () => {
    jest.spyOn(component, 'cleanUpDiagram');

    component.ngOnDestroy();
    expect(component.cleanUpDiagram).toHaveBeenCalled();
  });

  it('should call generateElements if alignmentData is present', () => {
    jest.spyOn(component, 'generateNodes');

    component.prepareDiagramData(alignmentLists);
    expect(component.generateNodes).toHaveBeenCalled();
    expect(component.alignmentDataCache?.alignmentObjectDtoList.length).not.toEqual(0);
  });

  it('should not call generateElements if alignmentData is empty', () => {
    jest.spyOn(component, 'generateNodes');

    let alignmentLists: AlignmentLists = {
      alignmentObjectDtoList: [],
      alignmentConnectionDtoList: [],
    };

    component.prepareDiagramData(alignmentLists);
    expect(component.generateNodes).not.toHaveBeenCalled();
  });

  it('should call prepareDiagramData when Subject receives new data', () => {
    jest.spyOn(component, 'cleanUpDiagram');
    jest.spyOn(component, 'prepareDiagramData');

    component.ngAfterViewInit();
    component.alignmentData.next(alignmentLists);

    expect(component.cleanUpDiagram).toHaveBeenCalled();
    expect(component.prepareDiagramData).toHaveBeenCalledWith(alignmentLists);
  });

  it('should generate correct diagramData for Objectives', () => {
    jest.spyOn(component, 'generateConnections');
    jest.spyOn(component, 'generateDiagram');
    jest.spyOn(component, 'generateObjectiveSVG').mockReturnValue('Test.svg');

    let edge = {
      data: {
        source: 'Ob1',
        target: 'Ob2',
      },
    };
    let element1 = {
      data: {
        id: 'Ob1',
      },
      style: {
        'background-image': 'Test.svg',
      },
    };
    let element2 = {
      data: {
        id: 'Ob2',
      },
      style: {
        'background-image': 'Test.svg',
      },
    };

    let diagramElements: any[] = [element1, element2];
    let edges: any[] = [edge];

    component.generateNodes(alignmentLists);

    expect(component.generateConnections).toHaveBeenCalled();
    expect(component.generateDiagram).toHaveBeenCalled();
    expect(component.diagramData).toEqual(diagramElements.concat(edges));
  });

  it('should generate correct diagramData for KeyResult Metric', () => {
    jest.spyOn(component, 'generateConnections');
    jest.spyOn(component, 'generateDiagram');
    jest.spyOn(component, 'generateObjectiveSVG').mockReturnValue('TestObjective.svg');
    jest.spyOn(component, 'generateKeyResultSVG').mockReturnValue('TestKeyResult.svg');
    jest.spyOn(keyResultServiceMock, 'getFullKeyResult').mockReturnValue(of(keyResultMetric));

    let diagramData: any[] = getReturnedAlignmentDataKeyResult();

    component.generateNodes(alignmentListsKeyResult);

    expect(component.generateConnections).toHaveBeenCalled();
    expect(component.generateDiagram).toHaveBeenCalled();
    expect(component.diagramData).toEqual(diagramData);
  });

  it('should generate correct diagramData for KeyResult Ordinal', () => {
    jest.spyOn(component, 'generateConnections');
    jest.spyOn(component, 'generateDiagram');
    jest.spyOn(component, 'generateObjectiveSVG').mockReturnValue('TestObjective.svg');
    jest.spyOn(component, 'generateKeyResultSVG').mockReturnValue('TestKeyResult.svg');
    jest.spyOn(keyResultServiceMock, 'getFullKeyResult').mockReturnValue(of(keyResult));

    let diagramData: any[] = getReturnedAlignmentDataKeyResult();

    component.generateNodes(alignmentListsKeyResult);

    expect(component.generateConnections).toHaveBeenCalled();
    expect(component.generateDiagram).toHaveBeenCalled();
    expect(component.diagramData).toEqual(diagramData);
  });

  it('should generate correct SVGs for Objective', () => {
    jest.spyOn(functions, 'generateObjectiveSVG');

    component.generateObjectiveSVG('Title 1', 'Team name 1', 'ONGOING');
    expect(functions.generateObjectiveSVG).toHaveBeenCalledWith('Title 1', 'Team name 1', getOnGoingIcon);

    component.generateObjectiveSVG('Title 2', 'Team name 2', 'SUCCESSFUL');
    expect(functions.generateObjectiveSVG).toHaveBeenCalledWith('Title 2', 'Team name 2', getSuccessfulIcon);

    component.generateObjectiveSVG('Title 3', 'Team name 3', 'NOTSUCCESSFUL');
    expect(functions.generateObjectiveSVG).toHaveBeenCalledWith('Title 3', 'Team name 3', getNotSuccessfulIcon);

    component.generateObjectiveSVG('Title 4', 'Team name 4', 'DRAFT');
    expect(functions.generateObjectiveSVG).toHaveBeenCalledWith('Title 4', 'Team name 4', getDraftIcon);
  });

  it('should generate correct SVGs for KeyResult', () => {
    jest.spyOn(functions, 'generateObjectiveSVG');

    component.generateObjectiveSVG('Title 1', 'Team name 1', 'ONGOING');
    expect(functions.generateObjectiveSVG).toHaveBeenCalledWith('Title 1', 'Team name 1', getOnGoingIcon);

    component.generateObjectiveSVG('Title 2', 'Team name 2', 'SUCCESSFUL');
    expect(functions.generateObjectiveSVG).toHaveBeenCalledWith('Title 2', 'Team name 2', getSuccessfulIcon);

    component.generateObjectiveSVG('Title 3', 'Team name 3', 'NOTSUCCESSFUL');
    expect(functions.generateObjectiveSVG).toHaveBeenCalledWith('Title 3', 'Team name 3', getNotSuccessfulIcon);

    component.generateObjectiveSVG('Title 4', 'Team name 4', 'DRAFT');
    expect(functions.generateObjectiveSVG).toHaveBeenCalledWith('Title 4', 'Team name 4', getDraftIcon);
  });
});

function getReturnedAlignmentDataKeyResult(): any[] {
  let edge = {
    data: {
      source: 'Ob3',
      target: 'KR102',
    },
  };
  let element1 = {
    data: {
      id: 'Ob3',
    },
    style: {
      'background-image': 'TestObjective.svg',
    },
  };
  let element2 = {
    data: {
      id: 'KR102',
    },
    style: {
      'background-image': 'TestKeyResult.svg',
    },
  };

  let diagramElements: any[] = [element1, element2];
  let edges: any[] = [edge];

  return diagramElements.concat(edges);
}
