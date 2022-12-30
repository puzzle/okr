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
});
