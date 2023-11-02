import './commands';

// Alternatively you can use CommonJS syntax:
// require('./commands')

import { mount } from 'cypress/angular';

// Augment the Cypress namespace to include type definitions for
// your custom command.
// Alternatively, can be defined in cypress/support/component.d.ts
// with a <reference path="./component" /> at the top of your spec.
declare global {
  namespace Cypress {
    interface Chainable {
      mount: typeof mount;
      loginAsUser(user: any): Chainable;
      getByTestId(testsId: string): Chainable;
    }
  }
}

Cypress.Commands.add('mount', mount);

// Example use:
// cy.mount(MyComponent)
