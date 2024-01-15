import * as users from '../fixtures/users.json';
import { uniqueSuffix } from '../support/utils';

describe('Team management tests', () => {
  const teamName = uniqueSuffix('New Team');
  describe('As GL', () => {
    beforeEach(() => {
      cy.loginAsUser(users.gl);
    });

    it('Opens teammanagement dialog', () => {
      cy.getByTestId('team-management').click();

      //Check if overview contains correct titles of teammanagement
      cy.contains('Teamverwaltung');
      cy.contains('Team hinzufügen');
      cy.contains('Alle Teams');
    });

    it('Create team', () => {
      cy.intercept('POST', '**/teams').as('addTeam');
      cy.getByTestId('team-management').click();

      cy.contains('Teamverwaltung');
      cy.getByTestId('add-team').click();
      cy.getByTestId('name').click().type(teamName, { delay: 1 });

      //Save team and check if team appears now on overview
      cy.getByTestId('save').click();
      cy.wait('@addTeam');
      cy.contains(teamName);
    });

    it('Edit team', () => {
      cy.intercept('PUT', '**/teams/*').as('saveTeam');

      cy.getByTestId('team-management').click();
      cy.contains('app-team-list .mat-mdc-list-item', 'LoremIpsum').click();
      cy.getByTestId('editTeamButton').click();
      cy.getByTestId('name').click().clear().type('LoremIpsumEdited', { delay: 1 });

      //Save Team and check if it was edited correctly
      cy.getByTestId('save').click();
      cy.wait('@saveTeam');
      cy.contains('LoremIpsumEdited');
    });

    it('Delete team', () => {
      cy.intercept('DELETE', '**/teams/*').as('saveTeam');

      cy.getByTestId('team-management').click();
      cy.contains('app-team-list .mat-mdc-list-item', 'LoremIpsum').click();

      //Click delete button
      cy.getByTestId('teamMoreButton').click();
      cy.getByTestId('teamDeleteButton').click();

      cy.contains('LoremIpsum').should('not.exist');
    });

    describe.only('Search', () => {
      it('Search user', () => {
        cy.getByTestId('team-management').click();

        cy.getByTestId('teamManagementSearch').click().type('pa', { delay: 1 });

        cy.contains('.mat-mdc-autocomplete-panel mat-option', 'Paco Eggimann (peggimann@puzzle.ch)');
        cy.contains('.mat-mdc-autocomplete-panel mat-option', 'Paco Egiman (egiman@puzzle.ch)');
        cy.contains('.mat-mdc-autocomplete-panel mat-option', 'Robin Papierer (papierer@puzzle.ch)').click();

        cy.contains('app-member-detail h2', 'Robin Papierer');
      });

      it('Search team', () => {
        cy.getByTestId('team-management').click();

        cy.getByTestId('teamManagementSearch').click().type('we are', { delay: 1 });

        cy.contains('.mat-mdc-autocomplete-panel mat-option', 'we are cube.³').click();

        cy.contains('app-member-list h3', 'we are cube.³');
      });

      it('Search mixed', () => {
        cy.getByTestId('team-management').click();

        cy.getByTestId('teamManagementSearch').click().type('puz', { delay: 1 });

        cy.contains('.mat-mdc-autocomplete-panel .mat-mdc-optgroup-label', 'Users');
        cy.contains('.mat-mdc-autocomplete-panel .mat-mdc-optgroup-label', 'Teams');
        cy.contains('.mat-mdc-autocomplete-panel mat-option', 'Paco Eggimann (peggimann@puzzle.ch)');
        cy.contains('.mat-mdc-autocomplete-panel mat-option', 'Paco Egiman (egiman@puzzle.ch)');
        cy.contains('.mat-mdc-autocomplete-panel mat-option', 'Robin Papierer (papierer@puzzle.ch)');
        cy.contains('.mat-mdc-autocomplete-panel mat-option', 'Puzzle ITC');
      });
    });
  });

  describe('As BL', () => {
    beforeEach(() => {
      cy.loginAsUser(users.bl);
    });

    it('Can not see teammanagement icons', () => {
      cy.getByTestId('team-management').should('not.exist');
    });
  });
});
