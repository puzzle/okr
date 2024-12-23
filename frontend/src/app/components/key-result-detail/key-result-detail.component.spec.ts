import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultDetailComponent } from './key-result-detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatDialogModule } from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';
import { of } from 'rxjs';
import { keyResult, keyResultWriteableFalse } from '../../shared/testData';
import { By } from '@angular/platform-browser';
import { KeyresultService } from '../../services/keyresult.service';
import { MatIconModule } from '@angular/material/icon';
import { ActivatedRoute } from '@angular/router';
import { ScoringComponent } from '../../shared/custom/scoring/scoring.component';
import { ConfidenceComponent } from '../confidence/confidence.component';
import { RefreshDataService } from '../../services/refresh-data.service';

const keyResultServiceMock = {
  getFullKeyResult: jest.fn()
};

const activatedRouteMock = {
  snapshot: {
    paramMap: {
      get: jest.fn()
    }
  }
};

describe('KeyResultDetailComponent', () => {
  let component: KeyResultDetailComponent;
  let fixture: ComponentFixture<KeyResultDetailComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        MatDialogModule,
        MatIconModule,
        TranslateModule.forRoot()
      ],
      declarations: [KeyResultDetailComponent,
        ScoringComponent,
        ConfidenceComponent],
      providers: [{
        provide: KeyresultService,
        useValue: keyResultServiceMock
      },
      {
        provide: ActivatedRoute,
        useValue: activatedRouteMock
      }]
    })
      .compileComponents();

    jest.spyOn(keyResultServiceMock, 'getFullKeyResult')
      .mockReturnValue(of(keyResult));
    activatedRouteMock.snapshot.paramMap.get.mockReturnValue(of(1));

    fixture = TestBed.createComponent(KeyResultDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should throw error when id is undefined', () => {
    activatedRouteMock.snapshot.paramMap.get.mockReturnValue(undefined);
    expect(() => component.ngOnInit())
      .toThrowError('keyresult id is undefined');
  });

  it('should display edit key-result button if writeable is true', async() => {
    const button = fixture.debugElement.query(By.css('[data-testId="edit-keyResult"]'));
    expect(button)
      .toBeTruthy();
  });

  it('should not display edit key-result button if writeable is false', async() => {
    jest.spyOn(keyResultServiceMock, 'getFullKeyResult')
      .mockReturnValue(of(keyResultWriteableFalse));
    component.ngOnInit();
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('[data-testId="edit-keyResult"]'));
    expect(button)
      .toBeFalsy();
  });

  it('should display add check-in button if writeable is true', async() => {
    const button = fixture.debugElement.query(By.css('[data-testId="add-check-in"]'));
    expect(button)
      .toBeTruthy();
  });

  it('should not display add check-in button if writeable is false', async() => {
    jest.spyOn(keyResultServiceMock, 'getFullKeyResult')
      .mockReturnValue(of(keyResultWriteableFalse));
    component.ngOnInit();
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('[data-testId="add-check-in"]'));
    expect(button)
      .toBeFalsy();
  });

  it('should trigger observable when subject gets next value', () => {
    const spy = jest.spyOn(component, 'loadKeyResult');
    const refreshDataService = TestBed.inject(RefreshDataService);
    refreshDataService.reloadKeyResultSubject.next();
    expect(spy)
      .toHaveBeenCalled();
  });

  it('should close subscription on destroy', () => {
    const spyNext = jest.spyOn(component.ngDestroy$, 'next');
    const spyComplete = jest.spyOn(component.ngDestroy$, 'complete');

    component.ngOnDestroy();

    expect(spyNext)
      .toHaveBeenCalled();
    expect(spyComplete)
      .toHaveBeenCalled();
  });
});
