import './commands';

declare global {
  namespace Cypress {
    interface Chainable {
      loginAsUser(user: any): Chainable;
      getByTestId(testsId: string): Chainable;
      tabForward(): void;
      tabBackward(): void;
      checkForErrorToaster(amount: number, errorMessages?: string[]): void;
      checkForSuccessToaster(amount: number, errorMessages?: string[]): void;
      checkForWarnToaster(amount: number, errorMessages?: string[]): void;
      tabForwardUntil(selector: string, limit?: number): void;
      tabBackwardUntil(selector: string, limit?: number): void;
      createOrdinalKeyresult(title: string | null, owner: string | null): void;
      createMetricKeyresult(title: string | null, baseline: string | null, stretchGoal: string | null): void;
      getZone(zone: string, onOverview: boolean): Chainable;
      validateScoring(isOverview: boolean, percentage: number): Chainable;
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
        changeConfidence: number,
        changeInfo: string | null,
        initiatives: string | null,
      ): void;
      fillOutCheckInOrdinal(
        currentZoneIndex: number,
        changeConfidence: number,
        changeInfo: string | null,
        initiatives: string | null,
      ): void;
    }
  }
}
