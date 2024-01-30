import * as users from '../fixtures/users.json';
import { uniqueSuffix } from '../support/utils';

describe('Team management tests', () => {
  const teamName = uniqueSuffix('New Team');
  const nameEsha = users.bl.name;

  describe('As GL', () => {
    before(() => {
      // login as bl to ensure this user exists in database
      cy.loginAsUser(users.bl);
      cy.getByTestId('user-name').click();
      cy.getByTestId('logout').click();
    });

    beforeEach(() => {
      cy.loginAsUser(users.gl);
      cy.getByTestId('team-management').click();
    });

    it('Opens teammanagement dialog', () => {
      //Check if overview contains correct titles of teammanagement
      cy.contains('Teamverwaltung');
      cy.contains('Team hinzufügen');
      cy.contains('Alle Teams');
    });

    it('Create team', () => {
      cy.intercept('POST', '**/teams').as('addTeam');

      cy.contains('Teamverwaltung');
      cy.getByTestId('add-team').click();
      cy.getByTestId('add-team-name').click().clear().type(teamName, { delay: 1 });
      cy.getByTestId('add-team-save').click();
      cy.wait('@addTeam');
      cy.contains(teamName);
    });

    it('Try to remove last admin of team should not work', () => {
      cy.intercept('PUT', '**/removeuser').as('removeUser');

      cy.get('app-team-list .mat-mdc-list-item').contains(teamName).click();
      cy.getByTestId('member-list-more').click();
      cy.getByTestId('remove-from-member-list').click();

      // dialog
      cy.contains(`Jaya Norris wirklich aus Team ${teamName} entfernen?`);
      cy.getByTestId('cancelDialog-confirm').click();

      cy.wait('@removeUser');

      cy.contains('Der letzte Administrator eines Teams kann nicht entfernt werden').should('exist');
    });

    it('clicking cancel in dialog when removing user should not remove user', () => {
      cy.intercept('PUT', '**/removeuser').as('removeUser');

      cy.get('app-team-list .mat-mdc-list-item').contains(teamName).click();
      cy.getByTestId('member-list-more').click();
      cy.getByTestId('remove-from-member-list').click();

      // cancel dialog
      cy.contains(`Jaya Norris wirklich aus Team ${teamName} entfernen?`);
      cy.getByTestId('cancelDialog-cancel').click();

      cy.get('@removeUser.all').then((interceptions) => {
        expect(interceptions).to.have.length(0);
      });
    });

    it('Edit team', () => {
      cy.intercept('GET', '**/users').as('getUsers');
      cy.intercept('GET', '**/teams').as('getTeams');

      cy.get('app-team-list .mat-mdc-list-item').contains('LoremIpsum').click();
      editTeamNameAndTest('IpsumLorem');
      cy.visit('team-management');

      cy.wait(['@getUsers', '@getTeams']);

      cy.contains('LoremIpsum').should('not.exist');
      cy.contains('IpsumLorem').should('exist');
      // set old team name again
      cy.get('app-team-list .mat-mdc-list-item').contains('IpsumLorem').click();
      editTeamNameAndTest('LoremIpsum');
    });

    it('Delete team', () => {
      cy.intercept('DELETE', '**/teams/*').as('saveTeam');
      cy.intercept('GET', '**/users').as('getUsers');

      cy.contains('app-team-list .mat-mdc-list-item', teamName).click();

      //Click delete button
      cy.getByTestId('teamMoreButton').click();
      cy.getByTestId('teamDeleteButton').click();

      // cancel dialog => cancel
      cy.contains(`${teamName} wirklich löschen?`);
      cy.getByTestId('cancelDialog-cancel').click();

      // try again and confirm dialog
      cy.getByTestId('teamMoreButton').click();
      cy.getByTestId('teamDeleteButton').click();
      cy.contains(`${teamName} wirklich löschen?`);
      cy.getByTestId('cancelDialog-confirm').click();

      cy.wait(['@saveTeam', '@getUsers']);

      cy.contains(teamName).should('not.exist');
    });

    describe('Search', () => {
      it('Search user', () => {
        cy.get('app-team-management-banner').getByTestId('teamManagementSearch').click().type('pa', { delay: 1 });

        cy.contains('.mat-mdc-autocomplete-panel mat-option', 'Paco Eggimann (peggimann@puzzle.ch)');
        cy.contains('.mat-mdc-autocomplete-panel mat-option', 'Paco Egiman (egiman@puzzle.ch)');
        cy.contains('.mat-mdc-autocomplete-panel mat-option', 'Robin Papierer (papierer@puzzle.ch)').click();

        cy.contains('app-member-detail h2', 'Robin Papierer');
      });

      it('Search team', () => {
        cy.get('app-team-management-banner').getByTestId('teamManagementSearch').click().type('we are', { delay: 1 });

        cy.contains('.mat-mdc-autocomplete-panel mat-option', 'we are cube.³').click();

        cy.contains('app-member-list h2', 'we are cube.³');
      });

      it('Search mixed', () => {
        cy.get('app-team-management-banner').getByTestId('teamManagementSearch').click().type('puz', { delay: 1 });

        cy.contains('.mat-mdc-autocomplete-panel .mat-mdc-optgroup-label', 'Users');
        cy.contains('.mat-mdc-autocomplete-panel .mat-mdc-optgroup-label', 'Teams');
        cy.contains('.mat-mdc-autocomplete-panel mat-option', 'Paco Eggimann (peggimann@puzzle.ch)');
        cy.contains('.mat-mdc-autocomplete-panel mat-option', 'Paco Egiman (egiman@puzzle.ch)');
        cy.contains('.mat-mdc-autocomplete-panel mat-option', 'Robin Papierer (papierer@puzzle.ch)');
        cy.contains('.mat-mdc-autocomplete-panel mat-option', 'Puzzle ITC');
      });
    });

    it('Navigate to Bobs profile and add him to BBT and LoremIpsum', () => {
      cy.intercept('PUT', '**/updateaddteammembership/*').as('updateEsha');

      navigateToUser(nameEsha);

      // add to team bbt as admin
      cy.get('app-member-detail').getByTestId('add-user').click();
      cy.get('app-member-detail').getByTestId('select-team-dropdown').click();
      cy.getByTestId('select-team-dropdown-option').contains('/BBT').click();
      cy.get('app-member-detail').getByTestId('select-team-role').click();
      cy.getByTestId('select-team-role-admin').click();
      cy.get('app-member-detail').getByTestId('add-user-to-team-save').click();

      cy.wait('@updateEsha');

      // add to team loremipsum as member
      cy.get('app-member-detail').getByTestId('add-user').click();
      cy.get('app-member-detail').getByTestId('select-team-dropdown').click();

      // team BBT should not be in list anymore
      cy.getByTestId('select-team-dropdown-option').should('not.contain', '/BBT');

      cy.getByTestId('select-team-dropdown-option').contains('LoremIpsum').click();
      cy.get('app-member-detail').getByTestId('select-team-role').click();
      cy.getByTestId('select-team-role-member').click();
      cy.get('app-member-detail').getByTestId('add-user-to-team-save').click();

      cy.wait('@updateEsha');
      // check table
      checkRolesForEsha();

      closeOverlay();

      let foundEsha = false;
      cy.get('app-member-list tbody tr')
        .each(($row) => {
          let usernameCell = $row.find('td:nth-child(2)');
          if (usernameCell.text().trim() === nameEsha) {
            foundEsha = true;
            let roleCell = $row.find('td:nth-child(3)');
            let teamsCell = $row.find('td:nth-child(4)');
            expect(roleCell.text().trim()).to.equal('Team-Admin, Team-Member');
            expect(teamsCell.text().trim()).to.equal('/BBT, LoremIpsum');
            return false;
          }
          return true;
        })
        .then(() => {
          expect(foundEsha).to.be.true;
        });
    });

    it('Navigate to user Esha and set as okr champion', () => {
      navigateToUser(nameEsha);
      cy.getByTestId('edit-okr-champion-readonly').contains('OKR Champion:');
      cy.getByTestId('edit-okr-champion-readonly').contains('Nein');
      cy.getByTestId('edit-okr-champion-edit').click();
      cy.getByTestId('edit-okr-champion-readonly').should('not.exist');
      cy.getByTestId('edit-okr-champion-checkbox').click();
      cy.getByTestId('edit-okr-champion-readonly').contains('OKR Champion:');
      cy.getByTestId('edit-okr-champion-readonly').contains('Ja');
      cy.contains('Der User wurde erfolgreich aktualisiert.');

      // reset okr champion to false
      cy.getByTestId('edit-okr-champion-edit').click();
      cy.getByTestId('edit-okr-champion-checkbox').click();
      cy.getByTestId('edit-okr-champion-readonly').contains('OKR Champion:');
      cy.getByTestId('edit-okr-champion-readonly').contains('Nein');

      // test click outside of element
      cy.getByTestId('edit-okr-champion-edit').click();
      cy.get('app-member-detail').find('h2').click();
      // checkbox should hide again
      cy.getByTestId('edit-okr-champion-readonly').contains('OKR Champion:');
      cy.getByTestId('edit-okr-champion-readonly').contains('Nein');
    });
  });

  describe('As BL', () => {
    beforeEach(() => {
      cy.loginAsUser(users.bl);
      cy.getByTestId('team-management').click();
    });

    it('should check if correct roles for BL are set', () => {
      cy.intercept('GET', '**/users/*').as('getEsha');

      cy.get('td').contains(nameEsha).click();
      cy.wait('@getEsha');

      checkRolesForEsha();
      closeOverlay();
    });

    it('should check if team loremIpsum cannot be edited', () => {
      cy.get('app-team-management').contains('LoremIpsum').click();
      cy.getByTestId('teamMoreButton').should('not.exist');
      cy.getByTestId('editTeamButton').should('not.exist');
      cy.getByTestId('member-list-more').should('not.exist');
      cy.getByTestId('edit-role').should('not.exist');
    });

    it('should check if team /BBT can be edited and edit name', () => {
      cy.get('app-team-management').contains('/BBT').click();
      cy.getByTestId('teamMoreButton').should('exist');
      editTeamNameAndTest('/BBT_edit');
      // restore old name
      editTeamNameAndTest('/BBT');
    });

    it('should add members to team /BBT', () => {
      cy.get('app-team-management').contains('/BBT').click();
      cy.getByTestId('add-team-member').click();
      cy.getByTestId('search-member-to-add').click();

      // esha should not exist (is already member of team)
      const matOption = '.cdk-overlay-container mat-option';
      cy.get(matOption).contains(nameEsha).should('not.exist');

      // add findus peterson
      cy.getByTestId('search-member-to-add').click().type('Find', { delay: 1 });
      cy.get(matOption).contains('Findus Peterson').click();

      // add robin papierer
      cy.getByTestId('search-member-to-add').click();
      cy.get(matOption).contains('Findus Peterson').should('not.exist');
      cy.get(matOption).contains('Robin Papierer').click();

      // check if Findus and Robin exists in table
      const allMemberTableTr = '#all-member-table tbody tr';
      cy.get(allMemberTableTr).eq(0).should('contain', 'Findus Peterson');
      cy.get(allMemberTableTr).eq(1).should('contain', 'Robin Papierer');

      // remove robin papierer from list
      cy.get(allMemberTableTr + ' button')
        .eq(1)
        .click();
      cy.get(allMemberTableTr).eq(1).should('not.exist');

      cy.getByTestId('save').click();
    });

    it('should change role of Findus Peterson to Team Admin', () => {
      cy.get('app-team-management').contains('/BBT').click();

      cy.get('app-member-list tbody tr')
        .each(($row) => {
          let usernameCell = $row.find('td:nth-child(2)');
          if (usernameCell.text().trim() !== 'Findus Peterson') {
            return;
          }
          $row.find(`[data-testId='edit-role']`).click();
        })
        .then(() => {
          cy.getByTestId('select-team-role').click();
          cy.getByTestId('select-team-role-admin').click();
          cy.getByTestId('select-team-role').should('not.exist');
          cy.contains('Das Team wurde erfolgreich aktualisiert.');
        });
    });

    it('should test that Findus Peterson cannot be added to further teams', () => {
      navigateToUser('Findus Peterson');

      // current user BL (Esha Harris) is only admin in /BBT team.
      // That's why 'add-team-member' should be disabled
      cy.get('app-member-detail').getByTestId('add-user').should('be.disabled');
    });

    it('should remove BBT membership of findus', () => {
      navigateToUser('Findus Peterson');
      cy.getByTestId('delete-team-member').click();
      cy.getByTestId('cancelDialog-confirm').click();
      cy.get('app-member-detail').contains('/BBT').should('not.exist');
    });

    it('should remove added memberships from esha', () => {
      navigateToUser(nameEsha);
      cy.getByTestId('delete-team-member').eq(0).click();
      cy.getByTestId('cancelDialog-confirm').click();
      cy.getByTestId('delete-team-member').eq(0).click();
      cy.getByTestId('cancelDialog-confirm').click();
      cy.get('app-member-detail').should('not.contain', '/BBT').and('not.contain', 'LoremIpsum');
    });

    it('Navigate to user Esha and check if okr champion is not editable', () => {
      navigateToUser(nameEsha);
      cy.getByTestId('edit-okr-champion-readonly').should('exist');
      cy.getByTestId('edit-okr-champion-edit').should('not.exist');
    });
  });
});

function closeOverlay() {
  cy.get('.cdk-overlay-backdrop').click(-50, -50, { force: true });
}

function checkRolesForEsha() {
  cy.get('app-member-detail tbody tr').eq(0).should('contain', '/BBT').and('contain', 'Team-Admin');
  cy.get('app-member-detail tbody tr').eq(1).should('contain', 'LoremIpsum').and('contain', 'Team-Member');
}

function editTeamNameAndTest(teamName: string) {
  cy.intercept('PUT', '**/teams/*').as('saveTeam');
  cy.getByTestId('editTeamButton').click();
  cy.getByTestId('add-team-name').click().clear().type(teamName, { delay: 1 });
  cy.getByTestId('add-team-save').click();
  cy.wait('@saveTeam');
  cy.contains(teamName);
}

function navigateToUser(userName: string) {
  cy.intercept('GET', '**/users/*').as('getUser');
  cy.get('td').contains(userName).click();
  cy.wait('@getUser');
}
