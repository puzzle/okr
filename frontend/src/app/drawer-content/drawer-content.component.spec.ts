import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrawerContentComponent } from './drawer-content.component';
import { By } from '@angular/platform-browser';
import { ObjectiveDetailComponent } from '../objective-detail/objective-detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatDialogModule } from '@angular/material/dialog';

describe('DrawerContentComponent', () => {
  let component: DrawerContentComponent;
  let fixture: ComponentFixture<DrawerContentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MatDialogModule],
      declarations: [DrawerContentComponent, ObjectiveDetailComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DrawerContentComponent);
    component = fixture.componentInstance;
    component.drawerContent = { id: 1, type: 'objective' };
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it.each([['objective', 'app-objective-detail']])(
    'should display right component',
    (type: string, selector: string) => {
      component.drawerContent = { id: 1, type: type };
      fixture.detectChanges();
      const contentComponent = fixture.debugElement.query(By.css(selector));

      expect(contentComponent).toBeTruthy();
    },
  );
});
