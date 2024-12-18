import * as users from '../fixtures/users.json';
import { getPercentageMetric, getPercentageOrdinal } from 'cypress/support/helper/scoringSupport';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import { Unit } from '../../src/app/shared/types/enums/Unit';
import KeyResultDetailPage from '../support/helper/dom-helper/pages/keyResultDetailPage';

describe('okr scoring', () => {
  let overviewPage = new CyOverviewPage();
  let keyResultDetailPage = new KeyResultDetailPage();

  beforeEach(() => {
    overviewPage = new CyOverviewPage();
    keyResultDetailPage = new KeyResultDetailPage();
    cy.loginAsUser(users.gl);
  });

  [
    [0, 100, 10],
    [0, 100, 31],
    [0, 100, 71],
    [0, 100, 100],
  ].forEach(([baseline, stretchGoal, value]) => {
    it('should display correct value on scoring component after creating metric check-in', () => {
      setupMetricKr(`Metric kr with check-in value ${value}`, baseline, stretchGoal, value);
      const percentage = getPercentageMetric(baseline, stretchGoal, value);
      cy.validateScoring(false, percentage);
      cy.get('.key-result-detail-attribute-show')
        .contains('Aktuell')
        .parent()
        .not(':contains(!)')
        .should('have.css', 'border-color')
        .and('not.equal', 'rgb(186, 56, 56)');

      keyResultDetailPage.close();
      cy.validateScoring(true, percentage);

      overviewPage
        .getKeyResultByName(`Metric kr with check-in value ${value}`)
        .not(':contains(*[class="scoring-error-badge"])');
    });
  });

  [
    [0, 100, -1],
    [200, 100, 250],
  ].forEach(([baseline, stretchGoal, value]) => {
    it('show indicator that value is negative', () => {
      setupMetricKr(`Check indicator with value ${value}`, baseline, stretchGoal, value);
      cy.validateScoring(false, 0);
      cy.get('.key-result-detail-attribute-show')
        .contains('Aktuell')
        .parent()
        .contains('!')
        .should('have.css', 'border-color')
        .and('equal', 'rgb(186, 56, 56)');

      keyResultDetailPage.close();
      cy.validateScoring(true, 0);

      overviewPage.getKeyResultByName(`Check indicator with value ${value}`).get('.scoring-error-badge');
    });
  });

  [['fail'], ['commit'], ['target'], ['stretch']].forEach(([zoneName]) => {
    it('should create ordinal checkin and validate value of scoring component', () => {
      overviewPage
        .addKeyResult()
        .fillKeyResultTitle('Ordinal scoring keyresult')
        .withOrdinalValues('My commit zone', 'My target zone', 'My stretch goal')
        .checkForDialogTextOrdinal()
        .fillKeyResultDescription('This is my description')
        .submit();

      keyResultDetailPage
        .visit('Ordinal scoring keyresult')
        .createCheckIn()
        .selectOrdinalCheckInZone(zoneName as 'fail' | 'commit' | 'target' | 'stretch')
        .setCheckInConfidence(8)
        .fillCheckInCommentary('Testveränderungen')
        .fillCheckInInitiatives('Testmassnahmen')
        .submit();
      const percentage = getPercentageOrdinal(zoneName);
      cy.validateScoring(false, percentage);
      keyResultDetailPage.close();
      cy.validateScoring(true, percentage);
    });
  });
});

function setupMetricKr(name: string, baseline: number, stretchGoal: number, value: number) {
  CyOverviewPage.do()
    .addKeyResult()
    .fillKeyResultTitle(name)
    .withMetricValues(Unit.PERCENT, baseline.toString(), stretchGoal.toString())
    .submit();
  KeyResultDetailPage.do()
    .visit(name)
    .createCheckIn()
    .fillMetricCheckInValue(value.toString())
    .fillCheckInCommentary('Testveränderungen')
    .fillCheckInInitiatives('Testmassnahmen')
    .setCheckInConfidence(8)
    .submit();
}
