import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultDetailComponent } from './key-result-detail.component';

describe('KeyResultDetailComponent', () => {
  let component: KeyResultDetailComponent;
  let fixture: ComponentFixture<KeyResultDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [KeyResultDetailComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(KeyResultDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
