import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveDetailComponent } from './objective-detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';
import { ObjectiveService } from '../../services/objective.service';
import { objective, objectiveWriteableFalse } from '../../shared/test-data';
import { of } from 'rxjs';
import { MatDialogModule } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { TranslateModule } from '@ngx-translate/core';

const objectiveService = {
  getFullObjective: jest.fn()
};

const activatedRouteMock = {
  snapshot: {
    paramMap: {
      get: jest.fn() as any
    }
  }
};

describe('ObjectiveDetailComponent', () => {
  let component: ObjectiveDetailComponent;
  let fixture: ComponentFixture<ObjectiveDetailComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        MatDialogModule,
        MatIconModule,
        TranslateModule.forRoot()
      ],
      providers: [{ provide: ObjectiveService,
        useValue: objectiveService },
      { provide: ActivatedRoute,
        useValue: activatedRouteMock }],
      declarations: [ObjectiveDetailComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ObjectiveDetailComponent);
    component = fixture.componentInstance;
    objectiveService.getFullObjective.mockReturnValue(of(objective));
    activatedRouteMock.snapshot.paramMap.get = jest.fn();
    activatedRouteMock.snapshot.paramMap.get.mockReturnValue(of(1));
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should throw an exception, if id is undefined', () => {
    activatedRouteMock.snapshot.paramMap.get = () => undefined;
    expect(() => component.ngOnInit())
      .toThrowError('objective id is undefined');
  });

  it('get data from backend', () => {
    fixture.detectChanges();
    component.objectiveId = 2;
    const title = fixture.debugElement.query(By.css('[data-testId="objective-title"]'))?.nativeElement.innerHTML;
    const description = fixture.debugElement.query(By.css('[data-testId="description"]'))?.nativeElement.innerHTML;
    expect(title)
      .toContain(objective.title);
    expect(description)
      .toContain(objective.description);
  });

  it('should display add key-result button if writeable is true', async() => {
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('[data-testId="add-key-result-objective-detail"]'));
    expect(button)
      .toBeTruthy();
  });

  it('should not display add key-result button if writeable is false', async() => {
    objectiveService.getFullObjective.mockReturnValue(of(objectiveWriteableFalse));
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('[data-testId="add-key-result-objective-detail"]'));
    expect(button)
      .toBeFalsy();
  });
});
