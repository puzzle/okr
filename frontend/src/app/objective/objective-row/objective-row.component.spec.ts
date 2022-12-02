import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveRowComponent } from './objective-row.component';
import { Objective } from '../../services/objective.service';
import { MatMenuModule } from '@angular/material/menu';
import { By } from '@angular/platform-browser';

describe('ObjectiveComponent', () => {
  let component: ObjectiveRowComponent;
  let fixture: ComponentFixture<ObjectiveRowComponent>;

  let objective: Objective = {
    id: 1,
    title: 'Wir wollen unseren Umsatz verdoppeln',
    ownerId: 2,
    ownerFirstname: 'Ruedi',
    ownerLastname: 'Grochde',
    description: 'Sehr wichtig',
    progress: 5,
    quarterId: 1,
    quarterNumber: 3,
    quarterYear: 2022,
    created: '01.01.2022'
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MatMenuModule],
      declarations: [ObjectiveRowComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ObjectiveRowComponent);
    component = fixture.componentInstance;
    component.objective = objective;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have objetive title as class objectiveTitle', () => {
    expect(
      fixture.nativeElement.querySelector('.objectiveTitle').textContent
    ).toEqual(objective.title);
  });

  it('should have progress label with progress from objective', () => {
    expect(
      fixture.nativeElement.querySelector('#progressSpan').textContent
    ).toEqual(objective.progress + '%');
  });

  it('should have progress bar with progress from objective', () => {
    expect(
      fixture.nativeElement.querySelector('.objectiveProgress').value
    ).toEqual(objective.progress.toString());
  });

  it('should have right icon arrow down', () => {
    expect(fixture.nativeElement.querySelector('.pointer').textContent).toEqual(
      'keyboard_arrow_down'
    );
  });

  it('should have menu button with icon', () => {
    expect(fixture.nativeElement.querySelector('button').textContent).toEqual(
      'more_vert'
    );
  });

  it('should have button with icon', () => {
    expect(fixture.nativeElement.querySelector('button').textContent).toEqual(
      'more_vert'
    );
  });

  // it('should have 4 menu items', () => {
  //   let button = fixture.debugElement.nativeElement.querySelector('#triggerButton');
  //   button.click();
  //   expect(
  //     fixture.debugElement.nativeElement.querySelector('.matMenu')
  //   ).toEqual(" ");
  // });
});
