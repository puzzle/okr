import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ObjectiveComponent } from './objective.component';
import { MatMenuModule } from '@angular/material/menu';
import { MatCardModule } from '@angular/material/card';
import { AsyncFactoryFn, ComponentHarness, HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatMenuHarness } from '@angular/material/menu/testing';
import { By } from '@angular/platform-browser';
import { State } from '../shared/types/enums/State';
import { RouterTestingModule } from '@angular/router/testing';
import { OverviewService } from '../shared/services/overview.service';
import { keyResultMetric, keyResultOrdinal, objective, quarter } from '../shared/testData';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { KeyresultMin } from '../shared/types/model/KeyresultMin';
import { ChangeDetectionStrategy, Component, ViewChild } from '@angular/core';
import { TeamComponent } from '../team/team.component';
import { MatSelectHarness } from '@angular/material/select/testing';

export class ObjectiveHarness extends ComponentHarness {
  static hostSelector = 'app-objective-column';
  protected getMatMenu: AsyncFactoryFn<MatMenuHarness> = this.locatorFor(MatMenuHarness);

  async isMenuOpen(): Promise<boolean> {
    const menu = await this.getMatMenu();
    return menu.isOpen();
  }

  async setMenuState(state: boolean): Promise<void> {
    const matMenu = await this.getMatMenu();
    if (state) {
      return matMenu.open();
    } else {
      return matMenu.close();
    }
  }
}

@Component({
  selector: 'app-team-wrapper',
  template: "<app-objective-column [objective]='objective'> </app-objective-column>",
})
export class ObjectiveWrapper {
  objective!: ObjectiveMin;
}

const overviewServiceMock = {
  getObjectiveWithKeyresults: jest.fn(),
};
describe('ObjectiveColumnComponent', () => {
  let componentWrapper: ObjectiveWrapper;
  let fixtureWrapper: ComponentFixture<ObjectiveWrapper>;
  // let fixture: ComponentFixture<ObjectiveComponent>;
  // let component: ObjectiveComponent;
  let loader: HarnessLoader;
  let harness: ObjectiveHarness;
  beforeEach(async () => {
    overviewServiceMock.getObjectiveWithKeyresults.mockReset();

    await TestBed.configureTestingModule({
      declarations: [ObjectiveComponent, ObjectiveWrapper],
      imports: [MatMenuModule, MatCardModule, NoopAnimationsModule, RouterTestingModule],
      providers: [{ provide: OverviewService, useValue: overviewServiceMock }],
    }).compileComponents();

    fixtureWrapper = TestBed.createComponent(ObjectiveWrapper);
    componentWrapper = fixtureWrapper.componentInstance;

    // fixture = TestBed.createComponent(ObjectiveComponent);
    // component = fixture.componentInstance;
    const childDebugElement = fixtureWrapper.debugElement.query(By.directive(TeamComponent));
    loader = TestbedHarnessEnvironment.loader(fixtureWrapper);
    componentWrapper.objective = objective;
    harness = await loader.getHarness(ObjectiveHarness);
    fixtureWrapper.detectChanges();
  });

  it('should create', () => {
    expect(componentWrapper).toBeTruthy();
  });

  test('Mat-menu should open and close', async () => {
    expect(await harness.isMenuOpen()).toBe(false);
    await harness.setMenuState(true);
    expect(await harness.isMenuOpen()).toBe(true);
    await harness.setMenuState(false);
    expect(await harness.isMenuOpen()).toBe(false);
  });

  test.each([
    [State.DRAFT, '../../assets/icons/draft-icon.svg'],
    [State.ONGOING, '../../assets/icons/ongoing-icon.svg'],
    [State.SUCCESSFUL, '../../assets/icons/successful-icon.svg'],
    [State.NOTSUCCESSFUL, '../../assets/icons/not-successful-icon.svg'],
  ])('Status-indicator should change based on the state given by the service', async (state: State, path) => {
    const objectiveTemp: ObjectiveMin = {
      id: 101,
      title: 'Increase User Engagement',
      state: state,
      quarter: quarter,
      keyresults: [keyResultMetric, keyResultOrdinal] as KeyresultMin[],
    } as ObjectiveMin;
    // let objectiveOtherReference = JSON.parse(JSON.stringify(objectiveTemp)) as ObjectiveMin;
    componentWrapper.objective = objectiveTemp;
    fixtureWrapper.detectChanges();

    await fixtureWrapper.whenStable();
    const image = fixtureWrapper.debugElement.query(By.css('img.status-indicator'));
    let statusIndicatorSrc = image.attributes['src'];
    expect(statusIndicatorSrc).toBe(path);
  });
});
