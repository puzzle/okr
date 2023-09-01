import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ObjectiveComponent } from './objective.component';
import { MatMenuModule } from '@angular/material/menu';
import { MatCardModule } from '@angular/material/card';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatMenuHarness } from '@angular/material/menu/testing';
import { By } from '@angular/platform-browser';
import { State } from '../shared/types/enums/State';
import { RouterTestingModule } from '@angular/router/testing';
import { OverviewService } from '../shared/services/overview.service';
import { of } from 'rxjs';
import { Objective } from '../shared/models/Objective';

const overviewServiceMock = {
  getObjectiveWithKeyresults: jest.fn(),
};
describe('ObjectiveColumnComponent', () => {
  let component: ObjectiveComponent;
  let fixture: ComponentFixture<ObjectiveComponent>;
  let loader: HarnessLoader;

  beforeEach(async () => {
    overviewServiceMock.getObjectiveWithKeyresults.mockReset();

    await TestBed.configureTestingModule({
      declarations: [ObjectiveComponent],
      imports: [MatMenuModule, MatCardModule, NoopAnimationsModule, RouterTestingModule],
      providers: [{ provide: OverviewService, useValue: overviewServiceMock }],
    }).compileComponents();
    fixture = TestBed.createComponent(ObjectiveComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  test('Mat-menu should open and close', async () => {
    const menu = await loader.getHarness(MatMenuHarness.with({ selector: '.three-dot-menu' }));
    expect(await menu.isOpen()).toBe(false);
    await menu.open();
    expect(await menu.isOpen()).toBe(true);
    await menu.close();
    expect(await menu.isOpen()).toBe(false);
  });

  test.each([
    [State.ONGOING, 'assets/icons/ongoing-icon.svg'],
    [State.DRAFT, 'assets/icons/draft-icon.svg'],
    [State.SUCCESSFUL, 'assets/icons/successful-icon.svg'],
    [State.NOTSUCCESSFUL, 'assets/icons/not-successful-icon.svg'],
  ])('Status-indicator should change based on the state given by the service', (state: State, path) => {
    overviewServiceMock.getObjectiveWithKeyresults.mockReturnValue(
      of<Objective>({ id: 1, title: 'Increase User Engagement', state: state })
    );
    fixture.detectChanges();
    expect(overviewServiceMock.getObjectiveWithKeyresults).toHaveBeenCalledTimes(1);

    let statusIndicatorSrc = fixture.debugElement.query(By.css('img[class="status-indicator"]')).attributes['src'];
    expect(statusIndicatorSrc).toBe(path);
  });
});
