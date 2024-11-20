import * as users from '../fixtures/users.json';
import CyOverviewPage from '../support/helper/pom-helper/pages/overviewPage';
import KeyResultDetailPage from '../support/helper/pom-helper/pages/keyResultDetailPage';

describe('e2e test for scoring adjustment on objective duplicate', () => {
  let op = new CyOverviewPage();
  let keyresultDetailPage = new KeyResultDetailPage();

  beforeEach(() => {
    op = new CyOverviewPage();
    keyresultDetailPage = new KeyResultDetailPage();
    cy.loginAsUser(users.gl);
  });

  it('Duplicate ordinal checkin and validate value of scoring component', () => {
    op.addKeyResult('Puzzle ITC', 'Wir wollen die Kundenzufriedenheit steigern')
      .fillKeyResultTitle('stretch keyresult for testing')
      .withOrdinalValues('Ex. val', 'Ex. val', 'Ex. val')
      .submit();

    cy.contains('stretch keyresult for testing');
    keyresultDetailPage
      .visit('stretch keyresult for testing')
      .createCheckIn()
      .selectOrdinalCheckInZone('stretch')
      .setCheckInConfidence(8)
      .fillCheckInCommentary('Testveränderungen')
      .fillCheckInInitiatives('Testmassnahmen')
      .submit();
    keyresultDetailPage.close();

    op.duplicateObjective('Wir wollen die Kundenzufriedenheit steigern')
      .fillObjectiveTitle('A duplicated Objective for this tool')
      .selectQuarter('3')
      .submit();

    op.checkForToaster('Das Objective wurde erfolgreich erstellt.', 'success');

    cy.visit('/?quarter=3');

    op.getKeyResultByName('stretch keyresult for testing')
      .findByTestId('scoring-component')
      .findByTestId('fail')
      .as('fail-area');

    cy.get('@fail-area').should(($fail) => {
      expect($fail).not.to.have.css('score-red');
      expect($fail).not.to.have.css('score-yellow');
      expect($fail).not.to.have.css('score-green');
      expect($fail).not.to.have.css('score-stretch');
    });
  });
});
