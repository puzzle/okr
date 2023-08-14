describe('OKR Login', () => {
  beforeEach(() => {
    cy.loginWithCredentials('johnny', '123'); // Call the custom login command before each test
  });

  it('Logs in with user johnny and has right overview and teams', () => {
    cy.contains('Objectives und Key Results');
    cy.contains('Overview');
    cy.contains('Team');
    cy.contains('Puzzle ITC');
    cy.contains('/BBT');
    cy.wait(500);
    cy.get('pzsh-nav-item:last').click();
    cy.contains('Teams');
    cy.contains('Team erstellen');
    cy.contains('Puzzle ITC');
    cy.contains('/BBT');
  });
});
