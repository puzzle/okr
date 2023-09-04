import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultDialogComponent } from './key-result-dialog.component';

describe('KeyResultDialogComponent', () => {
  let component: KeyResultDialogComponent;
  let fixture: ComponentFixture<KeyResultDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [KeyResultDialogComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(KeyResultDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
