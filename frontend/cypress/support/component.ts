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
      createOrdinalKeyresult(title: string | null, owner: string | null): void;
      createMetricKeyresult(title: string | null): void;
      checkForDialogText(): void;
      fillOutObjective(
        objectiveTitle: string,
        button: string,
        quarter?: string,
        desc?: string,
        createKeyResults?: boolean,
      ): void;
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
      fillOutCheckInMetric(
        currentValue: number,
        changeConfidence: boolean,
        changeInfo: string | null,
        initiatives: string | null,
      ): void;
      fillOutCheckInOrdinal(
        currentZoneIndex: number,
        changeConfidence: boolean,
        changeInfo: string | null,
        initiatives: string | null,
      ): void;
    }
  }
}
