import Dialog from './dialog';

export default class ArchiveDialog extends Dialog {
  checkForContentOnDialog(text: string) {
    this.getPage()
      .contains(text)
      .should('exist');
    return this;
  }

  changeLastActiveQuarter(quarter: string) {
    cy.get('select#archive-quarter')
      .select(quarter);
    return this;
  }

  override getPage(): Cypress.Chainable {
    return cy.get('app-archive-team-dialog');
  }
}
