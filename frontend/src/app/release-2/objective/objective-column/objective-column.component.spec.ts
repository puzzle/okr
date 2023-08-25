import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveColumnComponent } from './objective-column.component';
import { MatMenuModule } from '@angular/material/menu';
import { MatCardModule } from '@angular/material/card';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('ObjectiveColumnComponent', () => {
  let component: ObjectiveColumnComponent;
  let fixture: ComponentFixture<ObjectiveColumnComponent>;
  let loader: HarnessLoader;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ObjectiveColumnComponent],
      imports: [MatMenuModule, MatCardModule, NoopAnimationsModule],
    }).compileComponents();
    fixture = TestBed.createComponent(ObjectiveColumnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
