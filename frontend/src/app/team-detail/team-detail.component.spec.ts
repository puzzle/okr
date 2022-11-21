import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamDetailComponent } from './team-detail.component';

describe('TeamDetailComponent', () => {
  let component: TeamDetailComponent;
  let fixture: ComponentFixture<TeamDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TeamDetailComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TeamDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
