import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyresultDeleteDialogComponent } from './keyresult-delete-dialog.component';

describe('KeyresultDeleteDialogComponent', () => {
  let component: KeyresultDeleteDialogComponent;
  let fixture: ComponentFixture<KeyresultDeleteDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [KeyresultDeleteDialogComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(KeyresultDeleteDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
