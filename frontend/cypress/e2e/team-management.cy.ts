import * as users from '../fixtures/users.json';
import { uniqueSuffix } from '../support/helper/utils';
import ConfirmDialog from '../support/helper/dom-helper/dialogs/confirmDialog';
import TeamManagementPage from '../support/helper/dom-helper/pages/teamManagementPage';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import InviteMembersDialog from '../support/helper/dom-helper/dialogs/inviteMembersDialog';
import FilterHelper from '../support/helper/dom-helper/filterHelper';

describe('okr team-management', () => {
  const teamName = uniqueSuffix('New Team');
  const nameEsha = users.bl.name;

  describe('routing to overview', () => {
    beforeEach(() => {
      cy.loginAsUser(users.gl);
    });

    it('should preserve team-filter', () => {
      CyOverviewPage.do()
        .visitViaURL();
      FilterHelper.do()
        .toggleOption('/BBT')
        .toggleOption('Puzzle ITC');
      checkTeamsSelected();
      CyOverviewPage.do()
        .visitTeamManagement();
      checkTeamsSelected();
      TeamManagementPage.do()
        .backToOverview();
      checkTeamsSelected();
      CyOverviewPage.do()
        .visitTeamManagement();
      TeamManagementPage.do()
        .visitOverview();
      checkTeamsSelected();
    });

    function checkTeamsSelected() {
      FilterHelper.do()
        .optionShouldBeSelected('LoremIpsum')
        .optionShouldBeSelected('/BBT');
    }
  });

  describe('as "GL"', () => {
    let teamManagementPage: TeamManagementPage;
    before(() => {
      // login as bl to ensure this user exists in database
      cy.loginAsUser(users.bl);
      cy.getByTestId('username')
        .click();
      cy.getByTestId('logout')
        .click();
    });

    beforeEach(() => {
      cy.loginAsUser(users.gl);
      teamManagementPage = TeamManagementPage.do()
        .visitViaURL();
    });

    it('should create team', () => {
      cy.intercept('POST', '**/teams')
        .as('addTeam');

      teamManagementPage.addTeam()
        .fillName(teamName)
        .submit();
      cy.wait('@addTeam');
      cy.contains(teamName);
    });

    it('should not be able to remove last admin from team', () => {
      cy.intercept('PUT', '**/removeuser')
        .as('removeUser');

      cy.get('app-team-list .mat-mdc-list-item')
        .contains(teamName)
        .click();
      cy.getByTestId('member-list-more')
        .click();
      cy.getByTestId('remove-from-member-list')
        .click();

      ConfirmDialog.do()
        .checkForContentOnDialog('Mitglied entfernen')
        .checkForContentOnDialog(`Möchtest du Jaya Norris wirklich aus dem Team '${teamName}' entfernen?`)
        .submit();

      cy.wait('@removeUser');

      cy.contains('Der letzte Administrator eines Teams kann nicht entfernt werden')
        .should('exist');
    });

    it('should not remove user when canceling in confirm dialog', () => {
      cy.intercept('PUT', '**/removeuser')
        .as('removeUser');

      cy.get('app-team-list .mat-mdc-list-item')
        .contains(teamName)
        .click();
      cy.getByTestId('member-list-more')
        .click();
      cy.getByTestId('remove-from-member-list')
        .click();

      // cancel dialog
      ConfirmDialog.do()
        .checkForContentOnDialog('Mitglied entfernen')
        .checkForContentOnDialog(`Möchtest du Jaya Norris wirklich aus dem Team '${teamName}' entfernen?`)
        .cancel();

      cy.get('@removeUser.all')
        .then((interceptions) => {
          expect(interceptions).to.have.length(0);
        });
    });

    it('should edit team', () => {
      cy.intercept('GET', '**/users')
        .as('getUsers');
      cy.intercept('GET', '**/teams')
        .as('getTeams');

      cy.get('app-team-list .mat-mdc-list-item')
        .contains('LoremIpsum')
        .click();
      editTeamNameAndTest('IpsumLorem');
      cy.visit('team-management');

      cy.wait(['@getUsers',
        '@getTeams']);

      cy.contains('LoremIpsum')
        .should('not.exist');
      cy.contains('IpsumLorem')
        .should('exist');
      // set old team name again
      cy.get('app-team-list .mat-mdc-list-item')
        .contains('IpsumLorem')
        .click();
      editTeamNameAndTest('LoremIpsum');
    });

    it('should delete team', () => {
      // Click delete button and cancel
      teamManagementPage.deleteTeam(teamName)
        .cancel();

      // try again and confirm dialog
      teamManagementPage.deleteTeam(teamName)
        .submit();
    });

    describe('search', () => {
      it('should search and find user', () => {
        teamManagementPage.elements
          .teamSearch()
          .fill('pa')
          .shouldHaveOption('Paco Eggimann (peggimann@puzzle.ch)')
          .shouldHaveOption('Paco Egiman (egiman@puzzle.ch)')
          .selectOption('Robin Papierer (papierer@puzzle.ch)');

        cy.contains('app-member-detail h2', 'Robin Papierer');
      });

      it('should search and find team', () => {
        teamManagementPage.elements.teamSearch()
          .fill('we are')
          .selectOption('we are cube.³');

        cy.contains('app-member-list h2', 'we are cube.³');
      });

      it('should find users and teams when search matches both', () => {
        teamManagementPage.elements
          .teamSearch()
          .fill('puz')
          .shouldHaveLabel('Members')
          .shouldHaveLabel('Teams')
          .shouldHaveOption('Paco Eggimann (peggimann@puzzle.ch)')
          .shouldHaveOption('Paco Egiman (egiman@puzzle.ch)')
          .shouldHaveOption('Robin Papierer (papierer@puzzle.ch)')
          .shouldHaveOption('Puzzle ITC');
      });
    });

    describe('invite members', () => {
      it('should invite two members', () => {
        teamManagementPage.elements.registerMember()
          .click();
        const firstNames = InviteMembersDialog.do()
          .enterUser('Claudia', 'Meier', 'claudia.meier@test.ch')
          .addAnotherUser()
          .enterUser('Stefan', 'Schmidt', 'stefan.schmidt@test.ch')
          .addAnotherUser()
          .getFirstnames();

        // test error messages
        fillOutNewUser('Robin', '', 'papierer');
        cy.getByTestId('invite')
          .click();
        cy.contains('Angabe benötigt');
        cy.contains('E-Mail ungültig');
        cy.getByTestId('email-col_2')
          .focus();
        cy.realType('@puzzle.ch');
        cy.contains('E-Mail ungültig')
          .should('not.exist');
        cy.contains('E-Mail existiert bereits');
        cy.tabBackward();
        cy.realType('Papirer');
        cy.contains('Angabe benötigt')
          .should('not.exist');

        // delete last entry
        cy.tabForward();
        cy.tabForward();
        cy.realPress('Enter');
        cy.contains('papiererr@puzzle.ch')
          .should('not.exist');

        // save
        cy.getByTestId('invite')
          .click();
        cy.contains('Die Members wurden erfolgreich registriert');
        firstNames.forEach((email) => cy.contains(email));
      });
    });

    it('should navigate to bobs profile and add him to "BBT" and "LoremIpsum"', () => {
      cy.intercept('PUT', '**/updateaddteammembership/*')
        .as('updateEsha');

      navigateToUser(nameEsha);

      // add to team bbt as admin
      cy.get('app-member-detail')
        .findByTestId('add-user')
        .click();
      cy.get('app-member-detail')
        .findByTestId('select-team-dropdown')
        .click();
      cy.getByTestId('select-team-dropdown-option')
        .contains('/BBT')
        .click();
      cy.get('app-member-detail')
        .findByTestId('select-team-role')
        .click();
      cy.getByTestId('select-team-role-admin')
        .click();
      cy.get('app-member-detail')
        .findByTestId('add-user-to-team-save')
        .click();

      cy.wait('@updateEsha');

      // add to team loremipsum as member
      cy.get('app-member-detail')
        .findByTestId('add-user')
        .click();
      cy.get('app-member-detail')
        .findByTestId('select-team-dropdown')
        .click();

      // team BBT should not be in list anymore
      cy.getByTestId('select-team-dropdown-option')
        .should('not.contain', '/BBT');

      cy.getByTestId('select-team-dropdown-option')
        .contains('LoremIpsum')
        .click();
      cy.get('app-member-detail')
        .findByTestId('select-team-role')
        .click();
      cy.getByTestId('select-team-role-member')
        .click();
      cy.get('app-member-detail')
        .findByTestId('add-user-to-team-save')
        .click();

      cy.wait('@updateEsha');
      // check table
      checkRolesForEsha();

      closeOverlay();

      let foundEsha = false;
      cy.get('app-member-list tbody tr')
        .each(($row) => {
          const usernameCell = $row.find('td:nth-child(2)');
          if (usernameCell.text()
            .trim() === nameEsha) {
            foundEsha = true;
            const roleCell = $row.find('td:nth-child(3)');
            const teamsCell = $row.find('td:nth-child(4)');
            expect(roleCell.text()
              .trim()).to.equal('Team-Admin, Team-Member');
            expect(teamsCell.text()
              .trim()).to.equal('/BBT, LoremIpsum');
            return false;
          }
          return true;
        })
        .then(() => {
          expect(foundEsha).to.equal(true);
        });
    });

    it('should navigate to user "Esha" and set as okr champion', () => {
      navigateToUser(nameEsha);
      cy.getByTestId('edit-okr-champion-readonly')
        .contains('OKR Champion:');
      cy.getByTestId('edit-okr-champion-readonly')
        .contains('Nein');
      cy.getByTestId('edit-okr-champion-edit')
        .click();
      cy.getByTestId('edit-okr-champion-readonly')
        .should('not.exist');
      cy.getByTestId('edit-okr-champion-checkbox')
        .click();
      cy.getByTestId('edit-okr-champion-readonly')
        .contains('OKR Champion:');
      cy.getByTestId('edit-okr-champion-readonly')
        .contains('Ja');
      cy.contains('Der Member wurde erfolgreich aktualisiert.');

      // reset okr champion to false
      cy.getByTestId('edit-okr-champion-edit')
        .click();
      cy.getByTestId('edit-okr-champion-checkbox')
        .click();
      cy.getByTestId('edit-okr-champion-readonly')
        .contains('OKR Champion:');
      cy.getByTestId('edit-okr-champion-readonly')
        .contains('Nein');

      // test click outside of element
      cy.getByTestId('edit-okr-champion-edit')
        .click();
      cy.get('app-member-detail')
        .find('h2')
        .click();
      // checkbox should hide again
      cy.getByTestId('edit-okr-champion-readonly')
        .contains('OKR Champion:');
      cy.getByTestId('edit-okr-champion-readonly')
        .contains('Nein');
    });

    it.only('should have primary button on all team-management dialogs', () => {
      teamManagementPage.elements.registerMember()
        .click();
      InviteMembersDialog.do()
        .run(cy.buttonShouldBePrimary('invite'))
        .close();

      cy.contains('LoremIpsum')
        .click();
      cy.getByTestId('edit-team-button')
        .click();

      cy.buttonShouldBePrimary('save');
      cy.getByTestId('cancel')
        .click();

      cy.getByTestId('add-team-member')
        .click();
      cy.getByTestId('search-member-to-add')
        .click();
      cy.get('span')
        .contains('Paco Eggimann')
        .click();
      cy.buttonShouldBePrimary('save');
    });
  });

  describe('as "BL"', () => {
    beforeEach(() => {
      cy.loginAsUser(users.bl);
      cy.getByTestId('team-management')
        .click();
      cy.intercept('GET', '**/users/*')
        .as('getEsha');
    });

    it('should have correct roles on user "Esha"', () => {
      cy.get('td')
        .contains(nameEsha)
        .click();
      cy.wait('@getEsha');

      checkRolesForEsha();
      closeOverlay();
    });

    it('should not be able to edit team "LoremIpsum"', () => {
      cy.get('app-team-management')
        .contains('LoremIpsum')
        .click();
      cy.getByTestId('teamMoreButton')
        .should('not.exist');
      cy.getByTestId('edit-team-button')
        .should('not.exist');
      cy.getByTestId('member-list-more')
        .should('not.exist');
      cy.getByTestId('edit-role')
        .should('not.exist');
    });

    it('should be able to edit team "/BBT" and edit its name', () => {
      cy.get('app-team-management')
        .contains('/BBT')
        .click();
      cy.getByTestId('teamMoreButton')
        .should('exist');
      editTeamNameAndTest('/BBT_edit');
      // restore old name
      editTeamNameAndTest('/BBT');
    });

    it('should add members to team "/BBT"', () => {
      cy.get('app-team-management')
        .contains('/BBT')
        .click();
      cy.getByTestId('add-team-member')
        .click();
      cy.getByTestId('search-member-to-add')
        .click();

      // esha should not exist (is already member of team)
      const matOption = '.cdk-overlay-container mat-option';
      cy.get(matOption)
        .contains(nameEsha)
        .should('not.exist');

      // add findus peterson
      cy.getByTestId('search-member-to-add')
        .as('member-search')
        .click();
      cy.get('@member-search')
        .type('Find');
      cy.contains(matOption, 'Findus Peterson')
        .click();

      // add robin papierer
      cy.getByTestId('search-member-to-add')
        .click();
      cy.get(matOption)
        .contains('Findus Peterson')
        .should('not.exist');
      cy.get(matOption)
        .contains('Robin Papierer')
        .click();

      // check if Findus and Robin exists in table
      const allMemberTableTr = '#all-member-table tbody tr';
      cy.get(allMemberTableTr)
        .eq(0)
        .should('contain', 'Findus Peterson');
      cy.get(allMemberTableTr)
        .eq(1)
        .should('contain', 'Robin Papierer');

      // remove robin papierer from list
      cy.get(allMemberTableTr + ' button')
        .eq(1)
        .click();
      cy.get(allMemberTableTr)
        .eq(1)
        .should('not.exist');

      cy.getByTestId('save')
        .click();
    });

    it('should change role of "Findus Peterson" to team "Admin"', () => {
      cy.get('app-team-management')
        .contains('/BBT')
        .click();

      cy.get('app-member-list tbody tr')
        .each(($row) => {
          const usernameCell = $row.find('td:nth-child(2)');
          if (usernameCell.text()
            .trim() !== 'Findus Peterson') {
            return;
          }
          $row.find('[data-testId=\'edit-role\']')
            .click();
          cy.wait(500); // wait for dialog to open
        })
        .then(() => {
          cy.getByTestId('select-team-role')
            .click();
          cy.getByTestId('select-team-role-admin')
            .click();
          cy.getByTestId('select-team-role')
            .should('not.exist');
          cy.contains('Das Team wurde erfolgreich aktualisiert.');
        });
    });

    it('should not be able to add "Findus Peterson" to further teams', () => {
      navigateToUser('Findus Peterson');

      /*
       * current user BL (Esha Harris) is only admin in /BBT team.
       * That's why 'add-team-member' should be disabled
       */
      cy.get('app-member-detail')
        .getByTestId('add-user')
        .should('be.disabled');
    });

    it('should remove "BBT" membership from "Findus Peterson"', () => {
      navigateToUser('Findus Peterson');
      cy.getByTestId('delete-team-member')
        .click();

      ConfirmDialog.do()
        .checkForContentOnDialog('Mitglied entfernen')
        .checkForContentOnDialog('Möchtest du Findus Peterson wirklich aus dem Team \'/BBT\' entfernen?')
        .submit();

      cy.get('app-member-detail')
        .contains('/BBT')
        .should('not.exist');
    });

    it('should remove added memberships from "Esha"', () => {
      cy.intercept('PUT', '**/removeuser')
        .as('removeUser');

      navigateToUser(nameEsha);
      cy.getByTestId('delete-team-member')
        .eq(0)
        .click();

      ConfirmDialog.do()
        .checkForContentOnDialog('Mitglied entfernen')
        .checkForContentOnDialog(`Möchtest du ${nameEsha} wirklich aus dem Team '/BBT' entfernen?`)
        .submit();

      cy.wait('@removeUser');

      cy.getByTestId('delete-team-member')
        .eq(0)
        .click();

      ConfirmDialog.do()
        .checkForContentOnDialog('Mitglied entfernen')
        .checkForContentOnDialog(`Möchtest du ${nameEsha} wirklich aus dem Team 'LoremIpsum' entfernen?`)
        .submit();

      cy.get('app-member-detail')
        .should('not.contain', '/BBT')
        .and('not.contain', 'LoremIpsum');
    });

    it('should not be able to edit okr champion state of user "Esha"', () => {
      navigateToUser(nameEsha);
      cy.getByTestId('edit-okr-champion-readonly')
        .should('exist');
      cy.getByTestId('edit-okr-champion-edit')
        .should('not.exist');
    });
  });
});

