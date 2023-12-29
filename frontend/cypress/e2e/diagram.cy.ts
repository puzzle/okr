import * as users from '../fixtures/users.json';
import { onlyOn } from '@cypress/skip-test';

describe('OKR Diagram e2e tests', () => {
  describe('tests via click', () => {
    let myDiagram: any = null;
    let myRobot: any = null;

    beforeEach(() => {
      cy.loginAsUser(users.gl);
      cy.visit('/?quarter=2');
      onlyOn('chrome');

      // make sure the HTMLDivElement exists holding the Diagram
      cy.get('#myDiagramDiv');

      // load extensions/Robot.js dynamically
      cy.window().then((win) => {
        const scr = win.document.createElement('script');
        scr.src = 'https://unpkg.com/gojs/extensions/Robot.js';
        win.document.body.appendChild(scr);
      });

      // make sure it's loaded
      cy.window().should('have.property', 'Robot');

      // save these references for each test, which simplifies each test code
      cy.window().then((win) => {
        myDiagram = win.go.Diagram.fromDiv(win.document.getElementById('myDiagramDiv'));
        myRobot = new win.Robot(myDiagram);
      });

      cy.wait(1000);
    });

    it(`Create checkin metric`, () => {
      cy.window().then((win) => {
        expect(myDiagram.nodes.count).to.equal(16);
        expect(myDiagram.links.count).to.equal(15);
        const delta = myDiagram.findNodeForKey(1);
        expect(delta).to.not.equal(null);
        expect(delta.elt(0).fill).to.equal('#1E5A96');
        expect(delta.elt(0).text).to.equal('hello');
      });
    });
  });
});
