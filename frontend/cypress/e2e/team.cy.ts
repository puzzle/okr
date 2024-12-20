import * as users from "../fixtures/users.json";
import FilterHelper from "../support/helper/dom-helper/filterHelper";
import CyOverviewPage from "../support/helper/dom-helper/pages/overviewPage";
import TeammanagementPage from "../support/helper/dom-helper/pages/teammanagementPage";

describe("OKR team e2e tests", () => {
  describe("tests via click", () => {
    beforeEach(() => {
      cy.loginAsUser(users.gl);
      CyOverviewPage.do()
        .visitCurrentQuarter();
    });

    it("Should select teams from teamfilter", () => {
      const filterHelper = FilterHelper.do()
        .optionShouldBeSelected("Puzzle ITC")
        .optionShouldBeSelected("LoremIpsum")
        .optionShouldNotBeSelected("Alle")
        .optionShouldNotBeSelected("/BBT")
        .optionShouldNotBeSelected("we are cube");

      filterHelper
        .toggleOption("Alle")
        .optionShouldBeSelected("Alle", false)
        .optionShouldBeSelected("/BBT")
        .optionShouldBeSelected("Puzzle ITC")
        .optionShouldBeSelected("LoremIpsum")
        .optionShouldBeSelected("we are cube");

      filterHelper
        .toggleOption("/BBT")
        .optionShouldBeSelected("/BBT")
        .optionShouldNotBeSelected("Alle")
        .optionShouldNotBeSelected("Puzzle ITC")
        .optionShouldNotBeSelected("LoremIpsum")
        .optionShouldNotBeSelected("we are cube");

      filterHelper
        .toggleOption("Puzzle ITC")
        .optionShouldBeSelected("/BBT")
        .optionShouldNotBeSelected("Alle")
        .optionShouldBeSelected("Puzzle ITC")
        .optionShouldNotBeSelected("LoremIpsum")
        .optionShouldNotBeSelected("we are cube");
    });

    it("Deselect all teams from filter will display text on overview", () => {
      const filterHelper = FilterHelper.do()
        .optionShouldBeSelected("Puzzle ITC")
        .optionShouldBeSelected("LoremIpsum")
        .optionShouldNotBeSelected("Alle")
        .optionShouldNotBeSelected("/BBT")
        .optionShouldNotBeSelected("we are cube");

      filterHelper.toggleOption("Puzzle ITC")
        .toggleOption("LoremIpsum");

      cy.contains("Kein Team ausgewählt");
    });

    it("URL changes to the selected teams", () => {
      const filterHelper = FilterHelper.do()
        .optionShouldBeSelected("Puzzle ITC")
        .optionShouldBeSelected("LoremIpsum")
        .optionShouldNotBeSelected("Alle")
        .optionShouldNotBeSelected("/BBT")
        .optionShouldNotBeSelected("we are cube");

      filterHelper.validateUrlParameter("teams", ["5",
        "6"]);

      filterHelper.toggleOption("/BBT")
        .validateUrlParameter("teams", ["4",
          "5",
          "6"]);
      filterHelper.toggleOption("Puzzle ITC")
        .toggleOption("LoremIpsum")
        .toggleOption("/BBT");
      cy.url()
        .should("not.include", "teams=");
    });

    it("Select teams by url", () => {
      cy.url()
        .should("not.include", "teams");

      cy.visit("/?quarter=2&teams=4,5,8");
      FilterHelper.do()
        .optionShouldNotBeSelected("Alle")
        .optionShouldBeSelected("/BBT")
        .optionShouldBeSelected("Puzzle ITC")
        .optionShouldBeSelected("we are cube")
        .optionShouldNotBeSelected("LoremIpsum");
    });

    it("should display less button on mobile header", () => {
      const teammanagementPage = TeammanagementPage.do()
        .visitViaURL();
      cy.intercept("POST", "**/teams")
        .as("addTeam");

      teammanagementPage.addTeam()
        .fillName("X-Team")
        .submit();
      cy.wait("@addTeam");
      cy.contains("X-Team");
      teammanagementPage.addTeam()
        .fillName("Y-Team")
        .submit();
      cy.wait("@addTeam");
      cy.contains("Y-Team");
      teammanagementPage.addTeam()
        .fillName("Z-Team")
        .submit();
      cy.wait("@addTeam");
      cy.contains("Z-Team");

      teammanagementPage.visitOverview();

      // set viewport to < 768 to trigger mobile header
      cy.viewport(767, 1200);

      cy.getByTestId("expansion-panel-header")
        .click();
      cy.contains("Weniger");

      // reset viewport
      cy.viewport(Cypress.config("viewportWidth"), Cypress.config("viewportHeight"));

      cy.visit(`${teammanagementPage.getURL()}`);
      cy.intercept("DELETE", "**/teams/*")
        .as("deleteTeam");

      teammanagementPage.deleteTeam("X-Team")
        .submit();
      cy.wait("@deleteTeam");

      teammanagementPage.deleteTeam("Y-Team")
        .submit();
      cy.wait("@deleteTeam");

      teammanagementPage.deleteTeam("Z-Team")
        .submit();
      cy.wait("@deleteTeam");
    });
  });
});
