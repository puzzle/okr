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
      fillOutKeyResult(
        title: string,
        unit: string | null,
        baseline: string | null,
        stretchGoal: string | null,
        commitZone: string | null,
        targetZone: string | null,
        stretchZone: string | null,
        owner: string | null,
        description: string,
      ): void;
      createObjective(objectiveTitle: string, quarter: string, button: string, createKeyResults?: boolean): void;
    }
  }
}
