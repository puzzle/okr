import * as users from '../fixtures/users.json';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import KeyResultDetailPage from '../support/helper/dom-helper/pages/keyResultDetailPage';
import ObjectiveDialog from '../support/helper/dom-helper/dialogs/objectiveDialog';

let overviewPage = new CyOverviewPage();

beforeEach(() => {
  overviewPage = new CyOverviewPage();
  cy.loginAsUser(users.gl);
});

describe('Functionality of duplicating objectives and their belonging keyresults', () => {
  it('Should be able to duplicate a objective into this quarter, including all keyresults', () => {
    const duplicatedTitle = 'This is a duplicated objective with all keyResults';
    overviewPage
      .getObjectiveByName('Build a company culture that kills the competition.')
      .findByTestId('three-dot-menu')
      .click();
    overviewPage.selectFromThreeDotMenu('Objective duplizieren');
    ObjectiveDialog.do().fillObjectiveTitle(duplicatedTitle).submit();

    cy.contains('This is a default duplicated objective');
    overviewPage
      .getKeyresultOfObjective(
        'This is a duplicated objective with all keyResults',
        'New structure that rewards funny guys and innovation before the end of Q1.',
      )
      .should('exist');
    overviewPage
      .getKeyresultOfObjective(
        'This is a duplicated objective with all keyResults',
        'Monthly town halls between our people and leadership teams over the next four months.',
      )
      .should('exist');
    overviewPage
      .getKeyresultOfObjective(
        'This is a duplicated objective with all keyResults',
        'High employee satisfaction scores (80%+) throughout the year.',
      )
      .should('exist');
  });

  it('Should be able to duplicate a objective into this quarter, only including one keyresult', () => {
    const duplicatedTitle = 'This is a duplicated objective with one keyResult';
    overviewPage
      .getObjectiveByName('Build a company culture that kills the competition.')
      .findByTestId('three-dot-menu')
      .click();
    overviewPage.selectFromThreeDotMenu('Objective duplizieren');
    ObjectiveDialog.do().fillObjectiveTitle(duplicatedTitle).submit();

    cy.contains('This is a duplicated objective with one keyResult');
    overviewPage
      .getKeyresultOfObjective(
        'This is a duplicated objective with one keyResult',
        'New structure that rewards funny guys and innovation before the end of Q1.',
      )
      .should('exist');
    overviewPage
      .getKeyresultOfObjective(
        'This is a duplicated objective with one keyResult',
        'Monthly town halls between our people and leadership teams over the next four months.',
      )
      .should('not.exist');
    overviewPage
      .getKeyresultOfObjective(
        'This is a duplicated objective with one keyResult',
        'High employee satisfaction scores (80%+) throughout the year.',
      )
      .should('not.exist');
  });
});

// describe('e2e test for scoring adjustment on duplicate objective', () => {
//   it('Duplicate ordinal checkin and validate value of scoring component', () => {
//     overviewPage
//       .addKeyResult('Puzzle ITC', 'Wir wollen die Kundenzufriedenheit steigern')
//       .fillKeyResultTitle('stretch keyresult for testing')
//       .withOrdinalValues('Ex. val', 'Ex. val', 'Ex. val')
//       .submit();
//
//     cy.contains('stretch keyresult for testing');
//     keyresultDetailPage
//       .visit('stretch keyresult for testing')
//       .createCheckIn()
//       .selectOrdinalCheckInZone('stretch')
//       .setCheckInConfidence(8)
//       .fillCheckInCommentary('Testveränderungen')
//       .fillCheckInInitiatives('Testmassnahmen')
//       .submit();
//
//     cy.intercept('GET', '**/overview?*').as('indexPage');
//     keyresultDetailPage.close();
//     cy.wait('@indexPage');
//
//     overviewPage
//       .duplicateObjective('Wir wollen die Kundenzufriedenheit steigern')
//       .fillObjectiveTitle('A duplicated Objective for this tool')
//       .selectQuarter('3')
//       .submit();
//
//     overviewPage.checkForToaster('Das Objective wurde erfolgreich erstellt.', 'success');
//
//     overviewPage.visitNextQuarter();
//
//     overviewPage
//       .getKeyResultByName('stretch keyresult for testing')
//       .findByTestId('scoring-component')
//       .findByTestId('fail')
//       .as('fail-area');
//
//     cy.get('@fail-area').should(($fail) => {
//       expect($fail).not.to.have.css('score-red');
//       expect($fail).not.to.have.css('score-yellow');
//       expect($fail).not.to.have.css('score-green');
//       expect($fail).not.to.have.css('score-stretch');
//     });
//   });
// });
