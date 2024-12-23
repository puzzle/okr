import Dialog from './dialog';
import { uniqueSuffix } from '../../utils';
import Chainable = Cypress.Chainable;

export default class InviteMembersDialog extends Dialog {
  private readonly firstNames: string[] = [];

  override validatePage() {
    super.validatePage();
    this.getPage()
      .contains('Members registrieren')
      .should('exist');
  }

  enterUser(firstName: string, lastName: string, email: string) {
    firstName = uniqueSuffix(firstName);
    email = uniqueSuffix(email);
    this.firstNames.push(firstName);
    const firstNameInput = cy.get('[formcontrolname="firstName"]')
      .last();
    const lastNameInput = cy.get('[formcontrolname="lastName"]')
      .last();
    const emailInput = cy.get('[formcontrolname="email"]')
      .last();
    this.fillInput(firstNameInput, firstName);
    this.fillInput(lastNameInput, lastName);
    this.fillInput(emailInput, email);
    return this;
  }

  addAnotherUser() {
    cy.contains('Weiterer Member hinzufügen')
      .click();
    return this;
  }

  getFirstnames() {
    return this.firstNames;
  }

  override submit() {
    cy.getByTestId('invite')
      .click();
    return this.firstNames;
  }

  getPage(): Chainable {
    return cy.get('app-invite-user-dialog');
  }
}
