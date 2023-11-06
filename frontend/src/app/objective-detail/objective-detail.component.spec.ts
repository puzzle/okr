import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveDetailComponent } from './objective-detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';
import { ObjectiveService } from '../shared/services/objective.service';
import { objective, objectiveWriteableFalse } from '../shared/testData';
import { of } from 'rxjs';
import { MatDialogModule } from '@angular/material/dialog';

let objectiveService = {
  getFullObjective: jest.fn(),
};

describe('ObjectiveDetailComponent', () => {
  let component: ObjectiveDetailComponent;
  let fixture: ComponentFixture<ObjectiveDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MatDialogModule],
      providers: [{ provide: ObjectiveService, useValue: objectiveService }],
      declarations: [ObjectiveDetailComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ObjectiveDetailComponent);
    component = fixture.componentInstance;
    objectiveService.getFullObjective.mockReturnValue(of(objective));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('get data from backend', () => {
    fixture.detectChanges();
    component.objectiveId = 2;
    const title = fixture.debugElement.query(By.css('.title')).nativeElement.innerHTML;
    const description = fixture.debugElement.query(By.css('[data-test-id="description"]')).nativeElement.innerHTML;
    expect(title).toContain(objective.title);
    expect(description).toContain(objective.description);
  });

  it('should display add keyresult button if writeable is true', async () => {
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('[data-testId="add-keyResult-objective-detail"]'));
    expect(button).toBeTruthy();
  });

  it('should not display add keyresult button if writeable is false', async () => {
    objectiveService.getFullObjective.mockReturnValue(of(objectiveWriteableFalse));
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('[data-testId="add-keyResult-objective-detail"]'));
    expect(button).toBeFalsy();
  });
});
