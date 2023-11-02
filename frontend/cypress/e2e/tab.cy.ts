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
});
