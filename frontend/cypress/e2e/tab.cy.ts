import * as users from '../fixtures/users.json';
import { onlyOn } from '@cypress/skip-test';

describe('Tab workflow tests', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    onlyOn('chrome');
  });

  function openThreeDotMenu() {
    cy.get('.objective').first().focus();
    cy.tabForwardUntil('[data-testid="three-dot-menu"]');
    cy.realPress('Enter');
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
    cy.tabForward();
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

  function openAddCheckIn() {
    openKeyresultDetail();
    cy.tabForwardUntil('[data-testId="add-check-in"]');
    cy.focused().contains("Check-in erfassen");
    cy.realPress("Enter");
  }

  function openCheckInHistory() {
    openKeyresultDetail();
    cy.tabForwardUntil('[data-testId="show-check-ins"]');
    cy.focused().contains("Alle Check-ins anzeigen")
    cy.realPress("Enter");
  }

  // Header from here
  it('Tab to help element and user menu', () => {
    cy.tabForward();
    cy.focused().contains('Hilfe');
    cy.tabForward();
    cy.focused().contains('Jaya Norris');
  });

  it.only('Tab to help element and visit link', () => {
    cy.tabForward();
    cy.focused().contains('Hilfe');
    cy.realPress("Enter"); // Can't check the url since it's keeps localhost as the url for some reason
  });

  it('Tab to user menu and log out', () => {
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
  //to here

  // Overview from here
  it('Tab to objective, open detail view and close', () => {
    cy.tabForward();
    cy.tabForwardUntil('[data-testId="objective"]');
    cy.realPress('Enter');
    cy.url().should('include', 'objective');
    cy.contains('Beschrieb');
    cy.tabForward();
    cy.tabForward();
    cy.focused().contains('Key Result hinzufügen');
    closeDialogWithCross();
  });

  it('Edit objective with tab', () => {
    openThreeDotMenu();
    cy.focused().contains('Objective bearbeiten');
    cy.realPress('Enter');
    cy.contains('bearbeiten');
    editInputFields('Edited by Cypress');
    editInputFields('Edited by Cypress too');
    cy.tabForward();
    cy.realPress('Enter');
    cy.contains('Edited by Cypress');
  });

  it('Duplicate objective with tab', () => {
    openThreeDotMenu();
    cy.realPress('ArrowDown');
    cy.focused().contains('Objective duplizieren');
    cy.realPress('Enter');
    cy.contains('Objective duplizieren');
    editInputFields('Duplicated by Cypress');
    cy.tabForward();
    cy.focused().contains('GJ');
    cy.realPress('ArrowDown');
    cy.tabForward();
    cy.focused().contains('Speichern');
    cy.realPress('Enter');
    cy.tabForward();
    cy.tabForward();
    cy.tabForward();
    cy.tabForward();
    cy.focused().contains('GJ');
    cy.realPress('ArrowDown');
    cy.contains('Duplicated by Cypress');
  });

  it.skip('Complete objective dialog with tab', () => {
    openThreeDotMenu();
    cy.realPress('ArrowDown');
    cy.realPress('ArrowDown');
    cy.focused().contains('Objective abschliessen');
    cy.realPress('Enter');
    cy.contains('Objective abschliessen'); // Does not work yet. It's not possible to select Objective successful or unsuccessful with tab.
  });

  it('Create new objective with tab', () => {
    openCreateObjective();
    cy.focused().type('Objective by Cypress');
    cy.tabForward();
    cy.focused().type('Description of Objective...');
    cy.tabForward();
    cy.tabForward();
    cy.tabForward();
    cy.realPress('Enter');
    cy.contains('Objective by Cypress');
  });

  it('Delete objective with tab', () => {
    openThreeDotMenu();
    cy.focused().contains('Objective bearbeiten');
    cy.realPress('Enter');
    cy.contains('bearbeiten');
    cy.tabForwardUntil('[data-testId="delete"]');
    cy.focused().contains('Objective Löschen');
    cy.realPress('Enter');
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
    cy.focused().contains("Key Result bearbeiten")
    cy.realPress("Enter");
    editInputFields("This has been edited by Cypress");
    cy.tabForwardUntil('[data-testId="descriptionInput"]');
    editInputFields("Description of Cypress");
    cy.tabForwardUntil('[data-testId="submit"]');
    cy.focused().contains("Speichern")
    cy.realPress("Enter");
    cy.contains("This has been edited by Cypress")
  });

  it('Delete key result with tab', () => {
    openKeyresultDetail();
    cy.tabForwardUntil('[data-testId="edit-keyResult"]');
    cy.focused().contains("Key Result bearbeiten")
    cy.realPress("Enter");
    cy.tabForwardUntil('[data-testId="delete-keyResult"]');
    cy.focused().contains("Key Result löschen")
    cy.realPress("Enter")
    cy.focused().contains("Ja")
    cy.realPress("Enter");
    cy.contains("This has been edited by Cypress").should('not.exist');
  });

  it('Create new key result metric with tab', () => {
    openCreateKeyResult();
    cy.focused().type('KeyResult metric by Cypress');
    cy.contains('Einheit');
    cy.tabForward();
    cy.tabForward();
    cy.tabForward();
    cy.realPress('ArrowDown'); // -> Entspricht "CHF"
    cy.tabForward();
    cy.focused().type('0');
    cy.tabForward();
    cy.focused().type('10');
    fillInNewKeyResult();
    cy.contains('KeyResult metric by Cypress');
  });

  it('Create new key result ordinal with tab', () => {
    openCreateKeyResult();
    cy.focused().type('KeyResult ordinal by Cypress');
    cy.tabForward();
    cy.tabForward();
    cy.realPress('Enter');
    cy.contains('Commit Zone');
    cy.tabForward();
    cy.focused().type('Commit Zone');
    cy.tabForward();
    cy.focused().type('Target Zone');
    cy.tabForward();
    cy.focused().type('Stretch Zone');
    fillInNewKeyResult();
    cy.contains('KeyResult ordinal by Cypress');
  });

  it('Create metric check-in with tab', () => {
    // Unterscheidung zwischen metric und ordinal muss noch gemacht werden
    openAddCheckIn();
    cy.tabForward();
    editInputFields("5")
    cy.realPress("ArrowRight");
    cy.tabForward();
    cy.focused().contains("Weiter");
    cy.realPress("Enter");
    cy.tabForward();
    cy.tabForwardUntil('[data-testId="changeInfo"]');
    editInputFields("Check-in by Cypress");
    cy.tabForward();
    cy.focused().contains("Check-in erfassen");
    cy.realPress("Enter")
  });

  it.only('Create ordinal check-in with tab', () => {
    // Unterscheidung zwischen metric und ordinal muss noch gemacht werden
    openAddCheckIn();
    cy.tabForward();
    cy.realPress("ArrowDown")
    cy.realPress("ArrowDown")
    cy.tabForward();
    cy.realPress("ArrowRight");
    cy.tabForward();
    cy.focused().contains("Weiter");
    cy.realPress("Enter");
    cy.tabForward();
    cy.tabForwardUntil('[data-testId="changeInfo"]');
    editInputFields("Check-in by Cypress");
    cy.tabForward();
    cy.tabForwardUntil('[data-testId="save-check-in"]');
    cy.focused().contains("Check-in erfassen");
    cy.realPress("Enter");
  });

  it('Open check-in history with tab', () => {
    openCheckInHistory();
    cy.contains("Check-in by Cypress")
  });

  it('Edit metric check-in with tab', () => {
    // Unterscheidung zwischen metric und ordinal muss noch gemacht werden
    openCheckInHistory();
    cy.tabForward(); cy.tabForward();
    cy.realPress("Enter");
    cy.tabForward(); cy.tabForward(); cy.tabForward();
    editInputFields("8");
    cy.realPress("ArrowRight");
    cy.tabForward();
    cy.focused().contains("Weiter");
    cy.realPress("Enter");
    cy.tabBackward(); cy.tabBackward();
    editInputFields("Check-in by Cypress (edited)");
    cy.tabForward();
    cy.focused().contains("Speichern")
    cy.realPress("Enter");
    cy.contains("Check-in by Cypress (edited)");
  });

  it('Edit ordinal check-in with tab', () => {
    // Unterscheidung zwischen metric und ordinal muss noch gemacht werden
  });

  it('Close create keyResult with tab', () => {
    openCreateKeyResult();
    closeDialogWithCloseButton();
    openCreateKeyResult();
    closeDialogWithCross();
  });
});
