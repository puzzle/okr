import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExampleDialogComponent } from './example-dialog.component';

describe('ExampleDialogComponent', () => {
  let component: ExampleDialogComponent;
  let fixture: ComponentFixture<ExampleDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ExampleDialogComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ExampleDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
