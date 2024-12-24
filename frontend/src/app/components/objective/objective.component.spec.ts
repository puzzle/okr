import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ObjectiveComponent } from './objective.component';
import { MatMenuModule } from '@angular/material/menu';
import { MatCardModule } from '@angular/material/card';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { By } from '@angular/platform-browser';
import { State } from '../../shared/types/enums/state';
import { objectiveMin } from '../../shared/test-data';
import { MatMenuHarness } from '@angular/material/menu/testing';
import { KeyResultComponent } from '../key-result/key-result.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ScoringComponent } from '../../shared/custom/scoring/scoring.component';
import { ConfidenceComponent } from '../confidence/confidence.component';
import { ReactiveFormsModule } from '@angular/forms';
// @ts-ignore
import * as de from '../../../assets/i18n/de.json';
import { TranslateTestingModule } from 'ngx-translate-testing';
import { ObjectiveService } from '../../services/objective.service';
import { CompletedService } from '../../services/completed.servce';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

const objectiveServiceMock = {
  getFullObjective: jest.fn()
};

const completedServiceMock = {
  createCompleted: jest.fn(),
  deleteCompleted: jest.fn()
};

describe('ObjectiveColumnComponent', () => {
  let component: ObjectiveComponent;
  let fixture: ComponentFixture<ObjectiveComponent>;
  let loader: HarnessLoader;
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [
        ObjectiveComponent,
        KeyResultComponent,
        ScoringComponent,
        ConfidenceComponent,
        KeyResultComponent
      ],
      imports: [
        MatMenuModule,
        MatCardModule,
        NoopAnimationsModule,
        MatDialogModule,
        MatIconModule,
        MatTooltipModule,
        ReactiveFormsModule,
        TranslateTestingModule.withTranslations({
          de: de
        })
      ],
      providers: [{ provide: ObjectiveService,
        useValue: objectiveServiceMock },
      { provide: CompletedService,
        useValue: completedServiceMock },
      provideHttpClient(withInterceptorsFromDi())]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ObjectiveComponent);
    component = fixture.componentInstance;

    loader = TestbedHarnessEnvironment.loader(fixture);
    component.objective = objectiveMin;
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  test('mat-menu should open and close', async() => {
    component.isWritable = true;
    fixture.detectChanges();

    const menu = await loader.getHarness(MatMenuHarness.with({ selector: '[data-testId="three-dot-menu"]' }));
    expect(await menu.isOpen())
      .toBeFalsy();
    await menu.open();
    expect(await menu.isOpen())
      .toBeTruthy();
    await menu.close();
    expect(await menu.isOpen())
      .toBeFalsy();
  });

  test.each([
    [State.DRAFT,
      'assets/icons/draft-icon.svg'],
    [State.ONGOING,
      'assets/icons/ongoing-icon.svg'],
    [State.SUCCESSFUL,
      'assets/icons/successful-icon.svg'],
    [State.NOTSUCCESSFUL,
      'assets/icons/not-successful-icon.svg']
  ])('status-indicator should change based on the state given by the service', (state: State, path) => {
    component.objective = { ...objectiveMin,
      state: state };
    fixture.detectChanges();
    const image = fixture.debugElement.query(By.css('[data-testId="objective-state"]'));
    const statusIndicatorSrc = image.attributes['src'];
    expect(statusIndicatorSrc)
      .toBe(path);
  });

  test('mat-menu should not be present if is-writeable is false', async() => {
    component.isWritable = false;
    fixture.detectChanges();
    const menu = fixture.debugElement.query(By.css('[data-testId="objective-menu"]'));
    expect(menu)
      .toBeFalsy();
  });

  test('create key-result button should not be present if is-writeable is false', async() => {
    component.isWritable = false;
    const button = fixture.debugElement.query(By.css('[data-testId="add-key-result"]'));
    expect(button)
      .toBeFalsy();
  });
});
