import { DiagramComponent } from './diagram.component';
import { ComponentFixture, TestBed } from '@angular/core/testing';

describe('DiagramComponent', () => {
  let component: DiagramComponent;
  let fixture: ComponentFixture<DiagramComponent>;

  beforeEach(() => {
    fixture = TestBed.createComponent(DiagramComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
