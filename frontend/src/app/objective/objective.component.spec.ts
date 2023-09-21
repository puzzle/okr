import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ObjectiveComponent } from './objective.component';
import { MatMenuModule } from '@angular/material/menu';
import { MatCardModule } from '@angular/material/card';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { By } from '@angular/platform-browser';
import { State } from '../shared/types/enums/State';
import { RouterTestingModule } from '@angular/router/testing';
import { OverviewService } from '../shared/services/overview.service';
import { objectiveMin } from '../shared/testData';
import { MatMenuHarness } from '@angular/material/menu/testing';
import { KeyresultComponent } from '../keyresult/keyresult.component';

const overviewServiceMock = {
  getObjectiveWithKeyresults: jest.fn(),
};
describe('ObjectiveColumnComponent', () => {
  let component: ObjectiveComponent;
  let fixture: ComponentFixture<ObjectiveComponent>;
  let loader: HarnessLoader;
  beforeEach(() => {
    overviewServiceMock.getObjectiveWithKeyresults.mockReset();

    TestBed.configureTestingModule({
      declarations: [ObjectiveComponent, KeyresultComponent],
      imports: [MatMenuModule, MatCardModule, NoopAnimationsModule, RouterTestingModule],
      providers: [{ provide: OverviewService, useValue: overviewServiceMock }],
    }).compileComponents();

    fixture = TestBed.createComponent(ObjectiveComponent);
    component = fixture.componentInstance;

    loader = TestbedHarnessEnvironment.loader(fixture);
    component.objective = objectiveMin;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  test('Mat-menu should open and close', async () => {
    fixture.detectChanges();

    const menu = await loader.getHarness(MatMenuHarness.with({ selector: '[data-testid="objective-menu"]' }));
    expect(await menu.isOpen()).toBeFalsy();
    await menu.open();
    expect(await menu.isOpen()).toBeTruthy();
    await menu.close();
    expect(await menu.isOpen()).toBeFalsy();
  });

  test.each([
    [State.DRAFT, '../../assets/icons/draft-icon.svg'],
    [State.ONGOING, '../../assets/icons/ongoing-icon.svg'],
    [State.SUCCESSFUL, '../../assets/icons/successful-icon.svg'],
    [State.NOTSUCCESSFUL, '../../assets/icons/not-successful-icon.svg'],
  ])('Status-indicator should change based on the state given by the service', (state: State, path) => {
    component.objective = { ...objectiveMin, state: state };
    fixture.detectChanges();
    const image = fixture.debugElement.query(By.css('[data-testid="objective-state"]'));
    let statusIndicatorSrc = image.attributes['src'];
    expect(statusIndicatorSrc).toBe(path);
  });
});
