import * as users from '../fixtures/users.json';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import KeyResultDetailPage from '../support/helper/dom-helper/pages/keyResultDetailPage';
import ObjectiveDialog from '../support/helper/dom-helper/dialogs/objectiveDialog';
import { wait } from 'cypress-real-events/utils';

let overviewPage = new CyOverviewPage();

beforeEach(() => {
  overviewPage = new CyOverviewPage();
  cy.loginAsUser(users.gl);
});

describe('functionality of duplicating objectives and their belonging key-results', () => {
  const firstKeyResultName = 'New structure that rewards funny guys and innovation before the end of Q1.';
  const secondKeyResultName = 'Monthly town halls between our people and leadership teams over the next four months.';
  const thirdKeyResultName = 'High employee satisfaction scores (80%+) throughout the year.';
  const keyResultDetailPage = new KeyResultDetailPage();

  it('should be able to duplicate a objective into this quarter, including all keyResults', () => {
    const duplicatedTitle = 'This is a duplicated objective with all keyResults';

    overviewPage
      .duplicateObjective('Build a company culture that kills the competition.')
      .fillObjectiveTitle(duplicatedTitle)
      .submit();

    cy.contains(duplicatedTitle);
    overviewPage.getKeyResultOfObjective(duplicatedTitle, firstKeyResultName)
      .should('exist');
    overviewPage.getKeyResultOfObjective(duplicatedTitle, secondKeyResultName)
      .should('exist');
    overviewPage.getKeyResultOfObjective(duplicatedTitle, thirdKeyResultName)
      .should('exist');
  });

  it('should be able to duplicate a objective into this quarter, only including one key-result', () => {
    const duplicatedTitle = 'This is a duplicated objective with one keyResult';

    overviewPage
      .duplicateObjective('Build a company culture that kills the competition.')
      .fillObjectiveTitle(duplicatedTitle)
      .excludeKeyResults([secondKeyResultName,
        thirdKeyResultName])
      .submit();
    wait(1000);

    overviewPage.getKeyResultOfObjective(duplicatedTitle, firstKeyResultName);

    overviewPage
      .getAllKeyResultsOfObjective(duplicatedTitle)
      .should('not.contain', secondKeyResultName)
      .should('not.contain', thirdKeyResultName);
  });

  it('should not show option to select key-results when objective with no key-results is being duplicated', () => {
    const duplicatedTitle = 'This is a duplicated objective without any key-results';

    overviewPage.duplicateObjective('should not appear on staging, no sea takimata sanctus est Lorem ipsum dolor sit amet.');
    cy.contains('Key Results:')
      .should('not.exist');
    ObjectiveDialog.do()
      .fillObjectiveTitle(duplicatedTitle)
      .submit();

    overviewPage.getObjectiveByName(duplicatedTitle)
      .should('exist');
  });

  it('should be able to duplicate a objective into the next quarter, including all key-results', () => {
    const duplicatedTitle = 'This is a default objective with all key-results in quarter 3!';

    overviewPage
      .duplicateObjective('Build a company culture that kills the competition.')
      .fillObjectiveTitle(duplicatedTitle)
      .selectQuarter('3')
      .submit();

    overviewPage.visitNextQuarter();

    cy.contains(duplicatedTitle);
    overviewPage.getKeyResultOfObjective(duplicatedTitle, firstKeyResultName)
      .should('exist');
    overviewPage.getKeyResultOfObjective(duplicatedTitle, secondKeyResultName)
      .should('exist');
    overviewPage.getKeyResultOfObjective(duplicatedTitle, thirdKeyResultName)
      .should('exist');
  });

  it('should not duplicate objective when cancel button is clicked', () => {
    const duplicatedTitle = 'This is a never existing objective';

    overviewPage
      .duplicateObjective('Build a company culture that kills the competition.')
      .fillObjectiveTitle(duplicatedTitle)
      .fillObjectiveDescription('Wow this is a very nice description!')
      .cancel();

    cy.contains(duplicatedTitle)
      .should('not.exist');
  });
  it('should duplicate all attributes of key-result', () => {
    overviewPage
      .duplicateObjective('Build a company culture that kills the competition.')
      .submit();
    keyResultDetailPage.visit('New structure that rewards funny guys and innovation before the end of Q1.');
    cy.contains('Baseline: 5%');
    cy.contains('Stretch Goal: 1%');
    cy.contains('5/10');
    cy.contains('Metrisch');
    cy.contains('Paco Eggimann');
    cy.contains('GJ 24/25-Q3');
    cy.contains('Action Plan')
      .then(() => {
        cy.contains('Neue Pflanzen');
        cy.contains('Ein Buch');
        cy.contains('Mehr Getränke');
      });
  });
});

describe('verify functionality of scoring adjustment on duplicated objectives', () => {
  const keyResultDetailPage = new KeyResultDetailPage();

  it('should not duplicate check-ins with objective', () => {
    overviewPage
      .addKeyResult('Puzzle ITC', 'Wir wollen die Kundenzufriedenheit steigern')
      .fillKeyResultTitle('stretch keyresult for testing')
      .withOrdinalValues('Ex. val', 'Ex. val', 'Ex. val')
      .submit();

    cy.contains('stretch keyresult for testing');
    keyResultDetailPage
      .visit('stretch keyresult for testing')
      .createCheckIn()
      .selectOrdinalCheckInZone('stretch')
      .setCheckInConfidence(8)
      .fillCheckInCommentary('Testveränderungen')
      .fillCheckInInitiatives('Testmassnahmen')
      .submit();

    cy.intercept('GET', '**/overview?*')
      .as('indexPage');
    keyResultDetailPage.close();
    cy.wait('@indexPage');

    overviewPage
      .duplicateObjective('Wir wollen die Kundenzufriedenheit steigern')
      .fillObjectiveTitle('A duplicated Objective for this tool')
      .selectQuarter('3')
      .submit();

    overviewPage.checkForToaster('Das Objective wurde erfolgreich erstellt.', 'success');

    overviewPage.visitNextQuarter();

    overviewPage
      .getKeyResultByName('stretch keyresult for testing')
      .findByTestId('scoring-component')
      .findByTestId('fail')
      .as('fail-area');

    cy.get('@fail-area')
      .should(($fail) => {
        expect($fail).not.to.have.css('score-red');
        expect($fail).not.to.have.css('score-yellow');
        expect($fail).not.to.have.css('score-green');
        expect($fail).not.to.have.css('score-stretch');
      });
  });
});
