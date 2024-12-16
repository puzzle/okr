import { PageObjectMapperBase } from "../pageObjectMapperBase";

export abstract class Page extends PageObjectMapperBase {
  visit (_arg?: any): this {
    this.doVisit();
    return this.afterVisit();
  }

  visitViaURL (): this {
    cy.visit(this.getURL());
    return this.afterVisit();
  }

  protected abstract doVisit (arg?: any): void;

  afterVisit (): this {
    cy.url()
      .should("include",
        this.getURL());
    this.validatePage();
    return this;
  }

  abstract getURL (): string;
}
