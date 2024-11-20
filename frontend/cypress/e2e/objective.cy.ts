import * as users from '../fixtures/users.json';
import { onlyOn } from '@cypress/skip-test';
import CyOverviewPage from '../support/helper/pom-helper/pages/overviewPage';
import ObjectiveDialog from '../support/helper/pom-helper/dialogs/objectiveDialog';
import ConfirmDialog from '../support/helper/pom-helper/dialogs/confirmDialog';

describe('OKR Objective e2e tests', () => {
  let op = new CyOverviewPage();
  beforeEach(() => {
    op = new CyOverviewPage();
    cy.loginAsUser(users.gl);
  });

  describe('tests via click', () => {
    it(`Release Objective from Draft to Ongoing`, () => {
      op.addObjective().fillObjectiveTitle('A objective in state draft').submitDraftObjective();

      op.getObjectiveByNameAndState('A objective in state draft', 'draft').findByTestId('three-dot-menu').click();
      op.selectFromThreeDotMenu('Objective veröffentlichen');

      ConfirmDialog.do()
        .checkTitle('Objective veröffentlichen')
        .checkDescription('Soll dieses Objective veröffentlicht werden?')
        .submit();

      op.getObjectiveByNameAndState('A objective in state draft', 'ongoing').should('exist');
    });

    it(`Complete Objective with Successful`, () => {
      op.addObjective().fillObjectiveTitle('We want to complete this successful').submit();

      op.getObjectiveByNameAndState('We want to complete this successful', 'ongoing')
        .findByTestId('three-dot-menu')
        .click();

      op.selectFromThreeDotMenu('Objective abschliessen');

      cy.contains('Bewertung');
      cy.contains('Objective erreicht');
      cy.contains('Objective nicht erreicht');
      cy.contains('Kommentar (optional)');
      cy.contains('Objective abschliessen');
      cy.contains('Abbrechen');

      cy.getByTestId('successful').click();
      cy.getByTestId('submit').click();

      op.getObjectiveByNameAndState('We want to complete this successful', 'successful');
    });

    it(`Complete Objective with Not-Successful`, () => {
      op.addObjective().fillObjectiveTitle('A not successful objective').submit();

      op.getObjectiveByNameAndState('A not successful objective', 'ongoing').findByTestId('three-dot-menu').click();
      op.selectFromThreeDotMenu('Objective abschliessen');

      cy.contains('Bewertung');
      cy.contains('Objective erreicht');
      cy.contains('Objective nicht erreicht');
      cy.contains('Kommentar (optional)');
      cy.contains('Objective abschliessen');
      cy.contains('Abbrechen');

      cy.getByTestId('not-successful').click();
      cy.getByTestId('submit').click();

      op.getObjectiveByNameAndState('A not successful objective', 'not-successful');
    });

    it(`Reopen Successful Objective`, () => {
      op.addObjective().fillObjectiveTitle('This objective will be reopened after').submit();

      op.getObjectiveByNameAndState('This objective will be reopened after', 'ongoing')
        .findByTestId('three-dot-menu')
        .click();

      op.selectFromThreeDotMenu('Objective abschliessen');

      cy.getByTestId('successful').click();
      cy.getByTestId('submit').click();

      cy.wait(500);

      op.getObjectiveByNameAndState('This objective will be reopened after', 'successful')
        .findByTestId('three-dot-menu')
        .click();

      op.selectFromThreeDotMenu('Objective wiedereröffnen');

      ConfirmDialog.do()
        .checkTitle('Objective wiedereröffnen')
        .checkDescription('Soll dieses Objective wiedereröffnet werden?')
        .submit();

      op.getObjectiveByNameAndState('This objective will be reopened after', 'ongoing').should('exist');
    });

    it(`Cancel Reopen Successful Objective`, () => {
      op.addObjective().fillObjectiveTitle('The reopening of this objective will be canceled').submit();

      op.getObjectiveByNameAndState('The reopening of this objective will be canceled', 'ongoing')
        .findByTestId('three-dot-menu')
        .click();

      op.selectFromThreeDotMenu('Objective abschliessen');

      cy.getByTestId('successful').click();
      cy.getByTestId('submit').click();

      cy.wait(500);

      op.getObjectiveByNameAndState('he reopening of this objective will be canceled', 'successful')
        .findByTestId('three-dot-menu')
        .click();

      op.selectFromThreeDotMenu('Objective wiedereröffnen');

      ConfirmDialog.do()
        .checkTitle('Objective wiedereröffnen')
        .checkDescription('Soll dieses Objective wiedereröffnet werden?')
        .cancel();

      op.getObjectiveByNameAndState('The reopening of this objective will be canceled', 'successful').should('exist');
    });

    it('Cancel Ongoing objective back to draft state', () => {
      onlyOn('chrome');
      op.addObjective().fillObjectiveTitle('This objective will be returned to draft state').submit();

      op.getObjectiveByNameAndState('This objective will be returned to draft state', 'ongoing')
        .findByTestId('three-dot-menu')
        .click();
      op.selectFromThreeDotMenu('Objective als Draft speichern');

      ConfirmDialog.do()
        .checkTitle('Objective als Draft speichern')
        .checkDescription('Soll dieses Objective als Draft gespeichert werden?')
        .submit();

      op.getObjectiveByNameAndState('This objective will be returned to draft state', 'draft').should('exist');
    });

    it('Ongoing objective back to draft state', () => {
      onlyOn('chrome');
      op.addObjective().fillObjectiveTitle('Putting this objective back to draft state will be canceled').submit();

      op.getObjectiveByNameAndState('Putting this objective back to draft state will be canceled', 'ongoing')
        .findByTestId('three-dot-menu')
        .click();
      op.selectFromThreeDotMenu('Objective als Draft speichern');

      ConfirmDialog.do()
        .checkTitle('Objective als Draft speichern')
        .checkDescription('Soll dieses Objective als Draft gespeichert werden?')
        .cancel();

      op.getObjectiveByNameAndState('Putting this objective back to draft state will be canceled', 'ongoing').should(
        'exist',
      );
    });

    it(`Search for Objective`, () => {
      op.addObjective().fillObjectiveTitle('Search after this objective').submit();

      op.addObjective().fillObjectiveTitle('We dont want to search for this').submit();

      cy.contains('Search after this objective');
      cy.contains('We dont want to search for this');

      cy.scrollTo(0, 0);
      cy.wait(500);

      cy.getByTestId('objectiveSearch').first().click();
      cy.getByTestId('objectiveSearch').first().type('Search after').wait(350);

      cy.contains('Search after this objective');
      cy.contains('We dont want to search for this').should('not.exist');

      cy.getByTestId('objectiveSearch').first().as('objective-search').clear();
      cy.get('@objective-search').type('this').wait(350);

      cy.contains('Search after this objective');
      cy.contains('We dont want to search for this');

      cy.get('@objective-search').clear();
      cy.get('@objective-search').type('dont want to').wait(350);

      cy.contains('We dont want to search for this');
      cy.contains('Search after this objective').should('not.exist');

      cy.get('@objective-search').clear();
      cy.get('@objective-search').type('there is no objective').wait(350);

      cy.contains('We dont want to search for this').should('not.exist');
      cy.contains('Search after this objective').should('not.exist');
    });

    it(`Create Objective in other quarter`, () => {
      op.addObjective().fillObjectiveTitle('Objective in quarter 3').selectQuarter('3').submit();

      cy.contains('Objective in quarter 3').should('not.exist');

      op.visitNextQuarter();

      cy.contains('Objective in quarter 3');
    });

    it(`Edit Objective and move to other quarter`, () => {
      op.addObjective().fillObjectiveTitle('Move to another quarter on edit').submit();

      op.getObjectiveByNameAndState('Move to another quarter on edit', 'ongoing')
        .findByTestId('three-dot-menu')
        .click();

      op.selectFromThreeDotMenu('Objective bearbeiten');
      ObjectiveDialog.do().selectQuarter('3').submit();

      cy.contains('Move to another quarter on edit').should('not.exist');

      op.visitNextQuarter();
      cy.contains('Move to another quarter on edit');
    });
  });

  describe('tests via keyboard', () => {
    it(`Open objective aside via enter`, () => {
      cy.getByTestId('objective').first().find('[tabindex]').first().focus();
      cy.realPress('Enter');
      cy.url().should('include', 'objective');
    });
  });
});
