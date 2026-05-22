import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArchiveDialogComponent } from './archive-dialog.component';

describe('ArchiveDialogComponent', () => {
  let component: ArchiveDialogComponent;
  let fixture: ComponentFixture<ArchiveDialogComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [ArchiveDialogComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ArchiveDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
