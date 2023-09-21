import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveDetailComponent } from './objective-detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';
import { ObjectiveService } from '../shared/services/objective.service';
import { objective } from '../shared/testData';
import { SimpleChange, SimpleChanges } from '@angular/core';
import { of } from 'rxjs';

let objectiveService = {
  getFullObjective: jest.fn(),
};

describe('ObjectiveDetailComponent', () => {
  let component: ObjectiveDetailComponent;
  let fixture: ComponentFixture<ObjectiveDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [{ provide: ObjectiveService, useValue: objectiveService }],
      declarations: [ObjectiveDetailComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ObjectiveDetailComponent);
    component = fixture.componentInstance;
    objectiveService.getFullObjective.mockReturnValue(of(objective));
  });

  it('should create', () => {
    fixture.detectChanges();

    expect(component).toBeTruthy();
  });

  it('close on close button clicked', () => {
    fixture.detectChanges();

    const spy = jest.spyOn(ObjectiveDetailComponent.prototype, 'closeDrawer');
    const debugElement = fixture.debugElement.query(By.css('img[data-test-id="closeDrawer"]'));
    debugElement.nativeElement.click();
    fixture.detectChanges();
    expect(spy).toBeCalledTimes(1);
  });

  it('get data from backend', () => {
    component.objectiveId = '2';
    fixture.detectChanges();
    const changesObj: SimpleChanges = {
      prop1: new SimpleChange(null, '2', false),
    };
    component.ngOnChanges(changesObj);
    fixture.detectChanges();

    const title = fixture.debugElement.query(By.css('.title')).nativeElement.innerHTML;
    const description = fixture.debugElement.query(By.css('[data-test-id="description"]')).nativeElement.innerHTML;
    expect(title).toContain(objective.title);
    expect(description).toContain(objective.description);
  });
});
