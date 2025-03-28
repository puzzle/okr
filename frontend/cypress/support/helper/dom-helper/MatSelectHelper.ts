import { PageObjectMapperBase } from './pageObjectMapperBase';

export default class MatSelectHelper extends PageObjectMapperBase {
  validatePage(): void {
    // Does not need to be implemented this comment is for making linter happy
  }

  selectFromDropdown(selectId: string, option: string): void {
    cy.contains('Auswertung')
      .should('exist');
    cy.get(selectId)
      .find('.mat-mdc-form-field-flex')
      .first()
      .click();
    cy.contains(option);
    cy.get('mat-option')
      .contains(option)
      .click();
  }
}
