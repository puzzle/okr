import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveFilterComponent } from './objective-filter.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AppRoutingModule } from '../app-routing.module';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('ObjectiveFilterComponent', () => {
  let component: ObjectiveFilterComponent;
  let fixture: ComponentFixture<ObjectiveFilterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ObjectiveFilterComponent],
      imports: [
        HttpClientTestingModule,
        AppRoutingModule,
        MatFormFieldModule,
        MatIconModule,
        FormsModule,
        MatInputModule,
        NoopAnimationsModule,
      ],
    });
    fixture = TestBed.createComponent(ObjectiveFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
