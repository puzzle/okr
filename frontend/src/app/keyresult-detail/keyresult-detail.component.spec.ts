import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyresultDetailComponent } from './keyresult-detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatDialogModule } from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';
import { of } from 'rxjs';
import { keyResult, keyResultWriteableFalse } from '../shared/testData';
import { By } from '@angular/platform-browser';
import { KeyresultService } from '../shared/services/keyresult.service';
import { MatIconModule } from '@angular/material/icon';

const keyResultServiceMock = {
  getFullKeyResult: jest.fn(),
};

describe('KeyresultDetailComponent', () => {
  let component: KeyresultDetailComponent;
  let fixture: ComponentFixture<KeyresultDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MatDialogModule, MatIconModule, TranslateModule.forRoot()],
      declarations: [KeyresultDetailComponent],
      providers: [
        {
          provide: KeyresultService,
          useValue: keyResultServiceMock,
        },
      ],
    }).compileComponents();

    jest.spyOn(keyResultServiceMock, 'getFullKeyResult').mockReturnValue(of(keyResult));
    fixture = TestBed.createComponent(KeyresultDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display edit keyresult button if writeable is true', async () => {
    const button = fixture.debugElement.query(By.css('[data-testId="edit-keyResult"]'));
    expect(button).toBeTruthy();
  });

  it('should not display edit keyresult button if writeable is false', async () => {
    jest.spyOn(keyResultServiceMock, 'getFullKeyResult').mockReturnValue(of(keyResultWriteableFalse));
    component.ngOnInit();
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('[data-testId="edit-keyResult"]'));
    expect(button).toBeFalsy();
  });

  it('should display add check-in button if writeable is true', async () => {
    const button = fixture.debugElement.query(By.css('[data-testId="add-check-in"]'));
    expect(button).toBeTruthy();
  });

  it('should not display add check-in button if writeable is false', async () => {
    jest.spyOn(keyResultServiceMock, 'getFullKeyResult').mockReturnValue(of(keyResultWriteableFalse));
    component.ngOnInit();
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('[data-testId="add-check-in"]'));
    expect(button).toBeFalsy();
  });
});
