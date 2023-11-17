import * as users from '../fixtures/users.json';

describe('Team management tests', () => {
  describe('As GL', () => {
    beforeEach(() => {
      cy.loginAsUser(users.gl);
    });

    it('Opens teammanagement dialog', () => {
      cy.getByTestId('team-management').click();

      //Check if overview contains correct titles of teammanagement
      cy.contains('Teamverwaltung');
      cy.contains('Teamname');
      cy.contains('Organisationen');
    });

    it('Create team', () => {
      cy.getByTestId('team-management').click();
      cy.getByTestId('name').click().type('New Team');

      //Select gl as organisation
      cy.getByTestId('organisation-select').click().get('mat-option').contains('org_gl').click();
      cy.realPress('Escape');

      //Save team and check if team appears now on overview
      cy.getByTestId('save').click();
      cy.contains('New Team');
    });

    it('Edit team', () => {
      cy.getByTestId('edit-team-LoremIpsum').click();
      cy.getByTestId('name').type('Edited');
      cy.getByTestId('organisation-select').click();

      //Remove organisations from team
      cy.get('mat-option').contains('org_bl').click();
      cy.get('mat-option').contains('org_mobility').click();

      //Add organisation to team
      cy.get('mat-option').contains('org_azubi').click();
      cy.realPress('Escape');

      //Save Team and check if it was edited correctly
      cy.getByTestId('save').click();
      cy.contains('LoremIpsumEdited');
    });

    it('Delete team', () => {
      cy.getByTestId('edit-team-LoremIpsumEdited').click();
      //Click delete button
      cy.getByTestId('delete').click();
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
