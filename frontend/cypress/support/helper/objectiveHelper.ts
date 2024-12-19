export function filterByObjectiveName (objectiveName: string) {
  return (index: number, element: HTMLElement) => isObjectiveName(element, objectiveName);
}

export function filterByObjectiveState (icon: string) {
  return (index: number, element: HTMLElement) => isObjectiveState(element, icon);
}

const isObjectiveState = (element: HTMLElement, icon: string) => {
  return Cypress.$(element)
    .find(`[src='assets/icons/${icon}-icon.svg']`).length > 0;
};

const isObjectiveName = (element: HTMLElement, objectiveName: string) => {
  return Cypress.$(element)
    .find(`:contains("${objectiveName}")`).length > 0;
};

export function getObjectiveColumns () {
  return cy.get('.objective');
}
