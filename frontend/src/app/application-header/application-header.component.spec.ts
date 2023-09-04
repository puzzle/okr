import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationHeaderComponent } from './application-header.component';

class ResizeObserverMock {
  observe() {}

  unobserve() {}
}

describe('ApplicationHeaderComponent', () => {
  //@ts-ignore
  global.ResizeObserver = ResizeObserverMock;
  let component: ApplicationHeaderComponent;
  let fixture: ComponentFixture<ApplicationHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ApplicationHeaderComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
