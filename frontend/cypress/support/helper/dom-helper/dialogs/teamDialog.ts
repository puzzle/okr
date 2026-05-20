import Dialog from './dialog';
import Chainable = Cypress.Chainable;

export default class TeamDialog extends Dialog {
  override validatePage() {
    super.validatePage();
    this.getPage()
      .contains('Team erfassen');
  }

  fillName(name: string) {
    this.fillInputByTestId('add-team-name', name);
    return this;
  }

  fillDescription(description: string) {
    this.fillInputByTestId('add-team-description', description);
    return this;
  }

  override submit() {
    cy.getByTestId('save')
      .click();
  }

  getPage(): Chainable {
    return cy.get('app-add-edit-team-dialog');
  }
}
