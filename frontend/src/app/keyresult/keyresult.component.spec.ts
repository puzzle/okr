import { ComponentFixture, TestBed } from '@angular/core/testing';
import { KeyresultComponent } from './keyresult.component';
import { MatDialogModule } from '@angular/material/dialog';
import { keyResultMetricMin } from '../shared/testData';

describe('KeyresultComponent', () => {
  let component: KeyresultComponent;
  let fixture: ComponentFixture<KeyresultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MatDialogModule],
      declarations: [KeyresultComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(KeyresultComponent);
    component = fixture.componentInstance;
    component.keyResult = keyResultMetricMin;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
