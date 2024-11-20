import Dialog from './dialog';

export default class TeamDialog extends Dialog {
  override validatePage() {
    super.validatePage();
    this.getPage().contains('Team erfassen');
  }

  fillName(name: string) {
    this.fillInputByTestId('add-team-name', name);
    return this;
  }

  getPage(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('app-add-edit-team-dialog');
  }
}