function closeOverlay() {
  cy.get('.cdk-overlay-backdrop')
    .click(-50, -50, { force: true });
}

function checkRolesForEsha() {
  cy.get('app-member-detail tbody tr')
    .eq(0)
    .should('contain', '/BBT')
    .and('contain', 'Team-Admin');
  cy.get('app-member-detail tbody tr')
    .eq(1)
    .should('contain', 'LoremIpsum')
    .and('contain', 'Team-Member');
}

function editTeamNameAndTest(teamName: string) {
  cy.intercept('PUT', '**/teams/*')
    .as('saveTeam');
  cy.getByTestId('edit-team-button')
    .click();
  cy.getByTestId('add-team-name')
    .as('team-name')
    .click();
  cy.get('@team-name')
    .clear();
  cy.get('@team-name')
    .type(teamName);
  cy.getByTestId('save')
    .click();
  cy.wait('@saveTeam');
  cy.contains(teamName);
}

function navigateToUser(username: string) {
  cy.intercept('GET', '**/users/*')
    .as('getUser');
  cy.get('td')
    .contains(username)
    .click();
  cy.wait('@getUser');
}

function fillOutNewUser(firstName: string, lastName: string, email: string) {
  cy.realType(firstName);
  cy.tabForward();
  cy.realType(lastName);
  cy.tabForward();
  cy.realType(email);
}
