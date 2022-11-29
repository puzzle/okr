import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RowComponent } from './row.component';
import { MatMenuModule } from '@angular/material/menu';
import { Objective } from '../../models/Objective';

describe('ObjectiveComponent', () => {
  let component: RowComponent;
  let fixture: ComponentFixture<RowComponent>;

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
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MatMenuModule],
      declarations: [RowComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(RowComponent);
    component = fixture.componentInstance;
    component.element = objective;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have objetive title as class objectiveTitle', () => {
    expect(
      fixture.nativeElement.querySelector('mat-panel-title').textContent
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
