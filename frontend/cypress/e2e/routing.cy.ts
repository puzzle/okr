import * as users from "../fixtures/users.json";
import CyOverviewPage from "../support/helper/dom-helper/pages/overviewPage";
import TeammanagementPage from "../support/helper/dom-helper/pages/teammanagementPage";

describe("Routing", () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  describe("Route via url", () => {
    it("should route to overview", () => {
      // Visit autocalls the validatePage method
      CyOverviewPage.do()
        .visitViaURL();
    });

    it("should route to teammanagement", () => {
      // Visit autocalls the validatePage method
      TeammanagementPage.do()
        .visitViaURL();
    });

    it("should route from overview to team management ", () => {
      CyOverviewPage.do()
        .visitViaURL()
        .visitTeammanagement();
    });

    it("should route from team management to Overview via back button", () => {
      TeammanagementPage.do()
        .visitViaURL()
        .backToOverview();
    });

    it("should route from team management to Overview via logo", () => {
      TeammanagementPage.do()
        .visitViaURL()
        .visitOverview();
    });
  });
});
