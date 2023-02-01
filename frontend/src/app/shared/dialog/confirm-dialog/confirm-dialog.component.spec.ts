import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmDialogComponent } from './confirm-dialog.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { By } from '@angular/platform-browser';

describe('ConfirmDialog', () => {
  let component: ConfirmDialogComponent;
  let fixture: ComponentFixture<ConfirmDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ConfirmDialogComponent],
      providers: [
        { provide: MatDialogRef, useValue: {} },
        { provide: MAT_DIALOG_DATA, useValue: {} },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ConfirmDialogComponent);
    component = fixture.componentInstance;
    component.title = 'Willst du diese Aktion wirklich ausführen?';
    component.confirmText = 'Bestätigen';
    component.closeText = 'Abbrechen';
    fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should display correct labels', () => {
    const title = fixture.debugElement.query(By.css('#title'));
    expect(title.nativeElement.textContent).toContain(
      'Willst du diese Aktion wirklich ausführen?'
    );
  });

  test('should display correct confirm button text', () => {
    const title = fixture.debugElement.query(By.css('#confirm'));
    expect(title.nativeElement.textContent).toContain('Bestätigen');
  });

  test('should display correct close button text', () => {
    const title = fixture.debugElement.query(By.css('#close'));
    expect(title.nativeElement.textContent).toContain('Abbrechen');
  });
});
