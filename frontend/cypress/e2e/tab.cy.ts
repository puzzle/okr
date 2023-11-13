import * as users from '../fixtures/users.json';
import {onlyOn} from '@cypress/skip-test';

describe('Tab workflow tests', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    onlyOn('chrome');
  });

  function openThreeDotMenu() {
    cy.get('.objective').first().focus();
    cy.tabForwardUntil('[data-testid="objective-menu"]')
    cy.realPress("Enter")
  }

  function openCreateKeyResult() {
    cy.get('.objective').first().focus();
    cy.tabForwardUntil('[data-testId="add-keyResult"]');
    cy.focused().contains('Key Result hinzufügen');
    cy.realPress("Enter")
    cy.contains("Key Result erfassen")
  }

  function openCreateObjective() {
    cy.tabForward();
    cy.tabForwardUntil('[data-testId="add-objective"]')
    cy.focused().contains('Objective hinzufügen');
    cy.realPress("Enter");
    cy.contains("Objective für Puzzle ITC erfassen")
    cy.contains("Key Results im Anschluss erfassen");
  }

  function closeDialogWithClose() {
    cy.tabForward();
    cy.tabForwardUntil('[data-testId="cancel"]')
    cy.realPress("Enter");
  }

  function closeDialogWithCross() {
    cy.tabBackward();
    cy.realPress("Enter")
  }

  function fillInNewKeyResult() {
    cy.tabForward();
    cy.realPress("ArrowDown");
    cy.realPress("ArrowDown");
    cy.realPress("ArrowDown"); // -> Entspricht Bob Baumeister (Stand 13.11.2023)
    cy.realPress("Enter")
    cy.tabForward();
    cy.focused().type("Description!")
    cy.tabForward();
    cy.focused().type("Action point one")
    cy.tabForward();
    cy.focused().type("Action point two")
    cy.tabForward();
    cy.focused().type("Action point three")
    cy.tabForward();
    cy.tabForward();
    cy.tabForward();
    cy.focused().contains("Speichern & Neu");
    cy.tabBackward();
    cy.realPress("Enter")
  }

  // Header from here
  it('Tab to help element and user menu', () => {
    cy.tabForward();
    cy.focused().contains('Hilfe');
    cy.tabForward();
    cy.tabForward();
    cy.focused().contains('Jaya Norris');
  });

  it('Tab to quarter-filter, objective-filter and team-filter', () => {
    cy.tabForward();
    cy.tabForward();
    cy.tabForward();
    cy.tabForward();
    cy.focused().contains('GJ');
    cy.tabForward();
    cy.focused().type("Objective") // Decided to try writing since this changes the url. Sadly you can't use contains on placeholders otherwise I would have done that
    cy.url().should('include', 'objectiveQuery=objective');
    cy.tabForward();
    cy.tabForward();
    cy.tabForward();
    cy.focused().contains('Alle');
    cy.tabForward();
    cy.realPress("Enter")
    cy.url().should('include', 'teams');
  });
  //to here


  // Overview from here
  it('Tab to objective, open detail view and close', () => {
    cy.tabForward();
    cy.tabForwardUntil('[data-testId="objective"]');
    cy.realPress("Enter")
    cy.url().should('include', 'objective');
    cy.contains("Beschrieb")
    cy.tabForward();
    cy.tabForward();
    cy.focused().contains("Key Result hinzufügen")
    closeDialogWithCross();
  });

  it('Edit objective in three dot menu', () => {
    openThreeDotMenu();
    cy.focused().contains("Objective bearbeiten");
    cy.realPress("Enter")
    cy.contains("bearbeiten")
    cy.focused().type('{selectall}{backspace}')
    cy.focused().type("Edited by Cypress")
    cy.tabForward();
    cy.focused().type('{selectall}{backspace}')
    cy.focused().type("Edited by Cypress too")
    cy.tabForward();
    cy.tabForward();
    cy.realPress("Enter");
    cy.contains("Edited by Cypress")
  });

  it('Duplicate objective in three dot menu', () => {
    openThreeDotMenu();
    cy.realPress("ArrowDown")
    cy.focused().contains("Objective duplizieren")
    cy.realPress("Enter")
    cy.contains("Objective duplizieren")
  });

  it('Complete objective dialog in three dot menu', () => {
    openThreeDotMenu();
    cy.realPress("ArrowDown")
    cy.realPress("ArrowDown")
    cy.focused().contains("Objective abschliessen")
    cy.realPress("Enter")
    cy.contains("Objective abschliessen")
  });

  it('Create new objective', () => {
    openCreateObjective()
    cy.focused().type("Objective by Cypress")
    cy.tabForward();
    cy.focused().type("Description of Objective...");
    cy.tabForward();
    cy.tabForward();
    cy.tabForward();
    cy.realPress("Enter");
    cy.contains("Objective by Cypress");
  });

  it.only('Delete objective with edit in three dot menu', () => {
    cy.tabForward();
    cy.tabForwardUntil('[data-testid="objective-menu"]')
    cy.realPress("Enter")
    cy.focused().contains("Objective bearbeiten");
    cy.realPress("Enter")
    cy.contains("bearbeiten")
    cy.tabForward();
    cy.tabForwardUntil('[data-testId="delete-objective"]')
    cy.focused().contains("Objective Löschen")
    //cy.realPress("Enter");
  });

  it('Close create objective with cross or close button', () => {
    openCreateObjective();
    closeDialogWithClose();
    openCreateObjective();
    closeDialogWithCross();
  });

  it('Tab to key result, open detail view and close', () => {
    cy.get('.objective').first().focus();
    cy.tabForwardUntil('[data-testId="key-result"]')
    cy.focused().contains("Fail");
    cy.focused().contains("Commit");
    cy.focused().contains("Target");
    cy.focused().contains("Stretch");
    cy.focused().contains("Confidence");
    cy.realPress("Enter")
    cy.url().should('include', 'keyresult');
    cy.contains("Alle Check-ins anzeigen");
    cy.contains("Check-in erfassen");
    cy.contains("Key Result bearbeiten");
    closeDialogWithCross();
  });

  it('Create new key result metric', () => {
    openCreateKeyResult();
    cy.focused().type("KeyResult metric by Cypress")
    cy.contains("Einheit")
    cy.tabForward();
    cy.tabForward();
    cy.tabForward();
    cy.realPress("ArrowDown") // -> Entspricht "CHF"
    cy.tabForward();
    cy.focused().type("0")
    cy.tabForward();
    cy.focused().type("10")
    fillInNewKeyResult();
    cy.contains("KeyResult metric by Cypress");
  });

  it('Create new key result ordinal', () => {
    openCreateKeyResult();
    cy.focused().type("KeyResult ordinal by Cypress")
    cy.tabForward();
    cy.tabForward();
    cy.realPress("Enter")
    cy.contains("Commit Zone")
    cy.tabForward();
    cy.focused().type("Commit Zone")
    cy.tabForward();
    cy.focused().type("Target Zone")
    cy.tabForward();
    cy.focused().type("Stretch Zone")
    fillInNewKeyResult();
    cy.contains("KeyResult ordinal by Cypress");
  });

  it('Close create keyResult with cross or close button', () => {
    openCreateKeyResult();
    closeDialogWithClose();
    openCreateKeyResult();
    closeDialogWithCross();
  });
});
