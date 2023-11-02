import * as users from '../fixtures/users.json';

describe('OKR Overview', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  it.skip('Check order of teams', () => {
    const textsExpectedOrder = ['Puzzle ITC', '/BBT', 'LoremIpsum', 'we are cube.³'];
    cy.get('.team-title').then((elements) => {
      const texts: string[] = elements.map((_, el) => Cypress.$(el).text()).get();
      expect(texts).to.deep.equal(textsExpectedOrder);
    });
  });

  it('Tab ', () => {
    cy.realPress('Tab')
      .focused()
      .then((v) => console.log(v))
      .realPress('Tab')
      .focused()
      .then((v) => console.log(v))
      .realPress('Tab')
      .focused()
      .then((v) => console.log(v));
  });
});
