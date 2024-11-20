import * as users from '../fixtures/users.json';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import { Unit } from '../../src/app/shared/types/enums/Unit';
import KeyResultDetailPage from '../support/helper/dom-helper/pages/keyResultDetailPage';

describe('Tab workflow tests', () => {
  let overviewPage: CyOverviewPage;
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    overviewPage = new CyOverviewPage();
    overviewPage.elements.logo().parent().focus();
  });

  function focusedShouldHaveTestId(testId: string) {
    cy.focused().should('have.attr', 'data-testId', testId);
  }

  function tabAndCheck(testId: string, text?: string) {
    cy.tabForwardUntil(`[data-testId="${testId}"]`);
    focusedShouldHaveTestId(testId);
    if (text) {
      cy.focused().contains(text);
    }
  }

  it('should be able to tab to header items', () => {
    tabAndCheck('team-management', 'Teamverwaltung');
    tabAndCheck('help-button', 'Hilfe');
    tabAndCheck('user-options', 'Jaya Norris');
    cy.realPress('Enter');
    cy.pressUntilContains('Logout', 'ArrowDown');
    focusedShouldHaveTestId('logout');
    cy.realPress('Escape');
    tabAndCheck('quarterFilter', 'GJ');
    tabAndCheck('objectiveSearch');
    cy.tabForward();
    cy.focused()
      .children('img')
      .first()
      .should('have.attr', 'src')
      .and('match', /search-icon.svg/);
    cy.tabForward();
    cy.focused().contains('Alle');
  });

  it('should be able to tab to overview items', () => {
    tabAndCheck('team-management', 'Teamverwaltung');
    tabAndCheck('add-objective', 'Objective hinzufügen');
    tabAndCheck('objective');
    tabAndCheck('three-dot-menu');
    tabAndCheck('key-result');
    tabAndCheck('add-keyResult', 'Key Result hinzufügen');
  });

  describe('Objective', () => {
    it('should be able to tab objective dialog', () => {
      tabAndCheck('add-objective', 'Objective hinzufügen');
      cy.realPress('Enter');
      cy.contains('h2', 'Objective für');
      tabAndCheck('title');
      cy.realType('title');
      tabAndCheck('description');
      cy.realType('description');
      tabAndCheck('quarterSelect');
      tabAndCheck('save-draft');
      tabAndCheck('save');
      tabAndCheck('cancel');
    });

    it('should focus three dot menu after edit objective', () => {
      overviewPage.getObjectiveByState('ongoing').focus();
      tabAndCheck('three-dot-menu');
      cy.realPress('Enter');
      tabToThreeDotMenuOption('Objective bearbeiten');
      cy.contains('h2', 'Objective von');
      tabAndCheck('title');
      cy.realType('title');
      tabAndCheck('description');
      cy.realType('description');
      tabAndCheck('quarterSelect');
      tabAndCheck('cancel', 'Abbrechen');
      tabAndCheck('save', 'Speichern');
      cy.realPress('Enter');
      focusedShouldHaveTestId('three-dot-menu');
    });

    it('should be able to complete and reopen objective', () => {
      overviewPage.getObjectiveByState('ongoing').focus();
      tabAndCheck('three-dot-menu');
      cy.realPress('Enter');
      tabToThreeDotMenuOption('Objective abschliessen');
      cy.contains('h2', 'Objective abschliessen ');
      focusedShouldHaveTestId('close-dialog');
      cy.tabForward();
      cy.focused().contains('Objective erreicht');
      cy.realPress('Enter');
      cy.tabForward();
      cy.focused().contains('Objective nicht erreicht');
      tabAndCheck('completeComment');
      tabAndCheck('cancel', 'Abbrechen');
      tabAndCheck('submit', 'Objective abschliessen');
      cy.realPress('Enter');

      focusedShouldHaveTestId('three-dot-menu');
      cy.realPress('Enter');

      tabToThreeDotMenuOption('Objective wiedereröffnen');
      cy.contains('h2', 'Objective wiedereröffnen');
      focusedShouldHaveTestId('close-dialog');
      cy.contains('Soll dieses Objective wiedereröffnet werden?');
      tabAndCheck('confirm-no', 'Nein');
      tabAndCheck('confirm-yes', 'Ja');
      cy.realPress('Enter');
      focusedShouldHaveTestId('three-dot-menu');
    });

    it('should be able to set objective to draft and publish it', () => {
      overviewPage.getObjectiveByState('ongoing').focus();
      tabAndCheck('three-dot-menu');
      cy.realPress('Enter');
      tabToThreeDotMenuOption('Objective als Draft speichern');
      cy.contains('h2', 'Objective als Draft speichern');
      focusedShouldHaveTestId('close-dialog');
      cy.contains('Soll dieses Objective als Draft gespeichert werden?');
      tabAndCheck('confirm-no', 'Nein');
      tabAndCheck('confirm-yes', 'Ja');
      cy.realPress('Enter');

      focusedShouldHaveTestId('three-dot-menu');
      cy.realPress('Enter');

      tabToThreeDotMenuOption('Objective veröffentlichen');
      cy.contains('h2', 'Objective veröffentlichen');
      focusedShouldHaveTestId('close-dialog');
      cy.contains('Soll dieses Objective veröffentlicht werden?');
      tabAndCheck('confirm-no', 'Nein');
      tabAndCheck('confirm-yes', 'Ja');
      cy.realPress('Enter');
      focusedShouldHaveTestId('three-dot-menu');
    });

    it('should be able to open objective detail view', () => {
      overviewPage.getObjectiveByState('ongoing').focus();
      cy.realPress('Enter').tabForward();
      focusedShouldHaveTestId('closeDrawer');
      tabAndCheck('add-keyResult-objective-detail', 'Key Result hinzufügen');
      tabAndCheck('edit-objective', 'Objective bearbeiten');
    });
  });

  describe('Keyresult & Check-In', () => {
    it('Should be able to tab Keyresult dialog', () => {
      tabAndCheck('add-keyResult', 'Key Result hinzufügen');
      cy.realPress('Enter');
      focusedShouldHaveTestId('close-dialog');
      tabAndCheck('titleInput');
      cy.focused().type('Title');
      tabAndCheck('unit');
      tabAndCheck('baseline');
      tabAndCheck('stretchGoal');
      tabAndCheck('ownerInput');
      tabAndCheck('descriptionInput');
      tabAndCheck('actionInput');
      tabAndCheck('add-action-plan-line', 'Weitere Action hinzufügen');
      tabAndCheck('ordinalTab', 'Ordinal');
      cy.realPress('Enter');
      tabAndCheck('commitZone');
      cy.focused().type('Commit');
      tabAndCheck('targetZone');
      cy.focused().type('Target');
      tabAndCheck('stretchZone');
      cy.focused().type('Stretch');
      tabAndCheck('submit', 'Speichern');
      tabAndCheck('saveAndNew', 'Speichern & Neu');
      tabAndCheck('cancel', 'Abbrechen');
    });

    it('Should tab keyresult detail view', () => {
      overviewPage.getObjectiveByState('ongoing').findByTestId('key-result').first().focus();
      cy.realPress('Enter').tabForward();
      focusedShouldHaveTestId('close-drawer');
      tabAndCheck('show-all-checkins', 'Alle Check-ins anzeigen');
      cy.realPress('Enter');
      cy.contains('Check-in History');
      focusedShouldHaveTestId('close-dialog');
      tabAndCheck('edit-check-in');
      tabAndCheck('closeButton', 'Schliessen');
      cy.realPress('Enter');
      tabAndCheck('add-check-in', 'Check-in erfassen');
      tabAndCheck('edit-keyResult', 'Key Result bearbeiten');
    });

    it('Should tab create-check-in metric', () => {
      overviewPage
        .addOngoingKeyResult()
        .fillKeyResultTitle('A metric Keyresult for tabbing tests')
        .withMetricValues(Unit.CHF, '10', '100')
        .submit();
      KeyResultDetailPage.do().visit('A metric Keyresult for tabbing tests');
      tabAndCheck('add-check-in', 'Check-in erfassen');
      cy.realPress('Enter');
      cy.contains('Check-in erfassen');
      focusedShouldHaveTestId('close-dialog');
      tabAndCheck('changeInfo');
      tabAndCheck('check-in-metric-value');
      cy.focused().type('20');
      tabAndCheck('initiatives');
      cy.contains('5/10');
      cy.tabForward();
      cy.realPress('ArrowLeft');
      cy.contains('4/10');
      tabAndCheck('submit-check-in', 'Check-in speichern');
      tabAndCheck('cancel', 'Abbrechen');
    });

    it('Should tab create-check-in ordinal', () => {
      overviewPage
        .addOngoingKeyResult()
        .fillKeyResultTitle('A ordinal Keyresult for tabbing tests')
        .withOrdinalValues('Commit', 'Target', 'Stretch')
        .submit();
      KeyResultDetailPage.do().visit('A ordinal Keyresult for tabbing tests');
      tabAndCheck('add-check-in', 'Check-in erfassen');
      cy.realPress('Enter');
      cy.contains('Check-in erfassen');
      focusedShouldHaveTestId('close-dialog');
      tabAndCheck('changeInfo');
      cy.tabForward();
      cy.focused().closest('mat-radio-button').should('have.attr', 'data-testId', 'fail-radio');
      cy.realPress('ArrowDown');
      cy.focused().closest('mat-radio-button').should('have.attr', 'data-testId', 'commit-radio');
      cy.realPress('ArrowDown');
      cy.focused().closest('mat-radio-button').should('have.attr', 'data-testId', 'target-radio');
      cy.realPress('ArrowDown');
      cy.focused().closest('mat-radio-button').should('have.attr', 'data-testId', 'stretch-radio');
      tabAndCheck('initiatives');
      cy.contains('5/10');
      cy.tabForward();
      cy.realPress('ArrowLeft');
      cy.contains('4/10');
      tabAndCheck('submit-check-in', 'Check-in speichern');
      tabAndCheck('cancel', 'Abbrechen');
    });
  });

  describe('Team management', () => {
    it('Should tab team management', () => {
      tabAndCheck('team-management', 'Teamverwaltung');
      cy.realPress('Enter');
      tabAndCheck('routerLink-to-overview', 'Zurück zur OKR Übersicht');
      tabAndCheck('teamManagementSearch');
      tabAndCheck('add-team', 'Team erfassen');
      tabAndCheck('all-teams-selector', 'Alle Teams (4)');
      tabAndCheck('invite-member', 'Member registrieren');
    });
    it('Should tab create team', () => {
      cy.getByTestId('team-management').click();
      tabAndCheck('add-team');
      cy.realPress('Enter');
      cy.contains('Team erfassen');
      focusedShouldHaveTestId('close-dialog');
      tabAndCheck('add-team-name');
      cy.focused().type('Name of new team');
      tabAndCheck('save', 'Speichern');
      tabAndCheck('cancel', 'Abbrechen');
    });
    it('Should tab register member', () => {
      cy.getByTestId('team-management').click();
      tabAndCheck('invite-member');
      cy.realPress('Enter');
      cy.contains('Member registrieren');
      focusedShouldHaveTestId('close-dialog');
      tabAndCheck('new-member-first-name');
      tabAndCheck('new-member-last-name');
      tabAndCheck('email-col_0');
      cy.tabForward();
      cy.focused().closest('app-puzzle-icon-button').should('have.attr', 'icon', 'delete-icon.svg');
      tabAndCheck('new-member-add-row', 'Weiterer Member hinzufügen');
      tabAndCheck('invite', 'Einladen');
      tabAndCheck('new-member-cancel', 'Abbrechen');
    });
    it('Should tab edit member', () => {
      cy.getByTestId('team-management').click();
      cy.pressUntilContains('Alice Wunderland', 'Tab');
      cy.tabForward();
      cy.realPress('Enter');
      cy.tabForward();
      focusedShouldHaveTestId('close-drawer');

      // Field to toggle if user is OKR-Champion
      cy.tabForward();
      cy.focused().closest('app-puzzle-icon-button').should('have.attr', 'icon', 'edit.svg');
      cy.realPress('Enter');
      cy.tabForward();
      tabAndCheck('close-drawer');
      cy.tabForward();
      cy.focused().closest('mat-checkbox').should('have.attr', 'data-testId', 'edit-okr-champion-checkbox');

      // Field to edit role of assigned team
      cy.tabForward();
      cy.focused().closest('app-puzzle-icon-button').should('have.attr', 'icon', 'edit.svg');
      cy.realPress('Enter');
      cy.tabForward();
      tabAndCheck('select-team-role', 'Team-Member');

      // Button to delete assigned team
      cy.tabForward();
      cy.focused().closest('app-puzzle-icon-button').should('have.attr', 'icon', 'delete-icon.svg');

      // Button to add user to another team
      tabAndCheck('add-user');
      cy.realPress('Enter');
      cy.tabForward();
      tabAndCheck('select-team-dropdown', '/BBT');
      tabAndCheck('select-team-role', 'Team-Member');
      tabAndCheck('add-user-to-team-save', 'Hinzufügen');
      tabAndCheck('add-user-to-team-cancel', 'Abbrechen');
    });
  });
});

function tabToThreeDotMenuOption(name: string) {
  cy.pressUntilContains(name, 'ArrowDown');
  cy.realPress('Enter');
}
