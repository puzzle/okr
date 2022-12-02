import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatMenuModule } from '@angular/material/menu';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { RouterTestingModule } from '@angular/router/testing';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { trigger } from '@angular/animations';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import {Objective} from "../../services/objective.service";
import {KeyResultRowComponent} from "./key-result-row.component";
import { MenuEntry } from 'src/app/types/menu-entry';

describe('KeyResultKeyResultRowComponent', () => {
  let component: KeyResultRowComponent;
  let fixture: ComponentFixture<KeyResultRowComponent>;

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
    created: '',
  };

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
      declarations: [KeyResultRowComponent],
    })
        .overrideComponent(KeyResultRowComponent, {
          set: {
            animations: [trigger('entryModeTransition', [])],
          },
        })
        .compileComponents();

    fixture = TestBed.createComponent(KeyResultRowComponent);
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
    [10, 'bad', true],
    [50, 'medium', true],
    [10, 'good', false],
  ])(
      'Progressbar test with value: %i, barname %s, result: %o',
      (progress: number, barIdentifier: string, result: boolean) => {
        fixture = TestBed.createComponent(KeyResultRowComponent);
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
  ])('should have menu items', (menuEntries: MenuEntry[]) => {
    fixture = TestBed.createComponent(KeyResultRowComponent);
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

  // @ts-ignore
  it.each([
    [['Yanick Minder', '01.01.2022']],
    [['Vorname Nachname', '12.12.2012']],
  ])('test information', (informationArray: string[]) => {
    fixture = TestBed.createComponent(KeyResultRowComponent);
    component = fixture.componentInstance;
    component.element = objective;
    component.information = informationArray;
    fixture.detectChanges();

    let informationNodes: HTMLElement[] =
        fixture.debugElement.nativeElement.querySelector('mat-panel-description')!
            .children[0].children;
    let informationStrings: string[] = Array.from(informationNodes).map(
        (e) => e.textContent!
    );

    expect(informationStrings).toEqual(informationArray);
  });
});