import { PageObjectMapperBase } from "./pageObjectMapperBase";

export default class FilterHelper extends PageObjectMapperBase {
  validatePage (): void {
    // Does not need to be implemented
  }

  optionShouldBeSelected (text: string, onOverview = true): this {
    if (onOverview) {
      cy.contains("h1:visible", text)
        .should("have.length", 1);
    }
    this.getOption(text)
      .should("have.length", 1)
      .should("have.css", "background-color")
      .and("eq", "rgb(30, 90, 150)");
    return this;
  }

  optionShouldNotBeSelected (text: string): this {
    cy.contains("h1:visible", text)
      .should("not.exist");
    this.getOption(text)
      .should("have.length", 1)
      .should("have.css", "background-color")
      .and("eq", "rgb(255, 255, 255)");
    return this;
  }

  toggleOption (text: string): this {
    this.getOption(text)
      .click();
    return this;
  }

  private getOption (text: string): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.contains("mat-chip:visible", text);
  }
}
