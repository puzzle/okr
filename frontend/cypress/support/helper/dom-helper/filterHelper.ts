import { PageObjectMapperBase } from './pageObjectMapperBase';

export default class FilterHelper extends PageObjectMapperBase {
  validatePage(): void {
    // Does not need to be implemented this comment is for making linter happy
  }

  optionShouldBeSelected(text: string, isOnOverview = true): this {
    if (isOnOverview) {
      cy.contains('h1:visible', text)
        .should('have.length', 1);
    }
    this.getOption(text)
      .should('have.length', 1)
      .should('have.css', 'background-color')
      .and('eq', 'rgb(30, 90, 150)');
    return this;
  }

  optionShouldNotBeSelected(text: string): this {
    cy.contains('h1:visible', text)
      .should('not.exist');
    this.getOption(text)
      .should('have.length', 1)
      .should('have.css', 'background-color')
      .and('eq', 'rgb(255, 255, 255)');
    return this;
  }

  toggleOption(text: string): this {
    this.getOption(text)
      .click();
    return this;
  }

  public getOption(text: string): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.contains('mat-chip:visible', text);
  }

  public optionShouldNotExist(text: string): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('.team-title')
      .then((elements) => {
        const texts: string[] = elements.map((_, el) => Cypress.$(el)
          .text())
          .get();
        expect(texts).to.not.include(text);
      });
  }
}
