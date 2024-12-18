import * as users from '../fixtures/users.json';
import CyOverviewPage from '../support/helper/dom-helper/pages/overviewPage';
import TeamManagementPage from '../support/helper/dom-helper/pages/teamManagementPage';

describe('okr routing', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  it('should route to overview', () => {
    // Visit autocalls the validatePage method
    CyOverviewPage.do().visitViaURL();
  });

  it('should route to team-management', () => {
    // Visit autocalls the validatePage method
    TeamManagementPage.do().visitViaURL();
  });

  it('should route from overview to team-management ', () => {
    CyOverviewPage.do().visitViaURL().visitTeamManagement();
  });

  it('should route from team-management to overview via back button', () => {
    TeamManagementPage.do().visitViaURL().backToOverview();
  });

  it('should route from team-management to overview via logo', () => {
    TeamManagementPage.do().visitViaURL().visitOverview();
  });
});
