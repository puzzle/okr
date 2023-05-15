import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HelpDialogComponent } from './help-dialog.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { By } from '@angular/platform-browser';

describe('HelpDialog', () => {
  let component: HelpDialogComponent;
  let fixture: ComponentFixture<HelpDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HelpDialogComponent],
      providers: [
        { provide: MatDialogRef, useValue: {} },
        { provide: MAT_DIALOG_DATA, useValue: {} },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(HelpDialogComponent);
    component = fixture.componentInstance;
    component.title = 'Title';
    component.exampleTitle = 'Beispiele:';
    component.examples = ['example 1', 'example 2', 'example 3'];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should display correct labels', () => {
    const title = fixture.debugElement.query(By.css('#title'));
    const exampleTitel = fixture.debugElement.query(By.css('#exampleTitle'));
    const examples = fixture.debugElement.query(By.css('#examples'));
    expect(title.nativeElement.textContent).toContain(component.title);
    expect(exampleTitel.nativeElement.textContent).toContain(component.exampleTitle);
    expect(examples.nativeElement.textContent).toContain(component.examples![0]);
  });
});

describe('HelpDialog', () => {
  let component: HelpDialogComponent;
  let fixture: ComponentFixture<HelpDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HelpDialogComponent],
      providers: [
        { provide: MatDialogRef, useValue: {} },
        { provide: MAT_DIALOG_DATA, useValue: {} },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(HelpDialogComponent);
    component = fixture.componentInstance;
    component.title = 'Title';
    fixture.detectChanges();
  });

  test('should not throw error', () => {
    const title = fixture.debugElement.query(By.css('#title'));
    const exampleTitel = fixture.debugElement.query(By.css('#exampleTitle'));
    const examples = fixture.debugElement.query(By.css('#examples'));
    expect(title.nativeElement.textContent).toContain(component.title);
    expect(exampleTitel.nativeElement.textContent).toBe('');
  });
});
