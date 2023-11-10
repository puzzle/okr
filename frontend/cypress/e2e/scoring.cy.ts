import * as users from '../fixtures/users.json';

describe('Scoring component e2e tests', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  it('Creates metric checkin', () => {});
});
