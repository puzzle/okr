import { DiagramComponent } from './diagram.component';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('DiagramComponent', () => {
  let component: DiagramComponent;
  let fixture: ComponentFixture<DiagramComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DiagramComponent],
      imports: [HttpClientTestingModule],
    });
    fixture = TestBed.createComponent(DiagramComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
