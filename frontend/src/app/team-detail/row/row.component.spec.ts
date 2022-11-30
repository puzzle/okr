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
import { trigger } from '@angular/animations';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';

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

  let information: string[] = ['Yanick Minder', '01.01.2022'];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        BrowserDynamicTestingModule,
        NoopAnimationsModule,
        MatMenuModule,
        MatExpansionModule,
        MatIconModule,
        RouterTestingModule,
        MatProgressBarModule,
      ],
      declarations: [RowComponent],
    })
      .overrideComponent(RowComponent, {
        set: {
          animations: [trigger('entryModeTransition', [])],
        },
      })
      .compileComponents();

    fixture = TestBed.createComponent(RowComponent);
    component = fixture.componentInstance;
    component.element = objective;
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
    let progressLabel = fixture.nativeElement
      .querySelector('#progressContainer')
      .querySelector('.h5');
    expect(progressLabel.textContent).toEqual(objective.progress + '%');
  });

  it('should have progress bar with progress from objective', () => {
    let progressBar = fixture.nativeElement
      .querySelector('#progressContainer')
      .querySelector('mat-progress-bar');
    expect(progressBar.getAttribute('ng-reflect-value')).toEqual(
      objective.progress.toString()
    );
  });

  // @ts-ignore
  it.each([
    [100, 'good', true],
    [10, 'medium', true],
    [50, 'bad', true],
    [10, 'good', false],
  ])(
    'Progressbar test with value: %i, barname %s, result: %o',
    (progress: number, barIdentifier: string, result: boolean) => {
      fixture = TestBed.createComponent(RowComponent);
      component = fixture.componentInstance;
      component.element = { ...objective, progress: progress } as Objective;
      fixture.detectChanges();

      let progressBar = fixture.nativeElement
        .querySelector('#progressContainer')
        .querySelector('mat-progress-bar');

      let barPrefix = 'progress-bar-';
      let classList = [...progressBar.classList]
        .map((e) => e.toString())
        .filter((e) => e.includes(barPrefix));

      let expectedClass = barPrefix + barIdentifier;
      let activeClass = classList[0];
      expect(activeClass === expectedClass).toBe(result);
    }
  );

  it('should have menu button with icon', () => {
    expect(fixture.nativeElement.querySelector('button').textContent).toEqual(
      'more_vert'
    );
  });

  // @ts-ignore
  it.each([
    [
      [
        { displayName: 'Resultat hinzufügen', routeLine: 'result/add' },
        { displayName: 'Ziel bearbeiten', routeLine: 'objective/edit' },
        { displayName: 'Ziel duplizieren', routeLine: 'objective/duplicate' },
        { displayName: 'Ziel löschen', routeLine: 'objective/delete' },
      ] as MenuEntry[],
    ],
    [
      [
        { displayName: 'Resultat bearbeiten', routeLine: 'result/add' },
        { displayName: 'Resultat duplizieren', routeLine: 'objective/edit' },
        { displayName: 'Details einsehen', routeLine: 'result/add' },
        { displayName: 'Resultat löschen', routeLine: 'result/add' },
        { displayName: 'Messung hinzufügen', routeLine: 'result/add' },
      ] as MenuEntry[],
    ],
  ])('should have menu items', async (menuEntries: MenuEntry[]) => {
    fixture = TestBed.createComponent(RowComponent);
    component = fixture.componentInstance;
    component.element = objective;
    component.menuEntries = menuEntries;
    fixture.detectChanges();

    let button = fixture.debugElement.nativeElement.querySelector(
      'button[mat-icon-button]'
    );
    button.click();
    let matMenu: HTMLElement = document.querySelector('.mat-menu-content')!;
    let children = Array.from(matMenu.children)
      .map((e) => e.querySelector('span')!)
      .map((e) => e.textContent);
    let itemTexts = menuEntries.map((e) => e.displayName);
    expect(children).toEqual(itemTexts);
  });
});
