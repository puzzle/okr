import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrawerContentComponent } from './drawer-content.component';

describe('DrawerContentComponent', () => {
  let component: DrawerContentComponent;
  let fixture: ComponentFixture<DrawerContentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DrawerContentComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DrawerContentComponent);
    component = fixture.componentInstance;
    //TODO: Set DrawerContent differently in other tests
    component.drawerContent = { id: '1', type: 'objective' };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
