import * as users from '../fixtures/users.json';
import { getPercentageMetric, getPercentageOrdinal } from 'cypress/support/helper/scoringSupport';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import { Unit } from '../../src/app/shared/types/enums/Unit';
import KeyResultDetailPage from '../support/helper/dom-helper/pages/keyResultDetailPage';

describe('Scoring component e2e tests', () => {
  let op = new CyOverviewPage();
  let keyresultDetailPage = new KeyResultDetailPage();

  beforeEach(() => {
    op = new CyOverviewPage();
    keyresultDetailPage = new KeyResultDetailPage();
    cy.loginAsUser(users.gl);
  });

  [
    [0, 100, 10],
    [0, 100, 31],
    [0, 100, 71],
    [0, 100, 100],
  ].forEach(([baseline, stretchgoal, value]) => {
    it('Create metric checkin and validate value of scoring component', () => {
      setupMetricKR(`Metric kr with check-in value ${value}`, baseline, stretchgoal, value);
      const percentage = getPercentageMetric(baseline, stretchgoal, value);
      cy.validateScoring(false, percentage);
      cy.get('.keyResult-detail-attribute-show')
        .contains('Aktuell')
        .parent()
        .not(':contains(!)')
        .should('have.css', 'border-color')
        .and('not.equal', 'rgb(186, 56, 56)');

      keyresultDetailPage.close();
      cy.validateScoring(true, percentage);

      op.getKeyResultByName(`Metric kr with check-in value ${value}`).not(':contains(*[class="scoring-error-badge"])');
    });
  });

  [
    [0, 100, -1],
    [200, 100, 250],
  ].forEach(([baseline, stretchgoal, value]) => {
    it('show indicator that value is negative', () => {
      setupMetricKR(`Check indicator with value ${value}`, baseline, stretchgoal, value);
      cy.validateScoring(false, 0);
      cy.get('.keyResult-detail-attribute-show')
        .contains('Aktuell')
        .parent()
        .contains('!')
        .should('have.css', 'border-color')
        .and('equal', 'rgb(186, 56, 56)');

      keyresultDetailPage.close();
      cy.validateScoring(true, 0);

      op.getKeyResultByName(`Check indicator with value ${value}`).get('.scoring-error-badge');
    });
  });

  [['fail'], ['commit'], ['target'], ['stretch']].forEach(([zoneName]) => {
    it('Create ordinal checkin and validate value of scoring component', () => {
      op.addKeyResult()
        .fillKeyResultTitle('Ordinal scoring keyresult')
        .withOrdinalValues('My commit zone', 'My target zone', 'My stretch goal')
        .checkForDialogTextOrdinal()
        .fillKeyResultDescription('This is my description')
        .submit();

      keyresultDetailPage
        .visit('Ordinal scoring keyresult')
        .createCheckIn()
        .selectOrdinalCheckInZone(zoneName as 'fail' | 'commit' | 'target' | 'stretch')
        .setCheckInConfidence(8)
        .fillCheckInCommentary('Testveränderungen')
        .fillCheckInInitiatives('Testmassnahmen')
        .submit();
      const percentage = getPercentageOrdinal(zoneName);
      cy.validateScoring(false, percentage);
      keyresultDetailPage.close();
      cy.validateScoring(true, percentage);
    });
  });
});

function setupMetricKR(name: string, baseline: number, stretchgoal: number, value: number) {
  CyOverviewPage.do()
    .addKeyResult()
    .fillKeyResultTitle(name)
    .withMetricValues(Unit.PERCENT, baseline.toString(), stretchgoal.toString())
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
