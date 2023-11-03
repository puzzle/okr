import './commands';

declare global {
  namespace Cypress {
    interface Chainable {
      loginAsUser(user: any): Chainable;
      getByTestId(testsId: string): Chainable;
      tabForward(): void;
      tabBackward(): void;
      tabForwardUntil(selector: string, limit?: number): void;
      tabBackwardUntil(selector: string, limit?: number): void;
    }
  }
}
