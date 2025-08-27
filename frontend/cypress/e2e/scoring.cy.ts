import * as users from '../fixtures/users.json';
import { CheckInValue, getPercentageMetric, getPercentageOrdinal } from 'cypress/support/helper/scoringSupport';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import KeyResultDetailPage from '../support/helper/dom-helper/pages/keyResultDetailPage';
import { UNIT_PERCENT } from '../../src/app/shared/test-data';

describe('okr scoring', () => {
  let overviewPage = new CyOverviewPage();
  let keyResultDetailPage = new KeyResultDetailPage();

  beforeEach(() => {
    overviewPage = new CyOverviewPage();
    keyResultDetailPage = new KeyResultDetailPage();
    cy.loginAsUser(users.gl);
  });

  [[
    0,
    30,
    70,
    100,
    10
  ],
  [
    0,
    30,
    70,
    100,
    31
  ],
  [
    0,
    -30,
    -70,
    -100,
    -70
  ]].forEach(([
    baseline,
    commitValue,
    targetValue,
    stretchGoal,
    value
  ]) => {
    it('should display correct value on scoring component after creating metric check-in', () => {
      setupMetricKr(

        `Metric kr with check-in value ${value}`, baseline, stretchGoal, value

      );
      const percentage = getPercentageMetric(baseline, stretchGoal, value);
      const keyResult: CheckInValue = {
        baseline: baseline,
        commitValue: commitValue,
        targetValue: targetValue,
        stretchGoal: stretchGoal,
        lastCheckIn: value
      };

      cy.validateScoringMetric(false, keyResult, percentage);
      cy.get('.key-result-detail-attribute-show')
        .contains('Aktuell')
        .parent()
        .not(':contains(!)')
        .should('have.css', 'border-color')
        .and('not.equal', 'rgb(186, 56, 56)');

      keyResultDetailPage.close();
      cy.validateScoringMetric(true, keyResult, percentage);

      overviewPage
        .getKeyResultByName(`Metric kr with check-in value ${value}`)
        .not(':contains(*[class="scoring-error-badge"])');
      deleteKeyResult(`Metric kr with check-in value ${value}`, keyResultDetailPage);
    });
  });

  [[
    0,
    30,
    70,
    100,
    -1
  ],
  [
    200,
    170,
    130,
    100,
    250
  ]].forEach(([
    baseline,
    commitValue,
    targetValue,
    stretchGoal,
    value
  ]) => {
    it('show indicator that value is negative', () => {
      setupMetricKr(
        `Check indicator with value ${value}`, baseline, stretchGoal, value
      );
      const keyResult: CheckInValue = {
        baseline: baseline,
        commitValue: commitValue,
        targetValue: targetValue,
        stretchGoal: stretchGoal,
        lastCheckIn: value
      };
      cy.validateScoringMetric(false, keyResult, 0);
      cy.get('.key-result-detail-attribute-show')
        .contains('Aktuell')
        .parent()
        .contains('!')
        .should('have.css', 'border-color')
        .and('equal', 'rgb(186, 56, 56)');

      keyResultDetailPage.close();

      overviewPage.getKeyResultByName(`Check indicator with value ${value}`)
        .get('.scoring-error-badge');
      deleteKeyResult(`Check indicator with value ${value}`, keyResultDetailPage);
    });
  });

  [
    ['fail'],
    ['commit'],
    ['target'],
    ['stretch']
  ].forEach(([zoneName]) => {
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
      cy.validateScoringOrdinal(false, percentage);
      keyResultDetailPage.close();
      cy.validateScoringOrdinal(true, percentage);
      deleteKeyResult('Ordinal scoring keyresult', keyResultDetailPage);
    });
  });
});

function deleteKeyResult(keyResultName: string, keyResultDetailPage: KeyResultDetailPage) {
  keyResultDetailPage
    .visit(keyResultName)
    .editKeyResult()
    .deleteKeyResult()
    .checkForContentOnDialog('Key Result löschen')
    .checkForContentOnDialog('Möchtest du dieses Key Result wirklich löschen? Zugehörige Check-ins werden dadurch ebenfalls gelöscht!')
    .submit();
  keyResultDetailPage.close();
}

function setupMetricKr(
  name: string, baseline: number, stretchGoal: number, value: number
) {
  CyOverviewPage.do()
    .addKeyResult()
    .fillKeyResultTitle(name)
    .withMetricValues(
      UNIT_PERCENT.unitName, baseline.toString(), undefined, stretchGoal.toString()
    )
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
