import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageUnitsDialogComponent } from './manage-units-dialog.component';

describe('ManageUnitsDialogComponent', () => {
  let component: ManageUnitsDialogComponent;
  let fixture: ComponentFixture<ManageUnitsDialogComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [ManageUnitsDialogComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ManageUnitsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
