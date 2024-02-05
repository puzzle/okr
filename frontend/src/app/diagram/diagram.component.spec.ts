import { DiagramComponent } from './diagram.component';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { alignment1 } from '../shared/testData';

describe('DiagramComponent', () => {
  let component: DiagramComponent;
  let fixture: ComponentFixture<DiagramComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DiagramComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DiagramComponent);
    component = fixture.componentInstance;
    component.alignmentEntity = [alignment1];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should split text correctly', () => {
    let title =
      'At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.';

    let receive = component.adjustLabel(title, 36, 15);

    expect(receive).toEqual('At vero eos et accusam et justo duo do...');
  });
});
