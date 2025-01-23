import './commands';
import { keyCodeDefinitions } from 'cypress-real-events/keyCodeDefinitions';

declare global {
  export namespace Cypress {
    interface Chainable {
      loginAsUser(user: any): Chainable;
      logout(): void;
      getByTestId(testsId: string, text?: string): Chainable;
      buttonShouldBePrimary(buttonId: string): Chainable;
      findByTestId(testId: string, text?: string): Chainable;
      pressUntilContains(text: string, key: keyof typeof keyCodeDefinitions): void;
      tabForward(): void;
      tabBackward(): void;
      tabForwardUntil(selector: string, limit?: number): void;
      tabBackwardUntil(selector: string, limit?: number): void;
      getZone(zone: string, onOverview: boolean): Chainable;
      validateScoring(isOverview: boolean, percentage: number): Chainable;
    }
  }
}
