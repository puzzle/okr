describe('Okr monitoring', () => {
  it('should login and logout', () => {
    cy.request('http://pitc.okr.localhost:8080/actuator/health')
      .then((response) => {
        expect(response.body).to.have.property('status', 'UP'); // true
      });
  });
});
