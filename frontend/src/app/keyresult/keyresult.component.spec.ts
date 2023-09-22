import { ComponentFixture, TestBed } from '@angular/core/testing';
import { KeyresultComponent } from './keyresult.component';
import { keyResultMetricMin } from '../shared/testData';
import { MatDialogModule } from '@angular/material/dialog';

describe('KeyresultComponent', () => {
  let component: KeyresultComponent;
  let fixture: ComponentFixture<KeyresultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [KeyresultComponent],
      imports: [MatDialogModule],
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
