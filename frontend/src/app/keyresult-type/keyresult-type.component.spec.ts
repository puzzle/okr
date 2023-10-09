import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyresultTypeComponent } from './keyresult-type.component';

describe('KeyresultTypeComponent', () => {
  let component: KeyresultTypeComponent;
  let fixture: ComponentFixture<KeyresultTypeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [KeyresultTypeComponent]
    });
    fixture = TestBed.createComponent(KeyresultTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
