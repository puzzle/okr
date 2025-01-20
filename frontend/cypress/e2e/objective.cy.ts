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
        .checkForContentOnDialog('Objective veröffentlichen')
        .checkForContentOnDialog('Soll dieses Objective veröffentlicht werden?')
        .submit();

      overviewPage.getObjectiveByNameAndState('A objective in state draft', 'ongoing')
        .should('exist');
    });

    it('should complete objective as successful and write successful closing comment', () => {
      const title = 'This objective should be successful';
      const comment = 'This objective has been successfully completed. Good work';
      overviewPage.completeObjective(title)
        .completeAs(true)
        .writeClosingComment(comment)
        .submit();

      overviewPage.getObjectiveByNameAndState(title, 'successful')
        .click();
      cy.contains(comment);
    });

    it('should complete objective as not-successful and write unsuccessful closing comment', () => {
      const title = 'This objective should NOT be successful';
      const comment = 'This objective has not been completed successfully. We need to work on this';
      overviewPage.completeObjective(title)
        .completeAs(false)
        .writeClosingComment(comment)
        .submit();

      overviewPage.getObjectiveByNameAndState(title, 'not-successful')
        .click();
      cy.contains(comment);
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
        .checkForContentOnDialog('Objective wiedereröffnen')
        .checkForContentOnDialog('Soll dieses Objective wiedereröffnet werden?')
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
        .checkForContentOnDialog('Objective wiedereröffnen')
        .checkForContentOnDialog('Soll dieses Objective wiedereröffnet werden?')
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
        .checkForContentOnDialog('Objective als Draft speichern')
        .checkForContentOnDialog('Soll dieses Objective als Draft gespeichert werden?')
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
        .checkForContentOnDialog('Objective als Draft speichern')
        .checkForContentOnDialog('Soll dieses Objective als Draft gespeichert werden?')
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

    it.only('should have primary button on all objective dialogs', () => {
      overviewPage
        .addObjective()
        .fillObjectiveTitle('A objective for testing purposes')
        .checkForPrimaryButton('save-draft')
        .submit();

      overviewPage
        .openThreeDotMenuOfObjective('A objective for testing purposes')
        .selectFromThreeDotMenu('Objective bearbeiten');
      ObjectiveDialog.do()
        .checkForPrimaryButton('save')
        .cancel();

      overviewPage
        .openThreeDotMenuOfObjective('A objective for testing purposes')
        .selectFromThreeDotMenu('Objective abschliessen');
      cy.getByTestId('successful')
        .click();
      cy.getByTestId('submit')
        .should('have.attr', 'color', 'primary')
        .and('have.attr', 'mat-flat-button');
      cy.getByTestId('cancel')
        .click();

      overviewPage
        .openThreeDotMenuOfObjective('A objective for testing purposes')
        .selectFromThreeDotMenu('Objective als Draft speichern');
      ConfirmDialog.do()
        .checkForPrimaryButton()
        .cancel();

      overviewPage
        .openThreeDotMenuOfObjective('A objective for testing purposes')
        .selectFromThreeDotMenu('Objective löschen');
      ConfirmDialog.do()
        .checkForPrimaryButton()
        .cancel();

      overviewPage
        .duplicateObjective('A objective for testing purposes')
        .checkForPrimaryButton('save')
        .cancel();
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
