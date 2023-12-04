import * as users from '../fixtures/users.json';
import { onlyOn } from '@cypress/skip-test';

describe('Tab workflow tests', () => {
  beforeEach(() => {
    cy.visit('/?quarter=2');
  });
  function openThreeDotMenu() {
    cy.get('.objective').first().focus();
    cy.tabForwardUntil('[data-testId="three-dot-menu"]');
    cy.focused().realPress('Enter');
  }

  function openCreateKeyResult() {
    cy.get('.objective').first().focus();
    cy.tabForwardUntil('[data-testId="add-keyResult"]');
    cy.focused().contains('Key Result hinzufügen');
    cy.realPress('Enter');
    cy.contains('Key Result erfassen');
  }

  function openCreateObjective() {
    cy.get('.objective').first().focus();
    cy.tabForwardUntil('[data-testId="add-objective"]');
    cy.focused().contains('Objective hinzufügen');
    cy.realPress('Enter');
    cy.contains('Key Results im Anschluss erfassen');
  }

  function closeDialogWithCloseButton() {
    cy.tabForward();
    cy.tabForwardUntil('[data-testId="cancel"]');
    cy.realPress('Enter');
  }

  function closeDialogWithCross() {
    cy.tabBackward();
    cy.realPress('Enter');
  }

  function editInputFields(message: string) {
    cy.focused().type('{selectall}{backspace}');
    cy.focused().type(message);
  }

  function fillInNewKeyResult() {
    cy.tabForward();
    cy.realPress('ArrowDown');
    cy.realPress('ArrowDown');
    cy.realPress('ArrowDown'); // -> Entspricht Bob Baumeister (Stand 13.11.2023)
    cy.realPress('Enter');
    cy.tabForward();
    cy.focused().type('Description!');
    cy.tabForward();
    cy.realType('Action point one');
    cy.tabForward();
    cy.realType('Action point two');
    cy.tabForward();
    cy.realType('Action point three');
    cy.tabForward();
    cy.tabForward();
    cy.tabForward();
    cy.focused().contains('Speichern & Neu');
    cy.tabBackward();
    cy.focused().contains('Speichern');
    cy.realPress('Enter');
  }

  function openKeyresultDetail() {
    cy.get('.objective').first().focus();
    cy.tabForwardUntil('[data-testId="key-result"]');
    cy.focused().contains('Fail');
    cy.focused().contains('Commit');
    cy.focused().contains('Target');
    cy.focused().contains('Stretch');
    cy.focused().contains('Confidence');
    cy.realPress('Enter');
    cy.url().should('include', 'keyresult');
    cy.contains('Check-in erfassen');
    cy.contains('Key Result bearbeiten');
  }

  function openCheckInHistory() {
    cy.tabForwardUntil('[data-testId="show-all-checkins"]');
    cy.focused().contains('Alle Check-ins anzeigen');
    cy.realPress('Enter');
  }

  function fillOutOrdinalCheckin(commentary: string) {
    cy.tabForward();
    cy.tabForward();
    cy.tabForward();
    cy.tabForward(); // -> commentary
    editInputFields(commentary);
    cy.tabForward(); // -> zone
    cy.realPress('ArrowDown');
    cy.realPress('ArrowDown');
    cy.tabForward();
    cy.tabForward(); // -> confidence slider
    cy.realPress('ArrowRight');
  }

  function createNewObjectiveWithTab() {
    openCreateObjective();
    cy.wait(500);
    cy.contains('erfassen');
    cy.tabForward();
    cy.focused().type('Objective by Cypress');
    cy.tabForward();
    cy.focused().type('Description of Objective...');
    cy.tabForward();
    cy.tabForward();
    cy.tabForward();
    cy.realPress('Enter');
  }

  describe('Tool functionality without data', () => {
    beforeEach(() => {
      cy.loginAsUser(users.gl);
      onlyOn('chrome');
    });

    // Header from here
    it('Tab to help element and user menu', () => {
      cy.tabForward();
      cy.focused().contains('Teamverwaltung');
      cy.tabForward();
      cy.focused().contains('Hilfe');
      cy.tabForward();
      cy.focused().contains('Jaya Norris');
    });

    it('Tab to user menu and log out', () => {
      cy.tabForward();
      cy.tabForward();
      cy.tabForward();
      cy.focused().contains('Jaya Norris');
      cy.realPress('Enter');
      cy.focused().contains('Logout');
      cy.realPress('Enter');
    });

    it('Tab to quarter-filter, objective-filter and team-filter', () => {
      cy.tabForward();
      cy.tabForward();
      cy.tabForward();
      cy.tabForward();
      cy.focused().contains('GJ');
      cy.tabForward();
      cy.focused().type('Objective'); // Decided to try writing since this changes the url. Sadly you can't use contains on placeholders otherwise I would have done that
      cy.url().should('include', 'objectiveQuery=objective');
      cy.tabForward();
      cy.tabForward();
      cy.tabForward();
      cy.focused().contains('Alle');
      cy.tabForward();
      cy.realPress('Enter');
      cy.url().should('include', 'teams');
    });
  });

  describe('Tabbing with data', () => {
    beforeEach(() => {
      cy.loginAsUser(users.gl);
      onlyOn('chrome');
      cy.visit('/?quarter=2');
    });

    // Overview from here
    it('Tab to objective, open detail view and close', () => {
      cy.get('.objective').first().focus();
      cy.realPress('Enter');
      cy.url().should('include', 'objective');
      cy.contains('Beschrieb');
      cy.tabForward();
      cy.tabForward();
      cy.focused().contains('Key Result hinzufügen');
      closeDialogWithCross();
    });

    it('Tab to keyresult, open detail view and close', () => {
      cy.get('.key-result').first().focus();
      cy.realPress('Enter');
      cy.url().should('include', 'keyresult');
      cy.contains('Beschrieb');
      cy.contains('Fail');
      cy.contains('Commit');
      cy.contains('Target');
      cy.tabForward();
      cy.tabForward();
      cy.tabForward();
      cy.focused().contains('Check-in erfassen');
      cy.tabForward();
      cy.focused().contains('Key Result bearbeiten');
      cy.tabBackward();
      cy.tabBackward();
      closeDialogWithCross();
    });

    it('Edit objective with tab', () => {
      openThreeDotMenu();
      cy.focused().contains('Objective bearbeiten');
      cy.realPress('Enter');
      cy.contains('bearbeiten');
      cy.wait(500);
      cy.tabForward();
      editInputFields('Edited by Cypress');
      cy.tabForward();
      editInputFields('Edited by Cypress too');
      cy.tabForward();
      cy.tabForward();
      cy.realPress('Enter');
      cy.contains('Edited by Cypress');
    });

    it('Duplicate objective with tab', () => {
      openThreeDotMenu();
      cy.realPress('ArrowDown');
      cy.focused().contains('Objective duplizieren');
      cy.realPress('Enter');
      cy.wait(500);
      cy.contains('duplizieren');
      cy.tabForward();
      editInputFields('Duplicated by Cypress');
      cy.tabForward();
      cy.tabForward();
      cy.focused().contains('GJ');
      cy.realPress('ArrowDown');
      cy.tabForward();
      cy.focused().contains('Speichern');
      cy.realPress('Enter');
      cy.wait(500);
      cy.tabForward();
      cy.tabForward();
      cy.tabForward();
      cy.tabForward();
      cy.focused().contains('GJ');
      cy.realPress('ArrowDown');
      cy.contains('Duplicated by Cypress');
    });

    it('Complete objective dialog with tab', () => {
      openThreeDotMenu();
      cy.realPress('ArrowDown');
      cy.realPress('ArrowDown');
      cy.focused().contains('Objective abschliessen');
      cy.realPress('Enter');
      cy.wait(500);
      cy.contains('Objective abschliessen');
      cy.contains('Objective erreicht');
      cy.contains('Objective nicht erreicht');
      cy.tabForward();
      cy.tabForward();
      cy.realPress('Enter');
      cy.tabForward();
      cy.tabForward();
      cy.focused().type('The optional comment');
      cy.tabForward();
      cy.realPress('Enter');

      cy.get('.objective')
        .first()
        .getByTestId('objective-state')
        .should('have.attr', 'src', `assets/icons/successful-icon.svg`);

      openThreeDotMenu();
      cy.focused().contains('Objective wiedereröffnen');
      cy.realPress('Enter');
    });

    it('Create new objective with tab', () => {
      createNewObjectiveWithTab();
    });

    it('Delete objective with tab', () => {
      createNewObjectiveWithTab();
      cy.wait(500);
      cy.get('.objective').last().focus();
      cy.tabForwardUntil('[data-testId="three-dot-menu"]');
      cy.focused().realPress('Enter');
      cy.focused().contains('Objective bearbeiten');
      cy.realPress('Enter');
      cy.contains('bearbeiten');
      cy.tabForwardUntil('[data-testId="delete"]');
      cy.focused().contains('Objective Löschen');
      cy.realPress('Enter');
      cy.wait(500);
      cy.tabForward();
      cy.contains('Objective löschen');
      cy.focused().contains('Ja');
      cy.realPress('Enter');
    });

    it('Close create objective with tab', () => {
      openCreateObjective();
      closeDialogWithCloseButton();
      openCreateObjective();
      closeDialogWithCross();
    });

    it('Tab to key result, open detail view and close', () => {
      openKeyresultDetail();
      closeDialogWithCross();
    });

    it('Edit key result with tab', () => {
      openKeyresultDetail();
      cy.tabForwardUntil('[data-testId="edit-keyResult"]');
      cy.focused().contains('Key Result bearbeiten');
      cy.realPress('Enter');
      cy.tabForward();
      editInputFields('This has been edited by Cypress');
      cy.tabForwardUntil('[data-testId="descriptionInput"]');
      editInputFields('Description of Cypress');
      cy.tabForwardUntil('[data-testId="submit"]');
      cy.focused().contains('Speichern');
      cy.realPress('Enter');
      cy.contains('This has been edited by Cypress');
    });

    it('Delete key result with tab', () => {
      openKeyresultDetail();
      cy.tabForwardUntil('[data-testId="edit-keyResult"]');
      cy.focused().contains('Key Result bearbeiten');
      cy.realPress('Enter');
      cy.wait(500);
      cy.tabForwardUntil('[data-testId="delete-keyResult"]');
      cy.focused().contains('Key Result löschen');
      cy.realPress('Enter');
      cy.wait(500);
      cy.tabForward();
      cy.focused().contains('Ja');
      cy.realPress('Enter');
      cy.contains('This has been edited by Cypress').should('not.exist');
    });

    it('Create new key result metric with checkin and edit checkin with tab', () => {
      // Create keyresult
      openCreateKeyResult();
      cy.wait(500);
      cy.tabForward();
      cy.focused().type('KeyResult metric by Cypress');
      cy.contains('Einheit');
      cy.tabForward();
      cy.tabForward();
      cy.tabForward(); // -> unit
      cy.realPress('ArrowDown'); // -> Entspricht "CHF"
      cy.tabForward(); // -> baseline
      cy.focused().type('0');
      cy.tabForward(); // -> stretchgoal
      cy.focused().type('10');
      fillInNewKeyResult();
      cy.contains('KeyResult metric by Cypress');

      // Create check-in
      cy.getByTestId('keyresult').contains('KeyResult metric by Cypress').click();
      cy.tabForwardUntil('[data-testId="add-check-in"]');
      cy.focused().contains('Check-in erfassen');
      cy.realPress('Enter');
      cy.wait(500);
      cy.tabForward();
      cy.tabForward();
      cy.tabForward();
      cy.tabForward(); // -> commentary
      editInputFields('Check-in by Cypress');
      cy.tabForward(); // -> new value field
      editInputFields('5');
      cy.tabForward();
      cy.tabForward(); // -> confidence slider
      cy.realPress('ArrowRight');
      cy.tabForward();
      cy.focused().contains('Check-in speichern');
      cy.realPress('Enter');

      // Edit checkin
      openCheckInHistory();
      cy.tabForward();
      cy.realPress('Enter');
      cy.wait(500);
      cy.tabForward();
      cy.tabForward();
      cy.tabForward();
      cy.tabForward(); // -> commentary
      editInputFields('Check-in by Cypress (edited)');
      cy.tabForward(); // -> new value
      editInputFields('8');
      cy.tabForward();
      cy.tabForward(); // -> confidence slider
      cy.realPress('ArrowRight');
      cy.tabForward();
      cy.focused().contains('Speichern');
      cy.realPress('Enter');
      cy.contains('Check-in by Cypress (edited)');
    });

    it('Create new key result ordinal with checkin and edit checkin with tab', () => {
      // Create keyresult
      openCreateKeyResult();
      cy.wait(500);
      cy.tabForward(); // -> title
      cy.focused().type('KeyResult ordinal by Cypress');
      cy.tabForward();
      cy.tabForward();
      cy.realPress('Enter'); // -> switch to ordinal type
      cy.contains('Commit Zone');
      cy.tabForward();
      cy.focused().type('Commit Zone');
      cy.tabForward();
      cy.focused().type('Target Zone');
      cy.tabForward();
      cy.focused().type('Stretch Goal');
      fillInNewKeyResult();
      cy.contains('KeyResult ordinal by Cypress');

      // Create checkin
      cy.getByTestId('keyresult').contains('KeyResult ordinal by Cypress').click();
      cy.tabForwardUntil('[data-testId="add-check-in"]');
      cy.focused().contains('Check-in erfassen');
      cy.realPress('Enter');
      cy.wait(500);
      fillOutOrdinalCheckin('Check-in by Cypress');
      cy.tabForward();
      cy.focused().contains('Check-in speichern');
      cy.realPress('Enter');

      // Edit checkin
      openCheckInHistory();
      cy.tabForward();
      cy.realPress('Enter');
      cy.wait(500);
      fillOutOrdinalCheckin('Check-in by Cypress (edited)');
      cy.tabForward();
      cy.focused().contains('Speichern');
      cy.realPress('Enter');
      cy.contains('Check-in by Cypress (edited)');
    });

    it('Close create keyResult with tab', () => {
      openCreateKeyResult();
      closeDialogWithCloseButton();
      openCreateKeyResult();
      closeDialogWithCross();
    });
  });
});
