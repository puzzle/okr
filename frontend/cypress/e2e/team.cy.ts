import * as users from '../fixtures/users.json';
import FilterHelper from '../support/helper/dom-helper/filterHelper';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import TeamManagementPage from '../support/helper/dom-helper/pages/teamManagementPage';

describe('okr team', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    CyOverviewPage.do().visitCurrentQuarter();
  });

  it('should select teams from team-filter', () => {
    const filterHelper = FilterHelper.do()
      .optionShouldBeSelected('Puzzle ITC')
      .optionShouldBeSelected('LoremIpsum')
      .optionShouldNotBeSelected('Alle')
      .optionShouldNotBeSelected('/BBT')
      .optionShouldNotBeSelected('we are cube');

    filterHelper
      .toggleOption('Alle')
      .optionShouldBeSelected('Alle', false)
      .optionShouldBeSelected('/BBT')
      .optionShouldBeSelected('Puzzle ITC')
      .optionShouldBeSelected('LoremIpsum')
      .optionShouldBeSelected('we are cube');

    filterHelper
      .toggleOption('/BBT')
      .optionShouldBeSelected('/BBT')
      .optionShouldNotBeSelected('Alle')
      .optionShouldNotBeSelected('Puzzle ITC')
      .optionShouldNotBeSelected('LoremIpsum')
      .optionShouldNotBeSelected('we are cube');

    filterHelper
      .toggleOption('Puzzle ITC')
      .optionShouldBeSelected('/BBT')
      .optionShouldNotBeSelected('Alle')
      .optionShouldBeSelected('Puzzle ITC')
      .optionShouldNotBeSelected('LoremIpsum')
      .optionShouldNotBeSelected('we are cube');
  });

  it('should display info text on overview when deselecting all teams from filter', () => {
    const filterHelper = FilterHelper.do()
      .optionShouldBeSelected('Puzzle ITC')
      .optionShouldBeSelected('LoremIpsum')
      .optionShouldNotBeSelected('Alle')
      .optionShouldNotBeSelected('/BBT')
      .optionShouldNotBeSelected('we are cube');

    filterHelper.toggleOption('Puzzle ITC').toggleOption('LoremIpsum');

    cy.contains('Kein Team ausgewählt');
  });

  it('should change query params to reflect selected teams', () => {
    const filterHelper = FilterHelper.do()
      .optionShouldBeSelected('Puzzle ITC')
      .optionShouldBeSelected('LoremIpsum')
      .optionShouldNotBeSelected('Alle')
      .optionShouldNotBeSelected('/BBT')
      .optionShouldNotBeSelected('we are cube');

    filterHelper.validateUrlParameter('teams', ['5', '6']);

    filterHelper.toggleOption('/BBT').validateUrlParameter('teams', ['4', '5', '6']);
    filterHelper.toggleOption('Puzzle ITC').toggleOption('LoremIpsum').toggleOption('/BBT');
    cy.url().should('not.include', 'teams=');
  });

  it('should select teams from query params', () => {
    cy.url().should('not.include', 'teams');

    cy.visit('/?quarter=2&teams=4,5,8');
    FilterHelper.do()
      .optionShouldNotBeSelected('Alle')
      .optionShouldBeSelected('/BBT')
      .optionShouldBeSelected('Puzzle ITC')
      .optionShouldBeSelected('we are cube')
      .optionShouldNotBeSelected('LoremIpsum');
  });

  it('should display less button on mobile header', () => {
    let teamManagementPage = TeamManagementPage.do().visitViaURL();
    cy.intercept('POST', '**/teams').as('addTeam');

    teamManagementPage.addTeam().fillName('X-Team').submit();
    cy.wait('@addTeam');
    cy.contains('X-Team');
    teamManagementPage.addTeam().fillName('Y-Team').submit();
    cy.wait('@addTeam');
    cy.contains('Y-Team');
    teamManagementPage.addTeam().fillName('Z-Team').submit();
    cy.wait('@addTeam');
    cy.contains('Z-Team');

    teamManagementPage.visitOverview();

    // set viewport to < 768 to trigger mobile header
    cy.viewport(767, 1200);

    cy.getByTestId('expansion-panel-header').click();
    cy.contains('Weniger');

    // reset viewport
    cy.viewport(Cypress.config('viewportWidth'), Cypress.config('viewportHeight'));

    cy.visit(`${teamManagementPage.getURL()}`);
    cy.intercept('DELETE', '**/teams/*').as('deleteTeam');

    teamManagementPage.deleteTeam('X-Team').submit();
    cy.wait('@deleteTeam');

    teamManagementPage.deleteTeam('Y-Team').submit();
    cy.wait('@deleteTeam');

    teamManagementPage.deleteTeam('Z-Team').submit();
    cy.wait('@deleteTeam');
  });
});
