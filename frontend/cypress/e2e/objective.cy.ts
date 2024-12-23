import * as users from '../fixtures/users.json';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import ObjectiveDialog from '../support/helper/dom-helper/dialogs/objectiveDialog';
import ConfirmDialog from '../support/helper/dom-helper/dialogs/confirmDialog';

describe('okr objective', () => {
  let overviewPage = new CyOverviewPage();
  beforeEach(() => {
    overviewPage = new CyOverviewPage();
    cy.loginAsUser(users.gl);
  });

  describe('tests via click', () => {
    it('should release objective from draft to ongoing', () => {
      overviewPage.addObjective()
        .fillObjectiveTitle('A objective in state draft')
        .submitDraftObjective();

      overviewPage
        .getObjectiveByNameAndState('A objective in state draft', 'draft')
        .findByTestId('three-dot-menu')
        .click();
      overviewPage.selectFromThreeDotMenu('Objective veröffentlichen');

      ConfirmDialog.do()
        .checkTitle('Objective veröffentlichen')
        .checkDescription('Soll dieses Objective veröffentlicht werden?')
        .submit();

      overviewPage.getObjectiveByNameAndState('A objective in state draft', 'ongoing')
        .should('exist');
    });

    it('should complete objective with successful', () => {
      overviewPage.addObjective()
        .fillObjectiveTitle('We want to complete this successful')
        .submit();

      overviewPage
        .getObjectiveByNameAndState('We want to complete this successful', 'ongoing')
        .findByTestId('three-dot-menu')
        .click();

      overviewPage.selectFromThreeDotMenu('Objective abschliessen');

      cy.contains('Bewertung');
      cy.contains('Objective erreicht');
      cy.contains('Objective nicht erreicht');
      cy.contains('Kommentar (optional)');
      cy.contains('Objective abschliessen');
      cy.contains('Abbrechen');

      cy.getByTestId('successful')
        .click();
      cy.getByTestId('submit')
        .click();

      overviewPage.getObjectiveByNameAndState('We want to complete this successful', 'successful');
    });

    it('should complete objective with not-successful', () => {
      overviewPage.addObjective()
        .fillObjectiveTitle('A not successful objective')
        .submit();

      overviewPage
        .getObjectiveByNameAndState('A not successful objective', 'ongoing')
        .findByTestId('three-dot-menu')
        .click();
      overviewPage.selectFromThreeDotMenu('Objective abschliessen');

      cy.contains('Bewertung');
      cy.contains('Objective erreicht');
      cy.contains('Objective nicht erreicht');
      cy.contains('Kommentar (optional)');
      cy.contains('Objective abschliessen');
      cy.contains('Abbrechen');

      cy.getByTestId('not-successful')
        .click();
      cy.getByTestId('submit')
        .click();

      overviewPage.getObjectiveByNameAndState('A not successful objective', 'not-successful');
    });

    it('should reopen successful objective', () => {
      overviewPage.addObjective()
        .fillObjectiveTitle('This objective will be reopened after')
        .submit();

      overviewPage
        .getObjectiveByNameAndState('This objective will be reopened after', 'ongoing')
        .findByTestId('three-dot-menu')
        .click();

      overviewPage.selectFromThreeDotMenu('Objective abschliessen');

      cy.getByTestId('successful')
        .click();
      cy.getByTestId('submit')
        .click();

      cy.wait(500);

      overviewPage
        .getObjectiveByNameAndState('This objective will be reopened after', 'successful')
        .findByTestId('three-dot-menu')
        .click();

      overviewPage.selectFromThreeDotMenu('Objective wiedereröffnen');

      ConfirmDialog.do()
        .checkTitle('Objective wiedereröffnen')
        .checkDescription('Soll dieses Objective wiedereröffnet werden?')
        .submit();

      overviewPage.getObjectiveByNameAndState('This objective will be reopened after', 'ongoing')
        .should('exist');
    });

    it('should cancel reopening successful objective', () => {
      overviewPage.addObjective()
        .fillObjectiveTitle('The reopening of this objective will be canceled')
        .submit();

      overviewPage
        .getObjectiveByNameAndState('The reopening of this objective will be canceled', 'ongoing')
        .findByTestId('three-dot-menu')
        .click();

      overviewPage.selectFromThreeDotMenu('Objective abschliessen');

      cy.getByTestId('successful')
        .click();
      cy.getByTestId('submit')
        .click();

      cy.wait(500);

      overviewPage
        .getObjectiveByNameAndState('he reopening of this objective will be canceled', 'successful')
        .findByTestId('three-dot-menu')
        .click();

      overviewPage.selectFromThreeDotMenu('Objective wiedereröffnen');

      ConfirmDialog.do()
        .checkTitle('Objective wiedereröffnen')
        .checkDescription('Soll dieses Objective wiedereröffnet werden?')
        .cancel();

      overviewPage
        .getObjectiveByNameAndState('The reopening of this objective will be canceled', 'successful')
        .should('exist');
    });

    it('should cancel putting ongoing objective back to draft state', () => {
      overviewPage.addObjective()
        .fillObjectiveTitle('This objective will be returned to draft state')
        .submit();

      overviewPage
        .getObjectiveByNameAndState('This objective will be returned to draft state', 'ongoing')
        .findByTestId('three-dot-menu')
        .click();
      overviewPage.selectFromThreeDotMenu('Objective als Draft speichern');

      ConfirmDialog.do()
        .checkTitle('Objective als Draft speichern')
        .checkDescription('Soll dieses Objective als Draft gespeichert werden?')
        .submit();

      overviewPage
        .getObjectiveByNameAndState('This objective will be returned to draft state', 'draft')
        .should('exist');
    });

    it('should put ongoing objective back to draft state', () => {
      overviewPage
        .addObjective()
        .fillObjectiveTitle('Putting this objective back to draft state will be canceled')
        .submit();

      overviewPage
        .getObjectiveByNameAndState('Putting this objective back to draft state will be canceled', 'ongoing')
        .findByTestId('three-dot-menu')
        .click();
      overviewPage.selectFromThreeDotMenu('Objective als Draft speichern');

      ConfirmDialog.do()
        .checkTitle('Objective als Draft speichern')
        .checkDescription('Soll dieses Objective als Draft gespeichert werden?')
        .cancel();

      overviewPage
        .getObjectiveByNameAndState('Putting this objective back to draft state will be canceled', 'ongoing')
        .should('exist');
    });

    it('should search and find objectives', () => {
      overviewPage.addObjective()
        .fillObjectiveTitle('Search after this objective')
        .submit();

      overviewPage.addObjective()
        .fillObjectiveTitle('We dont want to search for this')
        .submit();

      cy.contains('Search after this objective');
      cy.contains('We dont want to search for this');

      cy.scrollTo(0, 0);
      cy.wait(500);

      cy.getByTestId('objective-search')
        .first()
        .click();
      cy.getByTestId('objective-search')
        .first()
        .type('Search after')
        .wait(350);

      cy.contains('Search after this objective');
      cy.contains('We dont want to search for this')
        .should('not.exist');

      cy.getByTestId('objective-search')
        .first()
        .as('objective-search')
        .clear();
      cy.get('@objective-search')
        .type('this')
        .wait(350);

      cy.contains('Search after this objective');
      cy.contains('We dont want to search for this');

      cy.get('@objective-search')
        .clear();
      cy.get('@objective-search')
        .type('dont want to')
        .wait(350);

      cy.contains('We dont want to search for this');
      cy.contains('Search after this objective')
        .should('not.exist');

      cy.get('@objective-search')
        .clear();
      cy.get('@objective-search')
        .type('there is no objective')
        .wait(350);

      cy.contains('We dont want to search for this')
        .should('not.exist');
      cy.contains('Search after this objective')
        .should('not.exist');
    });

    it('should create objective in other quarter', () => {
      overviewPage.addObjective()
        .fillObjectiveTitle('Objective in quarter 3')
        .selectQuarter('3')
        .submit();

      cy.contains('Objective in quarter 3')
        .should('not.exist');

      overviewPage.visitNextQuarter();

      cy.contains('Objective in quarter 3');
    });

    it('should edit objective and move it to another quarter', () => {
      overviewPage.addObjective()
        .fillObjectiveTitle('Move to another quarter on edit')
        .submit();

      overviewPage
        .getObjectiveByNameAndState('Move to another quarter on edit', 'ongoing')
        .findByTestId('three-dot-menu')
        .click();

      overviewPage.selectFromThreeDotMenu('Objective bearbeiten');
      ObjectiveDialog.do()
        .selectQuarter('3')
        .submit();

      cy.contains('Move to another quarter on edit')
        .should('not.exist');

      overviewPage.visitNextQuarter();
      cy.contains('Move to another quarter on edit');
    });
  });

  describe('tests via keyboard', () => {
    it('should open objective aside via enter', () => {
      cy.getByTestId('objective')
        .first()
        .find('[tabindex]')
        .first()
        .focus();
      cy.realPress('Enter');
      cy.url()
        .should('include', 'objective');
    });
  });
});
