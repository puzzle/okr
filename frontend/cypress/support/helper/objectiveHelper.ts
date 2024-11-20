export function filterByObjectiveName(objectiveName: string) {
  return (index: number, element: HTMLElement) => byObjectiveName(element, objectiveName);
}

export function filterByObjectiveState(icon: string) {
  return (index: number, element: HTMLElement) => byObjectiveState(element, icon);
}

const byObjectiveState = (element: HTMLElement, icon: string) => {
  return Cypress.$(element).find(`[src='assets/icons/${icon}-icon.svg']`).length > 0;
};

const byObjectiveName = (element: HTMLElement, objectiveName: string) => {
  return Cypress.$(element).find(`:contains("${objectiveName}")`).length > 0;
};

export function getObjectiveColumns() {
  return cy.get('.objective');
}
