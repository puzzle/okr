import * as users from '../fixtures/users.json';

describe('Tab workflow tests', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  it('First tabbable element is help element', () => {
    cy.realPress('Tab');
    cy.focused().contains('Hilfe');
  });

  it('After mouseclick on overview first element after tab should be the first objective', () => {
    cy.get('.overviewContainer').realClick();
    cy.realPress('Tab');
    cy.realPress('Tab');
    cy.focused().contains('Wir wollen die Zusammenarbeit im Team steigern.');
  });

  it('Tab forward and then tab backwards with tab+shift', () => {
    cy.realPress('Tab');
    cy.realPress('Tab');
    cy.realPress('Tab');
    cy.realPress('Tab');
    cy.focused().contains('GJ');
    cy.realPress(['Shift', 'Tab']);
    cy.realPress(['Shift', 'Tab']);
    cy.realPress(['Shift', 'Tab']);
    cy.focused().contains('Hilfe');
  });
});
