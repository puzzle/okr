import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RowComponent } from './row.component';
import { MatMenuModule } from '@angular/material/menu';
import { Objective } from '../../models/Objective';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MenuEntry } from '../../models/MenuEntry';
import { RouterTestingModule } from '@angular/router/testing';
import { MatProgressBarModule } from '@angular/material/progress-bar';

describe('RowComponent', () => {
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
    quarterNumber: 3,
    quarterYear: 2022,
    created: '',
  };

  let menuEntries: MenuEntry[] = [
    { displayName: 'Resultat hinzufügen', routeLine: 'result/add' },
    { displayName: 'Ziel bearbeiten', routeLine: 'objective/edit' },
    { displayName: 'Ziel duplizieren', routeLine: 'objective/duplicate' },
    { displayName: 'Ziel löschen', routeLine: 'objective/delete' },
  ];

  let information: string[] = ['Yanick Minder', '01.01.2022'];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        NoopAnimationsModule,
        MatMenuModule,
        MatExpansionModule,
        MatIconModule,
        RouterTestingModule,
        MatProgressBarModule,
      ],
      declarations: [RowComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(RowComponent);
    component = fixture.componentInstance;
    component.element = objective;
    component.menuEntries = menuEntries;
    component.information = information;
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
      fixture.nativeElement.querySelector('#progressContainer').children[0]
        .textContent
    ).toEqual(objective.progress + '%');
  });

  // it('should have progress bar with progress from objective', () => {
  //   let progressContainer = fixture.nativeElement.querySelector('#progressContainer');
  //   let progressbar = progressContainer.querySelector("mat-progress-bar");
  //   console.log(progressbar.getAttribute('value'));
  //   expect(
  //    progressbar.value
  //   ).toEqual(objective.progress.toString());
  // });

  it.each([
    [10, 'bad', true],
    [50, 'medium', true],
    [100, 'good', true],
    [100, 'good', false],
  ])(
    'ParameterizedTest',
    (progress: number, barIdentifier: string, result: boolean) => {
      component.element = { ...objective, progress: progress } as Objective;
      fixture.detectChanges();

      let progressContainer =
        fixture.nativeElement.querySelector('#progressContainer');
      let progressbar = progressContainer.querySelector('mat-progress-bar');

      let barPrefix = 'progress-bar-';
      let classList = [...progressbar.classList].filter((e) =>
        e.toString().includes(barPrefix)
      );

      let barName = barPrefix + barIdentifier;
      expect(classList[0] === barName).toBe(result);
    }
  );

  it('should have menu button with icon', () => {
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
