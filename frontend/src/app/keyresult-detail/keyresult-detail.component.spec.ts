import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyresultDetailComponent } from './keyresult-detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatDialogModule } from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';

describe('KeyresultDetailComponent', () => {
  let component: KeyresultDetailComponent;
  let fixture: ComponentFixture<KeyresultDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MatDialogModule, TranslateModule.forRoot()],
      declarations: [KeyresultDetailComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(KeyresultDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
