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
import { objective } from '../shared/testData';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { Component } from '@angular/core';
import { ObjectiveHarness } from '../shared/harness/objective-harness';

@Component({
  selector: 'app-team-wrapper',
  template: "<app-objective-column [objective]='objective'> </app-objective-column>",
})
// @ts-ignore
export class ObjectiveWrapper {
  objective!: ObjectiveMin;
}

const overviewServiceMock = {
  getObjectiveWithKeyresults: jest.fn(),
};
describe('ObjectiveColumnComponent', () => {
  let component: ObjectiveWrapper;
  let fixture: ComponentFixture<ObjectiveWrapper>;
  let loader: HarnessLoader;
  let harness: ObjectiveHarness;
  beforeEach(async () => {
    overviewServiceMock.getObjectiveWithKeyresults.mockReset();

    await TestBed.configureTestingModule({
      declarations: [ObjectiveComponent, ObjectiveWrapper],
      imports: [MatMenuModule, MatCardModule, NoopAnimationsModule, RouterTestingModule],
      providers: [{ provide: OverviewService, useValue: overviewServiceMock }],
    }).compileComponents();

    fixture = TestBed.createComponent(ObjectiveWrapper);
    component = fixture.componentInstance;

    loader = TestbedHarnessEnvironment.loader(fixture);
    component.objective = objective;
    harness = await loader.getHarness(ObjectiveHarness);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
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
  ])('Status-indicator should change based on the state given by the service', (state: State, path) => {
    component.objective = { ...objective, state: state };

    const image = fixture.debugElement.query(By.css('img.status-indicator'));
    let statusIndicatorSrc = image.attributes['src'];
    expect(statusIndicatorSrc).toBe(path);
  });
});
