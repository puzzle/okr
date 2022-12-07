import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultFormComponent } from './key-result-form.component';

describe('KeyResultFormComponent', () => {
  let component: KeyResultFormComponent;
  let fixture: ComponentFixture<KeyResultFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [KeyResultFormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(KeyResultFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
