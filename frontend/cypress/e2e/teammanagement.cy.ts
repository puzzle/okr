import * as users from '../fixtures/users.json';
import { v4 as uuidv4 } from 'uuid';
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
      cy.getByTestId('confirmYes').click();
      cy.contains('LoremIpsum').should('not.exist');
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
