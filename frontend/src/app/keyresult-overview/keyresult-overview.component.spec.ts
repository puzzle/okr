import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyresultOverviewComponent } from './keyresult-overview.component';

describe('KeyresultOverviewComponent', () => {
  let component: KeyresultOverviewComponent;
  let fixture: ComponentFixture<KeyresultOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [KeyresultOverviewComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(KeyresultOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
