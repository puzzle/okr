import * as users from '../fixtures/users.json';
import { onlyOn } from '@cypress/skip-test';

describe('Tab workflow tests', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    onlyOn('chrome');
  });

  it('First tabbable element is help element', () => {
    cy.realPress('Tab');
    cy.focused().contains('Hilfe');
  });

  it('Tab forward and then tab backwards with tab+shift', () => {
    cy.tabForward();
    cy.tabForward();
    cy.tabForward();
    cy.tabForward();
    cy.focused().contains('GJ');
    cy.tabBackward();
    cy.tabBackward();
    cy.tabBackward();
    cy.focused().contains('Hilfe');
  });

  it('Open dialog via tab and enter', () => {
    cy.visit('/?quarter=2');
    cy.get('.objective').first().focus();
    cy.tabForwardUntil('[data-testId="add-keyResult"]');
    const button = cy.focused();
    button.click();
    cy.tabForward();
    cy.get('.mat-mdc-dialog-content').contains('Key Result erfassen');
    cy.tabBackward();
    cy.focused().click();
    cy.wait(500);
    button.then((buttonBefore) => {
      cy.focused().then((buttonAfter) => {
        expect(buttonBefore.get(0)).to.eql(buttonAfter.get(0));
      });
    });
  });
});
