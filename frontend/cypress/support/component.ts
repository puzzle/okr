import './commands';
import { keyCodeDefinitions } from 'cypress-real-events/keyCodeDefinitions';

declare global {
  namespace Cypress {
    interface Chainable {
      loginAsUser(user: any): Chainable;
      getByTestId(testsId: string, text?: string): Chainable<JQuery<HTMLElement>>;
      findByTestId(testId: string, text?: string): Chainable<JQuery<HTMLElement>>;
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
