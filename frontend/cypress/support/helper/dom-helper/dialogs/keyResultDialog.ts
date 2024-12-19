import Dialog from "./dialog";
import { Unit } from "../../../../../src/app/shared/types/enums/Unit";
import ConfirmDialog from "./confirmDialog";
import Chainable = Cypress.Chainable;

export default class KeyResultDialog extends Dialog {
  fillKeyResultTitle (title: string) {
    this.fillInputByTestId("titleInput", title);
    return this;
  }

  fillKeyResultDescription (description: string) {
    this.fillInputByTestId("descriptionInput", description);
    return this;
  }

  withMetricValues (unit: Unit, baseline: string, stretchGoal: string) {
    cy.getByTestId("metricTab")
      .click();
    cy.getByTestId("unit")
      .select(unit);
    this.fillInputByTestId("baseline", baseline);
    this.fillInputByTestId("stretchGoal", stretchGoal);
    return this;
  }

  withOrdinalValues (commitZone: string, targetZone: string, stretchGoal: string) {
    cy.getByTestId("ordinalTab")
      .click();
    this.fillInputByTestId("commitZone", commitZone);
    this.fillInputByTestId("targetZone", targetZone);
    this.fillInputByTestId("stretchZone", stretchGoal);
    return this;
  }

  fillOwner (owner: string) {
    this.fillInputByTestId("ownerInput", owner);
    cy.realPress("ArrowDown")
      .realPress("Enter");
    return this;
  }

  addActionPlanElement (action: string) {
    cy.getByTestId("add-action-plan-line")
      .click();
    cy.getByTestId("actionInput")
      .filter((k, el) => {
        return (el as HTMLInputElement).value.trim() === "";
      })
      .first()
      .type(action);
    return this;
  }

  deleteKeyResult () {
    cy.getByTestId("delete-keyResult")
      .click();
    return new ConfirmDialog();
  }

  checkForDialogTextMetric () {
    cy.contains("Einheit");
    cy.contains("Baseline");
    cy.contains("Stretch Goal");
    this.checkForDialogText();
    return this;
  }

  checkForDialogTextOrdinal () {
    cy.contains("Commit Zone");
    cy.contains("Target Zone");
    cy.contains("Stretch Goal");
    this.checkForDialogText();
    return this;
  }

  private checkForDialogText () {
    cy.contains("Key Result erfassen");
    cy.contains("Titel");
    cy.contains("Metrisch");
    cy.contains("Ordinal");
    cy.contains("Owner");
    cy.contains("Beschreibung (optional)");
    cy.contains("Action Plan (optional)");
    cy.contains("Weitere Action hinzufügen");
    cy.contains("Speichern");
    cy.contains("Speichern & Neu");
    cy.contains("Abbrechen");
  }

  override submit () {
    cy.getByTestId("submit")
      .click();
  }

  saveAndNew () {
    cy.getByTestId("saveAndNew")
      .click();
  }

  getPage (): Chainable {
    return cy.get("app-key-result-form");
  }
}
