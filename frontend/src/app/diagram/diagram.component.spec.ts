import { DiagramComponent } from './diagram.component';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { overViewEntity1, overViewEntity2 } from '../shared/testData';

describe('DiagramComponent', () => {
  let component: DiagramComponent;
  let fixture: ComponentFixture<DiagramComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DiagramComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DiagramComponent);
    component = fixture.componentInstance;
    component.overviewEntity = [overViewEntity1, overViewEntity2];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
